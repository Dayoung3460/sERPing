package com.beauty1nside.hr.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Alias("EmployeeDTO")  // 별칭 추가
public class EmpDTO {
    
    private Long employeeNum; // 사원번호
    @Size(min = 5, max = 20, message = "사원 ID는 5~20자리여야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "사원 ID는 영문, 숫자, 언더바(_)만 사용할 수 있습니다.")
    private String employeeId; // 로그인 ID
    private String employeePw; // 비밀번호
    private String employeeName; // 사원 이름
    private String ssn; // 주민등록번호 (암호화 필요)
    private String email; // 이메일
    private String phone; // 연락처
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date hireDate; // 입사일
    private String position; // 직급 (공통코드: PO)
    private String positionCode;  // ✅ 직급 코드 (계약유형 자동 선택)
    private String authority; // 사원 권한 (공통코드: AU)
    private String status; // 재직 상태 (공통코드: ST)
    private Date quitDate; // 퇴사일
    private String address; // 주소
    private String addressDetail; // 상세주소
    private String zipCode; // 우편번호
    private String employmentType;  // ✅ 근무 유형 (ex: 정규직, 계약직)
    private String employmentTypeCode;  // ✅ 근무 유형 코드 (ex: ET001, ET002)
    private String bankName; // 급여 지급 은행명
    private String accountNum; // 계좌번호 (암호화 필요)
    private String profileImage; // 프로필 이미지 경로
    private String memo; // 메모
    private Date registerDate; // 등록일
    private Date updateDate; // 수정일
    private Long companyNum; // 회사번호 (FK)
    private String companyName; // 회사이름
    private String companyEngName; //회사영문명
    private Long departmentNum; // 부서번호 (FK)
    private Long salary; //연봉
    private String departmentName; //부서명
    private String parentDeptNum; //상위부서번호(departmentNum)
    private String firstSsn; //주민번호 앞자리
    private String secondSsn; //주민번호 뒷자리
    private Long parentDepartmentNum;  // ✅ 상위 부서 번호 추가
    private String parentDepartmentName;  // ✅ 상위 부서명 추가
    private String contractStatus;
    
}
