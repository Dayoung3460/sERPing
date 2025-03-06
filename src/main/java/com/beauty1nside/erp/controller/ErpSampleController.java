package com.beauty1nside.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/erp/*")
public class ErpSampleController {
	//샘플 페이지
	@GetMapping("/sample")
	public String sample() {
		return "erp/sample";
	};
}