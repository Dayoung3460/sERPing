/**
 * emp_register_modal.js - 사원 등록 모달 관리
 */

document.addEventListener("DOMContentLoaded", function () {
    applyCommonCodesToModal(); // 모달 공통 코드 적용
    setupEventListeners(); // 이벤트 리스너 연결
	
    // ✅ 초기화 버튼 이벤트 리스너 연결 (id 일치 확인)
    let resetBtn = document.getElementById("resetBtn");
    if (resetBtn) {
        resetBtn.addEventListener("click", resetEmployeeForm);
        console.log("✅ 초기화 버튼 이벤트 연결 완료");
    } else {
        console.error("❌ resetBtn을 찾을 수 없습니다.");
    }

    // ✅ 모달이 열릴 때마다 초기화
    let empRegisterModal = document.getElementById("empRegisterModal");
    if (empRegisterModal) {
        empRegisterModal.addEventListener("shown.bs.modal", resetEmployeeForm);
        console.log("✅ 모달 이벤트 리스너 연결 완료");
    } else {
        console.error("❌ empRegisterModal을 찾을 수 없습니다.");
    }
    
});

// ✅ 공통 코드 데이터를 모달에 적용하는 함수
function applyCommonCodesToModal() {
    console.log("📥 공통 코드 데이터를 모달에 적용 중...");

    // ✅ 부서 선택 적용
    const departmentSelect = document.getElementById("modalDepartment");
    departmentSelect.innerHTML = '<option value="">선택</option>';
    departments.forEach(dept => {
        departmentSelect.innerHTML += `<option value="${dept.DEPARTMENT_NUM}">${dept.DEPARTMENT_NAME}</option>`;
    });

    // ✅ 직급 선택 적용
    const positionSelect = document.getElementById("modalPosition");
    positionSelect.innerHTML = '<option value="">선택</option>';
    positions.forEach(pos => {
        positionSelect.innerHTML += `<option value="${pos.CMMNCODE}">${pos.CMMNNAME}</option>`;
    });

    // ✅ 재직 상태 버튼 적용
    populateStatusButtons("modalStatusGroup", statuses);

    // ✅ 근무 유형 버튼 적용
    populateEmploymentButtons("modalEmploymentTypeGroup", employmentTypes);
}

// ✅ 모달 초기화 함수
function initEmployeeRegisterModal() {
    console.log("✅ 사원 등록 모달 초기화...");
    
    applyCommonCodesToModal();
    
    // 모달이 열릴 때마다 실행될 이벤트 추가
    let empRegisterModal = document.getElementById("empRegisterModal");
    empRegisterModal.addEventListener("shown.bs.modal", resetEmployeeForm);
    
    // 기본 데이터 로드
    fetchNewEmployeeId();  // 사원번호 자동 생성

    // 버튼 이벤트 리스너 추가
    document.getElementById("registerEmployeeBtn").addEventListener("click", registerEmployee);
}

// ✅ 예금주 조회 기능
function checkAccountOwner() {
    const bankCode = document.getElementById("bankSelect").value;
    const accountNumber = document.getElementById("accountNumber").value;

    if (!bankCode || !accountNumber) {
        alert("⚠️ 은행 및 계좌번호를 입력하세요.");
        return;
    }

    fetch(`/api/iamport/account-holder?bankCode=${bankCode}&accountNumber=${accountNumber}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("accountHolderName").value = data;
        })
        .catch(error => console.error("❌ 예금주 조회 실패:", error));
}

// ✅ 모달 초기화 함수 (사원 등록 필드 초기화)
function resetEmployeeForm() {
    console.log("🟢 사원 등록 모달 초기화 실행됨");

    // 1️⃣ 모든 입력 필드 초기화
    document.querySelectorAll("#empRegisterModal input").forEach(input => {
        if (input.type === "text" || input.type === "email" || input.type === "number" || input.type === "date") {
            input.value = "";
        }
    });

    // 2️⃣ select 요소 초기화
    document.querySelectorAll("#empRegisterModal select").forEach(select => {
        select.selectedIndex = 0;
    });

    // 3️⃣ 라디오 버튼 초기화 (첫 번째 값으로 선택)
    let radioButtons = document.querySelectorAll("#empRegisterModal input[type='radio']");
    if (radioButtons.length > 0) {
        radioButtons[0].checked = true;
    }

    // 4️⃣ 프로필 이미지 기본 이미지로 변경
    let profilePreview = document.getElementById("profilePreview");
    if (profilePreview) {
        profilePreview.src = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
    }

    // 5️⃣ 예금주 필드 초기화
    let accountHolderName = document.getElementById("accountHolderName");
    if (accountHolderName) {
        accountHolderName.value = "";
    }

    // 6️⃣ 새 사원번호 가져오기
    fetchNewEmployeeId();

    console.log("✅ 초기화 완료");
}

// ✅ 사원 등록
function registerEmployee() {
    let employeeData = {
        employeeName: document.getElementById("employeeName").value,
        email: document.getElementById("emailInput").value + "@" + document.getElementById("emailDomainSelect").value,
        phone: document.getElementById("phoneNumber").value,
        hireDate: document.getElementById("hireDate").value,
        departmentNum: document.getElementById("modalDepartment").value,
        position: document.getElementById("modalPosition").value,
        employmentStatus: document.querySelector("input[name='employmentStatus']:checked")?.value || "",
        employmentType: document.querySelector("input[name='modalEmploymentType']:checked")?.value || "",
        salary: document.getElementById("salaryInput").value,
        bankName: document.getElementById("bankSelect").value,
        accountNum: document.getElementById("accountNumber").value,
        zipcode: document.getElementById("zipcode").value,
        address: document.getElementById("address").value,
        memo: document.getElementById("memo").value
    };

    console.log("📥 사원 등록 요청 데이터:", employeeData);

    fetch("/hr/rest/emp/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(employeeData)
    })
    .then(response => response.json())
    .then(data => {
        alert("✅ 사원 등록 완료!");
        location.reload();
    })
    .catch(error => console.error("❌ 서버 오류:", error));
}


// ✅ 이메일 입력 방식 초기화 함수
function initializeEmailInput() {
    let emailInput = document.getElementById("emailInput");
    let emailAt = document.getElementById("emailAt");
    let emailDomainSelect = document.getElementById("emailDomainSelect");

    if (!emailInput || !emailAt || !emailDomainSelect) {
        console.error("❌ 이메일 입력 요소가 존재하지 않습니다.");
        return;
    }

    emailDomainSelect.addEventListener("change", function () {
        let selectedValue = emailDomainSelect.value;

        if (selectedValue === "custom") {
            // ✅ "직접 입력" 선택 시 전체 입력 가능하도록 설정
            emailAt.style.display = "none"; // @ 숨기기
            emailInput.placeholder = "이메일 주소 전체 입력"; // 힌트 제공
            emailInput.value = ""; // 기존 값 초기화
        } else {
            // ✅ 특정 도메인을 선택하면 아이디 + 도메인 형태로 변경
            emailAt.style.display = "inline"; // @ 보이기
            emailInput.placeholder = "아이디 입력"; // 힌트 변경

            if (selectedValue !== "") {
                emailInput.value = ""; // 아이디 부분만 입력 가능하게 초기화
            }
        }
    });
    
}

