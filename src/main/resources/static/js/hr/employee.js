/**
 * employee.js
 */

let grid;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf"]').content;

document.addEventListener("DOMContentLoaded", function () {
    initializeGrid();
    setupEventListeners();
    fetchNewEmployeeId(); // 모달 열릴 때 사원번호 자동 입력
    
    let registerBtn = document.getElementById("registerBtn");
    
    if (registerBtn) {
        registerBtn.addEventListener("click", function (event) {
            console.log("🔍 등록 버튼 클릭됨!");

            // 🔹 입력값 검증 후 실행
           /* if (!validateEmployeeForm()) {
                console.warn("⚠️ 필수 입력값이 누락되었습니다. 등록을 중단합니다.");
                return;
            }*/

            // 🔹 사원 등록 실행
            registerEmployee();
        });

        console.log("✅ 등록 버튼 이벤트 리스너 연결 완료!");
    } else {
        console.error("❌ registerBtn 요소를 찾을 수 없습니다!");
    }
    
    document.getElementById("empRegisterModal").addEventListener("show.bs.modal", function () {
	    populateModalData();  // 모달 공통 코드 데이터 로드
	});

    
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

// 전화번호 포맷 함수 (01012345678 → 010-1234-5678)
function formatPhoneNumber({ value }) {
    if (!value) return ""; // 값이 없으면 빈 문자열 반환
    value = value.replace(/\D/g, ""); // 숫자 이외 문자 제거

    if (value.length === 11) {
        return value.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
    } else if (value.length === 10) {
        return value.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
    }
    return value; // 위 조건에 해당하지 않으면 원본 반환
}

// Toast Grid 초기화 함수
function initializeGrid() {
    var Grid = tui.Grid;
    const dataSource = {
        api: {
            readData: {
                url: 'http://localhost:81/hr/rest/emp/list',
                method: 'GET',
                initParams: { page: 1 }
            }
        },
        contentType: 'application/json',
    };

    Grid.applyTheme('stripe');

    grid = new Grid({
        el: document.getElementById('grid'),
        width: "100%",
        autoWidth: true,
        pageOptions: {
            useClient: false,
            perPage: 5,
        },
        rowHeaders: [{
            type: 'rowNum',
            header: "순번",
            width: 50,
        }],
        columns: [
            { header: "사원ID", name: "employeeId", align: "center", width: 100 },
            { header: "사원명", name: "employeeName", align: "center", sortable: true, width: 150 },
            { header: "부서", name: "departmentName", align: "center", sortable: true, width: 100 },
            { header: "직급", name: "position", align: "center", sortable: true, width: 100, formatter: formatCommonCode('position') },
            { header: "재직 상태", name: "status", align: "center", sortable: true, width: 100, formatter: formatCommonCode('status') },
            { header: "근무 유형", name: "employmentType", align: "center", sortable: true, width: 100, formatter: formatCommonCode('employmentType') },
            { header: "입사일", name: "hireDate", align: "center", sortable: true, width: 150, formatter: ({ value }) => value?.split('T')[0] || '' },
            { header: "연락처", name: "phone", align: "center", sortable: true, width: 150, formatter: formatPhoneNumber  },
            { header: "이메일", name: "email", align: "center", sortable: true, width: 200 }
        ],
        data: dataSource,
        rowHeaders: ['checkbox'],
        	
    });
}

// 공통 코드 목록 불러오기 (동적 적용)
let commonCodes = {}; // 🔹 공통 코드 저장 변수
function loadCommonCodes() {
    fetch("http://localhost:81/hr/rest/emp/common-codes")
        .then(response => response.json())
        .then(data => {
            commonCodes = data;
            populateFilters(); // 필터 UI 업데이트
            populateModals(); // 모달 UI 업데이트
        })
        .catch(error => console.error("공통 코드 로딩 실패:", error));
}

// 공통 코드 포맷터
function formatCommonCode(type) {
    return ({ value }) => commonCodes[type]?.[value] || value;
}

// 필터 UI 동적 생성
function populateFilters() {
    const positionSelect = document.getElementById("positionFilter");
    const statusSelect = document.getElementById("statusFilter");
    const employmentTypeSelect = document.getElementById("employmentTypeFilter");
    const departmentSelect = document.getElementById("departmentFilter");
    
    if (!positionSelect || !statusSelect || !employmentTypeSelect || !departmentSelect) {
        console.error("❌ populateFilters() 실행 실패! 필터 요소 중 일부가 존재하지 않습니다.");
        return; // 🔴 요소가 없으면 함수 실행 중단
    }

    // 기존 옵션 제거
    positionSelect.innerHTML = '<option value="">전체</option>';
    statusSelect.innerHTML = '<option value="">전체</option>';
    employmentTypeSelect.innerHTML = '<option value="">전체</option>';
    departmentSelect.innerHTML = '<option value="">전체</option>';

    // 공통 코드 옵션 추가
    Object.entries(commonCodes.position || {}).forEach(([key, value]) => {
        positionSelect.innerHTML += `<option value="${key}">${value}</option>`;
    });
    Object.entries(commonCodes.status || {}).forEach(([key, value]) => {
        statusSelect.innerHTML += `<option value="${key}">${value}</option>`;
    });
    Object.entries(commonCodes.employmentType || {}).forEach(([key, value]) => {
        employmentTypeSelect.innerHTML += `<option value="${key}">${value}</option>`;
    });
}

// 검색 필터 적용
function getFilterParams() {
    const departmentSelect = document.getElementById("searchDepartment");
    const subDepartmentSelect = document.getElementById("searchSubDepartment");

    let selectedDeptNum = departmentSelect.value; // 상위 부서 선택 값
    let selectedSubDeptNum = subDepartmentSelect.value; // 하위 부서 선택 값
    let selectedPosition = document.getElementById("searchPosition")?.value || "";
    let selectedStatus = document.querySelector("input[name='searchStatus']:checked")?.value || "";
if (selectedStatus === "on") selectedStatus = ""; // "전체" 선택 시 공백 처리
	let selectedEmploymentType = document.querySelector("input[name='employmentType']:checked")?.value || "";
	
    // 🔹 "검색 기준" 선택 값 가져오기
    let searchType = document.getElementById("searchCategory")?.value || "전체";
	let searchKeyword = document.getElementById("searchKeyword")?.value || "";
	searchKeyword = searchKeyword.trim(); // 앞뒤 공백 제거


    if (selectedStatus === "on") selectedStatus = ""; // "전체" 선택 시 공백 처리




    let params = {
        departmentNum: selectedDeptNum !== "all" ? selectedDeptNum : "",
        subDepartmentNum: selectedSubDeptNum !== "" ? selectedSubDeptNum : "",
        position: selectedPosition, // 직급 값 반영
        status: selectedStatus,
        employmentType: document.querySelector("input[name='employmentType']:checked")?.value === "on" ? "" : document.querySelector("input[name='employmentType']:checked")?.value,
        searchType: searchType,
        searchKeyword: searchKeyword,
    };
    
    
    // 🔹 검색어가 입력되었을 경우 처리
    if (searchKeyword !== "") {
        if (searchType === "전체") {
            // ✅ 검색어가 있으면 OR 조건으로 검색 (사원명 OR 사원ID OR 연락처)
            params.searchType = "전체";
            params.searchKeyword = searchKeyword;
        } else {
            // ✅ 특정 검색 기준 선택 시 해당 필드만 검색
            params.searchType = searchType;
            params.searchKeyword = searchKeyword;
        }
    }

    // 전체 부서 선택 시 모든 부서 표시
    if (selectedDeptNum === "all" || selectedDeptNum === "") {
        params.departmentNum = "";
    } else {
        params.departmentNum = selectedDeptNum;
    }

    // 하위 부서 선택 시 해당 부서만 필터링
    if (selectedSubDeptNum !== "") {
        params.subDepartmentNum = selectedSubDeptNum;
    }

    console.log("getFilterParams() 결과:", params);
    return params;

}


// Toast Grid 데이터 새로고침
function searchEmployees(page = 1) {
    const params = getFilterParams(); // 검색 필터 적용
    console.log("🔍 검색 요청 파라미터:", params);  // ✅ 파라미터 확인용 로그
    params.page = page; // 현재 페이지 값 추가

    // URLSearchParams 사용 (불필요한 중복 제거)
    const urlParams = new URLSearchParams(params);
    
    console.log("🔍 API 요청 URL:", `/hr/rest/emp/list?${urlParams.toString()}`);

    // Toast Grid 검색 필터 적용 후 데이터 새로 불러오기
    grid.readData(page, params, true);
}


// 필터 이벤트 리스너 설정
function setupEventListeners() {
    const searchBtn = document.getElementById("searchBtn");
    const resetBtn = document.getElementById("resetBtn");
    const searchKeywordInput = document.getElementById("searchKeyword");

    if (searchBtn) {
        searchBtn.addEventListener("click", function () {
            searchEmployees();
        });
    } else {
        console.error("❌ searchBtn 요소를 찾을 수 없음!");
    }

    if (searchKeywordInput) {
        searchKeywordInput.addEventListener("keyup", function (event) {
            console.log("Key pressed:", event.key);
            if (event.key === "Enter") {
                console.log("Enter pressed. Searching...");
                searchEmployees();
            }
        });
    } else {
        console.error("❌ searchKeyword 요소를 찾을 수 없음!");
    }

    document.querySelectorAll("#searchDepartment, #searchPosition,#searchSubDepartment, input[name='searchStatus'], input[name='employmentType']").forEach(filter => {
        if (filter) {
            filter.addEventListener("change", searchEmployees);
        } else {
            console.error("❌ 필터 요소를 찾을 수 없음:", filter);
        }
    });
}


// 초기화 버튼 기능
function resetFilters() {
    document.getElementById("searchCategory").value = "전체";
    document.getElementById("searchKeyword").value = "";
    document.getElementById("searchDepartment").value = "";
    document.getElementById("searchSubDepartment").value = "";
    document.getElementById("searchPosition").value = "";
    document.querySelectorAll("input[name='searchStatus']").forEach(btn => btn.checked = false);
    document.querySelectorAll("input[name='employmentType']").forEach(btn => btn.checked = false);

    searchEmployees(); // ✅ 모든 필터 초기화 후 전체 데이터 조회
}


document.getElementById("searchDepartment").addEventListener("change", function () {
    let selectedDeptNum = this.value; // 선택한 부서의 `DEPARTMENT_NUM`
    populateSubDepartments(selectedDeptNum); // 하위 부서 필터링
    searchEmployees(); // 부서 선택 후 자동 검색 실행
});

document.getElementById("searchSubDepartment").addEventListener("change", function () {
    searchEmployees(); // 하위 부서 선택 후 자동 검색 실행
});

// 직급 선택 시 자동 검색 실행
document.getElementById("searchPosition").addEventListener("change", function(){
	 searchEmployees();
});


// ✅ 상태 필터(재직, 퇴직, 휴직) 변경 시 검색 실행
document.querySelectorAll("input[name='searchStatus']").forEach(btn => {
    btn.addEventListener("change", function () {
        searchEmployees();
    });
});


// 🔹 새 사원번호 가져오기
function fetchNewEmployeeId() {
    fetch("/hr/rest/emp/new-employee-id")
        .then(response => response.text())
        .then(data => {
            document.getElementById("employeeIdInput").value = data; // 사원번호 입력칸에 자동 반영
        })
        .catch(error => console.error("❌ 사원번호 생성 오류:", error));
}

/**
 * 📌 입력값 검증 함수
 */
function validateEmployeeForm() {
    let employeeName = document.getElementById("employeeName")?.value.trim();
    let email = document.getElementById("email")?.value.trim();
    let phone = document.getElementById("phone")?.value.trim();
    let hireDate = document.getElementById("hireDate")?.value.trim();
    let departmentNum = document.getElementById("modalDepartment")?.value.trim();
    let position = document.getElementById("modalPosition")?.value.trim();
    let employmentType = document.querySelector("input[name='modalEmploymentType']:checked")?.value;

    if (!employeeName) {
        alert("⚠️ 사원명을 입력하세요.");
        return false;
    }
    if (!email) {
        alert("⚠️ 이메일을 입력하세요.");
        return false;
    }
    if (!phone) {
        alert("⚠️ 연락처를 입력하세요.");
        return false;
    }
    if (!hireDate) {
        alert("⚠️ 입사일을 선택하세요.");
        return false;
    }
    if (!departmentNum) {
        alert("⚠️ 부서를 선택하세요.");
        return false;
    }
    if (!position) {
        alert("⚠️ 직급을 선택하세요.");
        return false;
    }
    if (!employmentType) {
        alert("⚠️ 근무 유형을 선택하세요.");
        return false;
    }

    return true;
}


function registerEmployee() {
	let employmentId = document.querySelector("input[name='modalEmploymentType']:checked")?.id;
	let employmentValue = employmentId ? employmentId.substring(employmentId.lastIndexOf("_") + 1) : "";
    let empData = {
		employeeId: document.getElementById("employeeIdInput")?.value || "",
        employeeName: document.getElementById("employeeName")?.value || "",
        email: document.getElementById("email")?.value || "",
        phone: document.getElementById("phone")?.value || "",
        hireDate: document.getElementById("hireDate")?.value || "",
        departmentNum: document.getElementById("modalSubDepartment")?.value || "",
        position: document.getElementById("modalPosition")?.value || "",
        status: "ST001",
        employmentType: employmentValue || "",
        salary: document.getElementById("salary")?.value || "",
        bankName: document.getElementById("bankSelect")?.options[document.getElementById("bankSelect").selectedIndex].text.trim() || "",
        accountNum: document.getElementById("accountNumber")?.value || "",
        zipCode: document.getElementById("zipcode")?.value || "",
        address: document.getElementById("address")?.value || "",
        addressDetail: document.getElementById("addressDetail")?.value || "",
        memo: document.getElementById("memo")?.value || "",
        parentDeptNum: document.getElementById("modalDepartment")?.value || "",
        companyNum: document.getElementById("companyNumSJ")?.value || "",
        firstSsn: document.getElementById("firstSsn")?.value || "",
        secondSsn: document.getElementById("secondSsn")?.value || "",
        authority: document.getElementById("modalAutority")?.value || "",
    };
    
    
/*    	empData = {
	    employeeId: "250220007",
	    employeeName: "길동이",
	    email: "seozzini@gmail.com",
	    phone: "01000000000",
	    hireDate: "2025-02-22",
	    accountNum: "302015151210",
	    address: "경기 성남시 분당구 서판교로 32",
	    addressDetail: "10층",
	    bankName: "KB국민은행",
	    departmentNum: "7",         // 하위 부서
	    parentDeptNum: "8",         // 상위 부서
	    status: "ST001",
	    memo: "메모메모메",
	    phone: "01000000000",
	    position: "PO013",
	    salary: "50000000",
	    employmentType: "ET002",
	    zipCode: "13479",
	    companyNum: "1",
	    authority: "AU004",
	    ssn: "910000-2000000"
	    
	};*/



	console.log("empData::::::",empData);

    fetch("/hr/rest/emp/register", {
        method: "POST",
        headers: {
                'header': header,
                "Content-Type": "application/json",
                'X-CSRF-Token': token
            },
        body: JSON.stringify(empData)
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        return;
        //location.reload();
    })
    .catch(error => console.error("❌ 등록 실패:", error));
    
}

    
let globalDepartments = [];
let globalSubDepartments = [];
    

// ✅ 모달 공통 코드 데이터 로드
function populateModalData() {
    console.log("🔹 모달 공통 코드 데이터 불러오는 중...");

    fetch("/hr/rest/emp/common-codes")
        .then(response => response.json())
        .then(data => {
            if (!data) {
                console.error("❌ 공통 코드 데이터를 불러오지 못함.");
                return;
            }

            console.log("📥 불러온 공통 코드 데이터:", data);

            // ✅ 전역 변수에 부서 및 하위 부서 저장
            globalDepartments = data.departments;  
            globalSubDepartments = data.departments.filter(dept => dept.PARENT_DEPARTMENT_NUM !== null); // 하위 부서만 저장

            // ✅ 부서 (Department) 선택 리스트 설정
            const departmentSelect = document.getElementById("modalDepartment");
            departmentSelect.innerHTML = `
                <option value="">선택</option>
                <option value="1">본사</option>
                <option value="8">지점</option>
            `;

            // ✅ 하위 부서 초기화 (모든 하위 부서 표시)
            populateSubDepartments("");

            // ✅ 부서 선택 시 이벤트 리스너 추가
            departmentSelect.removeEventListener("change", handleDepartmentChange);
            departmentSelect.addEventListener("change", handleDepartmentChange);

            console.log("✅ 부서 목록 업데이트 완료!");
        })
        .catch(error => console.error("❌ 모달 공통 코드 데이터 불러오기 실패:", error));
}

// ✅ 부서 선택 변경 시 실행할 핸들러 함수
function handleDepartmentChange() {
    const selectedDeptNum = document.getElementById("modalDepartment").value;
    console.log("📌 선택한 부서:", selectedDeptNum);
    populateSubDepartments(selectedDeptNum);
}

// ✅ 선택된 부서에 따른 하위 부서 필터링 (동적 표시)
function populateSubDepartments(selectedDeptNum) {
    const subDepartmentSelect = document.getElementById("modalSubDepartment");

    // ✅ 기존 옵션 초기화 ("선택" 추가)
    subDepartmentSelect.innerHTML = `
        <option value="">선택</option>
    `;

    let filteredSubDepartments = [];

    if (!selectedDeptNum) {
        // ✅ "선택" 상태에서는 모든 하위 부서 표시
            subDepartmentSelect.innerHTML += `
        <option value="">선택</option>
    `;

    } else {
        // ✅ "본사" 또는 "지점"을 선택하면 해당 부서의 하위 부서만 표시
        filteredSubDepartments = globalSubDepartments.filter(
            subDept => String(subDept.PARENT_DEPARTMENT_NUM) === String(selectedDeptNum) // 🔥 `String` 변환하여 비교 오류 방지
        );
    }

    console.log("📌 선택한 부서:", selectedDeptNum);
    console.log("📌 필터링된 하위 부서 목록:", filteredSubDepartments); // 🔥 콘솔에 확인
    
    

    // ✅ 하위 부서 옵션 추가 (실제 데이터 기반)
    filteredSubDepartments.forEach(subDept => {
        let option = document.createElement("option");
        option.value = subDept.DEPARTMENT_NUM;
        option.textContent = subDept.DEPARTMENT_NAME;
        subDepartmentSelect.appendChild(option);
    });

    console.log("✅ 하위 부서 목록 업데이트 완료!", subDepartmentSelect.innerHTML); // 🔥 콘솔에서 확인
}

// Daum 우편번호 API를 활용한 주소 검색 함수
function openPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 우편번호 입력
            document.getElementById("zipcode").value = data.zonecode;

            // 주소 입력
            document.getElementById("address").value = data.roadAddress || data.jibunAddress;

            // 상세주소 입력란에 포커스 이동
            document.getElementById("addressDetail").focus();
        }
    }).open();
}


