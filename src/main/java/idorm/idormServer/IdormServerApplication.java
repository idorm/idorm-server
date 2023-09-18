package idorm.idormServer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class IdormServerApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:/home/ec2-user/idorm-test/application-develop.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(IdormServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}
}
