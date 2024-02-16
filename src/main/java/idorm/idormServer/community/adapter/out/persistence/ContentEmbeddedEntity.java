package idorm.idormServer.community.adapter.out.persistence;

import static idorm.idormServer.community.domain.CommentContent.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentEmbeddedEntity {

	@Column(name = "content", nullable = false, length = MAX_LENGTH)
	private String value;
}