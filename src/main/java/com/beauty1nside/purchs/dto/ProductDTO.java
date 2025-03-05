package com.beauty1nside.purchs.dto;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
	//상품
	private int goodsNum;
	private String goodsName;
	private int goodsCost;
	private String goodsCode;
	private int goodsPrice;
	private int goodsSupplyPrice;
	private String goodsStandard;
	private String goodsDescription;
	private String goodsImage;
	
	private String classificationName;
	private String classificationCode;
	private int classificationId;
	
	private Long brandId;
	private String brandCode;
	private String brandName;
	
	private Long employeeNum;
	private String employeeName;
	
	private Long vendorId;
	private String vendorName;
	
	private int marginRate;
	private int companyNum;
	
	//옵션
	private int optionNum;
	private String optionCode;
	private String optionName;
	private int optionSafetyInvoice;
	private int optionUseFlag;
	
	
	private Long warehouseId;
	private String warehouseName;
	
	//파일 이미지 업로드
	@Value("${img.upload}")
	private String imgUpload;
	

	private int safetyStock; //안전재고 계산 값
	private int totalRemainingQuantity; 	//상품옵션당 갯수
	private String 	goodsUseStatus; //상품 사용 유무 글자
	private int goodsUseFlag; //상품 사용 유무 플래그
	private String optionUseStatus;
	
	private int totalInputQuantity; //입고수량(개당)
	private int totalOutputQuantity; //출고수량(개당)
	private int totalQuantity;//총재고수량(개당)
	private int totalRestockQuantity;
	
	private String goodsLotNum;
	private Date goodsConsumptionDate;
	private Date warehousingDate;
	
	
	// ✅ 사용 여부 필터 추가
    private Boolean useGoods;
    private Boolean unUseGoods;
	
	
}
