package idorm.idormServer.community.postLike.application.port.out;

import idorm.idormServer.community.postLike.entity.PostLike;

public interface DeletePostLikePort {

  void delete(PostLike postLike);
}
