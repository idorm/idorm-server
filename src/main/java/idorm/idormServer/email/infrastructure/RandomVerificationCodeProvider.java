package idorm.idormServer.email.infrastructure;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.collectingAndThen;

import idorm.idormServer.email.domain.VerificationCode;
import idorm.idormServer.email.service.VerificationCodeProvider;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomVerificationCodeProvider implements VerificationCodeProvider {
    @Override
    public VerificationCode provide() {
        Random random = ThreadLocalRandom.current();
        return random.ints(0, 10)
                .mapToObj(String::valueOf)
                .limit(VerificationCode.CODE_LENGTH)
                .collect(collectingAndThen(joining(), VerificationCode::new));
    }
}
