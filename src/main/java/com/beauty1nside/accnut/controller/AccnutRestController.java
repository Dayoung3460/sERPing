package com.beauty1nside.accnut.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.beauty1nside.accnut.dto.AssetDTO;
import com.beauty1nside.accnut.dto.AssetSearchDTO;
import com.beauty1nside.accnut.dto.DealBookDTO;
import com.beauty1nside.accnut.dto.DealBookSearchDTO;
import com.beauty1nside.accnut.dto.DebtDTO;
import com.beauty1nside.accnut.dto.DebtSearchDTO;
import com.beauty1nside.accnut.dto.EtcBookSearchDTO;
import com.beauty1nside.accnut.dto.IncidentalCostSearchDTO;
import com.beauty1nside.accnut.dto.SalaryBookDTO;
import com.beauty1nside.accnut.dto.SalaryBookSearchDTO;
import com.beauty1nside.accnut.dto.SellingSearchDTO;
import com.beauty1nside.accnut.service.AssetService;
import com.beauty1nside.accnut.service.DealBookService;
import com.beauty1nside.accnut.service.DebtService;
import com.beauty1nside.accnut.service.EtcBookService;
import com.beauty1nside.accnut.service.IncidentalCostService;
import com.beauty1nside.accnut.service.JsonQueryService;
import com.beauty1nside.accnut.service.OtherService;
import com.beauty1nside.accnut.service.SalaryBookService;
import com.beauty1nside.accnut.service.SellingService;
import com.beauty1nside.accnut.service.TaxService;
import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.GridData;
import com.beauty1nside.common.Paging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@RestController
@AllArgsConstructor
@RequestMapping("/accnut/rest/*")
public class AccnutRestController {

	
	
	final AssetService assetService;
	final DebtService debtService;
	final DealBookService dealBookService;
	final SalaryBookService salaryBookService;
	final EtcBookService etcBookService;
	final IncidentalCostService incidentalCostService;
	final JsonQueryService jsonQueryService;
	RestTemplate restTemplate;
	final OtherService otherService;
	final SellingService sellingService;
	final TaxService taxService;
	
	
	// 목록 조회 ------------------------------------------------------------------------------------------
	
	@GetMapping("/asset/list")
	public Object assetList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			AssetSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(assetService.count(dto));
		
		// 농협 api 호출
		List<AssetDTO> list = assetService.list(dto);
		
