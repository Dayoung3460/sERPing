package com.beauty1nside.accnut.mapper;

import java.util.List;

import com.beauty1nside.accnut.dto.DebtDTO;
import com.beauty1nside.accnut.dto.DebtSearchDTO;

public interface DebtMapper {
	DebtDTO info(String debtCode);
	List<DebtDTO> list(DebtSearchDTO dto);
	int count(DebtSearchDTO dto);
	int insert(DebtDTO dto);
	int update(DebtDTO dto);
	
}
