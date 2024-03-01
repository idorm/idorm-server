package idorm.idormServer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class IdormServerApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "/home/ec2-user/idorm-test/application-develop.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(IdormServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}
}
