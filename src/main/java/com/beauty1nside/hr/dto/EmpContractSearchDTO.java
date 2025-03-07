package com.beauty1nside.hr.dto;

import lombok.Data;

@Data
public class EmpContractSearchDTO {
    private int start;      // 시작 페이지
    private int end;        // 끝 페이지
    private int pageUnit;   // 한 페이지당 보여줄 계약 수

    private String searchType;     // 검색 기준 (사원명, 부서명 등)
    private String searchKeyword;  // 검색어
    private Long departmentNum;    // 부서 번호
    private String departmentName; // 부서명
    private Long parentDepartmentNum; // ✅ 부모 부서 번호 (하위 부서 검색용)
    private String contractType;   // 계약 유형 (정규직, 계약직 등)
    private String contractStatus; // 계약 상태 (활성, 만료 등)
    private String startDate;      // 검색 기간 시작일
    private String endDate;        // 검색 기간 종료일
    private Long companyNum;       // 회사 번호 (세션에서 가져올 값)

    // ✅ 하위 부서 포함 여부 추가
    private boolean includeSubDepartments;

    // ✅ setter를 통해 Null 값 처리 (필요한 경우)
    public void setDepartmentNum(Long departmentNum) {
        this.departmentNum = (departmentNum != null) ? departmentNum : null;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = (departmentName != null && !departmentName.isEmpty()) ? departmentName : null;
    }
}
