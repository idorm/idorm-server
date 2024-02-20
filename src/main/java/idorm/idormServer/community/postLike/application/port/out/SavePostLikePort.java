package idorm.idormServer.community.postLike.application.port.out;

import idorm.idormServer.community.postLike.domain.PostLike;

public interface SavePostLikePort {

  void save(PostLike postLike);
}
