package com.beauty1nside.hr.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beauty1nside.hr.dto.DeptDTO;
import com.beauty1nside.hr.mapper.DeptMapper;
import com.beauty1nside.hr.service.DeptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service // ★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor // 파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class DeptServiceImpl implements DeptService {

	private final DeptMapper deptMapper;
	
    @Override
    public List<DeptDTO> list(Long companyNum) {
        return deptMapper.list(companyNum);
    }

    @Override
    public DeptDTO getCompanyInfo(Long companyNum) {

        return deptMapper.getCompanyInfo(companyNum);
    }
    
    
    @Override
    public Map<String, Object> getOrganization(Long companyNum) {
        List<DeptDTO> departments = deptMapper.list(companyNum);
        DeptDTO companyInfo = deptMapper.getCompanyInfo(companyNum);

        Map<Long, DeptDTO> deptMap = new HashMap<>();
        for (DeptDTO dept : departments) {
            deptMap.put(dept.getDepartmentNum(), dept);
            
            // 🚀 로그 추가 (부서별 직원 수 확인)
            System.out.println("📌 부서명: " + dept.getDepartmentName() + " | 직원 수: " + dept.getEmployeeCount());

            Integer empCount = dept.getEmployeeCount();
            int initialCount = (empCount != null) ? empCount : 0;

            dept.setTotalEmployeeCount(initialCount);
        }

        // ✅ 추가적인 직원 수 계산 없이, SQL에서 가져온 값 그대로 사용
        int noDeptEmployees = deptMapper.countEmployeesWithoutDepartment(companyNum);
        int totalEmployeeCount = departments.stream()
            .mapToInt(DeptDTO::getTotalEmployeeCount)
            .sum();

        if (noDeptEmployees > 0 && companyInfo.getTotalEmployeeCount() != totalEmployeeCount) {
            totalEmployeeCount += noDeptEmployees;
        }

        System.out.println("📌 최종 totalEmployeeCount 값: " + totalEmployeeCount);

        Map<String, Object> result = new HashMap<>();
        result.put("company", companyInfo);
        result.put("departments", departments);
        return result;
    }

    @Override
    public int insertDepartment(DeptDTO dept) {
        int result = deptMapper.insertDepartment(dept);
        System.out.println("📌 INSERT 실행 결과: " + result); // 🔥 로그 확인
        return result;
    }
    
    
    // 특정 부서 조회 (부모 부서 존재 여부 확인용)
    @Override
    public DeptDTO getDepartmentByNum(Long departmentNum) {
        return deptMapper.getDepartmentByNum(departmentNum);
    }
    
    // 부서에 속한 직원 수 조회
    @Override
    public int getEmployeeCountByDept(Long departmentNum) {
        return deptMapper.countEmployeesByDepartment(departmentNum);
    }

	@Override
    public int countEmployeesWithoutDepartment(Long companyNum) {
        return deptMapper.countEmployeesWithoutDepartment(companyNum);
	}
	
	
	@Override
	public int getTotalEmployeeCountByDept(Long departmentNum) {
	    return deptMapper.countTotalEmployeesByDepartment(departmentNum);
	}

    @Transactional
	@Override
	public void updateDepartment(DeptDTO dto) {
        //  부서 존재 여부 확인
        DeptDTO existingDept = deptMapper.getDepartmentByNum(dto.getDepartmentNum());
        if (existingDept == null) {
            throw new IllegalArgumentException("해당 부서가 존재하지 않습니다.");
        }

        //  부서명 & 상태 업데이트 (DeptDTO로 한 번에 전달)
        deptMapper.updateDepartment(dto);

        //  비활성화 처리 시 하위 부서 직원 체크
        if ("DU002".equals(dto.getDepartmentStatus())) { 
            int employeeCount = deptMapper.countTotalEmployeesByDepartment(dto.getDepartmentNum());
            if (employeeCount > 0) {
                throw new IllegalStateException("하위 부서에 사원이 있어 비활성화할 수 없습니다.");
            }
        }

        // 하위 부서 포함 상태 변경
        deptMapper.updateDepartmentStatus(dto);
    }


}
