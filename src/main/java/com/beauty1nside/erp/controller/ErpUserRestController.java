package com.beauty1nside.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.beauty1nside.erp.dto.ContractDTO;
import com.beauty1nside.erp.dto.ErpSearchDTO;
import com.beauty1nside.erp.dto.SubscriptionDetailDTO;
import com.beauty1nside.erp.dto.TaxInvoiceDTO;
import com.beauty1nside.erp.dto.UserDepartmentDTO;
import com.beauty1nside.erp.dto.erpSubscriptionInfoListDTO;
import com.beauty1nside.erp.service.ErpUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * ERP 사용 회사 REST 컨트롤러
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
 *  2025.02.18  표하연          결제 모듈 등록
 *  2025.02.20  표하연          전자계약 모듈 등록 / 구독내역 리스트 전달
 *  2025.02.26  표하연          계산서 처리 내역 등록
 *  2025.03.04  표하연          정기구독을 등록 한다 ( 결제X )
 *  2025.03.05  표하연          ERP 사용회사 부서 이름 호출
 *
 *  </pre>
*/
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@RestController
//@AllArgsConstructor
@RequestMapping("/erp/rest")
public class ErpUserRestController {
	
	/**
     * ErpUserService 서비스를 사용하기 위한 의존성 주입
     */
	@Autowired
	private ErpUserService erpUserService;
	
	/**
     * 고객 구독 정보를 받아옴
     *
     * @param int
     * @return List<erpSubscriptionInfoListDTO>
     */
	@GetMapping("/subList/{compnayNum}")
	public List<erpSubscriptionInfoListDTO> subList(@PathVariable(name="compnayNum") int compnayNum){
		log.info("aaaaaaaaaa "+compnayNum);
		return erpUserService.sublist(compnayNum);
	}
	
	/**
     * 회사 계약 상태 여부를 내보냄
     *
     * @param int
     * @return int
     */
	@GetMapping("/subcontact/{compnayNum}")
	public int subcontact(@PathVariable(name="compnayNum") int compnayNum){
		return erpUserService.subcontact(compnayNum);
	}
	
	/**
     * 회사 인사인원 확인
     *
     * @param int
     * @return int
     */
	@GetMapping("/hrlist/{compnayNum}")
	public int hrlist(@PathVariable(name="compnayNum") int compnayNum){
		log.info("bbbbbbb "+compnayNum);
		return erpUserService.hrlist(compnayNum);
	}
	
	/**
     * 프로퍼티에서 결제키를 가져온다
     *
     */
	private final String impKey;
	public ErpUserRestController(
            ErpUserService erpUserService, 
            @Value("${portone.api.key}") String impKey) {
        this.erpUserService = erpUserService;
        this.impKey = impKey;
    }
	/**
     * 결제키를 반환 해준다
     *
     * @return Map<String, String>
     */
	@GetMapping("/get-imp-key")
	public Map<String, String> getImpKey() {
        Map<String, String> response = new HashMap<>();
        response.put("impKey", impKey);
        return response;
    }
	/**
     * 기간구독 결제를 처리한다
     *
     * @return Map<String, String>
     */
	@PostMapping("/periodservicepay")
	public String periodservicepay(@RequestBody Map<String, Object> requestData) {
		//log.info("결제정보 : "+requestData);
		int num = erpUserService.periodservicepay(requestData);
		if(num == 1) {
			return "OK";
		}
		return "NO";
	}
	
	/**
     * 정기구독 정보를 등록 한다.
     *
     * @return Map<String, String>
     */
	@PostMapping("/regularRegister")
	public String regularRegister(@RequestBody Map<String, Object> requestData) {
		log.info("결제정보 : "+requestData);
		int num = erpUserService.regularRegister(requestData);
		if(num == 1) {
			return "OK";
		}
		return "NO";
	}
	
	/**
     * 그룹웨어 옵션 정보를 불러온다
     *
     * @param int
     * @return int
     */
	@GetMapping("/subgp/{compnayNum}")
	public int subgp(@PathVariable(name="compnayNum") int compnayNum){
		return erpUserService.gpoptioninfo(compnayNum);
	}
	
	
	/**
     * 맵데이터를 DTO로 변환 하기위한 오브젝트 맵퍼 의존성 주입
     *
     */
	@Autowired
	ObjectMapper objectMapper;
	
	/**
     * 회사가 작성한 전자계약서의 내용을 등록한다
     *
     * @param Map<String, Object>
     * @return String
     */
	@PostMapping("/contactinput")
	public boolean contactinput(@RequestBody Map<String, Object> requestData) {
		log.info("결제정보 : "+requestData);
		ContractDTO contractDTO = objectMapper.convertValue(requestData, ContractDTO.class);
        log.info("변환된 DTO: {}", contractDTO);
		return erpUserService.insertcontract(contractDTO);
	}
	
	/**
     * 회사가 작성한 전자계약서의 내용을 내보낸다
     *
     * @param int
     * @return ContractDTO
     */
	@GetMapping("/contactread/{compnayNum}")
	public ContractDTO contactread(@PathVariable(name="compnayNum") int compnayNum) {
		return erpUserService.readcontract(compnayNum);
	}
	
	/**
     * 회사의 지금까지 구독한 리스트를 가져온다
     *
     * @param int
     * @param int
     * @param int
     * @param ErpSearchDTO
     * @param Paging
     * @return Object
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
	//@GetMapping("/subscriptionlist/{compnayNum}")
	@GetMapping("/subscriptionlist")
	public Object subscriptionlist(
			@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			//@PathVariable(name="compnayNum") int compnayNum,
			ErpSearchDTO dto,
			Paging paging
			) throws JsonMappingException, JsonProcessingException{
		
		//한페이지에 몇개 나오게 할껀지
		paging.setPageUnit(perPage);
		// 현재 페이지 셋팅
		paging.setPage(page);
		log.info("★★★"+dto);
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		// 페이징처리
		paging.setTotalRecord(erpUserService.subscriptioncount(dto));
				
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), paging.getTotalRecord(), erpUserService.subscriptionlist(dto) );
		return result;
	}
	
	/**
     * 구독리스트의 상세 구독내역을 가져온다
     *
     * @param int
     * @return List<SubscriptionDetailDTO>
     */
	@GetMapping("/subscriptionDetail/{subscriptionNum}")
	public List<SubscriptionDetailDTO> subscriptionDetail(@PathVariable(name="subscriptionNum") int subscriptionNum) {
		return erpUserService.subscriptionDetail(subscriptionNum);
	}
	
	
	/**
     * 구독 결제한 현금영수증, 세금계산서 데이터를 삽입한다
     *
     * @param TaxInvoiceDTO
     * @return String
     */
	@PostMapping("/taxbillpublishing")
	public String taxbillpublishing(@RequestBody TaxInvoiceDTO dto) {
		log.info(dto);
		if(erpUserService.insertTaxInvoice(dto)) {
			return "OK";			
		}else {
			return "NO";
		}
	}
	
	/**
     * ERP사용회사 부서명 호출
     *
     * @param int
     * @return UserDepartmentDTO
     */
	@GetMapping("/userDepartmentName/{departmentNum}")
	public UserDepartmentDTO userDepartmentName(@PathVariable(name="departmentNum") int departmentNum) {
		//log.info("부서번호"+departmentNum);
		return erpUserService.userDepartmentName(departmentNum);
	}
	
}
