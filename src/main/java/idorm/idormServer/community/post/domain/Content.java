package idorm.idormServer.community.post.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.exception.CommunityResponseCode;
import lombok.Getter;

@Getter
public class Content {

  private static final int MIN_LENGTH = 1;
  private static final int MAX_LENGTH = 300;

  private String value;

  public Content(final String value) {
    validate(value);
    this.value = value;
  }

  public static Content forMapper(final String value) {
    return new Content(value);
  }

  public void validate(String value) {
    Validator.validateNotBlank(value);
    Validator.validateNotNull(value);
    Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH,
        CommunityResponseCode.INVALID_CONTENT_LENGTH);
  }
}