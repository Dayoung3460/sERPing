package com.beauty1nside.erp.controller;

import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beauty1nside.erp.dto.testDTO;
import com.beauty1nside.erp.service.ErpAdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ERP 회사 관리 사용하는 정보를 CURD한다
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.12  표하연          최초 생성
 *  2025.03.02  표하연          문의페이지 작성
 *
 *  </pre>
*/
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/erp/*")
public class ErpAdminController {
	
	/**
     * ErpAdminMapper 맵퍼를 사용하기 위한 의존성 주입 {서비스}
     */
	private final ErpAdminService erpAdminService;
	
	/**
     * DB연결 확인을 위하여 샘플 데이터를 조회
     *
     * @return String
     */
	@GetMapping("dbtest")
	public String dbtest(Model model) {
		List<testDTO> list = erpAdminService.test();
		log.info(list.toString());
		model.addAttribute("list", list);
		return "erp/dbtest";
	}
	
	/**
     * ERP 관리자 페이지 연결
     * csrf 토큰이 필요해서 모델과 리퀘스트가 들어감
     *
     * @param Model
     * @param HttpServletRequest
     * @param HttpSession
     * @return String
     */
	@GetMapping("/admin")
	public String layout(Model model, HttpServletRequest request, HttpSession session) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
        
        //서버단에서 사전 차단
        Object ErpEmployeeInfo = session.getAttribute("ErpEmployeeInfo");
        // 세션이 없으면 로그인 페이지로 리다이렉트
        if (ErpEmployeeInfo == null) {
            return "redirect:login";
        }
        
        return "erp/admin";
	}
	
	/**
     * ERP 관리자 문의 페이지 연결
     *
     * @param Model
     * @param HttpServletRequest
     * @param HttpSession
     * @return String
     */
	@GetMapping("/cs")
	public String cs(Model model, HttpServletRequest request, HttpSession session) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
        //서버단에서 사전 차단
        Object ErpEmployeeInfo = session.getAttribute("ErpEmployeeInfo");
        // 세션이 없으면 로그인 페이지로 리다이렉트
        if (ErpEmployeeInfo == null) {
            return "redirect:login";
        }
        return "erp/cs";
	}
	
	/**
     * ERP 팀 소개
     *
     * @return String
     */
	@GetMapping("/team")
	public String team() {
		return "erp/spectator/team";
	}
	
	/**
     * ERP 가격 정보
     *
     * @return String
     */
	@GetMapping("/pricing")
	public String pricing() {
		return "erp/spectator/pricing";
	}
	
	/**
     * ERP 문의
     *
     * @return String
     */
	@GetMapping("/contact")
	public String contact() {
		return "erp/spectator/contact";
	}
	
	/**
     * ERP 소개 페이지 연결
     *
     * @return String
     */
	@GetMapping("/main")
	public String main() {
		return "erp/spectator/main";
	}

}
