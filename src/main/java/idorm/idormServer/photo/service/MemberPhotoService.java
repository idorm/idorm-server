package idorm.idormServer.photo.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.MemberPhoto;
import idorm.idormServer.photo.repository.MemberPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPhotoService {

    @Value("${s3.bucket-name.member-photo}")
    private String memberPhotoBucketName;
    private final PhotoService photoService;
    private final MemberPhotoRepository memberPhotoRepository;

    /**
     * DB에 회원 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public MemberPhoto save(MemberPhoto memberPhoto) {
        try {
            return memberPhotoRepository.save(memberPhoto);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(MemberPhoto memberPhoto) {
        try {
            memberPhoto.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public MemberPhoto createMemberPhoto(Member member, MultipartFile file) {

        String folderName = "profile-photo/" + member.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String memberPhotoUrl = photoService.insertFileToS3(memberPhotoBucketName, folderName, fileName, file);

        MemberPhoto memberPhoto = MemberPhoto.builder()
                .member(member)
                .photoUrl(memberPhotoUrl)
                .build();

        return save(memberPhoto);
    }

    /**
     * 회원 사진 S3에서 삭제 및 DB에서 photoUrl 삭제 |
     * 회원 탈퇴 시 사용한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteFromS3(Member member) {

        String photoUrl = member.getMemberPhoto().getPhotoUrl();

        if (photoUrl == null)
            return;

        int filePathFirstIndexFromPhotoUrl = photoUrl.indexOf("profile-photo/");
        if (filePathFirstIndexFromPhotoUrl == -1)
            return;

        try {
            member.getMemberPhoto().removePhotoUrl();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        String filePath = photoUrl.substring(filePathFirstIndexFromPhotoUrl);
        photoService.deleteFileFromS3(memberPhotoBucketName, filePath);
    }
}
