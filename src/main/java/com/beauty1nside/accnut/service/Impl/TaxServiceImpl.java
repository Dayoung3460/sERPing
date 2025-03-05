package com.beauty1nside.accnut.service.Impl;

import java.util.List;
import java.util.List;
import java.util.List;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beauty1nside.accnut.mapper.TaxMapper;
import com.beauty1nside.accnut.service.TaxService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Service	//★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor //파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class TaxServiceImpl implements TaxService{
	
	final TaxMapper taxMapper;
	
	@Override
	@Transactional
	public int insert(Map<String, Object> dto) {
		// TODO Auto-generated method stub
		
		int pk = taxMapper.maxHeader();
		Map<String, Object> total = (Map<String, Object>) dto.get("total");
		Map<String, Object> to = (Map<String, Object>) dto.get("to");
		Map<String, Object> from = (Map<String, Object>) dto.get("from");
		List<Map<String, Object>> detail = (List<Map<String, Object>>) dto.get("detail");
		
		total.put("taxNum", pk);
		log.info(total);
		log.info(detail);
		
		taxMapper.insertHeader(from, to, total, pk);
		
		detail.forEach(info -> {
			taxMapper.insertDetail(pk, info);
		});
		
		
		
		
		return 0;
	}
	
}
