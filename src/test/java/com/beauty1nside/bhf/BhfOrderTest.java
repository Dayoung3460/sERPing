package com.beauty1nside.bhf;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.beauty1nside.bhf.dto.goodsorder.BhfGoodsOpDTO;
import com.beauty1nside.bhf.dto.goodsorder.BhfOrdDtlVO;
import com.beauty1nside.bhf.dto.goodsorder.BhfOrdVO;
import com.beauty1nside.bhf.mapper.BhfOrderMapper;

@SpringBootTest
public class BhfOrderTest {

	@Autowired 
	BhfOrderMapper mapper;
	
	//@Test
	public void testStruct() {
		BhfOrdVO vo = new BhfOrdVO();
		vo.setBranchOfficeId("Yedam");
		vo.setDueDate((java.sql.Date) new Date(2025-02-14));
		vo.setRemark("");
		vo.setCompanyNum(14);
		vo.setFiles(List.of(new BhfOrdDtlVO("g1","skin","o1","100ml","1b/6p",5),
				            new BhfOrdDtlVO("g1","skin","o2","500ml","1b/6p",3),
				            new BhfOrdDtlVO("g1","lotion","o1","50ml","1b/6p",10)
				   ));
		
		mapper.orderPrd(vo);
		
		System.out.println("TEST실행성공");
	}
	
	//@Test
	public void goodsListTest() {
		List<BhfGoodsOpDTO> list = mapper.goodsList(null);
		System.out.println(list);
	}
	
	@Test
	public void optionListTest() {
		String goodsCode = "LH001";
		List<BhfGoodsOpDTO> list = mapper.optionList(goodsCode);
		System.out.println(list);
	}
	
}
