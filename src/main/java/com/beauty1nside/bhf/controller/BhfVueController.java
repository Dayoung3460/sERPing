package com.beauty1nside.bhf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/bhf/*")
public class BhfVueController {
	
	// 내 화면에서도 사이드바가 보이게 하는거
	@ModelAttribute
	  public void setAttributes(Model model) {
	    model.addAttribute("menu", "branch");
	  }
	
	//바코드 페이지
	@GetMapping("/barcodeCreate")
	public String barcodeCreate() {
		return "bhf/barcodeCreate";
	};

	//샘플 페이지
	@GetMapping("/")
	public String sample() {
		return "bhf/sample";
	};

	// 상품 발주 요청 페이지
	@GetMapping("/order")
	public String order() {
		return "bhf/orderRequest";
	};
	
	// 상품 발주 조회 페이지
	@GetMapping("/ordList")
	public String orderList() {
		return "bhf/orderList";
	};
	
	// 교환 및 반품 요청 페이지(바코드)
	@GetMapping("/returning")
	public String returnInsert() {
		return "bhf/returnInsert";
	}
	
	// 교환 및 반품 조회 페이지
	@GetMapping("/returnList")
	public String returnList() {
		return "bhf/returnList";
	}
	
	// 상품 입고 페이지
	@GetMapping("/goodsIn")
	public String goodsIn() {
		return "bhf/goodsIn";
	}
	
	// 재고 관리 페이지
	@GetMapping("/inventory")
	public String inventory() {
		return "bhf/inventory";
	}
	
	// 재고 조정 내역
	@GetMapping("/invenHistory")
	public String invenHistory() {
		return "bhf/invenHistory";
	}
	
	// 마감 정산
	@GetMapping("/closing")
	public String closing() {
		return "bhf/closing";
	}
	
}
