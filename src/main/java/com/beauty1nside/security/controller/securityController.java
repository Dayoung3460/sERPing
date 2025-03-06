package com.beauty1nside.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@AllArgsConstructor
public class securityController {
  @GetMapping("/no-permission")
  public String noPermission() {
    return "security/no_permission";
  }
}
