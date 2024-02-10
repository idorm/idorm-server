package idorm.idormServer.email.domain;

import static idorm.idormServer.common.exception.ExceptionCode.FIELD_REQUIRED;
import static idorm.idormServer.common.exception.ExceptionCode.INVALID_CODE;

import idorm.idormServer.common.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class VerificationCodeTest {

    @Test
    void null_이면_예외() {
        // when & then
        Assertions.assertThatThrownBy(() -> new VerificationCode(null))
                .isInstanceOf(CustomException.class)
                .matches(e -> ((CustomException) e).getExceptionCode().equals(FIELD_REQUIRED));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "234-31", "abc-def", "   -   ", "123-4567", "-123-4-31"})
    void 인증코드_형식이_맞지_않으면_예외(String code) {
        // when & then
        Assertions.assertThatThrownBy(() -> new VerificationCode(code))
                .isInstanceOf(CustomException.class)
                .matches(e -> ((CustomException) e).getExceptionCode().equals(INVALID_CODE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"111-111", "123-456", "015-031"})
    void 올바른_생성(String code) {
        // when
        VerificationCode verificationCode = new VerificationCode(code);

        // then
        Assertions.assertThat(verificationCode.getCode()).isEqualTo(code);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "234-31", "abc-def", "   -   ", "123-4567", "-123-4-31"})
    void 검증_시_인증코드_형식이_맞지_않으면_예외(String code) {
        // given
        VerificationCode verificationCode = new VerificationCode("123-456");

        // when & then
        Assertions.assertThatThrownBy(() -> verificationCode.verify(code))
                .isInstanceOf(CustomException.class)
                .matches(e -> ((CustomException) e).getExceptionCode().equals(INVALID_CODE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"111-111", "123-456", "015-031"})
    void 올바른_검증(String code) {
        // when
        VerificationCode verificationCode = new VerificationCode(code);

        // then
        Assertions.assertThatNoException().isThrownBy(() -> verificationCode.verify(code));
    }
}