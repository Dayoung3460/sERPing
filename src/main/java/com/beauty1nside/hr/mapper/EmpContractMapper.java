package com.beauty1nside.hr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpContractSearchDTO;

@Mapper
public interface EmpContractMapper {
    // ✅ 근로계약 등록
    int insertContract(EmpContractDTO contract);

    // ✅ 특정 사원의 최근 계약 조회
    EmpContractDTO getLastContract(Long employeeNum);
    
    // ✅ 특정 사원의 최신 근로계약서 조회
    Map<String, Object> getContractData(Long employeeNum, Long companyNum);
    
    // ✅ 전체 근로계약 목록 조회 (페이징 없음)
    List<EmpContractDTO> getAllContracts();
    
    // ✅ 검색 및 페이징 포함 근로계약 조회 (DTO 활용)
    List<EmpContractDTO> searchContracts(EmpContractSearchDTO searchDTO);
    
    // ✅ 추가: 계약 개수 카운트
    int countContracts(EmpContractSearchDTO searchDTO);
}
