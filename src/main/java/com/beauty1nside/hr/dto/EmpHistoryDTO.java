package com.beauty1nside.hr.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmpHistoryDTO {
    private Long historyNum;          // 발령 이력 번호 (PK)
    private Long employeeNum;         // 대상 사원 번호
    private Long companyNum;          // 소속 회사 번호
    private String previousPosition;  // 기존 직급
    private String newPosition;       // 새로운 직급
    private Long previousDepartmentNum; // 기존 부서 번호
    private Long newDepartmentNum;    // 새로운 부서 번호
    private String previousEmploymentType; // 기존 고용 유형
    private String newEmploymentType; // 새로운 고용 유형

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;   // 발령 등록일 (SYSDATE)
}
