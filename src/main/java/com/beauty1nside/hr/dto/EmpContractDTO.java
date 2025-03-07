package com.beauty1nside.hr.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EmpContractDTO {
    
    private Long contractNum;  // 계약번호
    private Long employeeNum;  // 사원번호 (FK)
    private Long companyNum;   // 회사번호 (FK)
    private Long departmentNum; // 부서번호 (FK)
    private String position;   // 직급
    private String contractType; // 계약 유형 (공통코드: CT)
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractStartDate; // 계약 시작일
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractEndDate; // 계약 종료일
    
    private String workLocation; // 근무지
    private String jobDescription; // 업무 내용
    private String workDays; // 근무일
    
    private String workStartTime; // 근무 시작 시간
    private String workEndTime;   // 근무 종료 시간
    private String breakStartTime; // 휴게 시작 시간
    private String breakEndTime;   // 휴게 종료 시간
    
    private int probationPeriod; // 수습기간 (개월)
    private String contractStatus; // 계약 상태 (CS001: 진행중, CS002: 종료)
    private String terminationReason; // 계약 해지 사유
    private String contractFilePath; // 계약서 파일 경로
    
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate; // 등록일

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate; // 수정일

    private Double annualSalary;  // 연봉
    private Double monthlySalary;  // 월급
    private Double bonus; // 상여금
    private Double additionalPay; // 추가 수당
    
    private Integer salaryPaymentDate;  // 급여 지급일 (1~31)
    private String paymentMethod;  // 급여 지급 방식 (계좌이체 / 직접지급)
    
    private String annualLeavePolicy = "근로기준법에 따라 부여";  
    private String socialInsurance = "고용보험, 산재보험, 국민연금, 건강보험 가입";  
    
    private String employeeName;
    private String companyName;

    
    
    
}
