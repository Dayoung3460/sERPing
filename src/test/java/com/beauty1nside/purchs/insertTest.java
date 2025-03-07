package com.beauty1nside.purchs; 
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.beauty1nside.purchs.dto.ProdInsertDtlVO;
import com.beauty1nside.purchs.dto.ProdInsertVO;
import com.beauty1nside.purchs.mapper.productMapper;
import com.beauty1nside.purchs.service.productService;

@SpringBootTest
public class insertTest {

    @Autowired 
    private productMapper mapper;
    
    @Autowired
    private productService service;

    //@Test
    public void productInsertTest() {
        ProdInsertVO vo = new ProdInsertVO();
        vo.setGoodsName("dfsfd");
        vo.setGoodsCost(4565454);
        vo.setGoodsPrice(4546545);
        vo.setGoodsSupplyPrice(12121);
        vo.setGoodsStandard("sdfsfsd");
        vo.setGoodsDescription("sdffsfdsfsd");
        vo.setGoodsImage("sfdsdfsd");
        vo.setClassificationId(1);
        vo.setBrandId(1);
        vo.setEmployeeNum(1);
        vo.setVendorId(1);
        vo.setCompanyNum(1);

        // ✅ List.of() 괄호 수정
        vo.setFiles(List.of(
            new ProdInsertDtlVO("ㄴㄹㅇㄴㄹㄴㅇ", 3),
            new ProdInsertDtlVO("ㄴㄹㅇㄴㄹ", 4)
        ));

        // ✅ 상품 등록 실행
        service.goodsinsert(vo);
    }
}
