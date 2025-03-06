package com.beauty1nside.login;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2  //log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
public class LoginController {
  
  @GetMapping("/login")
  public String login() {
    // 현재 로그인한 사용자 정보 가져오기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
    System.out.println("auth != null = " + (auth != null));
    System.out.println("auth.isAuthenticated() = " + auth.isAuthenticated());
    System.out.println("auth.getPrincipal() = " + auth.getPrincipal());
    
    // 로그인 상태인지 확인
//    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
//    if (auth != null && auth.isAuthenticated()) {
//      return "/index"; // 로그인된 상태면 홈으로 이동
//    }
//
    return "login/login"; // 로그인되지 않았다면 로그인 페이지 반환
  }
  
  @PostMapping("/login")
  public String login(@RequestParam String employeeId, @RequestParam String employeePw) {
    System.out.println("입력된 employeeId: " + employeeId);
    System.out.println("입력된 employeePw: " + employeePw);
    return "index";
  }
  
}
