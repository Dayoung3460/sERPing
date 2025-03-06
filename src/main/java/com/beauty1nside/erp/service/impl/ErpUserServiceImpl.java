package com.beauty1nside.erp.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.beauty1nside.erp.dto.ContractDTO;
import com.beauty1nside.erp.dto.ErpSearchDTO;
import com.beauty1nside.erp.dto.ErpSubOptionDTO;
import com.beauty1nside.erp.dto.SubScriptionDTO;
import com.beauty1nside.erp.dto.SubscriptionDetailDTO;
import com.beauty1nside.erp.dto.TaxInvoiceDTO;
import com.beauty1nside.erp.dto.UserDepartmentDTO;
import com.beauty1nside.erp.dto.erpSubscriptionInfoListDTO;
import com.beauty1nside.erp.mapper.ErpUserMapper;
import com.beauty1nside.erp.service.ErpUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ERP 사용 회사 CRUD 하는 서비스 클래스
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
 *  2025.03.04  표하연          정기구독을 등록 한다 ( 결제X )
 *  2025.03.05  표하연          ERP 사용회사 부서 이름 호출
 *
 *  </pre>
*/
@Log4j2
@Service	//★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor //파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class ErpUserServiceImpl implements ErpUserService {

	/**
     * ErpUserMapper 맵퍼를 사용하기 위한 의존성 주입
     */
	private final ErpUserMapper erpUserMapper;
	
	/**
     * 고객 구독 정보를 받아옴
     *
     * @param int
     * @return List<erpSubscriptionInfoListDTO>
     */
	@Override
	public List<erpSubscriptionInfoListDTO> sublist(int comapnyNum) {
		return erpUserMapper.sublist(comapnyNum);
	}
	
	/**
     * 회사 계약 상태 여부를 내보냄
     *
     * @param int
     * @return int
     */
	@Override
	public int subcontact(int comapnyNum) {
		return erpUserMapper.subcontact(comapnyNum);
	}

	/**
     * 회사 인사인원 확인
     *
     * @param int
     * @return int
     */
	@Override
	public int hrlist(int companyNum) {
		return erpUserMapper.hrlist(companyNum);
	}

	/**
     * 기간 구독 서비스를 등록한다
     *
     * @param Map<String, Object>
     * @return int
     */
	@Override
	@Transactional
	public int periodservicepay(Map<String, Object> requestData) {
		
		log.info("결제정보 : "+requestData);
		
	    int companyNum = (int) requestData.get("companyNum");
		
		//결제 헤더 등록하기 erp_subscription_list [1개] [{완료}]
		String price = requestData.get("price").toString().replace(",", "");
		requestData.put("price", price);
	
		//결제 바디 등록하기 erp_subscription_tail [여러개] [{완료}]
		erpUserMapper.prosubscriptionlist(requestData);
		
		int lastKey = erpUserMapper.lastpaykey(requestData);
		//log.info("마지막기본키 : "+lastKey);
		
		//결제 정보로 구독목록 업뎃하기 erp_subscription_info_list [여러개] 바디 포문안에서 해야함
		String[] optionNum = ((String) requestData.get("orderId")).split("_");
		String[] optionDay = ((String) requestData.get("dataDay")).split("-");
		int i = 0;
		for (String ele : optionNum) {
		    //ele => 옵션번호 // 이거랑 lastKey 이용해서 인서트
			
			String[] optioninfo = optionDay[i].split("_");
			
		    //구독 결제 바디 저장
		    erpUserMapper.prosubscriptiontail(lastKey, Integer.parseInt(ele), Integer.parseInt(optioninfo[0]), optioninfo[1]);
		    
		    ErpSubOptionDTO dto = erpUserMapper.lastoptionlist(Integer.parseInt(ele));
		    log.info("dto정보 : "+dto);
		    //log.info("dto정보 : "+dto.getSubscriptionNameNum());
		    // 옵션번호로 구독정보 조회해서 서 해당 구독정보에 맞는 구독 상품 업데이트 하기
		    erpSubscriptionInfoListDTO subDTO = null;
		    if(dto.getSubscriptionNameNum() == 1 || dto.getSubscriptionNameNum() == 6) {
		    	subDTO = erpUserMapper.subinfo(companyNum, 1, 6);		        
		    }else if(dto.getSubscriptionNameNum() == 2 || dto.getSubscriptionNameNum() == 7) {
		    	subDTO = erpUserMapper.subinfo(companyNum, 2, 7);
		    }else if(dto.getSubscriptionNameNum() == 3 || dto.getSubscriptionNameNum() == 8) {
		    	subDTO = erpUserMapper.subinfo(companyNum, 3, 8);
		    }else if(dto.getSubscriptionNameNum() == 4 || dto.getSubscriptionNameNum() == 9) {
		    	subDTO = erpUserMapper.subinfo(companyNum, 4, 9);
		    }else if(dto.getSubscriptionNameNum() == 5 || dto.getSubscriptionNameNum() == 10) {
		    	subDTO = erpUserMapper.subinfo(companyNum, 5, 10);
		    }
		    //구독정보 인서트 [{완료}]
		    if(subDTO == null){
		    	subDTO = new erpSubscriptionInfoListDTO();
		    	log.info("계약 정보가 없음 : "+subDTO);
		    	//기간 맟춤인지 기간추가인지 확인 [{완료}]
		    	if( 	   dto.getSubscriptionOption().equals("기간맞춤")
		    			|| dto.getSubscriptionOption().equals("50명(기간맞춤)")
		    			|| dto.getSubscriptionOption().equals("100명(기간맞춤)")
		    			|| dto.getSubscriptionOption().equals("무제한(기간맞춤)") 
		    		) {
		    		erpSubscriptionInfoListDTO needenddate = erpUserMapper.subinfo(companyNum, 1, 6);
		    		subDTO.setSubscriptionForm("EL01");	//구독형태 기간구독으로
		    		subDTO.setCompanyNum(companyNum);		//회사번호
		    		subDTO.setSubscriptionNameNum(dto.getSubscriptionNameNum());	//구독이름
		    		subDTO.setSubscriptionOptionNum(dto.getSubscriptionOptionNum());	//구독옵션
		    		subDTO.setSubscriptionEndDate(needenddate.getSubscriptionEndDate());
		    		erpUserMapper.prosubinsert(subDTO);
		    	//기간 안 맞추고 내가 원하는 기간으로 인서트 [{완료}]
		    	}else {
		    		//원하는 기간으로 구독 (기존 나의 정보 가져와서 추가기간 추가해서 업데이트 ) [{완료}]
		    		Date subscriptionEndDate = new Date();	//기간
		    		int addDays = dto.getSubscriptionPeriod();		//추가기간
		    		LocalDate updatedLocalDate  = subscriptionEndDate.toInstant()
		                    .atZone(ZoneId.systemDefault())
		                    .toLocalDate()
		                    .plusDays(addDays);
		    		Date updatedDate = Date.from(updatedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		    		//log.info("업데이트된 만료일 : " + updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    		subDTO.setSubscriptionForm("EL01");	//구독형태 기간구독으로
		    		subDTO.setCompanyNum(companyNum);		//회사번호
		    		subDTO.setSubscriptionNameNum(dto.getSubscriptionNameNum());	//구독이름
		    		subDTO.setSubscriptionOptionNum(dto.getSubscriptionOptionNum());	//구독옵션
		    		subDTO.setSubscriptionEndDate(updatedDate);
		    		erpUserMapper.prosubinsert(subDTO);
		    	}
		    //구독정보 업데이트 [(테스트필요)]
		    }else{
		    	log.info("계약 정보가 있어요!!! : "+subDTO);
		    	//기간 맞춤인경우 [(테스트필요)]
		    	if(dto.getSubscriptionOption().equals("기간맞춤")) {
		    		// 해당회사의 1또느 2 인 정보가져와서 그걸로 업데이트 [(테스트필요)]
		    		erpSubscriptionInfoListDTO needenddate = erpUserMapper.subinfo(companyNum, 1, 6);
		    		subDTO.setSubscriptionForm("EL01");	//구독형태 기간구독으로
		    		subDTO.setSubscriptionNameNum(dto.getSubscriptionNameNum());	//구독이름
		    		subDTO.setSubscriptionOptionNum(dto.getSubscriptionOptionNum());	//구독옵션
		    		subDTO.setSubscriptionEndDate(needenddate.getSubscriptionEndDate());
		    		erpUserMapper.prosubupdate(subDTO);
		    	//개별 기간인경우 [(완료)]
		    	}else {
		    		//기간 추가임 (기존 나의 정보 가져와서 추가기간 추가해서 업데이트 ) [(완료)]
		    		Date subscriptionEndDate = subDTO.getSubscriptionEndDate();	//기간
		    		int addDays = dto.getSubscriptionPeriod();		//추가기간
		    		LocalDate updatedLocalDate  = subscriptionEndDate.toInstant()
		                    .atZone(ZoneId.systemDefault())
		                    .toLocalDate()
		                    .plusDays(addDays);
		    		Date updatedDate = Date.from(updatedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		    		//log.info("업데이트된 만료일 : " + updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    		subDTO.setSubscriptionForm("EL01");	//구독형태 기간구독으로
		    		subDTO.setSubscriptionNameNum(dto.getSubscriptionNameNum());	//구독이름
		    		subDTO.setSubscriptionOptionNum(dto.getSubscriptionOptionNum());	//구독옵션
		    		subDTO.setSubscriptionEndDate(updatedDate);
		    		erpUserMapper.prosubupdate(subDTO);
		    	}
		    }
		    i++;
		}
		log.info("결제정보 : "+requestData);
		return 1;
	}
	
	/**
     * 정기 구독 서비스를 등록한다
     *
     * @param Map<String, Object>
     * @return int
     */
	@Override
	@Transactional
	public int regularRegister(Map<String, Object> requestData) {
		String[] tempList = ((String) requestData.get("dataDay")).split("-");
		int[] optionList = new int[tempList.length];
		int[] priceList = new int[tempList.length];

		for (int i = 0; i < tempList.length; i++) {
		    String[] parts = tempList[i].split("_");
		    // 옵션 값 저장
		    optionList[i] = Integer.parseInt(parts[0]);
		    // 가격 값 저장
		    priceList[i] = Integer.parseInt(parts[1]); 
		    //옵션 값을 이용해서 구독이름 및 옵션 정보 가져옴
		    log.info( "구독정보 : {}",erpUserMapper.subscriptionoptionname(optionList[i]) );
		    
		    //CS에 등록할 구독 정보 처리중
		}

		// 데이터 확인 출력
		System.out.println("dataDayList: " + Arrays.toString(optionList));
		System.out.println("priceList: " + Arrays.toString(priceList));
		
		
		
		
		
		
		
		return 0;
	}

	/**
     * 그룹웨어 옵션 정보를 불러온다
     *
     * @param int
     * @return int
     */
	@Override
	public int gpoptioninfo(int comapnyNum) {
		return erpUserMapper.gpoptioninfo(comapnyNum);
	}

	/**
     * 사용계약서를 등록한다
     *
     * @param ContractDTO
     * @return int
     */
	@Override
	public boolean insertcontract(ContractDTO dto) {
		return erpUserMapper.insertcontract(dto) >= 1 ? true : false;
	}

	/**
     * 사용계약서 정보를 불러온다
     *
     * @param int
     * @return ContractDTO
     */
	@Override
	public ContractDTO readcontract(int companyNum) {
		return erpUserMapper.readcontract(companyNum);
	}

	/**
     * 결제 정보 리스트를 조회
     *
     * @param ErpSearchDTO
     * @return List<subscriptioncount>
     */
	@Override
	public List<SubScriptionDTO> subscriptionlist(ErpSearchDTO searchDTO) {
		return erpUserMapper.subscriptionlist(searchDTO);
	}

	/**
     * 결제 정보 리스트의 갯수를 조회
     *
     * @param ErpSearchDTO
     * @return int
     */
	@Override
	public int subscriptioncount(ErpSearchDTO searchDTO) {
		return erpUserMapper.subscriptioncount(searchDTO);
	}

	/**
     * 구독리스트의 상세 구독내역을 가져온다
     *
     * @param int
     * @return List<SubscriptionDetailDTO>
     */
	@Override
	public List<SubscriptionDetailDTO> subscriptionDetail(int subscriptionNum) {
		return erpUserMapper.subscriptionDetail(subscriptionNum);
	}

	/**
     * 구독 결제한 현금영수증, 세금계산서 데이터를 삽입한다
     *
     * @param TaxInvoiceDTO
     * @return boolean
     */
	@Override
	public boolean insertTaxInvoice(TaxInvoiceDTO dto) {
		return erpUserMapper.insertTaxInvoice(dto)==1 ? true : false;
	}

	/**
     * 부서와 부모 부서를 조회한다
     *
     * @param int
     * @return UserDepartmentDTO	
     */
	@Override
	public UserDepartmentDTO userDepartmentName(int departmentNum) {
		return erpUserMapper.userDepartmentName(departmentNum);
	}
	
}
