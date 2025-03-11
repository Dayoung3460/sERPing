package com.beauty1nside.accnut.service.Impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beauty1nside.accnut.dto.DealBookDTO;
import com.beauty1nside.accnut.dto.DebtDTO;
import com.beauty1nside.accnut.dto.DebtSearchDTO;
import com.beauty1nside.accnut.mapper.DealBookMapper;
import com.beauty1nside.accnut.mapper.DebtMapper;
import com.beauty1nside.accnut.service.DebtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service	//★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor //파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class DebtServiceImpl implements DebtService{
	
	private final DebtMapper debtMapper;
	final DealBookMapper dealBookMapper;
	
	@Override
	public DebtDTO info(String debtCode) {
		// TODO Auto-generated method stub
		return debtMapper.info(debtCode);
	}
	
	@Override
	public List<DebtDTO> list(DebtSearchDTO dto) {
		// TODO Auto-generated method stub
		return debtMapper.list(dto);
	}
	
	@Override
	public int count(DebtSearchDTO dto) {
		// TODO Auto-generated method stub
		return debtMapper.count(dto);
	}
	
	@Override
	public int insert(DebtDTO dto) {
		// TODO Auto-generated method stub
		return debtMapper.insert(dto);
	}
	
	@Override
	@Transactional
	public int update(List<DebtDTO> dtoList) {
		// TODO Auto-generated method stub
		int result = 0;
		Long total = 0L;
		int com = dtoList.get(0).getCompanyNum();
		LocalDate now = LocalDate.now();
		int month = now.getMonthValue() == 1 ? 12 : now.getMonthValue() - 1;
		int year = month == 12 ? now.getYear() - 1 : now.getYear();
		String text = year + "년 " + month + "월 분 미지급급";
		
		for(DebtDTO dto : dtoList) {
			int co = debtMapper.update(dto);
			total += dto.getAmount();
			result += co;
		}
		
		DealBookDTO deal = new DealBookDTO();
		deal.setSection("EE02");
		deal.setTypesOfTransaction("AC19");
		deal.setAmount(total);
		deal.setVatAlternative("N");
		deal.setDealingsContents(text);
		deal.setDealDate(Date.valueOf(now));
		deal.setDepartment("DT001");
		deal.setCompanyNum(com);
				
		dealBookMapper.insert(deal);
				
		return result;
	}
	
}
