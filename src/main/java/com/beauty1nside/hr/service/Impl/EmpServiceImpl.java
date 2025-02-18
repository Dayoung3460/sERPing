package com.beauty1nside.hr.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.hr.dto.EmpSearchDTO;
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
		// TODO Auto-generated method stub
		return empMapper.list(dto);
	}

	@Override
	public int count(EmpSearchDTO dto) {
		// TODO Auto-generated method stub
		return empMapper.count(dto);
	}

	@Override
	public Map<String, Object> getCommonCodes() {
	    Map<String, Object> codes = new HashMap<>();
	    codes.put("departments", empMapper.getDepartments());
	    codes.put("positions", empMapper.getPositions());

	    // ✅ 근무 유형을 리스트로 변환하여 반환
	    List<Map<String, String>> employmentTypeList = empMapper.getEmploymentTypes();
	    if (employmentTypeList == null || employmentTypeList.isEmpty()) {
	        employmentTypeList = new ArrayList<>(); // 빈 리스트 반환하여 `null` 방지
	    }
	    codes.put("employmentTypes", employmentTypeList);

	    codes.put("statuses", empMapper.getStatuses());
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
    public void registerEmployee(EmpDTO empDTO) {
        // 오늘 날짜 기준 (YYMMDD)
        String today = new SimpleDateFormat("yyMMdd").format(new Date());

        // 현재 가장 큰 employee_id 가져오기
        String maxEmployeeId = getNewEmployeeId();
        int newSeq = 1;
        
        if (maxEmployeeId != null && maxEmployeeId.startsWith(today)) {
            // 오늘 날짜와 같은 ID가 있다면 마지막 3자리 증가
            String lastSeq = maxEmployeeId.substring(6);
            newSeq = Integer.parseInt(lastSeq) + 1;
        }

        // 새 employee_id 생성
        String newEmployeeId = today + String.format("%03d", newSeq);
        empDTO.setEmployeeId(newEmployeeId);

        // 사원 등록
        empMapper.insertEmployee(empDTO);
    }



}
