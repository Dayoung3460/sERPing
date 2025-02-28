package com.beauty1nside.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.beauty1nside.hr.dto.DeptDTO;

@Mapper
public interface DeptMapper {
	
    // 부서 전체 목록 조회
    List<DeptDTO> list(Long companyNum);
    
    // 회사 정보 조회 (회사명, 영문명, 회사번호)
    DeptDTO getCompanyInfo(Long companyNum);
    
    // 부서 추가
    int insertDepartment(DeptDTO dto);
    
    // 특정 부서 조회 (부모 부서 존재 여부 확인용)
    DeptDTO getDepartmentByNum(Long departmentNum);
    
    // 부서 수정
    int updateDepartment(DeptDTO dto);
    
    // 부서에 속한 직원수 조회
    int countEmployeesByDepartment(Long departmentNum);

}
