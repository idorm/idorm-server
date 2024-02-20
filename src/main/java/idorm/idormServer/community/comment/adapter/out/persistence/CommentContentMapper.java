package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.domain.CommentContent;
import idorm.idormServer.community.post.adapter.out.persistence.ContentEmbeddedEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class CommentContentMapper {

  public ContentEmbeddedEntity toEntity(CommentContent commentContent) {
    return new ContentEmbeddedEntity(commentContent.getValue());
  }

  public CommentContent toDomain(ContentEmbeddedEntity entity) {
    return CommentContent.forMapper(entity.getValue());
  }
}