package com.beauty1nside.purchs.dto;


import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderlistDTO {
	private String orderId;
	private String orderName;
	private Date orderDate;
	private Date dueDate;
	private String goodsCode;
	private String goodsName;
	private String optionCode;
	private String optionName;
	private int optionNum;
	private String goodsStandard;
	private int quantity;
	
	private int companyNum;
	
	private int vendorId;
	private String vendorName;
	private int goodsSupplyPrice;
	
	
	private String startDate ;
	private String endDate;
	

}
