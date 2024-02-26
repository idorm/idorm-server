package idorm.idormServer.community.comment.application.port.out;


import idorm.idormServer.community.comment.entity.Comment;

public interface SaveCommentPort {

  void save(Comment comment);
}
