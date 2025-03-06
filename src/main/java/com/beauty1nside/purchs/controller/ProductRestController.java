package com.beauty1nside.purchs.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.beauty1nside.purchs.dto.ProdInsertVO;
import com.beauty1nside.purchs.dto.ProdUpdateVO;
import com.beauty1nside.purchs.dto.ProductDTO;
import com.beauty1nside.purchs.dto.ProductSearchDTO;
import com.beauty1nside.purchs.dto.PurchInsertVO;
import com.beauty1nside.purchs.dto.PurchUpdateVO;
import com.beauty1nside.purchs.dto.PurchaseDTO;
import com.beauty1nside.purchs.dto.PurchaseSearchDTO;
import com.beauty1nside.purchs.dto.WarehouseInsertVO;
import com.beauty1nside.purchs.dto.WarehouseSearchDTO;
import com.beauty1nside.purchs.service.productService;
import com.beauty1nside.purchs.service.purchaseService;
import com.beauty1nside.purchs.service.warehouseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@RestController
//@AllArgsConstructor
@RequiredArgsConstructor
@RequestMapping("/purchs/rest/*")
public class ProductRestController {
	final productService productService;
	final purchaseService purchaseService;
	final warehouseService warehouseService;
	
   
	
	// ✅ 카테고리 목록 조회 (companyNum 기준)
	@GetMapping("/product/catelist")
    public ResponseEntity<?> getCategoryList(@RequestParam("companyNum") int companyNum) {
        System.out.println("📢 요청받은 companyNum: " + companyNum); // ✅ 서버 로그 확인

        List<ProductDTO> categoryList = productService.getCatelist(companyNum);

        if (categoryList == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 데이터베이스에서 카테고리 목록을 가져오지 못했습니다.");
        }

        if (categoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("⚠️ 카테고리 데이터 없음");
        }

        return ResponseEntity.ok(categoryList);
    }
	
	//브랜드 모달 데이터 조회 
	@GetMapping("/brand/list")
	public Object brandList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
							@RequestParam(name="page", defaultValue = "1" ,required = false) int page,
							@RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
							@ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		
	
		
		//페이징 유닛 수 
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		//페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		//페이징 처리 
		paging.setTotalRecord(productService.brandcount(dto));
		
