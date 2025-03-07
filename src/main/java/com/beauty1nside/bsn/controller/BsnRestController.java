package com.beauty1nside.bsn.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beauty1nside.bhf.dto.returninglist.BhfReturnListSearchDTO;
import com.beauty1nside.bsn.dto.OrderSearchDTO;
import com.beauty1nside.bsn.dto.cs.BsnReturningRefusalDTO;
import com.beauty1nside.bsn.dto.cs.BsnReturningRegistDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliveryDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliveryDetailDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliverySearchDTO;
import com.beauty1nside.bsn.dto.order.BhfOrderDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderCancelDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderDetailDTO;
import com.beauty1nside.bsn.service.BsnCsService;
import com.beauty1nside.bsn.service.BsnCustomException;
import com.beauty1nside.bsn.service.BsnOrderService;
import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용

@RestController
@AllArgsConstructor
@RequestMapping("/bsn/rest/*")
public class BsnRestController {
	
	final private BsnOrderService bsnOrderService;
	
	final private BsnCsService bsnCsService;
	
	@GetMapping("/bhfOrder")
	public Object bhfOrder(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			OrderSearchDTO searchDTO, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		
		
		//첫 페이지, 마지막 페이지
		searchDTO.setStart(paging.getFirst());
		searchDTO.setEnd(paging.getLast());
		
		// 날짜 값이 빈 문자열("")로 넘어오면 null로 설정

		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBhfOrder(searchDTO), bsnOrderService.getBhfOrder(searchDTO) );
		
		//페이징을 위해 검색결과 수 구하기

