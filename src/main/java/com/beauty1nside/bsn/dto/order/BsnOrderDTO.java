package com.beauty1nside.bsn.dto.order;

import java.util.Date;
import java.util.List;

import com.beauty1nside.bsn.dto.OrderSearchDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BsnOrderDTO extends OrderSearchDTO {
	private String orderId;			//bsnOrder의 PK
	private String orderCode;		//bhfOrder의 PK
	private String branchOfficeId;
	private String orderName;
	private Long total_amount;
	private Long purchaseVat;
	private Date orderDate;
	private Date registerDate;
	private Date cancleDate;
	private Date dueDate;
	private Date sendingDate;
	private String orderStatus;
	private int employeeNum;
	private String employeeName;
	private String remark;
	private int companyNum;

	
	private List<BsnOrderDetailDTO> orderDetails;

	
}
