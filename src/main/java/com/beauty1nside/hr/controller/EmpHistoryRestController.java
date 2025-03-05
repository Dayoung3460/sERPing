package com.beauty1nside.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beauty1nside.common.Paging;
import com.beauty1nside.hr.dto.EmpHistoryDTO;
import com.beauty1nside.hr.dto.EmpHistorySearchDTO;
import com.beauty1nside.hr.service.EmpHistoryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/hr/rest")
public class EmpHistoryRestController {
	final EmpHistoryService empHistoryService;
	
    // 인사발령 등록
    @PostMapping("/history/register")
    public ResponseEntity<String> registerHistory(@RequestBody EmpHistoryDTO historyDTO) {
        log.info("📌 인사발령 등록 요청: {}", historyDTO);

        // 세션에서 로그인된 사용자 가져오기
        Long loggedInUserNum = getLoggedInUserNum();
        Long loggedInCompanyNum = getLoggedInCompanyNum();

        if (loggedInUserNum == null || loggedInCompanyNum == null) {
            return ResponseEntity.status(403).body("❌ 접근 권한이 없습니다.");
        }

        // 회사번호 검증 (현재 로그인된 회사 소속인지 확인)
        if (!loggedInCompanyNum.equals(historyDTO.getCompanyNum())) {
            return ResponseEntity.status(403).body("❌ 해당 회사의 인사발령만 등록 가능합니다.");
        }


        empHistoryService.insertHistory(historyDTO);
        return ResponseEntity.ok("인사발령 등록 완료!");
    }

    // 인사발령 목록 조회 (페이징)
    @GetMapping("/history/list")
    public ResponseEntity<Map<String, Object>> listHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @ModelAttribute EmpHistorySearchDTO searchDTO,
            Paging paging) {

        log.info("📌 인사발령 목록 조회 요청: {}", searchDTO);

        // 로그인된 사용자 정보 가져오기
        Long loggedInCompanyNum = getLoggedInCompanyNum();
        if (loggedInCompanyNum == null) {
            return ResponseEntity.status(403).body(null);
        }

        searchDTO.setCompanyNum(loggedInCompanyNum);
        paging.setPageUnit(perPage);
        paging.setPage(page);
        searchDTO.setStart(paging.getFirst());
        searchDTO.setEnd(paging.getLast());

        // 총 개수 조회
        int totalRecords = empHistoryService.countHistory(searchDTO);
        paging.setTotalRecord(totalRecords);

        // 인사발령 리스트 조회
        List<EmpHistoryDTO> historyList = empHistoryService.listHistory(searchDTO);

        // 결과 데이터 반환
        Map<String, Object> result = new HashMap<>();
        result.put("totalRecords", totalRecords);
        result.put("historyList", historyList);

        return ResponseEntity.ok(result);
    }

    // 인사발령 승인
    @PutMapping("/approve/{historyNum}")
    public ResponseEntity<String> approveHistory(@PathVariable Long historyNum) {
        log.info("📌 인사발령 승인 요청: historyNum={}", historyNum);

        Long loggedInUserNum = getLoggedInUserNum();
        if (loggedInUserNum == null) {
            return ResponseEntity.status(403).body("❌ 승인 권한이 없습니다.");
        }

        empHistoryService.approveHistory(historyNum, loggedInUserNum);
        return ResponseEntity.ok("인사발령 승인 완료!");
    }

    // 인사발령 반려
    @PutMapping("/reject/{historyNum}")
    public ResponseEntity<String> rejectHistory(
            @PathVariable Long historyNum,
            @RequestParam("rejectReason") String rejectReason) {
        
        log.info("📌 인사발령 반려 요청: historyNum={}, 이유={}", historyNum, rejectReason);

        Long loggedInUserNum = getLoggedInUserNum();
        if (loggedInUserNum == null) {
            return ResponseEntity.status(403).body("❌ 반려 권한이 없습니다.");
        }

        empHistoryService.rejectHistory(historyNum, loggedInUserNum, rejectReason);
        return ResponseEntity.ok("인사발령 반려 완료!");
    }

    // 로그인한 사용자의 사원번호 가져오기
    private Long getLoggedInUserNum() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername() != null ? Long.parseLong(((UserDetails) principal).getUsername()) : null;
        }
        return null;
    }

    // 로그인한 사용자의 회사번호 가져오기
    private Long getLoggedInCompanyNum() {
        // 세션이나 JWT에서 회사번호 가져오는 로직 추가 필요
        return 1L; // 🔹 임시 값 (실제 세션에서 가져오도록 변경)
    }
}

