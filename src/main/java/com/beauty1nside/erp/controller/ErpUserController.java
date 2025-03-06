package com.beauty1nside.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beauty1nside.erp.service.ErpAdminService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ERP 사용 회사 view 처리 관련 컨트롤러
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.17  표하연          최초 생성
 *  2025.02.19  표하연          계약서 페이지 작성
 *  2025.02.20  표하연          구독 정보 확인 페이지 작성
 *
 *  </pre>
*/
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/erp/*")
public class ErpUserController {
	
	/**
     * 기간 구독 결제 페이지
     *
     * @return String
     */
	@GetMapping("/fppay")
	public String fppay() {
		return "erp/user/fpsubscriptionpay";
	}
	
	/**
     * 회사 사용계약서 페이지
     *
     * @return String
     */
	@GetMapping("/usercontact")
	public String usercontact() {
		return "erp/user/userContact";
	}
	
	/**
     * 회사 구독확인 페이지
     *
     * @return String
     */
	@GetMapping("/erpsubinfo")
	public String erpsubinfo() {
		return "erp/user/erpsubinfo";
	}

}
