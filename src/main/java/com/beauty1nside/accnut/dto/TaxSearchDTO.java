package com.beauty1nside.accnut.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class TaxSearchDTO {
	int start;
	int end;
	int pageUnit;
	
	int toRgno;
	Date rgdate;
	int companyNum;
}
