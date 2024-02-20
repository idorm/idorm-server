package idorm.idormServer.community.comment.application.port.out;


import idorm.idormServer.community.comment.domain.Comment;

public interface SaveCommentPort {

  void save(Comment comment);
}
