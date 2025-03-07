	package com.beauty1nside.hr.controller;
	
	import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.beauty1nside.common.FileConfig;
import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.hr.dto.EmpSearchDTO;
import com.beauty1nside.hr.service.EmpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
	
	@Log4j2
	@RestController
	@AllArgsConstructor
	@RequestMapping("/hr/rest")
	public class HrRestController {
		final EmpService empService;
		final PasswordEncoder passwordEncoder;
		final FileConfig fileConfig;
		
		@GetMapping("/emp/list")
		public Object empList(@RequestParam(name = "perPage", defaultValue = "2", required = false) int perPage, 
				@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
				@ModelAttribute EmpSearchDTO dto, HttpSession session) throws JsonMappingException, JsonProcessingException {
			
		    Paging paging = new Paging(); // 🔥 `@ModelAttribute` 사용하지 않고 직접 생성
		    paging.setPageUnit(perPage);
		    paging.setPage(page);
			
			log.info("📥 empList 호출됨");
		    log.info("🔍 검색 DTO 값: {}", dto);
		    
		    // ✅ 세션에서 `companyNum` 가져오기
		    Long sessionCompanyNum = (Long) session.getAttribute("companyNum");
	
		    
		    // ✅ `companyNum`이 없거나 유효하지 않으면 접근 차단
		    if (sessionCompanyNum == null || sessionCompanyNum <= 0) {
		        return ResponseEntity.status(403).body("잘못된 접근입니다. (세션에 회사번호 없음)");
		    }
	
		    // ✅ 검색 DTO에 `companyNum` 설정 (세션 기반)
		    dto.setCompanyNum(sessionCompanyNum);
		    
		    // ✅ 검색 조건이 올바르게 전달되는지 확인
		    log.info("🔎 searchType: {}", dto.getSearchType());
		    log.info("🔎 searchKeyword: {}", dto.getSearchKeyword());
		    log.info("🔎 departmentNum: {}", dto.getDepartmentNum()); // 🔥 확인용
			
			
			// 페이징 유닛 수
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			
			// 페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			// 페이징 처리
		    int totalRecords = empService.count(dto);
		    paging.setTotalRecord(totalRecords);
			log.info("📊 총 레코드 수: {}", empService.count(dto));
			
			List<EmpDTO> empList = empService.list(dto);
			
			// grid 배열 처리
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), totalRecords, empList);
			return ResponseEntity.ok().body(result);
		}
		
	    @GetMapping("/emp/common-codes/{companyNum}")
	    public ResponseEntity<Map<String, Object>> getCommonCodes(@PathVariable(name="companyNum") int companyNum) {
	        Map<String, Object> result = empService.getCommonCodes(companyNum);
	        return ResponseEntity.ok(result);
	    }	
	    
	 
	    
	    // 🔹 사원 등록 API
	    @PostMapping("/emp/register")
	    public ResponseEntity<Map<String, String>> registerEmployee(EmpDTO empDTO,
	    											   @RequestPart(value = "image", required = false) MultipartFile file, HttpSession session) {
	    	
	    	log.info("empDTO={}",empDTO);
	    	log.info("ssnFirstPart={}",empDTO.getSsn());
	    	
	        // ✅ 세션에서 `companyNum` 가져오기
	        Long sessionCompanyNum = (Long) session.getAttribute("companyNum");
	        
	        // ✅ `companyNum`이 없거나 유효하지 않으면 접근 차단
	        if (sessionCompanyNum == null || sessionCompanyNum <= 0) {
	            return ResponseEntity.status(403).body(Map.of("error", "잘못된 접근입니다. (세션에 회사번호 없음)"));
	        }
	
	        // ✅ DTO의 `companyNum`을 세션 값으로 설정 (보안 강화)
	        empDTO.setCompanyNum(sessionCompanyNum);
	
	        
	        // ✅ 한글 포함 여부 확인
	        if (!empDTO.getEmployeeId().matches("^[A-Za-z0-9_]+$")) {
	            return ResponseEntity.status(400).body(Map.of("error", "사원 ID는 영문, 숫자, 언더바(_)만 사용할 수 있습니다."));
	        }
	    	
	     // 주민등록번호 입력 검증
	        if (empDTO.getFirstSsn() == null || empDTO.getFirstSsn().isBlank() ||
	            empDTO.getSecondSsn() == null || empDTO.getSecondSsn().isBlank()) {
	            log.warn("🚨 주민번호가 입력되지 않음, 등록 중단!");
	            return ResponseEntity.status(400).body(Map.of("error", "주민등록번호를 입력해주세요."));
	        }

	        // 🔹 길이 검증 추가
	        if (!empDTO.getFirstSsn().matches("\\d{6}") || !empDTO.getSecondSsn().matches("\\d{7}")) {
	            log.warn("🚨 주민번호 형식 오류: 앞자리={}, 뒷자리={}", empDTO.getFirstSsn(), empDTO.getSecondSsn());
	            return ResponseEntity.status(400).body(Map.of("error", "주민등록번호는 앞 6자리, 뒤 7자리 숫자로 입력해주세요."));
	        }

	        // ✅ 주민번호 암호화 저장
	        String newSsn = empDTO.getFirstSsn() + "-" + passwordEncoder.encode(empDTO.getSecondSsn());
	        empDTO.setSsn(newSsn);
	    	
	    	//비밀번호: 생년월일 8자리
	    	String ssnFirstPart = empDTO.getFirstSsn();
	    	log.info("ssnFirstPart={}",ssnFirstPart);
	    	ssnFirstPart = passwordEncoder.encode(ssnFirstPart);
	    	log.info("암호화한 ssnFirstPart={}",ssnFirstPart);
	    	empDTO.setEmployeePw(ssnFirstPart);
	    	
	    	// ✅ 주소 합치기: null 체크 후 빈 문자열("") 처리
	    	String address = empDTO.getAddress() != null ? empDTO.getAddress().trim() : "";
	    	String addressDetail = empDTO.getAddressDetail() != null ? empDTO.getAddressDetail().trim() : "";

	    	// ✅ addressDetail이 존재하면 괄호 포함, 아니면 빈 값으로 저장
	    	if (!address.isEmpty()) {
	    	    empDTO.setAddress(!addressDetail.isEmpty() ? address + " (" + addressDetail + ")" : address);
	    	} else {
	    	    empDTO.setAddress(""); // 🚨 완전히 비어있는 값 설정 (null 방지)
	    	}

	    	log.info("변경된 empDTO={}", empDTO);

	    	
	        // ✅ 파일이 존재하는 경우에만 업로드 수행
	        if (file != null && !file.isEmpty()) {
	            try {
	            	// c:에 저장
	            	String imgPath = fileConfig.getUploadpath();
	                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	                Path uploadPath = Paths.get(imgPath + fileName);
	                Files.createDirectories(uploadPath.getParent());
	                Files.write(uploadPath, file.getBytes());
	                
	                // 이미지 경로이름 
	                String imageUrl = fileConfig.getImgpath();
	                empDTO.setProfileImage(imageUrl + fileName);
	            } catch (Exception e) {
	                log.error("❌ 파일 업로드 실패:", e);
	                return ResponseEntity.status(500).body(Map.of("error", "파일 업로드 실패"));
	            }
	        } else {
	            //log.info("🚨 프로필 이미지 없음, 기본 이미지 적용");
	            //empDTO.setProfileImage("/file/image/mypage/profile/noProfileImg.jpg"); // 기본 이미지 설정
	            log.info("🚨 프로필 이미지 없음, `null`로 설정");
	            empDTO.setProfileImage(null); // 기본 이미지 설정을 없애고 `null` 저장
	        }
	
	        // ✅ 사원 등록 실행
	        try {
	            empService.registerEmployee(empDTO);
	            return ResponseEntity.ok(Map.of("message", "사원 등록 성공!"));
	        } catch (DuplicateKeyException e) {
	            String errorMessage = e.getMessage(); // 예외 메시지 가져오기
	            log.error("❌ 중복 오류 발생: {}", errorMessage);

	            if (errorMessage.contains("사원 ID")) {
	                return ResponseEntity.status(409).body(Map.of("error", "이미 등록된 사원 ID입니다."));
	            } else if (errorMessage.contains("이메일")) {
	                return ResponseEntity.status(409).body(Map.of("error", "이미 등록된 이메일입니다."));
	            }

	            return ResponseEntity.status(409).body(Map.of("error", "중복된 데이터가 존재합니다."));
	        } catch (Exception e) {
	            log.error("❌ 사원 등록 실패:", e);
	            return ResponseEntity.status(500).body(Map.of("error", "등록 중 오류 발생"));
	        }
	    }
	    
	    @GetMapping("/emp/new-employee-id")
	    public ResponseEntity<String> getNewEmployeeId() {
	        String newEmployeeId = empService.getNewEmployeeId(); // 새 사원번호 가져오기
	        return ResponseEntity.ok(newEmployeeId);
	    }
	    
	    @GetMapping("/emp/departments/{companyNum}")
	    public ResponseEntity<List<String>> getDepartments(@PathVariable(name="companyNum") int companyNum) {
	        return ResponseEntity.ok(empService.getDepartments(companyNum));
	    }
	
	
	    // ✅ 새로운 전체 부서 목록 조회 (부서번호 + 부서이름 반환)
	    @GetMapping("/emp/department-list")
	    public ResponseEntity<List<Map<String, Object>>> getDepartmentList() {
	        return ResponseEntity.ok(empService.getDepartmentList());
	    }
	
	    // ✅ 하위 부서 목록 조회
	    @GetMapping("/emp/sub-departments")
	    public ResponseEntity<List<Map<String, Object>>> getSubDepartments(@RequestParam("departmentNum") String departmentNum) {
	        return ResponseEntity.ok(empService.getSubDepartments(departmentNum));
	    }
	    
	    // ✅ 특정 부서의 사원 목록 조회 API
	    @GetMapping("/emp/{departmentNum}")
	    public List<EmpDTO> getEmployeesByDept(
	            @PathVariable Long departmentNum,
	            @RequestParam(defaultValue = "1") int page,
	            @RequestParam(defaultValue = "5") int size
	    ) {
	        EmpSearchDTO searchDTO = new EmpSearchDTO();
	        searchDTO.setDepartmentNum(departmentNum);
	        searchDTO.setStart((page - 1) * size + 1);
	        searchDTO.setEnd(page * size);
	        
	        return empService.listByDept(searchDTO);	
	    }
	    
	    
	    @GetMapping("/emp/organization")
	    public Object getEmployeesForOrganization(
	            @RequestParam(defaultValue = "1") int page,
	            @RequestParam(defaultValue = "5") int perPage,
	            @ModelAttribute EmpSearchDTO dto,
	            Paging paging
	    ) throws JsonMappingException, JsonProcessingException {
	        
	        paging.setPageUnit(perPage);
	        paging.setPage(page);
	        
	        // 페이징 조건
	        dto.setStart(paging.getFirst());
	        dto.setEnd(paging.getLast());
	        
	        //페이징처리
	        paging.setTotalRecord(empService.countForSubDept(dto));
	        
	        log.info("dto::::::::::::::{}",dto);
	        log.info("paging:::::::::::::{}",paging);
	        
	        //grid 배열 처리
	        GridArray grid = new GridArray();
	        Object result = grid.getArray(paging.getPage(), paging.getTotalRecord(), empService.listWithSubDept(dto));
	        return result;
	
	    }
	
	 // ✅ 특정 회사(companyNum)에 속한 사원 목록 조회 API (사원명 검색 + 페이징 적용)
	    @GetMapping("/emp/list-by-company")
	    public ResponseEntity<Map<String, Object>> getEmployeesByCompany(
	            @RequestParam("companyNum") Long companyNum,
	            //@RequestParam(name = "departmentNum", required = false) Long departmentNum,
	            @RequestParam(name = "perPage", defaultValue = "10") int perPage,
	            @RequestParam(name = "page", defaultValue = "1") int page,
	            @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
	
	        // DTO 설정
	        EmpSearchDTO searchDTO = new EmpSearchDTO();
	        searchDTO.setCompanyNum(companyNum);
	        //searchDTO.setDepartmentNum(departmentNum);
	        searchDTO.setSearchType("employeeName");  // ✅ 사원명 검색으로 고정
	        searchDTO.setSearchKeyword(searchKeyword); // 검색어 설정
	        searchDTO.setStart((page - 1) * perPage + 1);
	        searchDTO.setEnd(page * perPage);
	
	        // ✅ 검색된 사원 수
	        int totalRecords = empService.countEmployeesByCompany(searchDTO);
	        
	        // ✅ 검색된 사원 목록
	        List<EmpDTO> employees = empService.getEmployeesByCompanyWithSearch(searchDTO);
	
	        // 결과 맵핑
	        Map<String, Object> result = new HashMap<>();
	        result.put("totalRecords", totalRecords);
	        result.put("employees", employees);
	
	        return ResponseEntity.ok(result);
	    }
	
		/*
		 * // ✅ 근로계약 및 급여 등록 API
		 * 
		 * @PostMapping("/contract/register") public ResponseEntity<String>
		 * registerContract(@RequestBody Map<String, Object> requestData) {
		 * log.info("📥 계약 정보 수신: {}", requestData);
		 * 
		 * try { ObjectMapper objectMapper = new ObjectMapper();
		 * 
		 * // ✅ 1. 근로 계약 정보 변환 EmpContractDTO contractDTO =
		 * objectMapper.convertValue(requestData, EmpContractDTO.class);
		 * log.info("📌 변환된 계약 DTO: {}", contractDTO);
		 * 
		 * // ✅ 2. 급여 정보 추출 (필요한 필드만 포함하도록 별도 생성) SalaryDTO salaryDTO = new SalaryDTO();
		 * salaryDTO.setEmployeeNum(contractDTO.getEmployeeNum());
		 * salaryDTO.setContractNum(contractDTO.getContractNum());
		 * salaryDTO.setCompanyNum(contractDTO.getCompanyNum());
		 * salaryDTO.setMonthlySalary(contractDTO.getAnnualSalary() / 12);
		 * salaryDTO.setBonus(contractDTO.getBonus());
		 * salaryDTO.setAdditionalPay(contractDTO.getAdditionalPay());
		 * salaryDTO.setSalaryPaymentDate(contractDTO.getSalaryPaymentDate());
		 * salaryDTO.setPaymentMethod(contractDTO.getPaymentMethod());
		 * 
		 * log.info("📌 변환된 급여 DTO: {}", salaryDTO);
		 * 
		 * // ✅ 3. 서비스 호출 (근로계약 + 급여 함께 저장)
		 * empService.registerContractWithSalary(contractDTO, salaryDTO);
		 * 
		 * return ResponseEntity.ok("✅ 계약 등록 완료!"); } catch (Exception e) {
		 * log.error("❌ 계약 등록 실패:", e); return
		 * ResponseEntity.status(500).body("계약 등록 중 오류 발생: " + e.getMessage()); } }
		 */
	
	
	
	
		/*
		 * // ✅ 특정 사원의 계약 목록 조회
		 * 
		 * @GetMapping("/contract/{employeeNum}") public
		 * ResponseEntity<List<EmpContractDTO>> getContractsByEmployee(@PathVariable
		 * Long employeeNum) { return
		 * ResponseEntity.ok(empService.getContractsByEmployee(employeeNum)); }
		 * 
		 * // ✅ 특정 사원의 급여 내역 조회
		 * 
		 * @GetMapping("/salary/{employeeNum}") public ResponseEntity<List<SalaryDTO>>
		 * getSalariesByEmployee(@PathVariable Long employeeNum) { return
		 * ResponseEntity.ok(empService.getSalariesByEmployee(employeeNum)); }
		 */
	    
	    @GetMapping("/emp/check-employee-id")
	    public ResponseEntity<Map<String, Object>> checkEmployeeId(@RequestParam("employeeId") String employeeId) {
	        boolean exists = empService.isEmployeeIdExists(employeeId);

	        if (exists) {
	            return ResponseEntity.status(409).body(Map.of("error", "이미 사용 중인 사원 ID입니다."));
	        }
	        return ResponseEntity.ok(Map.of("message", "사용 가능한 사원 ID입니다."));
	    }
	    
	}
