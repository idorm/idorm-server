package idorm.idormServer.photo.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    @Value("${s3.bucket-name}")
    private String bucketName;
    private final PhotoRepository photoRepository;
    private final AmazonS3Client amazonS3Client;
    private final String CALENDAR_FOLDER = "calendar";

    /**
     * DB에 프로필 사진 저장 여부 확인 |
     * 404(FILE_NOT_FOUND)
     */
    public void isExistProfilePhoto(Member member) {
        boolean result = photoRepository.existsByMemberId(member.getId());

        if(result == false) {
            throw new CustomException(null, FILE_NOT_FOUND);
        }
    }

    /**
     * DB에 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteMemberPhoto(Photo photo) {
        try {
            photoRepository.delete(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * DB에 Photo 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(Photo photo) {
        try {
            photoRepository.save(photo);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 프로필 사진 저장 |
     * 사진을 repository와 S3에 저장한다. 매핑된 Member의 profilePhoto에도 저장한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Photo createProfilePhoto(Member member, MultipartFile file) {

        validateFileType(file);

        String folderName = "profile-photo/" + member.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String photoUrl = insertFileToS3(folderName, fileName, file);
        
        Photo profilePhoto = Photo.ProfilePhotoBuilder()
                .folderName(folderName)
                .fileName(fileName)
                .photoUrl(photoUrl)
                .member(member)
                .build();
        save(profilePhoto);
        
        try {
            member.updateProfilePhoto(profilePhoto);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        return profilePhoto;
    }

    /**
     * 프로필 사진 삭제 |
     * S3, repository에서 삭제한다. 매핑된 Member의 profilePhoto도 자동으로 삭제된다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteProfilePhoto(Member member) {

        Optional<Photo> profilePhoto = null;
        try {
            profilePhoto = photoRepository.findByMemberId(member.getId());
            if (profilePhoto.isEmpty())
                return;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        deleteFileFromS3(profilePhoto.get().getFolderName(), profilePhoto.get().getFileName());
        deleteMemberPhoto(profilePhoto.get());
    }

    /**
     * 커뮤니티 게시글 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<Photo> savePostPhotos(Post post, List<MultipartFile> files) {

        String folderName = "community/" + post.getDormCategory() + "/" + "post-" + post.getId();

        List<Photo> savedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");
            String photoUrl = insertFileToS3(folderName, fileName, file);

            Photo savedPhoto = Photo.PostPhotoBuilder()
                    .folderName(folderName)
                    .fileName(fileName)
                    .photoUrl(photoUrl)
                    .post(post)
                    .build();
            save(savedPhoto);

            try {
                post.addPostPhoto(savedPhoto);
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }
            
            savedPhotos.add(savedPhoto);
        }
        return savedPhotos;
    }

    /**
     * 게시글 삭제 시 해당 게시글의 모든 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteAllPostPhotoByDeletedPost(Post post) {
        try {
            List<Photo> postPhotos = photoRepository.findByPostId(post.getId());
            if (postPhotos.isEmpty()) {
                return;
            }
            for (Photo photo : postPhotos) {
                photo.delete();
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 사진 단건 조회 |
     * 404(POST_PHOTO_NOT_FOUND)
     */
    public Photo findById(Long postId, Long photoId) {
        return photoRepository.findByIdAndPostIdAndIsDeletedFalse(photoId, postId)
                .orElseThrow(() -> {
                    throw new CustomException(null, POST_PHOTO_NOT_FOUND);
                });
    }

    /**
     * 파일 형식 검증 |
     * 파일 형식은 .png, .jpg, .jpeg 만 받는다. |
     * 415(FILE_TYPE_UNSUPPORTED)
     */
    public void validateFileType(MultipartFile file) {
        String type = file.getContentType().split("/")[1];

        if (!type.equals("jpg") && !type.equals("jpeg") && !type.equals("png")) {
            throw new CustomException(null, FILE_TYPE_UNSUPPORTED);
        }
    }

    /**
     * MultipartFile을 File로 전환 |
     * MultipartFile을 받아서 File의 형태로 전환하여 반환한다.
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertingFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertingFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertingFile;
    }

    /**
     * S3에 파일 저장 |
     * 파일을 전환하고 특정 파일 관련된 폴더에 파일을 저장하고 URL을 반환한다. |
     * 500(SERVER_ERROR)
     */
    private String insertFileToS3(String folderName, String fileName, MultipartFile file) {

        try {
            File convertedFile = convertMultiPartToFile(file);
            String uploadingFileName = folderName + "/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadingFileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            String url = amazonS3Client.getUrl(bucketName, uploadingFileName).toString();
            return url;
        } catch (IOException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * S3에 파일 삭제 |
     * 500(SERVER_ERROR)
     */
    private void deleteFileFromS3(String folderName, String fileName) {
        String deletingFileName = folderName + "/" + fileName;
        try {
            amazonS3Client.deleteObject(bucketName, deletingFileName);
        } catch (SdkClientException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 캘린더 사진 저장 |
     */
    public String putImage(MultipartFile file) {
        String fileName = UUID.randomUUID().toString();

        return insertFileToS3(CALENDAR_FOLDER, fileName, file);
    }

    /**
     * 캘린더 사진 삭제 |
     */
    public void deleteImage(String fileName) {
        deleteFileFromS3(CALENDAR_FOLDER, fileName);
    }
}
