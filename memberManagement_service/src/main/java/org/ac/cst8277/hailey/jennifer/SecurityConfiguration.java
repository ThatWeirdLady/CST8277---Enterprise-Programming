package org.ac.cst8277.hailey.jennifer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.authorizeHttpRequests((authz) -> {
					authz
							.requestMatchers("/token").authenticated()
							.anyRequest().permitAll();
				})
				.exceptionHandling(e -> e
						.authenticationEntryPoint(
								new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.oauth2Login(withDefaults());
		return http.build();
	}

}