package idorm.idormServer.email.adapter.out.api;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import idorm.idormServer.email.application.port.out.GenerateVerificationCodePort;
import idorm.idormServer.email.domain.VerificationCode;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GenerateVerificationCodeAdapter implements GenerateVerificationCodePort {

    @Override
    public VerificationCode generate() {
        Random random = ThreadLocalRandom.current();
        List<String> randomNumbers = random.ints(0, 10)
                .mapToObj(String::valueOf)
                .distinct()
                .limit(VerificationCode.CODE_LENGTH)
                .collect(toList());

        String front = randomNumbers.subList(0, 3).stream().collect(joining());
        String back = randomNumbers.subList(3, 6).stream().collect(joining());

        return new VerificationCode(front + "-" + back);
    }
}
