package com.beauty1nside.hr.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.hr.dto.EmpSearchDTO;
import com.beauty1nside.hr.dto.SalaryDTO;
import com.beauty1nside.hr.mapper.EmpMapper;
import com.beauty1nside.hr.service.EmpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service // ★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor // 파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class EmpServiceImpl implements EmpService {

	private final EmpMapper empMapper;

	@Override
	public EmpDTO info(Long employeeNum) {
		// TODO Auto-generated method stub
		return empMapper.info(employeeNum);
	}

	@Override
	public List<EmpDTO> list(EmpSearchDTO dto) {
	    return empMapper.list(dto);
	}
	
	// 하위부서 포함 사원 전체조회
	@Override
	public List<EmpDTO> listWithSubDept(EmpSearchDTO dto) {
	        return empMapper.listWithSubDept(dto);

	}
	
	@Override
	public int count(EmpSearchDTO dto) {
		// TODO Auto-generated method stub
		return empMapper.count(dto);
	}
	
	// 하위부서 포함 카운트
	@Override
	public int countForSubDept(EmpSearchDTO dto) {
		// TODO Auto-generated method stub
		return empMapper.countForSubDept(dto);
	}
	

	

	@Override
	public Map<String, Object> getCommonCodes(int companyNum) {
	    Map<String, Object> codes = new HashMap<>();
	    codes.put("departments", empMapper.getDepartments(companyNum));
	    codes.put("positions", empMapper.getPositions());


	    // ✅ 근무 유형을 리스트로 변환하여 반환
	    List<Map<String, String>> employmentTypeList = empMapper.getEmploymentTypes();
	    if (employmentTypeList == null || employmentTypeList.isEmpty()) {
	        employmentTypeList = new ArrayList<>(); // 빈 리스트 반환하여 `null` 방지
	    }
	    codes.put("employmentTypes", employmentTypeList);

	    codes.put("statuses", empMapper.getStatuses());
	    codes.put("auths", empMapper.getAuths());
	    return codes;
	}
	
	@Override
	public String getNewEmployeeId() {
	    // 오늘 날짜 (YYMMDD 형식)
	    String today = new SimpleDateFormat("yyMMdd").format(new Date());

	    // 현재 가장 큰 employee_id의 마지막 3자리 조회
	    String maxEmployeeSeq = empMapper.getMaxEmployeeId();
	    int newSeq = (maxEmployeeSeq != null) ? Integer.parseInt(maxEmployeeSeq) + 1 : 1;

	    // 새 employee_id 생성 (6자리 날짜 + 3자리 증가값)
	    return today + String.format("%03d", newSeq);
	}
	
	@Override
	public boolean checkEmailExists(String email) {
	    return empMapper.checkEmailExists(email) > 0;
	}


    @Override
    public void registerEmployee(EmpDTO empDTO) {
		/*
		 * // 오늘 날짜 기준 (YYMMDD) String today = new SimpleDateFormat("yyMMdd").format(new
		 * Date());
		 * 
		 * // 현재 가장 큰 employee_id 가져오기 String maxEmployeeId = getNewEmployeeId(); int
		 * newSeq = 1;
		 * 
		 * if (maxEmployeeId != null && maxEmployeeId.startsWith(today)) { // 오늘 날짜와 같은
		 * ID가 있다면 마지막 3자리 증가 String lastSeq = maxEmployeeId.substring(6); newSeq =
		 * Integer.parseInt(lastSeq) + 1; }
		 * 
		 * // 새 employee_id 생성 String newEmployeeId = today + String.format("%03d",
		 * newSeq); empDTO.setEmployeeId(newEmployeeId);
		 * 
		 * // ✅ 비밀번호 암호화 (예제: "beauty1nside"를 기본 비밀번호로 설정) if (empDTO.getPosition() ==
		 * null) empDTO.setEmployeePw("0000"); // 기본 비밀번호
		 * 
		 * // ✅ 기본값 설정 (공통 코드 적용) if (empDTO.getPosition() == null)
		 * empDTO.setPosition("PO001"); // 기본 직급 if (empDTO.getAuthority() == null)
		 * empDTO.setAuthority("AU004"); // 기본 권한 if (empDTO.getStatus() == null)
		 * empDTO.setStatus("ST001"); // 재직 상태 if (empDTO.getEmploymentType() == null)
		 * empDTO.setEmploymentType("ET001");
		 */

        // ✅ 사원 ID 중복 확인
        if (isEmployeeIdExists(empDTO.getEmployeeId())) {
            throw new DuplicateKeyException("이미 등록된 사원 ID입니다.");
        }

        // ✅ 이메일 중복 확인
        if (checkEmailExists(empDTO.getEmail())) {
            throw new DuplicateKeyException("이미 등록된 이메일입니다.");
        }

        // ✅ 데이터 저장
        empMapper.insertEmployee(empDTO);
        
        
    }
    
    @Override
    public boolean isEmployeeIdExists(String employeeId) {
        return empMapper.checkEmpIDExists(employeeId) > 0; // ✅ 변환 적용
    }

	@Override
	public List<String> getDepartments(int companyNum) {
		// TODO Auto-generated method stub
		return empMapper.getDepartments(companyNum); // ✅ 기존 방식 유지
	}

	@Override
	public List<Map<String, Object>> getDepartmentList() {
		// TODO Auto-generated method stub
		return empMapper.getDepartmentList(); // ✅ 새로운 방식 추가
	}

	@Override
	public List<Map<String, Object>> getSubDepartments(String departmentNum) {
		// TODO Auto-generated method stub
		return empMapper.getSubDepartments(departmentNum); // ✅ 하위 부서 조회 추가
	}
	
    @Override
    public List<EmpDTO> listByDept(EmpSearchDTO searchDTO) {
        return empMapper.listByDept(searchDTO);
    }
    
    
    @Override
    public List<EmpDTO> getEmployeesByCompanyWithSearch(EmpSearchDTO searchDTO) {
        return empMapper.getEmployeesByCompanyWithSearch(searchDTO);
    }

    @Override
    public int countEmployeesByCompany(EmpSearchDTO searchDTO) {
        return empMapper.countEmployeesByCompany(searchDTO);
    }
    
    
    @Transactional
    @Override
    public void registerContractWithSalary(EmpContractDTO contractDTO, SalaryDTO salaryDTO) {

            // 근로계약 정보 저장
            empMapper.insertContract(contractDTO);
            log.info("📌 계약 등록 완료! 계약 정보: {}", contractDTO);
    }


	@Override
	public List<EmpContractDTO> getContractsByEmployee(Long employeeNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalaryDTO> getSalariesByEmployee(Long employeeNum) {
		// TODO Auto-generated method stub
		return null;
	}





}
