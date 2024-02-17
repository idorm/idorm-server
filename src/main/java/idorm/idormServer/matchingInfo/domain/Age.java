package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;
import lombok.Getter;

@Getter
public class Age {

  private static final int MIN_AGE_SIZE = 20;
  private static final int MAX_AGE_SIZE = 50;

  private Integer value;

  public Age(final Integer value) {
    validate(value);
    this.value = value;
  }

  public static Age forMapper(final Integer age) {
    return new Age(age);
  }

  private void validate(final Integer value) {
    Validator.validateNotNull(value);
    Validator.validateSize(value, MIN_AGE_SIZE, MAX_AGE_SIZE,
        MatchingInfoResponseCode.INVALID_AGE_LENGTH);
  }
}
