package com.beauty1nside.accnut.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beauty1nside.accnut.dto.SellingDTO;
import com.beauty1nside.accnut.dto.SellingSearchDTO;
import com.beauty1nside.accnut.mapper.ChartingMapper;
import com.beauty1nside.accnut.service.ChartingService;
import com.beauty1nside.hr.dto.DeptDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service	//★이거 무조건 넣어!!
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
@RequiredArgsConstructor //파이널 붙어 있는 필드만 초기화 해준다
public class ChartingServiceImpl implements ChartingService {

	private final ChartingMapper mapper;
	
	@Override
	public List<SellingDTO> chartinfo(SellingSearchDTO dto) {
		return mapper.chartinfo(dto);
	}

	@Override
	public List<DeptDTO> comSeachDept(int companyNum) {
		return mapper.comSeachDept(companyNum);
	}

}
