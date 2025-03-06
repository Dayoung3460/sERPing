package com.beauty1nside.erp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 부서별 권한 처리를 위한 DTO 생성 원래 부서생성할때 메뉴 접근권한 만들었어야함
 * @author ERP 관리자 개발팀 표하연
 * @since 2025.03.05
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.03.05  표하연          최초 생성
 *
 *  </pre>
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartmentDTO {
	private Long childDepartmentNum;
    private String childDepartmentName;
    private Long parentDepartmentNum;
    private String parentDepartmentName;
}
