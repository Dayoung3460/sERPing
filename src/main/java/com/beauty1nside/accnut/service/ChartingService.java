package com.beauty1nside.accnut.service;

import java.util.List;

import com.beauty1nside.accnut.dto.SellingDTO;
import com.beauty1nside.accnut.dto.SellingSearchDTO;

public interface ChartingService {
	List<SellingDTO> chartinfo(SellingSearchDTO dto);
}
