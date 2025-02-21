package com.beauty1nside.security;

import jakarta.servlet.http.HttpServletResponse;
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
        .requestMatchers("/", "/common/**", "/css/**", "/docs/**", "/file/**", "/fonts/**", "/images/**", "/js/**", "/scss/**", "/templates/**", "/vendors/**", "/gulpfile.js", "/erp/**", "/login/**").permitAll()
        .requestMatchers("/api/**").authenticated() // API 요청은 인증된 사용자만 가능
        .anyRequest().authenticated()
      )
//    인증되지 않은 사용자가 API 호출 시 401 Unauthorized 반환.
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint((request, response, authException) -> {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        })
      )
      .csrf(csrf -> csrf.ignoringRequestMatchers("/erp/rest/**"))  // /erp/rest/**` 경로의 CSRF 보호 해제 표하연 202502181029 추가
      .formLogin((form) -> form
        .loginPage("/login")
        .usernameParameter("employeeId")
        .passwordParameter("employeePw")
//        .defaultSuccessUrl("/", true) // 로그인 성공 후 항상 "/"로 이동
        .successHandler(authenticationSuccessHandler())
        .permitAll()
      )
      .rememberMe(remember -> remember
        .key("uniqueAndSecret") // 🔹 쿠키 암호화 키
        .tokenValiditySeconds(60 * 60 * 24 * 30) // 🔹 30일 유지
        .rememberMeParameter("remember-me") // 🔹 체크박스 name 속성
      )
//      .logout((logout) -> logout.permitAll())
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .deleteCookies("JSESSIONID", "remember-me") // 🔹 로그아웃 시 쿠키 삭제
      )
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