		for (AssetDTO asset : list) {
			// 구분이 통장일 때
	        if ("통장".equals(asset.getSection())) {
				// 금융기관이 농협일 때 
				if(  "농협".equals(asset.getFinancialInstitution() ) ) {
					
					// 핀어카운트 조회
					NHService nh = new NHService();
					String finAcno = nh.getFinAcno(asset);
					
					// 실제 통장 잔고 조회
					nh = new NHService();
					String amount = nh.getAmount(finAcno);
					// 통장잔고로 금액 변경
					if(amount != null) {
						asset.setAmount(Integer.parseInt(amount));						
					}
					
				} // 농협일때 if문
			} // 통장일때 if문
		} // for 문
		
		
		//log.info(list);
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), assetService.count(dto), list );
		return result;
	}
	
	@GetMapping("/debt/list")
	public Object debtList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			DebtSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(debtService.count(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), debtService.count(dto), debtService.list(dto) );
		return result;
	}
	
	@GetMapping("/book/list")
	public Object dealBookList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			DealBookSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(dealBookService.count(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), dealBookService.count(dto), dealBookService.list(dto) );
		return result;
	}
	
	@GetMapping("/salary/list")
	public Object salaryBookList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			SalaryBookSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(salaryBookService.count(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), salaryBookService.count(dto), salaryBookService.list(dto) );
		return result;
	}
	
	@GetMapping("/etc/list")
	public Object etcBookList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			EtcBookSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(etcBookService.count(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), etcBookService.count(dto), etcBookService.list(dto) );
		return result;
	}
	
	@GetMapping("/incidental/list")
	public Object incidentalList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			IncidentalCostSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(incidentalCostService.count(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), incidentalCostService.count(dto), incidentalCostService.list(dto) );
		return result;
	}
	
	
	// String => json => [{},{},{}]  으로 출력
    @GetMapping("/json/test")
    public Object jsonTest() throws JsonMappingException, JsonProcessingException{
    	
            String jsonString = jsonQueryService.jsonTest();
            GridArray grid = new GridArray();
            
            List<ObjectNode> jsonNode = grid.getNewList(jsonString);
            
            return jsonNode;
    }
	
    @GetMapping("option/list")
    public ResponseEntity<Map<String, Object>> optionList(@RequestParam String goodsName, @RequestParam int companyNum) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		List<Map<String, Object>> result = otherService.optionList(goodsName, companyNum);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    }
    
    @GetMapping("bhf/list")
    public ResponseEntity<Map<String, Object>> bhfList(@RequestParam int companyNum) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		List<Map<String, Object>> result = otherService.bhfList(companyNum);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    	
    }
    
    @GetMapping("/selling/list")
	public Object sellinglList(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			SellingSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(sellingService.listCount(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), sellingService.listCount(dto), sellingService.list(dto) );
		return result;
	}
    
    @GetMapping("/selling/info")
	public Object sellinglInfo(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			SellingSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(sellingService.infoCount(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), sellingService.infoCount(dto), sellingService.info(dto) );
		return result;
	}
    
    @GetMapping("company/info")
    public ResponseEntity<Map<String, Object>> companyInfo(@RequestParam int companyNum) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		Map<String, Object> result = otherService.companyInfo(companyNum);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    	
    }
    
    @GetMapping("dept/list")
    public ResponseEntity<Map<String, Object>> deptList(@RequestParam int companyNum) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		List<Map<String, Object>> result = otherService.deptList(companyNum);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    	
    }
    
    @GetMapping("day/list")
    public ResponseEntity<Map<String, Object>> dayList(@RequestParam(name = "companyNum") int companyNum, @RequestParam(name = "bhfId") String bhfId, @RequestParam(name = "month") String month) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		List<Map<String, Object>> result = otherService.dayList(companyNum, bhfId, month);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    	
    }
    
    @GetMapping("closing/list")
    public ResponseEntity<Map<String, Object>> closingList(@RequestParam(name = "code") String code) {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		List<Map<String, Object>> result = otherService.closingList(code);
	        response.put("status", "success");
	        response.put("message", "조회 성공");
	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("조회 실패", e);
	        response.put("status", "error");
	        response.put("message", "조회 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
    	
    }
    
    
    
	// 삽입 ----------------------------------------------------------------------------------------------
	
	
	@PostMapping("/asset/insert")
	public ResponseEntity<Map<String, Object>> assetsInsert(@RequestBody AssetDTO dto) {
		log.info(dto);
		if("AC02".equals(dto.getSection())) {
			if("FI01".equals(dto.getFinancialInstitution())) {
				NHService nh = new NHService();
				String rgno = nh.getRgno(dto);
				log.info(rgno);
				dto.setRgno(rgno);
				log.info(dto);
			}
		}
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	assetService.insert(dto);
	        response.put("status", "success");
	        response.put("message", "등록 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@PostMapping("/debt/insert")
	public ResponseEntity<Map<String, Object>> debtInsert(@RequestBody DebtDTO dto) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	debtService.insert(dto);
	        response.put("status", "success");
	        response.put("message", "등록 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@PostMapping("/book/insert")
	public Map bookInsert(@RequestBody GridData<DealBookDTO> dto) {
		dto.getCreatedRows().forEach(dtos -> {
			log.info("list: " + dtos);
			if(dtos.getSection() != null) {
				dealBookService.insert(dtos);
			}
		});
		
		return Collections.singletonMap("result", true);
	}
	
	@PostMapping("/tax/insert")
	public ResponseEntity<Map<String, Object>> taxInsert(@RequestBody Map<String, Object> dto) {
	    Map<String, Object> response = new HashMap<>();
	    log.info(dto);
	    try {
	    	int result = taxService.insert(dto);
	        response.put("status", "success");
	        response.put("message", "등록 성공");	        response.put("result", result);
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
	
	
	// 수정 ----------------------------------------------------------------------------------------------
	
	@PutMapping("/salary/update")
	public ResponseEntity<Map<String, Object>> salaryUpdate(@RequestBody List<SalaryBookDTO> dtoList) {
		int total = 0;
		int companyNum= dtoList.get(0).getCompanyNum();
		
		for(SalaryBookDTO dto : dtoList) {
			total += dto.getPaymentAmount();
		}
		
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	salaryBookService.update(dtoList);
	    	// 급여통장에서 빠짐
	    	NHService nh = new NHService();
	    	// 급여통장 조회
	    	AssetDTO assetDTO = assetService.info("급여", companyNum);
	    	// 급여통장 핀어카운트 조회
	    	String finAcno = nh.getFinAcno(assetDTO);
	    	nh.withdraw(finAcno, String.valueOf(total));
	    	
	        response.put("status", "success");
	        response.put("message", "수정 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
	    } catch (Exception e) {
	        log.error("등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "수정 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
	
	// 금융 결제원 api
	@GetMapping("bank/your0770")
	public Object getAccessToken() {
		String tokenUrl = "https://openapi.openbanking.or.kr/oauth/2.0/token";
	    
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("client_id", "c659795a-b1ec-46fc-b554-2a2b80eceb8a");
	    params.add("client_secret", "0165ca41-ef50-4cd8-acf4-40d68a50214d");
	    params.add("scope", "oob");
	    params.add("grant_type", "client_credentials");

	    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
	    ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

	    return response.getBody().get("access_token").toString();
	}
	
}
