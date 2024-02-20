package idorm.idormServer.community.post.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.exception.CommunityResponseCode;
import lombok.Getter;

@Getter
public class Title {

  private static final int MIN_LENGTH = 1;
  public static final int MAX_LENGTH = 50;

  private String value;

  public Title(final String value) {
    validate(value);
    this.value = value;
  }

  public static Title forMapper(final String title) {
    return new Title(title);
  }


  public void validate(String value) {
    Validator.validateNotBlank(value);
    Validator.validateNotNull(value);
    Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH,
        CommunityResponseCode.INVALID_TITLE_LENGTH);
  }
}