package com.beauty1nside.erp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.beauty1nside.erp.dto.ContractDTO;
import com.beauty1nside.erp.dto.ErpSearchDTO;
import com.beauty1nside.erp.dto.ErpSubOptionDTO;
import com.beauty1nside.erp.dto.ErpSubscriptionOptionDTO;
import com.beauty1nside.erp.dto.SubScriptionDTO;
import com.beauty1nside.erp.dto.SubscriptionDetailDTO;
import com.beauty1nside.erp.dto.TaxInvoiceDTO;
import com.beauty1nside.erp.dto.UserDepartmentDTO;
import com.beauty1nside.erp.dto.erpSubscriptionInfoListDTO;

/**
 * ERP 사용 회사 CRUD 하는 맵퍼 클래스
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.17  표하연          최초 생성
 *  2025.02.18  표하연          기간 구독 결제 서비스 생성
 *  2025.02.20  표하연          사용계약서 정보를 CR 한다
 *  2025.02.21  표하연          결제 목록을 리드한다
 *  2025.02.26  표하연          구독 결제한 현금영수증, 세금계산서 데이터를 삽입한다
 *  2025.03.05  표하연          ERP 사용회사 부서 이름 호출
 *
 *  </pre>
*/
public interface ErpUserMapper {

	/**
     * 고객 구독 정보를 받아옴
     *
     * @param int
     * @return List<erpSubscriptionInfoListDTO>
     */
	public List<erpSubscriptionInfoListDTO> sublist(int comapnyNum);
	
	/**
     * 회사 계약 상태 여부를 내보냄
     *
     * @param int
     * @return int
     */
	public int subcontact(int comapnyNum);
	
	/**
     * 회사 인사인원 확인
     *
     * @param int
     * @return int
     */
	public int hrlist(int companyNum);
	
	/**
     * 구독목록(결제헤더)를 등록한다
     *
     * @param Map<String, Object>
     * @return int
     */
	public int prosubscriptionlist(Map<String, Object> map);
	
	/**
     * 구독목록(결제헤더)의 마지막 결제 기본키를 가져온다
     *
     * @param Map<String, Object>
     * @return int
     */
	public int lastpaykey(Map<String, Object> map);
	
	/**
     * 구독목록 결제 옵션 정보를 저장한다
     *
     * @param int
     * @param int
     * @return int
     */
	public int prosubscriptiontail(@Param("subscriptionNum") int subscriptionNum, 
									@Param("subscriptionOptionNum") int subscriptionOptionNum,
									@Param("subscriptionPeriod") int subscriptionPeriod,
									@Param("subscriptionOptionPrice") String subscriptionOptionPrice
									);
	
	/**
     * 구독 옵션 정보를 가져온다 단일 레코드
     *
     * @param int
     * @return ErpSubOptionDTO
     */
	public ErpSubOptionDTO lastoptionlist(int subscriptionOptionNum);
	
	/**
     * 회사가 현재 구독중인 정보 확인
     *
     * @param int
     * @param int
     * @param int
     * @return erpSubscriptionInfoListDTO
     */
	public erpSubscriptionInfoListDTO subinfo(@Param("companyNum") int companyNum, @Param("subnamenum1") int subnamenum1, @Param("subnamenum2") int subnamenum2);
	
	/**
     * 구독정보를 업데이트 한다 (기간구독)
     *
     * @param erpSubscriptionInfoListDTO
     * @return void
     */
	public void prosubupdate(erpSubscriptionInfoListDTO dto);
	
	/**
     * 구독정보를 인서트 한다 (기간구독)
     *
     * @param erpSubscriptionInfoListDTO
     * @return void
     */
	public void prosubinsert(erpSubscriptionInfoListDTO dto);
	
	/**
     * 그룹웨어 옵션 정보를 불러온다
     *
     * @param int
     * @return int
     */
	public int gpoptioninfo(int comapnyNum);
	
	/**
     * 사용계약서를 등록한다
     *
     * @param ContractDTO
     * @return int
     */
	public int insertcontract(ContractDTO dto);
	
	/**
     * 사용계약서 정보를 불러온다
     *
     * @param int
     * @return ContractDTO
     */
	public ContractDTO readcontract(int companyNum);
	
	/**
     * 결제 정보 리스트를 조회
     *
     * @param ErpSearchDTO
     * @return List<subscriptioncount>
     */
	public List<SubScriptionDTO> subscriptionlist(ErpSearchDTO searchDTO);
	
	/**
     * 결제 정보 리스트의 갯수를 조회
     *
     * @param ErpSearchDTO
     * @return int
     */
	public int subscriptioncount(ErpSearchDTO searchDTO);
	
	/**
     * 구독리스트의 상세 구독내역을 가져온다
     *
     * @param int
     * @return List<SubscriptionDetailDTO>
     */
	public List<SubscriptionDetailDTO> subscriptionDetail(int subscriptionNum);
	
	
	/**
     * 구독 결제한 현금영수증, 세금계산서 데이터를 삽입한다
     *
     * @param TaxInvoiceDTO
     * @return int
     */
	public int insertTaxInvoice(TaxInvoiceDTO dto);
	
	/**
     * 구독 옵션 번호에 맞는 정보를 조회해서 보여준다
     *
     * @param int
     * @return ErpSubscriptionOptionDTO
     */
	public ErpSubscriptionOptionDTO subscriptionoptionname(int optionNum);
	
	/**
     * 부서와 부모 부서를 조회한다
     *
     * @param int
     * @return UserDepartmentDTO	
     */
	public UserDepartmentDTO userDepartmentName(int departmentNum);
	
}
