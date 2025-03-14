package com.beauty1nside.accnut.service;

import java.util.List;

import com.beauty1nside.accnut.dto.AssetDTO;
import com.beauty1nside.accnut.dto.AssetSearchDTO;

public interface AssetService {
	AssetDTO info(String assetsName, int companyNum);
	List<AssetDTO> list(AssetSearchDTO dto);
	int count(AssetSearchDTO dto);
	int insert(AssetDTO dto);
}
