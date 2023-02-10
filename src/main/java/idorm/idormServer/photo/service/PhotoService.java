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
            throw new CustomException(FILE_NOT_FOUND);
        }
    }

    /**
     * DB에 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deletePhoto(Photo photo) {
        try {
            photoRepository.delete(photo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * DB에 회원 프로필 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void saveProfilePhoto(Photo photo) {
        try {
            photoRepository.save(photo);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Photo 회원 프로필 사진 저장 |
     * 사진을 repository와 S3에 저장한다. 매핑된 Member의 profilePhoto에도 저장한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Photo createProfilePhoto(Member member, MultipartFile file) {

        validateFileType(file);

        String folderName = "profile-photo/" + member.getId();

        Photo createdProfilePhoto = Photo.ProfilePhotoBuilder()
                .folderName(folderName)
                .member(member)
                .build();
        saveProfilePhoto(createdProfilePhoto);

        String fileName = createdProfilePhoto.getId() + file.getContentType().replace("image/", ".");
        String photoUrl = insertFileToS3(folderName, fileName, file);

        try {
            createdProfilePhoto.setFileName(fileName);
            createdProfilePhoto.setPhotoUrl(photoUrl);
            member.updateProfilePhoto(createdProfilePhoto);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }

        return createdProfilePhoto;
    }

    /**
     * 프로필 사진 삭제 |
     * S3, repository에서 삭제한다. 매핑된 member의 profilePhoto도 자동으로 삭제된다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteProfilePhotos(Member member) {

        String folderName = member.getProfilePhoto().getFolderName();
        List<Photo> profilePhotos = null;
        try {
            profilePhotos = photoRepository.findByFolderName(folderName);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }

        for(Photo photo : profilePhotos) {
            deleteFileFromS3(folderName, photo.getFileName());
            deletePhoto(photo);
        }
    }

    /**
     * 커뮤니티 게시글 사진 저장 |
     * 기존에 저장되어있는 사진이 있다면 해당 게시글의 모든 사진을 aws와 DB에서 삭제한 후 다시 사진을 저장한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<Photo> savePostPhotos(Post post, Member member, List<String> fileNames, List<MultipartFile> files) {

        String folderName = "community/" + post.getDormNum() + "/" + "post-" + post.getId();
        List<Photo> photos = new ArrayList<>();

        if(post.getPhotos() != null) {
            deletePostFullPhotos(post, member);
        }

        int fileIndex = 0;
        for (MultipartFile file : files) {
            String fileName = fileNames.get(fileIndex);

            String url = insertFileToS3(folderName, fileName, file);

            Photo savedPhoto = null;
            try {

                Photo photo = Photo.PostPhotoBuilder()
                        .folderName(folderName)
                        .fileName(fileName)
                        .url(url)
                        .post(post)
                        .build();

                savedPhoto = photoRepository.save(photo);
            } catch (RuntimeException e) {
                e.getStackTrace();
                throw new CustomException(SERVER_ERROR);
            }

            photos.add(savedPhoto);
            fileIndex += 1;
        }

        return photos;
    }

    /**
     * 게시글 사진 삭제 - 해당 게시글의 전체 사진 삭제 |
     * 폴더명 (post-{postId}/)을 받으면 해당 폴더 내의 전체 사진을 삭제합니다.
     */
    @Transactional
    public void deletePostFullPhotos(Post post, Member member) {

        String folderName = "community/" + post.getDormNum() + "/" + "post-" + post.getId();

        List<Photo> foundPhotos = photoRepository.findByFolderName(folderName);

        for(Photo photo : foundPhotos) {
            deleteFileFromS3(folderName, photo.getFileName());
            try {
                photoRepository.delete(photo);
            } catch (RuntimeException e) {
                e.getStackTrace();
                throw new CustomException(SERVER_ERROR);
            }
        }
    }

    /**
     * 게시글 삭제 시 커뮤니티 게시글 사진 isDeleted true로 변경
     */
    @Transactional
    public void deletePhotoDeletingPost(Long postId) {
        try {
            List<Photo> foundPhotos = photoRepository.findByPostId(postId);
            for (Photo photo : foundPhotos) {
                photo.removePhoto();
            }
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
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

    /**
     * 파일 형식 검증 |
     * 파일 형식은 .png, .jpg, .jpeg 만 받는다. |
     * 415(FILE_TYPE_UNSUPPORTED)
     */
    private void validateFileType(MultipartFile file) {
        String type = file.getContentType().split("/")[1];

        if (!type.equals("jpg") && !type.equals("jpeg") && !type.equals("png")) {
            throw new CustomException(FILE_TYPE_UNSUPPORTED);
        }
    }

    /**
     * MultipartFile을 File로 전환 |
     * MultipartFile을 받아서 File의 형태로 전환하여 반환한다. |
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
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
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
            throw new CustomException(SERVER_ERROR);
        }
    }
}