		//grid배열 처리 
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), productService.brandcount(dto), productService.getBrandlist(dto));
		return result;
	
	}
	
	//거래처 모달 데이터 조회 
	@GetMapping("/vendor/list")
	public Object vendorList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
							@RequestParam(name="page", defaultValue = "1" ,required = false) int page,
							@RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
							@ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		
		
		//페이징 유닛 수 
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		//페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		//페이징 처리 
		paging.setTotalRecord(productService.vendorcount(dto));
		
		//grid배열 처리 
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), productService.vendorcount(dto), productService.getVendorlist(dto));
		return result;
	
	}
	
	//창고 모달 데이터 조회 
		@GetMapping("/warehouse/list")
		public Object warehouseList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
								@RequestParam(name="page", defaultValue = "1" ,required = false) int page,
								@RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
								@ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
			
			
			//페이징 유닛 수 
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			//페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			//페이징 처리 
			paging.setTotalRecord(productService.warehousecount(dto));
			
			//grid배열 처리 
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), productService.warehousecount(dto), productService.getWarehouselist(dto));
			return result;
		
		}
		
		
	
		
		//상품 등록
		@PostMapping("/product/insert")
		// Map을 같이 사용해서 status,message 등을 사용 가능하다
		// ProdInsertVO 안에 List<ProInsertDtlVO> files 있어서 ProInsertDtlVO를 따로 넣지 않아도 된다.
		public ResponseEntity<Map<String,Object>> productInsert(@RequestBody ProdInsertVO prodInsertVO){
			log.info("컨트롤러====={}",prodInsertVO);
			Map<String, Object> response = new HashMap<>();
			 try {
				productService.goodsinsert(prodInsertVO);
				response.put("status", "success");
				response.put("message", "제품 등록 성공");
				return ResponseEntity.ok(response);
			 } catch(Exception e) {
				 e.printStackTrace(); // 🔥 로그 출력 추가
				 log.error("등록실패", e);
				 response.put("status", "error");
				 response.put("message", "서버 오류 발생: " + e.getMessage()); // 🔥 오류 메시지 반환
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			 }
			
		}
		
		
				
		//발주서등록
		@PostMapping("/purchase/insert")
		public ResponseEntity<Map<String, Object>> purchaseInsert(@RequestBody List<PurchInsertVO> purchInsertVOList) {
		    log.info("컨트롤러====={}", purchInsertVOList);
		    Map<String, Object> response = new HashMap<>();
		    try {
		        purchaseService.purchaseInsert(purchInsertVOList);
		        response.put("status", "success");
		        response.put("message", "발주 등록 성공");
		        return ResponseEntity.ok(response);
		    } catch(Exception e) {
		        e.printStackTrace();
		        log.error("등록 실패", e);
		        response.put("status", "error");
		        response.put("message", "서버 오류 발생: " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}
		
		//발주서 조회 
		@GetMapping("/purchase/list")
		public Object purchaseList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
								   @RequestParam(name="page", defaultValue = "1" ,required = false) int page,
								   @RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
								   @RequestParam(name="startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
							       @RequestParam(name="endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
								   @ModelAttribute PurchaseSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
			// 회사 번호를 DTO에 설정 (필수)
		    dto.setCompanyNum(companyNum); 
		    
		 // 날짜를 String 형태로 변환
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String startDateStr = (startDate != null) ? formatter.format(startDate) : null;
		    String endDateStr = (endDate != null) ? formatter.format(endDate) : null;
			
		    dto.setStartDate(startDateStr);
		    dto.setEndDate(endDateStr);
		    
			//페이징 유닛 수 
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			//페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			//페이징 처리 
			paging.setTotalRecord(purchaseService.purchaseCount(dto));
			
			//grid배열 처리 
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), purchaseService.purchaseCount(dto),purchaseService.getPurchaselist(dto));
			return result;
		
		}
		
		
		
		//미입고 발주서 조회 
		@GetMapping("/nonwarehousing/list")
		public Object nonwarehousing(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
								   @RequestParam(name="page", defaultValue = "1" ,required = false) int page,
								   @RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
								   @RequestParam(name="startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
							       @RequestParam(name="endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
								   @ModelAttribute PurchaseSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
			// 회사 번호를 DTO에 설정 (필수)
		    dto.setCompanyNum(companyNum); 
		    
		 // 날짜를 String 형태로 변환
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String startDateStr = (startDate != null) ? formatter.format(startDate) : null;
		    String endDateStr = (endDate != null) ? formatter.format(endDate) : null;
			
		    dto.setStartDate(startDateStr);
		    dto.setEndDate(endDateStr);
		    
			//페이징 유닛 수 
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			//페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			//페이징 처리 
			paging.setTotalRecord(purchaseService.nonwarehousingCount(dto));
			
			//grid배열 처리 
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), purchaseService.nonwarehousingCount(dto),purchaseService.nonWarehousinglist(dto));
			return result;
		
		}
				
		//입고등록
		@PostMapping("/warehouse/insert")
		public ResponseEntity<Map<String, Object>> warehouseInsert(@RequestBody List<WarehouseInsertVO> warehouseInsertVOList) {
		    log.info("컨트롤러====={}", warehouseInsertVOList);
		    Map<String, Object> response = new HashMap<>();
		    try {
		    	warehouseService.warehouseInsert(warehouseInsertVOList);
		        response.put("status", "success");
		        response.put("message", "발주 등록 성공");
		        return ResponseEntity.ok(response);
		    } catch(Exception e) {
		        e.printStackTrace();
		        log.error("등록 실패", e);
		        response.put("status", "error");
		        response.put("message", "서버 오류 발생: " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}
		
		//입고 조회 
			@GetMapping("/warehouselist/list")
			public Object warehouseListSearch(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
									   @RequestParam(name="page", defaultValue = "1" ,required = false) int page,
									   @RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
									   @RequestParam(name="startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
								       @RequestParam(name="endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
									   @ModelAttribute WarehouseSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
				// 회사 번호를 DTO에 설정 (필수)
			    dto.setCompanyNum(companyNum); 
			    
			 // 날짜를 String 형태로 변환
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    String startDateStr = (startDate != null) ? formatter.format(startDate) : null;
			    String endDateStr = (endDate != null) ? formatter.format(endDate) : null;
				
			    dto.setStartDate(startDateStr);
			    dto.setEndDate(endDateStr);
			    
				//페이징 유닛 수 
				paging.setPageUnit(perPage);
				paging.setPage(page);
				
				//페이징 조건
				dto.setStart(paging.getFirst());
				dto.setEnd(paging.getLast());
				
				//페이징 처리 
				paging.setTotalRecord(warehouseService.warehouseCount(dto));
				
				//grid배열 처리 
				GridArray grid = new GridArray();
				Object result = grid.getArray(paging.getPage(), warehouseService.warehouseCount(dto),warehouseService.getWarehouselist(dto));
				return result;
			
			}
			
			//상품 수정
			@PostMapping("/product/update")
			// Map을 같이 사용해서 status,message 등을 사용 가능하다
			// ProdInsertVO 안에 List<ProInsertDtlVO> files 있어서 ProInsertDtlVO를 따로 넣지 않아도 된다.
			public ResponseEntity<Map<String,Object>> productUpdate(@RequestBody ProdUpdateVO prodUpdateVO){
				log.info("컨트롤러====={}",prodUpdateVO);
				Map<String, Object> response = new HashMap<>();
				 try {
					productService.goodUpdate(prodUpdateVO);
					response.put("status", "success");
					response.put("message", "제품 수정 성공");
					return ResponseEntity.ok(response);
				 } catch(Exception e) {
					 e.printStackTrace(); // 🔥 로그 출력 추가
					 log.error("수정실패", e);
					 response.put("status", "error");
					 response.put("message", "서버 오류 발생: " + e.getMessage()); // 🔥 오류 메시지 반환
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				 }
				
			}
			
	//상품별 재고 조회  
		@GetMapping("/goods/nums")
		public Object goodsNumList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
								   @RequestParam(name="page", defaultValue = "1" ,required = false) int page,
								   @RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
								   @ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
			// 회사 번호를 DTO에 설정 (필수)
		    dto.setCompanyNum(companyNum); 

			//페이징 유닛 수 
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			//페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			//페이징 처리 
			paging.setTotalRecord(productService.goodsNumCount(dto));
			
			//grid배열 처리 
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), productService.goodsNumCount(dto),productService.getGoodsNum(dto));
			return result;
		
		}
		
		//lot별 재고 조회  
		@GetMapping("/goodslot/nums")
		public Object goodsLotNumlist(@RequestParam(name="perPage",defaultValue="10", required = false) int perPage,
		                              @RequestParam(name="page", defaultValue = "1" ,required = false) int page,
		                              @RequestParam(name="companyNum", required=true) int companyNum,  
		                              @RequestParam(name="optionNum", required=true) int optionNum,  
		                              @ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		    
		    // 회사번호 & 옵션번호 설정
		    dto.setCompanyNum(companyNum);
		    dto.setOptionNum(optionNum);

		    // 페이징 설정
		    paging.setPageUnit(perPage);
		    paging.setPage(page);
		    
		    // 페이징 조건
		    dto.setStart(paging.getFirst());
		    dto.setEnd(paging.getLast());
		    
		    // 전체 레코드 수 조회
		    int totalCount = productService.goodsLotNumCount(dto);
		    paging.setTotalRecord(totalCount);
		    
		    // LOT 데이터 조회
		    List<ProductDTO> lotList = productService.getGoodsLotNum(dto);

		    // JSON 응답 데이터 포맷
		    Map<String, Object> result = new HashMap<>();
		    result.put("contents", lotList);
		    result.put("totalCount", totalCount);

		    return ResponseEntity.ok(result);
		}
		
		//상품 수정
		@PostMapping("/purchase/update")
		// Map을 같이 사용해서 status,message 등을 사용 가능하다
		// ProdInsertVO 안에 List<ProInsertDtlVO> files 있어서 ProInsertDtlVO를 따로 넣지 않아도 된다.
		public ResponseEntity<Map<String,Object>> purchsUpdate(@RequestBody PurchUpdateVO purchUpdateVO){
			log.info("컨트롤러====={}",purchUpdateVO);
			Map<String, Object> response = new HashMap<>();
			 try {
				purchaseService.purchUpdate(purchUpdateVO);
				response.put("status", "success");
				response.put("message", "발주서 수정 성공");
				return ResponseEntity.ok(response);
			 } catch(Exception e) {
				 e.printStackTrace(); // 🔥 로그 출력 추가
				 log.error("수정실패", e);
				 response.put("status", "error");
				 response.put("message", "서버 오류 발생: " + e.getMessage()); // 🔥 오류 메시지 반환
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			 }
			
		}
		
	
		// ✅ 발주 취소 요청 API
	  
		@PostMapping("/purchase/cancel")
		public ResponseEntity<?> cancelPurchase(@RequestBody PurchaseDTO request) {
		    Long purchaseNum = request.getPurchaseNum();
		    Integer companyNum = request.getCompanyNum();

		    System.out.println("📢 요청 받은 purchaseNum: " + purchaseNum);
		    System.out.println("📢 요청 받은 companyNum: " + companyNum);

		    if (purchaseNum == null || companyNum == null) {
		        return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "필수 값이 없습니다."));
		    }

		    boolean success = purchaseService.cancelPurchase(companyNum, purchaseNum);

		    if (success) {
		        return ResponseEntity.ok(Map.of("status", "success", "message", "발주 취소 완료"));
		    } else {
		        return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "발주 취소 실패"));
		    }
		}



			
			
		

}