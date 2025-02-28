package com.beauty1nside.bsn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beauty1nside.bhf.dto.returninglist.BhfReturnListDTO;
import com.beauty1nside.bhf.dto.returninglist.BhfReturnListSearchDTO;


import com.beauty1nside.bsn.mapper.BsnCsMapper;
import com.beauty1nside.bsn.service.BsnCsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BsnCsServiceImpl implements BsnCsService{

	final BsnCsMapper bsnCsMapper;
	
	@Override
	public List<BhfReturnListDTO> bhfReturningList(BhfReturnListSearchDTO dto) {
		return bsnCsMapper.selectBhfReturningList(dto);
	}

	@Override
	public int countBhfReturningList(BhfReturnListSearchDTO dto) {
		return bsnCsMapper.countBhfReturningList(dto);
	}

}
