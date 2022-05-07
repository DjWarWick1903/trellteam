package ro.dev.trellteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@CrossOrigin(origins = "*")
public class TrellTeamApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrellTeamApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				//registry.addMapping("/security/login").allowedOrigins("http://localhost:63342/trellteam-fe");
				registry.addMapping("/*")
						.allowedHeaders("")
						.allowedOrigins("");
			}
		};
	}*/
}
