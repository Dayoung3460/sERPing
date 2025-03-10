package com.beauty1nside.accnut.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class DebtDTO {
	String debtCode;
	String debtName;
	String section;
	Date registerDate;
	String creditor;
	Long amount;
	double interest;
	Date timeLimit;
	Date prearrangementDueDate;
	int companyNum;
}
