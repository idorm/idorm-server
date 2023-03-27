package idorm.idormServer.photo.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.photo.domain.PostPhoto;
import idorm.idormServer.photo.repository.PostPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static idorm.idormServer.exception.ExceptionCode.POSTPHOTO_NOT_FOUND;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostPhotoService {

    @Value("${s3.bucket-name.post-photo}")
    private String postPhotoBucketName;
    private final PhotoService photoService;
    private final PostPhotoRepository postPhotoRepository;

    /**
     * DB에 PostPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(PostPhoto photo) {
        try {
            postPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 커뮤니티 게시글 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<PostPhoto> savePostPhotos(Post post, List<MultipartFile> files) {

        String folderName = post.getDormCategory() + "/" + "post-" + post.getId();

        List<PostPhoto> savedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");
            String photoUrl = photoService.insertFileToS3(postPhotoBucketName, folderName, fileName, file);

            PostPhoto savedPostPhoto = null;

            try {
                savedPostPhoto = PostPhoto.builder()
                        .post(post)
                        .photoUrl(photoUrl)
                        .build();
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }
            save(savedPostPhoto);

            savedPhotos.add(savedPostPhoto);
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
            List<PostPhoto> postPhotos = postPhotoRepository.findByPostId(post.getId());
            if (postPhotos.isEmpty()) {
                return;
            }
            for (PostPhoto postPhoto : postPhotos) {
                postPhoto.delete();
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 사진 단건 조회 |
     * 404(POSTPHOTO_NOT_FOUND)
     */
    public PostPhoto findById(Long postId, Long photoId) {
        return postPhotoRepository.findByIdAndPostIdAndIsDeletedFalse(photoId, postId)
                .orElseThrow(() -> {
                    throw new CustomException(null, POSTPHOTO_NOT_FOUND);
                });
    }
}
