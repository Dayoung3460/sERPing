package com.beauty1nside.bsn.mapper;

import java.util.List;

import com.beauty1nside.bhf.dto.returninglist.BhfReturnListDTO;
import com.beauty1nside.bhf.dto.returninglist.BhfReturnListSearchDTO;

import com.beauty1nside.bsn.dto.BsnGoodsLOTDTO;
import com.beauty1nside.bsn.dto.OrderSearchDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliveryDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliveryDetailDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliveryLotDTO;
import com.beauty1nside.bsn.dto.delivery.BsnDeliverySearchDTO;
import com.beauty1nside.bsn.dto.order.BhfOrderDTO;
import com.beauty1nside.bsn.dto.order.BhfOrderDetailDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderCancelDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderDetailDTO;
import com.beauty1nside.bsn.dto.order.BsnOrderHistoryDTO;


public interface BsnOrderMapper {
	
	// 발주신청 조회
	List<BhfOrderDTO> selectBhfOrder(OrderSearchDTO orderSearchDTO);
	
	//발주신청건수 확인
	int getCountOfBhfOrder(OrderSearchDTO orderSearchDTO);
	
	//발주신청 상세 조회
	List<BhfOrderDetailDTO> selectBhfOrderDetail(BhfOrderDTO bhfOrderDTO); 
	
	//발주신청의 상품수 확인
	int getCountOfBhfOrderDetail(BhfOrderDTO bhfOrderDTO);
	
	//주문 등록
	void insertBsnOrder(BsnOrderDTO bsnOrderDTO);
	
	//발주신청 취소
	void updateCancelBhfOrder(BhfOrderDTO bhfOrderDTO);
	
	
	
	
	//주문 조회
	List<BsnOrderDTO> selectBsnOrder(OrderSearchDTO orderSearchDTO);
	//주문건수 확인
	int getCountOfBsnOrder(OrderSearchDTO orderSearchDTO);
	
	//주문 상세 조회
	List<BsnOrderDetailDTO> selectBsnOrderDetail(BsnOrderDTO bsnOrderDTO);
	
	//주문 CS기록 조회
	List<BsnOrderHistoryDTO> selectBsnOrderHistory(BsnOrderDTO bsnOrderDTO);
	
	int getCountOfBsnOrderDetail(BsnOrderDTO bsnOrderDTO);
	
	//주문 취소
	void cancelBsnOrder(BsnOrderCancelDTO bsnOrderCancelDTO);
	
	
	
	
	//출고 조회
	List<BsnDeliveryDTO> selectBsnDelivery(BsnDeliverySearchDTO bsnDeliverySearchDTO);
	
	//출고 카운팅
	int getCountBsnDelivery(BsnDeliverySearchDTO bsnDeliverySearchDTO);
	
	//출고 상세조회
	List<BsnDeliveryDetailDTO> selectBsnDeliveryDetail(BsnDeliveryDTO bsnDeliveryDTO);
	
	//출고 상세조회 카운팅
	int getCountBsnDeliveryDetail(BsnDeliveryDTO bsnDeliveryDTO);
	
	//출고 LOT 상세 조회
	List<BsnDeliveryLotDTO> selectBsnDeliveryLotDetail(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	
	//출고 LOT 상세 조회 카운팅
	int getCountBsnDeliveryLotDetail(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	
	//상품 재고 LOT별 조회(임시)
	List<BsnGoodsLOTDTO> selectGoodsWarehouseLot(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	
	//상품 재고 LOT별 조회 카운팅
	int getCountGoodsWarehouseLot(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	
	//출고 LOT 상세 등록(연결)
	void insertBsnDeliveryLotDetail(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	//출고 LOT 수량 수정
	void updateBsnDeliveryLotDetail(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	//출고 LOT 제거
	void deleteBsnDeliveryLotDetail(BsnDeliveryDetailDTO bsnDeliveryDetailDTO);
	//출고 확정(완료)
	void confirmBsnDelivery(String deliveryId);
	
	
	
	
	
}
