package com.beauty1nside.bhf.dto.goodsorderlist;

import java.sql.Date;

import lombok.Data;

@Data
public class BhfOrdListDTO {

	private String orderCode;
	private String branchOfficeId;
	private Date orderDate;
	private Date dueDate;
	private String progressStatus;
	private String remark;
	private String orderCancelReason;
	private String countName;
	
	private String goodsCode;
	private String goodsName;
	private String optionCode;
	private String optionName;
	private String goodsStandard;
	private int quantity;
	
	private int companyNum;
	
}
