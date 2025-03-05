package com.beauty1nside.purchs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beauty1nside.purchs.dto.PurchInsertVO;
import com.beauty1nside.purchs.dto.PurchUpdateVO;
import com.beauty1nside.purchs.dto.PurchaseDTO;
import com.beauty1nside.purchs.dto.PurchaseSearchDTO;
import com.beauty1nside.purchs.mapper.purchaseMapper;
import com.beauty1nside.purchs.service.purchaseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Service //★이거 무조건 넣어!!
@RequiredArgsConstructor
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class purchaseServiceImpl implements purchaseService{
	private final purchaseMapper purchaseMapper;

	@Override
	@Transactional
	public void purchaseInsert(List<PurchInsertVO> voList) {
		  // 리스트 내 각 그룹에 대해 프로시저 호출
        for(PurchInsertVO vo : voList) {
            purchaseMapper.purchaseInsert(vo);
        }
		
	}

	@Override
	public List<PurchaseDTO> getPurchaselist(PurchaseSearchDTO dto) {
		return purchaseMapper.purchaseList(dto);
	}

	@Override
	public int purchaseCount(PurchaseSearchDTO dto) {
		return purchaseMapper.purchaseCount(dto);
	}

	@Override
	public List<PurchaseDTO> nonWarehousinglist(PurchaseSearchDTO dto) {
		return purchaseMapper.nonwarehousinglist(dto);
	}

	@Override
	public int nonwarehousingCount(PurchaseSearchDTO dto) {
		return purchaseMapper.nonwarehousingCount(dto);
	}

	@Override
	public List<PurchaseDTO> getPurchsinfo(Long purchaseNum, int companyNum) {
		
		return purchaseMapper.purchaseinfo(purchaseNum, companyNum);
	}

	@Override
	public void purchUpdate(PurchUpdateVO vo) {
		log.info("수정 내용 서비스 == {}",vo);
		purchaseMapper.purchsupdate(vo);

	}

	@Override
	public boolean cancelPurchase(int companyNum, Long purchaseNum) {
		int result = purchaseMapper.cancelPurchase(companyNum, purchaseNum);
		return result > 0; // ✅ 성공적으로 변경된 행이 1개 이상이면 true 반환
	}
	
	

	

}
