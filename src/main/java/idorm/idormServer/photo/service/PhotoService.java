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

    @Value("${s3.bucketName}")
    private String bucketName;
    private final PhotoRepository photoRepository;
    private final AmazonS3Client amazonS3Client;
    private final String CALENDAR_FOLDER = "calendar";

    /**
     * Photo 파일이름으로 사진 조회 |
     */
    public Optional<Photo> findOneByFileName(String fileName) {

        Optional<Photo> foundPhoto = photoRepository.findByFileName(fileName);

        return foundPhoto;
    }

    /**
     * Photo 멤버 프로필 사진 저장 |
     * 사진을 S3에 저장한 후에 디비에 관련 정보를 입력한다. 저장 중 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Photo saveProfilePhoto(Member member, String fileName, MultipartFile file) {

        String folderName = "profile-photo/" + member.getEmail() + "-" + member.getId();
        String url = insertFileToS3(folderName, fileName, file);

        try {
            Photo photo = Photo.ProfilePhotoBuilder()
                    .folderName(folderName)
                    .fileName(fileName)
                    .url(url)
                    .member(member)
                    .build();

            Photo savedPhoto = photoRepository.save(photo);

            return savedPhoto;
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Photo 커뮤니티 게시글 사진 저장 |
     * 기존에 저장되어있는 사진이 있다면 해당 게시글의 모든 사진을 aws와 DB에서 삭제한 후 다시 사진을 저장한다.
     * 저장 중 오류가 발생하면 500(Internal Server Error)을 던진다.
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
                throw new CustomException(SERVER_ERROR);
            }

            photos.add(savedPhoto);
            fileIndex += 1;
        }

        return photos;
    }

    /**
     * Photo 캘린더 사진 저장 |
     */
    public String putImage(MultipartFile file) {
        String fileName = UUID.randomUUID().toString();

        return insertFileToS3(CALENDAR_FOLDER, fileName, file);
    }

    /**
     * S3에 파일 저장 |
     * 파일을 전환하고 특정 파일 관련된 폴더에 파일을 저장하고 URL을 반환한다. 파일 전환시 오류가 발생하면 500(Internal Server Error)을 던진다.
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
            throw new CustomException(SERVER_ERROR);
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
     * 프로필 사진이 존재하는지 확인한다. 멤버 삭제할 때의 경우에는 프로필 사진 등록여부를 확인 할 필요가 없기 때문이다.
     * 그러나 프로필 사진 삭제 api로 요청을 하는 경우에는 사진이 저장되어 있지 않다면 FILE_NOT_FOUND 예외를 던져야 한다.
     */
    public void isExistProfilePhoto(Member member) {
        boolean result = photoRepository.existsByMemberId(member.getId());

        if(result == false) {
            throw new CustomException(FILE_NOT_FOUND);
        }
    }

    /**
     * Photo 폴더명으로 프로필 사진 삭제 |
     * 사진을 S3에서 삭제한 후에 디비에서 관련 정보 찾아 삭제를 한다. 디비에서 관련 정보를 찾는 중 오류가 발생하면 404(Not Found)를 던지고, 디비에서
     * 데이터를 삭제하는 과정에서 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public void deleteProfilePhotos(Member member) {

        String folderName = "profile-photo/" + member.getEmail() + "-" + member.getId();

        List<Photo> profilePhotos = photoRepository.findByFolderName(folderName);

        for(Photo photo : profilePhotos) {
            String deleteFileName = photo.getFileName();
            deleteFileFromS3(folderName, deleteFileName);

            try {
                photoRepository.delete(photo);
            } catch (RuntimeException e) {
                throw new CustomException(SERVER_ERROR);
            }
        }

    }

    /**
     * Photo 게시글 사진 삭제 - 해당 게시글의 전체 사진 삭제 |
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
                throw new CustomException(SERVER_ERROR);
            }
        }
    }

    /**
     * 게시글 삭제 시 커뮤니티 게시글 사진 isDeleted true로 변경
     */
    @Transactional
    public void deletePhotoDeletingPost(Long postId) {
        List<Photo> foundPhotos = photoRepository.findByPostId(postId);
        for (Photo photo : foundPhotos) {
            photo.updateIsDeleted();
        }
    }

    /**
     * Photo 캘린더 사진 삭제 |
     */
    public void deleteImage(String fileName) {
        deleteFileFromS3(CALENDAR_FOLDER, fileName);
    }

    /**
     * S3에 파일 삭제 |
     * S3로부터 파일을 삭제한다. 삭제 중 오류가 발생하면 500(Internal Server Error)을 던진다.
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
