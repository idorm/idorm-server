package idorm.idormServer.photo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.exceptions.CustomException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static idorm.idormServer.exceptions.ErrorCode.*;

@Slf4j
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
        log.info("IN PROGRESS | Photo 조회 At " + LocalDateTime.now() +
                " | 파일명 = " + fileName);

        Optional<Photo> foundPhoto = photoRepository.findByFileName(fileName);

        log.info("COMPLETE | Photo 조회 At " + LocalDateTime.now() +
                " | 파일명 = " + fileName);
        return foundPhoto;
    }

    /**
     * Photo 멤버 프로필 사진 저장 |
     * 사진을 S3에 저장한 후에 디비에 관련 정보를 입력한다. 저장 중 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Photo save(Member member, String fileName, MultipartFile file) {
        log.info("IN PROGRESS | Photo 프로필 사진 저장 At " + LocalDateTime.now() +
                " | 멤버 아이디 = "  + member.getId() + " 파일명 = " + fileName);

        String folderName = "profile-photo/" + member.getEmail() + "-" + member.getId();
        String url = insertFileToS3(folderName, fileName, file);

        try {
            Photo photo = Photo.builder()
                    .folderName(folderName)
                    .fileName(fileName)
                    .url(url)
                    .member(member)
                    .build();

            Photo savedPhoto = photoRepository.save(photo);

            log.info("COMPLETE | Photo 프로필 사진 저장 At " + LocalDateTime.now() +
                    " | 멤버 아이디 = "  + member.getId() + " 파일명 = " + fileName);
            return savedPhoto;
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("Photo 프로필 사진 save 중 에러 발생", e);
        }
    }

    /**
     * Photo 커뮤니티 게시글 사진 저장 |
     * 사진을 S3에 저장한 후에 디비에 관련 정보를 입력한다. 저장 중 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public List<Photo> savePostPhotos(Post post, Member member, List<String> fileNames, List<MultipartFile> files) {

        log.info("IN PROGRESS | Photo 커뮤니티 게시글 사진 저장 At " + LocalDateTime.now() +
                " | 게시글 아이디 = " + post.getId() + " 멤버 아이디 = "  + member.getId() + " 파일 사이즈 = " + files.size());

        String folderName = "community/" + post.getDormNum() + "/" + "post-" + post.getId();
        List<Photo> photos = new ArrayList<>();

        if(post.getPhotos() != null) {
            for(Photo photo : post.getPhotos()) {
                photos.add(photo);
            }
        }

        int fileNameIndex = 0;
        for (MultipartFile file : files) {
            String fileName = fileNames.get(fileNameIndex);

            String url = insertFileToS3(folderName, fileName, file);

            Photo savedPhoto = null;
            try {
                Photo photo = Photo.builder()
                        .folderName(folderName)
                        .fileName(fileName)
                        .url(url)
                        .member(member)
                        .post(post)
                        .build();

                savedPhoto = photoRepository.save(photo);
            } catch (InternalServerErrorException e) {
                throw new InternalServerErrorException("Photo savePostPhotos 중 서버 에러 발생", e);
            }

            photos.add(savedPhoto);
            fileNameIndex += 1;
        }

        log.info("COMPLETE | Photo 커뮤니티 게시글 사진 저장 At " + LocalDateTime.now() +
                " | 게시글 아이디 = " + post.getId() + " 멤버 아이디 = "  + member.getId() + " 파일 사이즈 = " + files.size());
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
        log.info("IN PROGRESS | S3에 파일 저장 At " + LocalDateTime.now() +
                " | 폴더명 = " + folderName + " | 파일명 = " + fileName);
        try {
            File convertedFile = convertMultiPartToFile(file);
            String uploadingFileName = folderName + "/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadingFileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            String url = amazonS3Client.getUrl(bucketName, uploadingFileName).toString();
            log.info("COMPLETE | S3에 파일 저장 At " + LocalDateTime.now() + " | " + url);
            return url;
        } catch (IOException e) {
            throw new InternalServerErrorException("Photo insertFileToS3 중 에러 발생", e);
        }
    }

    /**
     * MultipartFile을 File로 전환 |
     * MultipartFile을 받아서 File의 형태로 전환하여 반환한다.
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        log.info("IN PROGRESS | MultipartFile을 File로 전환 At " + LocalDateTime.now());
        File convertingFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertingFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        log.info("COMPLETE | MultipartFile을 File로 전환 At " + LocalDateTime.now());
        return convertingFile;
    }

    /**
     * Photo 멤버 프로필 사진 수정 |
     * 사진을 S3에 저장한 후에 디비에 관련 정보를 입력한다. 저장 중 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Photo update(Member member, String fileName, MultipartFile file) {
        log.info("IN PROGRESS | Photo 업데이트 At " + LocalDateTime.now() +
                " | 멤버 아이디 = "  + member.getId() + " 파일명 = " + fileName);

        String folderName = member.getEmail() + "-" + member.getId();
        insertFileToS3(folderName, fileName, file);

        Photo savedPhoto = photoRepository.findByFileName(fileName)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND));

        savedPhoto.modifyUpdatedAt(LocalDateTime.now());

        log.info("COMPLETE | Photo 업데이트 At " + LocalDateTime.now() +
                " | 멤버 아이디 = "  + member.getId() + " 파일명 = " + fileName);
        return savedPhoto;
    }

    /**
     * Photo 폴더명으로 프로필 사진 삭제 |
     * 사진을 S3에서 삭제한 후에 디비에서 관련 정보 찾아 삭제를 한다. 디비에서 관련 정보를 찾는 중 오류가 발생하면 404(Not Found)를 던지고, 디비에서
     * 데이터를 삭제하는 과정에서 오류가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public void deleteProfilePhotos(Member member) {
        log.info("IN PROGRESS | Photo 프로필 사진 삭제 At " + LocalDateTime.now());

        String folderName = "profile-photo/" + member.getEmail() + "-" + member.getId();

        List<Photo> profilePhotos = photoRepository.findByFolderName(folderName);

        if(profilePhotos.isEmpty()) {
            throw new CustomException(FILE_NOT_FOUND);
        }

        for(Photo photo : profilePhotos) {
            String deleteFileName = photo.getFileName();
            deleteFileFromS3(folderName, deleteFileName);

            try {
                photoRepository.delete(photo);
            } catch (InternalServerErrorException e) {
                throw new InternalServerErrorException("Photo deleteProfilePhotos 중 서버 에러 발생", e);
            }
        }

        log.info("COMPLETE | Photo 프로필 사진 삭제 At " + LocalDateTime.now());
    }

    /**
     * Photo 게시글 사진 삭제 - 해당 게시글의 전체 사진 삭제 |
     * 폴더명 (post-{postId}/)을 받으면 해당 폴더 내의 전체 사진을 삭제합니다.
     */
    @Transactional
    public void deletePostFullPhotos(Post post, Member member) {
        log.info("IN PROGRESS | Photo 커뮤니티 게시글 폴더 전체 삭제 At " + LocalDateTime.now() +
                " | 게시글 아이디 = " + post.getId() + " 멤버 아이디 = "  + member.getId());

        String folderName = "community/" + post.getDormNum() + "/" + "post-" + post.getId();

        List<Photo> foundPhotos = photoRepository.findByFolderName(folderName);

        for(Photo photo : foundPhotos) {
            deleteFileFromS3(folderName, photo.getFileName());
            try {
                photoRepository.delete(photo);
            } catch (InternalServerErrorException e) {
                throw new InternalServerErrorException("Photo deletePostFullPhotos 중 서버 에러 발생", e);
            }
        }
        log.info("COMPLETE | Photo 커뮤니티 게시글 폴더 전체 삭제 At " + LocalDateTime.now());
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
        } catch (Exception e) {
            throw new InternalServerErrorException("Photo deleteFileFromS3 중 에러 발생", e);
        }
    }
}
