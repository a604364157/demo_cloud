package com.jjx.cloudauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjx.cloudauth.handler.MobileLoginSuccessHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 认证相关配置
 * @author jiangjx
 */
@Primary
@Order(90)
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Lazy
	@Autowired
	private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http.authorizeRequests()
			.antMatchers("/actuator/**", "/token/**").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable();
	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationSuccessHandler mobileLoginSuccessHandler() {
		return MobileLoginSuccessHandler.builder()
			.objectMapper(objectMapper)
			.clientDetailsService(clientDetailsService)
			.passwordEncoder(passwordEncoder())
			.defaultAuthorizationServerTokenServices(defaultAuthorizationServerTokenServices).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
