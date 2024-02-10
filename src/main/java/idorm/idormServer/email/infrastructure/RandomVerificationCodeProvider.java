package idorm.idormServer.email.infrastructure;

import static java.util.stream.Collectors.*;

import idorm.idormServer.email.domain.VerificationCode;
import idorm.idormServer.email.service.VerificationCodeProvider;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomVerificationCodeProvider implements VerificationCodeProvider {
    @Override
    public VerificationCode provide() {
        Random random = ThreadLocalRandom.current();
        List<String> randomNumbers = random.ints(0, 10)
                .mapToObj(String::valueOf)
                .distinct()
                .limit(VerificationCode.CODE_LENGTH)
                .collect(toList());

        String code = randomNumbers.subList(0, 3) + "-" + randomNumbers.subList(3, 6);
        return new VerificationCode(code);
    }
}