package com.beauty1nside.accnut.service;

import java.util.List;
import java.util.Map;

import com.beauty1nside.accnut.dto.TaxSearchDTO;

public interface TaxService {
	int insert(Map<String, Object> dto);
	List<Map<String, Object>> list(TaxSearchDTO dto);
	int count(TaxSearchDTO dto);
}
