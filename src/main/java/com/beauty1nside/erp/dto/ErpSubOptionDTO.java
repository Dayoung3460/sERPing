package com.beauty1nside.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구독 옵션 정보를 담는 DTO 클래스
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.02.18
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.02.18  표하연          최초 생성
 *
 *  </pre>
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpSubOptionDTO {
	//erp_subscription_option
    private int subscriptionOptionNum;   // 구독 옵션 번호 (PK)
    private int subscriptionNameNum;     // 구독 상품 번호 (외래키)
    private String subscriptionOption;   // 구독 옵션 (예: 50명, 100명, 9999명)
    private String subscriptionOptionPrice; // 구독 옵션 가격
    private int subscriptionDiscount;    // 구독 할인율
    private int subscriptionPeriod;      // 구독 기간
    
    //erp_subscription_name
    private String subscriptionName;   // 구독 상품 이름
}