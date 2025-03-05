package com.beauty1nside.bsn.dto.delivery;

import java.util.Date;

import com.beauty1nside.bsn.dto.OrderSearchDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BsnDeliveryDTO extends BsnDeliverySearchDTO {
	private String deliveryId;
	private Date deliveryDate;
	
	
	private String orderId;			//bsnOrder의 PK
	private String orderName;
	private String branchOfficeId;
	private Date orderDate;
	private Date dueDate;
	private String deliveryStatus;
	private String remark;
	private int employeeNum;
	private String employeeName;
	private int companyNum;
}
