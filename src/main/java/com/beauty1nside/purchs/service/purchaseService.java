package com.beauty1nside.purchs.service;

import java.util.List;

import com.beauty1nside.purchs.dto.PurchInsertVO;
import com.beauty1nside.purchs.dto.PurchUpdateVO;
import com.beauty1nside.purchs.dto.PurchaseDTO;
import com.beauty1nside.purchs.dto.PurchaseSearchDTO;

public interface purchaseService {

  //발주서 등록 
	public void purchaseInsert(List<PurchInsertVO> voList);
	
	//발주서 조회
	public List<PurchaseDTO> getPurchaselist(PurchaseSearchDTO dto);
	int purchaseCount(PurchaseSearchDTO dto);
	
	//미입고 발주서 조회 
	public List<PurchaseDTO> nonWarehousinglist(PurchaseSearchDTO dto);
	int nonwarehousingCount(PurchaseSearchDTO dto);
	
	//발주서 상세 조회
	List<PurchaseDTO>getPurchsinfo(Long purchaseNum,int companyNum);
	
	//발주서수정
	public void purchUpdate(PurchUpdateVO vo);
	
	 // 발주 취소 메서드
    boolean cancelPurchase(int companyNum, Long purchaseNum);
}
