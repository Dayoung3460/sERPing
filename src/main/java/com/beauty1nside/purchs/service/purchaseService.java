package com.beauty1nside.purchs.service;

import java.util.List;

import com.beauty1nside.purchs.dto.PurchInsertVO;
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
}
