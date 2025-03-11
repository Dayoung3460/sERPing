package com.beauty1nside.accnut.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DealBookDTO {
	String dealingsAccountBookCode;
	String section;
	String typesOfTransaction;
	Long amount;
	String vatAlternative;
	String dealingsContents;
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	java.util.Date 사용시 데이트타임포맷 어노테이션 사용
	Date dealDate;
	String department;
	int companyNum;
}
