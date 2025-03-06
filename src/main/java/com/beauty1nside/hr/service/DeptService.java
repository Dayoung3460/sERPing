package com.beauty1nside.hr.service;

import java.util.List;
import java.util.Map;

import com.beauty1nside.hr.dto.DeptDTO;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.hr.dto.EmpSearchDTO;


public interface DeptService {
	
    // 부서 목록 조회
    List<DeptDTO> list(Long companyNum);

    // 회사 정보 조회
    DeptDTO getCompanyInfo(Long companyNum);
    
    Map<String, Object> getOrganization(Long companyNum);
    
    // 부서 추가
    int insertDepartment(DeptDTO dept);
    
    // 특정 부서 조회 (부모 부서 존재 여부 확인용)
    DeptDTO getDepartmentByNum(Long departmentNum);
    
    // 부서 수정
    void updateDepartment(DeptDTO dto);
    
    // 부서에 속한 직원 수 조회
    int getEmployeeCountByDept(Long departmentNum);
    

    // 부서가 없는 직원 수 조회
    int countEmployeesWithoutDepartment(Long companyNum);
    
    // 하위부서까지 포함한 직원 수 조회
    int  getTotalEmployeeCountByDept(Long departmentNum);

}