		//검색결과 - 해당 페이지 내용
		return result;		
				
		
	};
	
	//
	@GetMapping("/bhfOrder/detail")
	public Object bhfOrderDetail(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "orderCode", defaultValue = "bhf_ord1") String orderCode,
			BhfOrderDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		//첫 페이지, 마지막 페이지
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		dto.setOrderCode(orderCode);
		
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBhfOrderDetail(dto), bsnOrderService.getBhfOrderDetail(dto) );

		return result;		

	};
	
	@PostMapping("/order/insert")
	public ResponseEntity<Map<String, Object>> ordInsert(@RequestBody BsnOrderDTO bsnOrderDTO) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			bsnOrderService.registerOrder(bsnOrderDTO);
			response.put("status", "success");
	        response.put("message", "주문 등록 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("주문 등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "주문 등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	@PostMapping("/bhfOrder/cancel")
	public ResponseEntity<Map<String, Object>> bhfOrderCancle(@RequestBody BhfOrderDTO bhfOrederDTO){
		Map<String, Object> response = new HashMap<>();
		
		try {
			bsnOrderService.cancelBhfOrder(bhfOrederDTO);
			response.put("status", "success");
	        response.put("message", "취소 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("취소 실패", e);
	        response.put("status", "error");
	        response.put("message", "요청 취소 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@GetMapping("/order/list")
	public Object bsnOrder(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			OrderSearchDTO searchDTO, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		
		
		//첫 페이지, 마지막 페이지
		searchDTO.setStart(paging.getFirst());
		searchDTO.setEnd(paging.getLast());
		
		// 날짜 값이 빈 문자열("")로 넘어오면 null로 설정

		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBsnOrder(searchDTO), bsnOrderService.getBsnOrder(searchDTO) );
		
		//페이징을 위해 검색결과 수 구하기

		//검색결과 - 해당 페이지 내용
		return result;		
	};
	
	@GetMapping("/order/list/detail")
	public Object bsnOrderDetail(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "orderId", defaultValue = "bsn_order12") String orderId,
			BsnOrderDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		//첫 페이지, 마지막 페이지
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		dto.setOrderId(orderId);
		
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBsnOrderDetail(dto), bsnOrderService.getBsnOrderDetail(dto) );

		return result;		

	};
	
	@GetMapping("/order/list/history")
	public Object bsnOrderHistory(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "orderId", defaultValue = "bsn_order20") String orderId,
			BsnOrderDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		//첫 페이지, 마지막 페이지
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		dto.setOrderId(orderId);
		
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), 10, bsnOrderService.getBsnOrderHistory(dto) );

		return result;		

	};
	@PutMapping("/order/list/cancel")
	public Object bsnOrderCanel(@RequestBody BsnOrderCancelDTO bsnOrderCancelDTO){
		Map<String, Object> response = new HashMap<>();
	
		try {
			bsnOrderService.cancelBsnOrder(bsnOrderCancelDTO);
			response.put("status", "success");
	        response.put("message", "취소 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("취소 실패", e);
	        response.put("status", "error");
	        response.put("message", "요청 취소 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	};
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//출고 조회
	@GetMapping("/deli")
	public Object delivery(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			BsnDeliverySearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
	
		//한 페이지에 출력할 수 : 기본값: 5
				paging.setPageUnit(perPage);
				//현재 페이지(기본값: 1)
				paging.setPage(page);
				
				//첫 페이지, 마지막 페이지
				dto.setStart(paging.getFirst());
				dto.setEnd(paging.getLast());

				
				GridArray grid = new GridArray();
				Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBsnDelivery(dto), bsnOrderService.getBsnDelivery(dto));

				return result;	
	}
	
	@GetMapping("/deli/detail")
	public Object deliveryDetail(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "deliveryId", defaultValue = "bsn_dlivy_4") String deliveryId,
			BsnDeliveryDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		//첫 페이지, 마지막 페이지
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		dto.setDeliveryId(deliveryId);
		
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBsnDeliveryDetail(dto), bsnOrderService.getBsnDeliveryDetail(dto) );

		return result;		

	};
	
	@GetMapping("/deli/lot/detail")
	public Object deliveryLotDetail(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "deliveryDetailId", defaultValue = "5") int deliveryDetailId,
			BsnDeliveryDetailDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
		
		
		//한 페이지에 출력할 수 : 기본값: 5
		paging.setPageUnit(perPage);
		//현재 페이지(기본값: 1)
		paging.setPage(page);
		
		//첫 페이지, 마지막 페이지
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		dto.setDeliveryDetailId(deliveryDetailId);
		
		GridArray grid = new GridArray();
		Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfBsnDeliveryLotDetail(dto), bsnOrderService.getBsnDeliveryLotDetail(dto) );

		return result;		

	};
	
	
	
	//연결할 수 있는 LOT 조회
	@GetMapping("/goods/lot")
	public Object goodsLot(@RequestParam(name = "perPage", defaultValue = "5", required = false) int perPage,
			@RequestParam(name ="page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "optionCode", required = false) String optionCode,
			@RequestParam(name = "deliveryDetailId", required = false) int deliveryDetailId,
			@RequestParam(name = "companyNum", required = false, defaultValue = "1") int companyNum,
			BsnDeliveryDetailDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException  {
	
		//한 페이지에 출력할 수 : 기본값: 5
				paging.setPageUnit(perPage);
				//현재 페이지(기본값: 1)
				paging.setPage(page);
				
				//첫 페이지, 마지막 페이지
				dto.setStart(paging.getFirst());
				dto.setEnd(paging.getLast());
				dto.setOptionCode(optionCode);
				dto.setDeliveryDetailId(deliveryDetailId);
				dto.setCompanyNum(companyNum);
				
				GridArray grid = new GridArray();
				Object result = grid.getArray(paging.getPage(), bsnOrderService.getCountOfGoodsWarehouseLot(dto), bsnOrderService.getGoodsWarehouseLot(dto) );

				return result;	
	}
	
	//출고 LOT 등록(연결)
	@PostMapping("/deli/lot/detail/insert")
	public ResponseEntity<Map<String, Object>> deliveryLotDetailInsert(@RequestBody BsnDeliveryDetailDTO bsnDeliveryDetailDTO) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			bsnOrderService.registerBsnDeliveryLotDetail(bsnDeliveryDetailDTO);
			response.put("status", "success");
	        response.put("message", "LOT 등록 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("LOT 등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "LOT 등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	//출고 LOT 수량 수정
	@PutMapping("/deli/lot/detail/modify")
	public ResponseEntity<Map<String, Object>> deliveryLotDetailModify(@RequestBody BsnDeliveryDetailDTO bsnDeliveryDetailDTO){
		Map<String, Object> response = new HashMap<>();
		try {
			bsnOrderService.modifyBsnDeliveryLotDetail(bsnDeliveryDetailDTO);
				
			response.put("status", "success");
		    response.put("message", "LOT 수정 성공");
		    return ResponseEntity.ok(response); // JSON 형태 응답
				
		} catch (Exception e) {
			log.error("LOT 삭제 실패", e);
		    response.put("status", "error");
		    response.put("message", "LOT 수정 실패");
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	//출고 LOT 삭제
	@DeleteMapping("/deli/lot/detail/delete")
	public ResponseEntity<Map<String, Object>> deliveryLotDetailDelete(@RequestBody BsnDeliveryDetailDTO bsnDeliveryDetailDTO){
		Map<String, Object> response = new HashMap<>();
		try {
			bsnOrderService.removeBsnDeliveryLotDetail(bsnDeliveryDetailDTO);
			
			response.put("status", "success");
	        response.put("message", "LOT 삭제 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("LOT 삭제 실패", e);
	        response.put("status", "error");
	        response.put("message", "LOT 삭제 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	//출고 확정(완료)
	@PutMapping("/deli/confirm")
	public ResponseEntity<Map<String, Object>> deliveryConfrim(@RequestBody BsnDeliveryDTO bsnDeliveryDTO){
		Map<String, Object> response = new HashMap<>();
		try {
			bsnOrderService.completeBsnDelivery(bsnDeliveryDTO);
			response.put("status", "success");
	        response.put("message", "출고 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (BsnCustomException e) {
			log.error("출고 실패", e);
	        response.put("status", "error");
	        response.put("message", e.getErrorMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/cs/request")
	public Object csRequestList(@RequestParam(name = "perPage", defaultValue = "2", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			BhfReturnListSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(bsnCsService.countBhfReturningList(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), bsnCsService.countBhfReturningList(dto), bsnCsService.bhfReturningList(dto) );
		return result;
	}
	
	@GetMapping("/cs/request/detail")
	public Object csRequestDetail(@RequestParam(name = "perPage", defaultValue = "2", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			BhfReturnListSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(1000);
		paging.setPage(page);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		//paging.setTotalRecord(bsnCsService.getCountOfBsnCsReturningDetail(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), bsnCsService.getCountOfBsnCsReturningDetail(dto), bsnCsService.bhfReturningDetail(dto) );
		return result;
	}
	
	@GetMapping("/cs/returning/record")
	public Object csReturningRecord(@RequestParam(name = "perPage", defaultValue = "2", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			BhfReturnListSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		//paging.setTotalRecord(bsnCsService.getCountOfBsnCsReturningDetail(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), bsnCsService.getCountOfBsnCsReturningDetail(dto), bsnCsService.getBsnCsReturningDetail(dto) );
		return result;
	}
	
	@PostMapping("/cs/request/confirm")
	public ResponseEntity<Map<String, Object>> csRequestconfirm(@RequestBody BsnReturningRegistDTO bsnReturningRegistDTO) {
		Map<String, Object> response = new HashMap<>();
		
		
		try {
			bsnCsService.registBsnReturning(bsnReturningRegistDTO);
			response.put("status", "success");
	        response.put("message", "반품 및 교환 처리 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("주문 등록 실패", e);
	        response.put("status", "error");
	        response.put("message", "반품 및 교환 처리 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	@PutMapping("/cs/request/cancel")
	public ResponseEntity<Map<String, Object>> csRequestCancle(@RequestBody BsnReturningRefusalDTO bsnReturningRefusalDTO){
		Map<String, Object> response = new HashMap<>();
		
		try {
			bsnCsService.cancleBsnReturning(bsnReturningRefusalDTO);
			response.put("status", "success");
	        response.put("message", "요청 취소 성공");
	        return ResponseEntity.ok(response); // JSON 형태 응답
			
		} catch (Exception e) {
			log.error("취소 실패", e);
	        response.put("status", "error");
	        response.put("message", "요청 취소 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
}
