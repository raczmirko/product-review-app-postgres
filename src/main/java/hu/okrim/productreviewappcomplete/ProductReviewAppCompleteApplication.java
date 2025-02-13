package hu.okrim.productreviewappcomplete;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@SpringBootApplication
public class ProductReviewAppCompleteApplication {

	public static void main(String[] args) {
		// Load environment variables from .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load(); // Avoid exception if .env is missing

		setSystemProperty("DB_URL", dotenv);
		setSystemProperty("DB_USER", dotenv);
		setSystemProperty("DB_PASSWORD", dotenv);

		SpringApplication.run(ProductReviewAppCompleteApplication.class, args);
	}

	private static void setSystemProperty(String key, Dotenv dotenv) {
		String value = dotenv.get(key);
		if (value != null) {
			System.setProperty(key, value);
		} else {
			throw new IllegalStateException("Missing required environment variable: " + key);
		}
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
