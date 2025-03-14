package com.beauty1nside.accnut.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.beauty1nside.accnut.service.AssetService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/accnut/*")
public class AccnutSampleController {

	// 내 화면에서도 사이드바가 보이게 하는거
    @ModelAttribute
      public void setAttributes(Model model) {
        model.addAttribute("menu", "account");
      }
	
	//샘플 페이지
	@GetMapping("/")
	public String sample() {
		return "accnut/sample";
	};
	
	final AssetService assetService;
	
//	----------------------------------------------------------------------------
	
	//자산관리 페이지
	@GetMapping("/asset")
	public String asset(Model model) {
		
		//String abc = "a1000";
		//model.addAttribute("info", assetService.info(abc) );
		
		return "accnut/asset";
	};
	
	//채무 관리 페이지
	@GetMapping("/debt")
	public String debt() {
		return "accnut/debt";
	};
	
	//거래 대장 등록 페이지
	@GetMapping("/book_insert")
	public String book_insert() {
		return "accnut/book_insert";
	};
	
	//거래 대장 조회 페이지
	@GetMapping("/book_select")
	public String book_select() {
		return "accnut/book_select";
	};
	
	//급여 처리 페이지
	@GetMapping("/salary")
	public String salary() {
		return "accnut/salary";
	};
	
	//미지급금 처리 페이지
	@GetMapping("/unpay")
	public String unpay() {
		return "accnut/unpay";
	};
	
	//기타 지급 처리 페이지
	@GetMapping("/etc_pay")
	public String etc_pay() {
		return "accnut/etc_pay";
	};
	
	//부대 비용 처리 페이지
	@GetMapping("/incidental_cost")
	public String incidental_cost() {
		return "accnut/incidental_cost";
	};
	
	//세금계산서 발행 페이지
	@GetMapping("/tax_insert")
	public String tax_insert() {
		return "accnut/tax_insert";
	};
	
	//세금계산서 조회 페이지
	@GetMapping("/tax_select")
	public String tax_select() {
		return "accnut/tax_select";
	};
	
	//매출 조회 페이지
	@GetMapping("/selling_select")
	public String selling_select() {
		return "accnut/selling_select";
	};
	
	//매출 현황 페이지
	@GetMapping("/selling_status")
	public String selling_status() {
		return "accnut/selling_status";
	};
	
	
	// 여기서 부터는 테스트 페이징
	@GetMapping("test")
	public String test(Model model) {
//		model.addAttribute("list", assetService.list());
		return "accnut/test";
	}
	
	
	// 결제 테스트
	@GetMapping("checkout")
	public String accTest(Model model) {
//		model.addAttribute("list", assetService.list());
		return "accnut/checkout";
	}
	
	@GetMapping("success")
	public ModelAndView successTest(@RequestParam String paymentType, @RequestParam String orderId, @RequestParam String paymentKey, @RequestParam int amount) {
		ModelAndView modelAndView = new ModelAndView("accnut/success");
		modelAndView.addObject("paymentType", paymentType);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("paymentKey", paymentKey);
        modelAndView.addObject("amount", amount);
        return modelAndView;
	}
	
	@GetMapping("fail")
	public ModelAndView failTest(@RequestParam String message, @RequestParam String code) {
		ModelAndView modelAndView = new ModelAndView("accnut/fail");
		modelAndView.addObject("message", message);
        modelAndView.addObject("code", code);
        return modelAndView;
	}
	
	// 계좌 조회 할 페이지 => 실패함 accessToken이 없음
	@GetMapping("accTest")
	public String acc1Test(Model model) {
//		model.addAttribute("list", assetService.list());
		return "accnut/accTest";
	}
	
	
	
}