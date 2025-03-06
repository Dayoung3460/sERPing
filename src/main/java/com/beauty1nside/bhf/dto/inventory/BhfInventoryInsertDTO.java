package com.beauty1nside.bhf.dto.inventory;

import java.sql.Date;

import lombok.Data;

@Data
public class BhfInventoryInsertDTO {

	private String goodsCode;
	private String goodsName;
	private String optionCode;
	private String optionName;
	private String goodsStandard;
	private int quantity;
	private int mediationQuantity;
	private Date mediationDate;
	private String mediationReason;
	private String branchOfficeId;
	private int companyNum;
	
}
