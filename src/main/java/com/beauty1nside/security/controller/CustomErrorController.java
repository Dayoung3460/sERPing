package com.beauty1nside.security.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
  
  @RequestMapping("/error")
  public String handleError() {
    return "login/login";
  }
}
