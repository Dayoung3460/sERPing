package com.beauty1nside.accnut.mapper;

import java.util.List;

import com.beauty1nside.accnut.dto.SellingDTO;
import com.beauty1nside.accnut.dto.SellingSearchDTO;
import com.beauty1nside.hr.dto.DeptDTO;

public interface ChartingMapper {
	List<SellingDTO> chartinfo(SellingSearchDTO dto);
	
	List<DeptDTO> comSeachDept(int companyNum);
}
