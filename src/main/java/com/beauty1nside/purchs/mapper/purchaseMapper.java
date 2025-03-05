package com.beauty1nside.purchs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.beauty1nside.purchs.dto.ProdUpdateVO;
import com.beauty1nside.purchs.dto.PurchInsertVO;
import com.beauty1nside.purchs.dto.PurchUpdateVO;
import com.beauty1nside.purchs.dto.PurchaseDTO;
import com.beauty1nside.purchs.dto.PurchaseSearchDTO;

public interface purchaseMapper {
	//발주서 등록 (거래처 id 별로 등록)
	public void purchaseInsert(PurchInsertVO vo);
	
	//발주서 조회 
	List<PurchaseDTO>purchaseList(PurchaseSearchDTO dto);
	int purchaseCount(PurchaseSearchDTO dto);
	
	
	//미입고발주서조회 
	List<PurchaseDTO>nonwarehousinglist(PurchaseSearchDTO dto);
	int nonwarehousingCount(PurchaseSearchDTO dto);
	
	//발주서 상세 정보 조회 
	List<PurchaseDTO>purchaseinfo(@Param("purchaseNum") Long purchaseNum, 
								  @Param("companyNum") int companyNum);
	
	//발주서 수정 
	 public void purchsupdate (PurchUpdateVO vo);
	 
	 //발주서 삭제
	 int cancelPurchase(@Param("companyNum") int companyNum, @Param("purchaseNum") Long purchaseNum); 
}
