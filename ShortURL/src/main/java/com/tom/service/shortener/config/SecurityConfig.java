	package com.tom.service.shortener.config;
	
	import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.tom.service.shortener.common.AuthEntryPointJwt;
import com.tom.service.shortener.repository.RedisPersistentTokenRepository;

import lombok.RequiredArgsConstructor;
	
	@Configuration
	@EnableWebSecurity
	@EnableMethodSecurity
	@RequiredArgsConstructor
	public class SecurityConfig {
	
		@Value("${application.security.jwt.secret-key}")
		private String secretKey;
	
		private final AuthEntryPointJwt unauthorizedHandler;
	
		private final RedisTemplate<String, Object> redisTemplate;
	
		private final UserDetailsService userDetailsService;
		
		@Bean
		SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http.csrf(AbstractHttpConfigurer::disable)
					.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
					.authorizeHttpRequests(auth -> auth
							.requestMatchers("/swagger-ui/**").denyAll()
						    .requestMatchers("/v3/api-docs/**").denyAll()
							.requestMatchers("/actuator/**").authenticated()
							.requestMatchers("/api/v1/dev/**").authenticated()
							.requestMatchers("/api/v1/**").permitAll()
							.requestMatchers("/**").permitAll()
							.anyRequest().permitAll()
							)
					.rememberMe(rememberMe -> rememberMe
							.tokenValiditySeconds(1800)
							.key(secretKey)
							.userDetailsService(userDetailsService)
							.tokenRepository(persistentTokenRepository(redisTemplate)))
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
					.httpBasic(Customizer.withDefaults());
			return http.build();
		}
	
		@Bean
		PersistentTokenRepository persistentTokenRepository(RedisTemplate<String, Object> redisTemplate) {
			return new RedisPersistentTokenRepository(redisTemplate);
		}
	}