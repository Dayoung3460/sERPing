package com.beauty1nside.purchs.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.beauty1nside.purchs.dto.ProductDTO;
import com.beauty1nside.purchs.dto.ProductSearchDTO;
import com.beauty1nside.purchs.controller.ProductRestController;
import com.beauty1nside.purchs.dto.ProdInsertVO;
import com.beauty1nside.purchs.dto.ProdUpdateVO;
import com.beauty1nside.purchs.mapper.productMapper;
import com.beauty1nside.purchs.service.productService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Service //★이거 무조건 넣어!!
@RequiredArgsConstructor
//@AllArgsConstructor	//모든필드를 생성자 초기화 한다
//필드 하나뿐이면 그냥 all 하고 여러개이면 Required
public class productServiceImpl implements productService{
	
	private final productMapper productMapper;
	

	

	@Override
	public List<ProductDTO> getBrandlist(ProductSearchDTO dto) {
		return productMapper.brandlist(dto);
	}
	
	@Override
	public int brandcount(ProductSearchDTO dto) {
		return productMapper.brandcount(dto);
	}

	@Override
	public List<ProductDTO> getVendorlist(ProductSearchDTO dto) {
		return productMapper.vendorlist(dto);
	}

	@Override
	public int vendorcount(ProductSearchDTO dto) {
		return productMapper.vendorcount(dto);
	}

	@Override
	public List<ProductDTO> getWarehouselist(ProductSearchDTO dto) {
		return productMapper.warehouselist(dto);
	}

	@Override
	public int warehousecount(ProductSearchDTO dto) {
		return productMapper.warehousecount(dto);
	}

	@Override
	public void goodsinsert(ProdInsertVO vo) {
		log.info("서비스 == {}",vo);
		productMapper.goodsinsert(vo);
		
	}

	@Override
	public List<ProductDTO> getProductlist(ProductSearchDTO dto) {
		return productMapper.goodslist(dto);
	}

	@Override
	public int productcount(ProductSearchDTO vo) {
		return productMapper.productcount(vo);
	}

	@Override
	public List<ProductDTO> getCatelist(int companyNum) {
		
		return productMapper.getCatelist(companyNum);
	}

	@Override
	public List<ProductDTO> getGoodsOption(String goodsCode, int companyNum) {
		return productMapper.goodsoptioninfo(goodsCode, companyNum);
	}

	@Override
	public void goodUpdate(ProdUpdateVO vo) {
		log.info("수정 내용 서비스 == {}",vo);
		productMapper.goodsupdate(vo);
		
	}

	@Override
	public List<ProductDTO> getGoodsNum(ProductSearchDTO dto) {
		return productMapper.goodsNum(dto);
	}

	@Override
	public int goodsNumCount(ProductSearchDTO vo) {
		return productMapper.goodsNumcount(vo);
	}

	@Override
	public List<ProductDTO> getGoodsLotNum(ProductSearchDTO dto) {
		return productMapper.goodsLotNum(dto);
	}

	@Override
	public int goodsLotNumCount(ProductSearchDTO vo) {
		return productMapper.goodsLotNumcount(vo);
	}
	

}
