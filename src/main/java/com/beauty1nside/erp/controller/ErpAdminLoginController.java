package com.beauty1nside.erp.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beauty1nside.erp.dto.ErpEmployeeDTO;
import com.beauty1nside.erp.service.ErpAdminLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ERP 제공 회사의 직원들이 관리자 페이지에 로그인한다
 * REST API로 하여도 되나 전통적 방식을 해보기로 함
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.16
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.16  표하연          최초 생성
 *
 *  </pre>
*/
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/erp/*")
public class ErpAdminLoginController {
	
	/**
     * ErpAdminLogin 맵퍼를 사용하기 위한 의존성 주입 {서비스}
     */
	private final ErpAdminLoginService erpAdminLoginService;
	/**
     * PasswordEncoder 비밀번호 암호화 코드 주입
     */
	private final PasswordEncoder passwordEncoder;
	
	/**
     * ERP 관리자 로그인 페이지 연결
     * csrf 토큰이 필요해서 모델과 리퀘스트가 들어감
     *
     * @param Model
     * @param HttpServletRequest
     * @param HttpSession
     * @return String
     */
	@GetMapping("/login")
	public String layout(Model model, HttpServletRequest request, HttpSession session) {
		
		// erp 사용업체는 보안 인증이 꼭 필요하다고 해서 동일 프로젝트 내부여서 토큰 삽입
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
        
        //로그인 하면 세션 받아옴
        ErpEmployeeDTO employee = (ErpEmployeeDTO) session.getAttribute("ErpEmployeeInfo");
        
        //세션 있으면 로그인페이지
        if (employee != null) {
            return "redirect:admin"; // ✅ 로그인한 상태에서는 관리자 페이지로 이동
        }
        
        return "erp/login"; // 관리자 페이지 반환
	}
	
	/**
     * ERP 관리자 로그인 확인
     *
     * @param String
     * @param String
     * @param Model
     * @param HttpSession
     * @return String
     */
	@PostMapping("/login")
	 public String login(@RequestParam String employeeId, 
			 			@RequestParam String employeePw, 
			 			Model model,
			 			HttpSession session) {
		
		//관리하는 회사들 정보를 업데이트 한다 역정규화로 발생한 처리
		//스케쥴러로 매일 밤 12시에 1번만 하게 해야하는데 임시방편으로 사용중
		erpAdminLoginService.companystateupdate();
		
		//비밀번호 암호화 (입력되는 비밀번호는 암호화 안해도 됨)
		//employeePw = passwordEncoder.encode(employeePw);
		//log.info(employeePw);
		
		if(erpAdminLoginService.loginCount(employeeId)) {
			ErpEmployeeDTO dto = erpAdminLoginService.loginConfig(employeeId);
			if (passwordEncoder.matches(employeePw, dto.getEmployeePw())) {
				log.info("로그인 성공: {}", employeeId);
				session.setAttribute("ErpEmployeeInfo", dto);
				return "redirect:admin";
		    } else {
		        log.warn("비밀번호 불일치: {}", employeeId);
		        model.addAttribute("loginResult", "실패");
		        return "erp/login";
		    }
		}else {
			return "erp/login";
		}
	}
	
	/**
     * ERP 관리자 로그 아웃
     *
     * @param HttpSession
     * @return String
     */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 세션 제거 (완전 초기화)
	    session.invalidate();
	    return "redirect:login";
	}
}
