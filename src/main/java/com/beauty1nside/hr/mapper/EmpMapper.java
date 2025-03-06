package com.beauty1nside.hr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.hr.dto.EmpSearchDTO;
import com.beauty1nside.hr.dto.SalaryDTO;

@Mapper
public interface EmpMapper {
	
	EmpDTO info(Long employeeNum);
	List<EmpDTO> list(EmpSearchDTO dto);
	List<EmpDTO> listWithSubDept(EmpSearchDTO dto); // 하위 부서 포함 사원 전체 조회
	int count(EmpSearchDTO dto);
	int countForSubDept(EmpSearchDTO dto); // 하위부서 포함 카운트
	
    List<String> getDepartments(int companyNum); // 부서 목록
    List<Map<String, Object>> getDepartmentList(); // ✅ 새로 추가 (부서번호 + 부서이름 포함)
    List<Map<String, Object>> getSubDepartments(String departmentNum); // ✅ 하위 부서 조회 추가
    List<String> getPositions(); // 직급 목록
    List<Map<String, String>> getEmploymentTypes(); // 근무 유형 목록
    List<String> getStatuses(); // 재직 상태 목록
    List<String> getAuths(); // 권한 목록
    
    // 가장 최근 등록된 사원 ID 조회
    String getMaxEmployeeId(); 
    
    // 사원 등록
    void insertEmployee(EmpDTO empDTO);
    
    // 이메일 중복 체크 추가
    int checkEmailExists(String email);
    
    // 사원 ID 중복 체크
    int checkEmpIDExists(String employeeId);
    
    // 특정 부서의 사원 목록 가져오기 (부서 이름 포함)
    List<EmpDTO> listByDept(EmpSearchDTO dto);

 // ✅ 특정 회사(companyNum)의 사원 목록 (검색 & 페이징 적용)
    List<EmpDTO> getEmployeesByCompanyWithSearch(EmpSearchDTO searchDTO);

    // ✅ 특정 회사(companyNum)의 사원 개수 조회
    int countEmployeesByCompany(EmpSearchDTO searchDTO);
    
    
    // ✅ 계약 정보 삽입 (계약번호 자동 생성)
    void insertContract(EmpContractDTO contractDTO);
    
    Long getLastInsertedContractNum(Long employeeNum);  // 추가

    // ✅ 급여 정보 삽입
    void insertSalary(SalaryDTO salaryDTO);

    // ✅ 특정 사원의 계약 목록 조회
    List<EmpContractDTO> getContractsByEmployee(Long employeeNum);

    // ✅ 특정 사원의 급여 내역 조회
    List<SalaryDTO> getSalariesByEmployee(Long employeeNum);


}
