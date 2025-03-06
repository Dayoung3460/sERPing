package com.beauty1nside.accnut.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.beauty1nside.accnut.mapper.OtherMapper;
import com.beauty1nside.accnut.service.OtherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service	//★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor //파이널 붙어 있는 필드만 초기화 해준다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class OtherServiceImpl implements OtherService{

	final OtherMapper otherMapper;
	
	@Override
	public List<Map<String, Object>> optionList(String goodsName, int companyNum) {
		return otherMapper.optionList(goodsName, companyNum);
	}
	
	@Override
	public List<Map<String, Object>> bhfList(int companyNum) {
		return otherMapper.bhfList(companyNum);
	}
	
	@Override
	public Map<String, Object> companyInfo(int companyNum) {
		// TODO Auto-generated method stub
		return otherMapper.companyInfo(companyNum);
	}
	
	@Override
	public List<Map<String, Object>> deptList(int companyNum) {
		// TODO Auto-generated method stub
		return otherMapper.deptList(companyNum);
	}
	
	@Override
	public List<Map<String, Object>> dayList(int companyNum, String bhfId, String month) {
		// TODO Auto-generated method stub
		return otherMapper.dayList(companyNum, bhfId, month);
	}
	
	@Override
	public List<Map<String, Object>> closingList(String code) {
		// TODO Auto-generated method stub
		return otherMapper.closingList(code);
	}
	
}
