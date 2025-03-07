package com.beauty1nside.hr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpContractSearchDTO;

@Mapper
public interface EmpContractMapper {
    // ✅ 근로계약 등록 (DB에 삽입)
    int insertContract(EmpContractDTO contract);

    // ✅ 특정 사원의 최근 계약 조회
    EmpContractDTO getLastContract(@Param("employeeNum") Long employeeNum);
    
    // ✅ 계약 목록 조회 (필요하면 추가)
    List<EmpContractDTO> getAllContracts();
    
    // 특정 사원의 최신 근로계약서 조회
    Map<String, Object> getContractData(@Param("employeeNum") Long employeeNum, @Param("companyNum") Long companyNum);
    
    // ✅ 동적 검색 및 페이징 포함 근로계약 조회
    List<EmpContractDTO> searchContracts(EmpContractSearchDTO searchDTO);
}
