package com.beauty1nside.purchs.mapper;

import java.util.List;

import com.beauty1nside.purchs.dto.ProductDTO;
import com.beauty1nside.purchs.dto.ProductSearchDTO;

public interface productMapper {
	//카테고리 조회 (드롭박스)
	List<ProductDTO>catelist();
	
	//브랜드 조회 (모달)
	List<ProductDTO>brandlist(ProductSearchDTO dto);
	int brandcount(ProductSearchDTO dto);
	
	//거래처 조회 (모달)
	List<ProductDTO>vendorlist(ProductSearchDTO dto);
	int vendorcount(ProductSearchDTO dto);
	
	//창고조회 (모달)
	List<ProductDTO>warehouselist(ProductSearchDTO dto);
	int warehousecount(ProductSearchDTO dto);
	
	//상품,옵션 등록 
	int insert(ProductDTO productDTO);
}
