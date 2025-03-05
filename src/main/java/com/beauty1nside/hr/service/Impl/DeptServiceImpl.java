package com.beauty1nside.hr.service.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

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
        log.info("departmentsdepartmentsdepartments={}",departments.size());

        Map<Long, DeptDTO> deptMap = new HashMap<>();
        for (DeptDTO dept : departments) {
            deptMap.put(dept.getDepartmentNum(), dept);
            dept.setTotalEmployeeCount(dept.getEmployeeCount());
        }

        // 🚨 방문한 부서 체크 (중복 방지)
        Set<Long> visitedDepartments = new HashSet<>();
        for (DeptDTO dept : departments) {
            if (!visitedDepartments.contains(dept.getDepartmentNum())) {
                addEmployeeCountToParent(dept, deptMap, visitedDepartments);
            }
        }

        // ✅ 부서 없는 직원 수 조회
        int noDeptEmployees = deptMapper.countEmployeesWithoutDepartment(companyNum);

        // 🚨 부서 직원 합계만 계산 후, **부서 없는 직원만 따로 더하기**
        int totalEmployeeCount = departments.stream()
            .mapToInt(DeptDTO::getTotalEmployeeCount)
            .sum() + noDeptEmployees;

        //companyInfo.setTotalEmployeeCount(departments.size());
        
        // ✅ 로그 출력 (디버깅)
        System.out.println("📌 최종 totalEmployeeCount 값: " + totalEmployeeCount);

        // 4️⃣ 반환 데이터 구성
        Map<String, Object> result = new HashMap<>();
        result.put("company", companyInfo);
        result.put("departments", departments);
        return result;
    }
    
    /**
     * 🔥 하위 부서 직원 수를 상위 부서에 재귀적으로 추가하는 메서드 (중복 방지)
     */
    private void addEmployeeCountToParent(DeptDTO dept, Map<Long, DeptDTO> deptMap, Set<Long> visited) {
        if (dept.getParentDepartmentNum() != null) {
            Long parentNum = dept.getParentDepartmentNum();
            DeptDTO parentDept = deptMap.get(parentNum);
            
            if (parentDept != null) {
                // 🚨 이미 방문한 부서는 중복 합산 방지 (위치 변경)
                if (!visited.contains(parentNum)) {
                    visited.add(parentNum);
                    addEmployeeCountToParent(parentDept, deptMap, visited);
                }
                
                // ✅ 하위 부서 직원 수를 부모 부서에 합산
                parentDept.setTotalEmployeeCount(
                    parentDept.getTotalEmployeeCount() + dept.getTotalEmployeeCount()
                );
            }
        }
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
    
    
    // 부서 수정
    @Override
    public int updateDepartment(DeptDTO dept) {
        return deptMapper.updateDepartment(dept);
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

}
