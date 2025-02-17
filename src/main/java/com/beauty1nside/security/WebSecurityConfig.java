package com.beauty1nside.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//      .authorizeHttpRequests(authorize -> authorize
//        .anyRequest().permitAll() // 모든 요청을 인증 없이 허용
//      )
//      .csrf(csrf -> csrf.disable()) // CSRF 비활성화
//      .formLogin(form -> form.disable()) // 로그인 폼 비활성화
//      .logout(logout -> logout.disable()); // 로그아웃 비활성화
//
//    return http.build();
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((requests) -> requests
        .requestMatchers("/", "/common/**", "/css/**", "/docs/**", "/file/**", "/fonts/**", "/images/**", "/js/**", "/scss/**", "/templates/**", "/vendors/**", "/gulpfile.js", "/erp/**").permitAll()
//        .requestMatchers("/erp/**").hasRole("AU001")
        .anyRequest().authenticated()
      )
      .formLogin((form) -> form
        .loginPage("/login")
        .usernameParameter("employeeId")
        .passwordParameter("employeePw")
//        .defaultSuccessUrl("/", true) // 로그인 성공 후 항상 "/"로 이동
        .successHandler(authenticationSuccessHandler())
        .permitAll()
      )
      .logout((logout) -> logout.permitAll())
//      .csrf(csrf -> csrf.disable())	// 기본적으로 사용으로 되어있음 이거 없애면 사용임
		.cors(cors -> cors
             .configurationSource(CorsConfig.corsConfigurationSource())
      );
    http.exceptionHandling(ex -> ex.accessDeniedPage("/no-permission"));
//    http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()));

    return http.build();
  }
  
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomLoginSuccessHandler();
  }
}