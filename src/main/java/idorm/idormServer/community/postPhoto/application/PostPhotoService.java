package idorm.idormServer.community.postPhoto.application;

import java.util.List;

import org.springframework.stereotype.Service;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postPhoto.application.port.in.PostPhotoUseCase;
import idorm.idormServer.community.postPhoto.application.port.out.DeletePostPhotoPort;
import idorm.idormServer.community.postPhoto.application.port.out.LoadPostPhotoPort;
import idorm.idormServer.community.postPhoto.application.port.out.SavePostPhotoPort;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostPhotoService implements PostPhotoUseCase {

  private final SavePostPhotoPort savePostPhotoPort;
  private final DeletePostPhotoPort deletePostPhotoPort;
  private final LoadPostPhotoPort loadPostPhotoPort;

  @Override
  public void save(final Post post, final List<String> photoUrls) {
    final List<PostPhoto> postPhotos = photoUrls.stream()
        .map(photoUrl -> new PostPhoto(post, photoUrl))
        .toList();
    postPhotos.forEach(postPhoto -> savePostPhotoPort.save(postPhoto));
  }

  @Override
  public void delete(final List<PostPhoto> postPhotos) {
    postPhotos.forEach(postPhoto -> deletePostPhotoPort.deleteById(postPhoto.getId()));
  }

  @Override
  public List<PostPhoto> findAllByPost(final Post post) {
    return loadPostPhotoPort.findByPostId(post.getId());
  }
}