package idorm.idormServer.email.adapter.out.api;

import static idorm.idormServer.email.entity.Email.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.application.port.out.GenerateVerificationCodePort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GenerateVerificationCodeAdapter implements GenerateVerificationCodePort {

	@Override
	public String generate() {
		Random random = ThreadLocalRandom.current();
		List<String> randomNumbers = random.ints(0, 10)
			.mapToObj(String::valueOf)
			.distinct()
			.limit(CODE_LENGTH)
			.collect(toList());

		String front = randomNumbers.subList(0, 3).stream().collect(joining());
		String back = randomNumbers.subList(3, 6).stream().collect(joining());

		return front + "-" + back;
	}
}
