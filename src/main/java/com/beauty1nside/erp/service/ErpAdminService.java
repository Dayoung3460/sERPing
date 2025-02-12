package com.beauty1nside.erp.service;

import java.util.List;

import com.beauty1nside.erp.dto.CompanyListDTO;
import com.beauty1nside.erp.dto.testDTO;

/**
 * ERP 회사 관리자 관련 정보 CURD 서비스
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.12  표하연          최초 생성
 *
 *  </pre>
*/
public interface ErpAdminService {
	/**
     * DB연결 확인을 위하여 샘플 데이터를 조회
     *
     * @return testDTO
     */
	public List<testDTO> test();
	
	/**
     * ERP 사용 회사 전체 리스트를 조회한다
     *
     * @return List<CompanyListDTO>
     */
	public List<CompanyListDTO> companyList();
}
