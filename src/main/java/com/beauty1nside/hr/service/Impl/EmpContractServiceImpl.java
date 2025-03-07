package com.beauty1nside.hr.service.Impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beauty1nside.hr.dto.EmpContractDTO;
import com.beauty1nside.hr.dto.EmpContractSearchDTO;
import com.beauty1nside.hr.mapper.EmpContractMapper;
import com.beauty1nside.hr.service.EmpContractService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Log4j2
@Service // ★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor // 파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class EmpContractServiceImpl implements EmpContractService {

    private final EmpContractMapper empContractMapper;

    // 근로계약 등록 (트랜잭션 적용)
    @Override
    @Transactional
    public int registerContract(EmpContractDTO contract) {
        return empContractMapper.insertContract(contract);
    }

    // 특정 사원의 최근 계약 조회
    @Override
    public EmpContractDTO getLastContract(Long employeeNum) {
        return empContractMapper.getLastContract(employeeNum);
    }

    
    @Override
    public byte[] generateContractPdf(Long employeeNum, Long companyNum) {
        try {
            // 1️⃣ 근로계약 데이터 조회
            Map<String, Object> contractData = empContractMapper.getContractData(employeeNum, companyNum);
            if (contractData == null) {
                throw new RuntimeException("해당 사원의 근로계약서를 찾을 수 없습니다.");
            }

            // 2️⃣ Jasper 템플릿 로드
            InputStream templateStream = getClass().getResourceAsStream("/jasper/contract_template.jasper");
            if (templateStream == null) {
                throw new FileNotFoundException("Jasper 템플릿을 찾을 수 없습니다.");
            }

            // 3️⃣ Jasper 리포트 데이터 채우기
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateStream, contractData, new JREmptyDataSource());

            // 4️⃣ PDF 생성 및 반환
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // ✅ 전체 근로계약 조회
    @Override
    @Transactional(readOnly = true)
    public List<EmpContractDTO> getAllContracts() {
        return empContractMapper.getAllContracts();
    }

    @Override
    public List<EmpContractDTO> searchContracts(EmpContractSearchDTO searchDTO) {
        return empContractMapper.searchContracts(searchDTO);
    }
    
    
    // ✅ 추가: 계약 개수 카운트
    @Override
    public int countContracts(EmpContractSearchDTO searchDTO) {
        return empContractMapper.countContracts(searchDTO);
    }

}
