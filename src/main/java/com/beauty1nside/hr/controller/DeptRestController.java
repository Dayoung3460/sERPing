package com.beauty1nside.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beauty1nside.hr.dto.DeptDTO;
import com.beauty1nside.hr.service.DeptService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/hr/rest")
public class DeptRestController {
	final DeptService deptService;
	final PasswordEncoder passwordEncoder;
	
	// 부서 목록 조회
	@GetMapping("/dept/list")
    public ResponseEntity<?> list(HttpSession session) {
	    // ✅ 세션에서 `companyNum` 가져오기
	    Long sessionCompanyNum = (Long) session.getAttribute("companyNum");

	    if (sessionCompanyNum == null || sessionCompanyNum <= 0) {
	        return ResponseEntity.status(403).body("잘못된 접근입니다. (세션에 회사번호 없음)");
	    }

	    // ✅ 회사 번호를 기반으로 부서 목록 조회
	    List<DeptDTO> departments = deptService.list(sessionCompanyNum);
	    
	    return ResponseEntity.ok(departments);
	}
	
    // 회사 정보 조회
    @GetMapping("/companyInfo")
    public DeptDTO getCompanyInfo(@RequestParam Long companyNum) {
        return deptService.getCompanyInfo(companyNum);
    }
    
    // 부서 추가 API
    @PostMapping("/dept/add")
    public ResponseEntity<String> addDepartment(@RequestBody DeptDTO dept, HttpSession session) {
    	log.info("session={}",session);
    	Long companyNum =  (Long) session.getAttribute("companyNum"); // 세션에서 companyNum 가져오기
        if (companyNum == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        // ✅ 필수값 검증
        if (dept.getDepartmentName() == null || dept.getDepartmentName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("부서명은 필수 입력 사항입니다.");
        }
        if (dept.getCompanyNum() == null) {
            return ResponseEntity.badRequest().body("회사 번호가 누락되었습니다.");
        }

        // ✅ 부모 부서 존재 여부 검증
        if (dept.getParentDepartmentNum() != null) {
            DeptDTO parentDept = deptService.getDepartmentByNum(dept.getParentDepartmentNum());
            if (parentDept == null) {
                return ResponseEntity.badRequest().body("부모 부서가 존재하지 않습니다.");
            }
        }

        // ✅ DEPARTMENT_TYPE 기본값 설정
        if (dept.getDepartmentType() == null) {
            dept.setDepartmentType("DT001");
        }

        int result = deptService.insertDepartment(dept);
        return result > 0 ? ResponseEntity.ok("부서 추가 성공") : ResponseEntity.badRequest().body("부서 추가 실패");
    }
    
    @GetMapping("/dept/get/{departmentNum}")
    public ResponseEntity<?> getDepartmentByNum(@PathVariable Long departmentNum) {
    	DeptDTO department = deptService.getDepartmentByNum(departmentNum);
        if (department == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 부서를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(department);
    }
    
    //부서 수정
    @PutMapping("/dept/update")
    public ResponseEntity<Map<String, Object>> updateDepartment(@RequestBody DeptDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            deptService.updateDepartment(dto);
            response.put("success", true);
            response.put("message", "부서 정보가 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.put("success", false);
            response.put("message",  " " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ 서버 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ✅ 특정 부서의 직원 수 조회 API
    @GetMapping("/dept/employees/{departmentNum}")
    public ResponseEntity<?> getEmployeeCount(@PathVariable Long departmentNum) {
        int employeeCount = deptService.getEmployeeCountByDept(departmentNum);
        return ResponseEntity.ok(Map.of("employeeCount", employeeCount));
    }
    
}



