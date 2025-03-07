package com.beauty1nside.accnut.dto;

import lombok.Data;

@Data
public class SellingDTO {
	private String resultDate;
	private String officeId;
	private String optionCode;
	private String optionName;
	private int sellQy;
	private int minusQy;
	int companyNum;
	int price;
	long totalPrice;
	//표하연이 넣음
	private String goodsName;
}
