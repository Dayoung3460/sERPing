package com.beauty1nside.hr.service;

import java.util.List;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpContractSearchDTO;


public interface EmpContractService {
	
    // 근로계약 등록
    int registerContract(EmpContractDTO contract);

    // 특정 사원의 최근 계약 조회
    EmpContractDTO getLastContract(Long employeeNum);

    // 전체 계약 목록 조회
    List<EmpContractDTO> getAllContracts();

}