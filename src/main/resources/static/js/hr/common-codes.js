/**
 * common-codes.js
 */

document.addEventListener("DOMContentLoaded", function () {
    loadCommonCodes();
});

// 부서 목록을 담을 변수
let departments = []; // 부서 목록 저장
let positions = [];   // 직급 목록 저장
let statuses = [];    // 재직 상태 저장
let employmentTypes = []; // 근무 유형 저장

// 공통 코드 데이터 불러오는 함수
function loadCommonCodes() {
    fetch('/hr/rest/emp/common-codes')
        .then(response => response.json())
        .then(data => {
            console.log("공통 코드 로드됨:", data); // 데이터 확인용
            if (!data || !data.departments) {
                console.error("⚠ No department data received.");
                return;
            }
            
  // ✅ 기존 배열도 업데이트 (🔴 핵심 수정 부분)
            departments = data.departments || [];
            positions = data.positions || [];
            statuses = data.statuses || [];
            employmentTypes = data.employmentTypes || [];

            // ✅ 공통 코드 객체에도 저장 (기존 코드 유지)
            commonCodes.departments = departments;
            commonCodes.positions = positions;
            commonCodes.statuses = statuses;
            commonCodes.employmentTypes = employmentTypes;

            // ✅ 필터 & 모달 UI 업데이트
            populateDepartmentSelect(departments);
            populatePositionSelect(positions);
            populateStatusButtons(statuses);
            populateEmploymentButtons(employmentTypes);
            
            populateFilters();

            // ✅ 이벤트 발생 (employee.js에서 로딩 완료 후 실행하도록)
            document.dispatchEvent(new Event("commonCodesLoaded"));
        })
        .catch(error => console.error("❌ Error loading common codes:", error));
}

// 부서 목록 동적으로 추가
function populateDepartmentSelect(departments) {
    const departmentSelect = document.getElementById("searchDepartment");
    departmentSelect.innerHTML = '<option value="">선택</option>'; // 초기화

    // 최상위 부서 (부모 없는 부서)만 추가
    let parentDepartments = departments.filter(dept => !dept.PARENT_DEPARTMENT_NUM);
    parentDepartments.forEach(dept => {
        departmentSelect.innerHTML += `<option value="${dept.DEPARTMENT_NUM}">${dept.DEPARTMENT_NAME}</option>`;
    });

    //  부서 선택 시, 하위 부서 필터링
    departmentSelect.addEventListener("change", function () {
        const selectedDeptCode = this.value;
        populateSubDepartments(selectedDeptCode);
    });
}

// 하위 부서 필터링 함수
function populateSubDepartments(parentCode) {
    const subDepartmentSelect = document.getElementById("searchSubDepartment");
    subDepartmentSelect.innerHTML = '<option value="">선택</option>'; // 🔥 초기화

	let subDepartments = [];
	
    if (!parentCode) {
        // ✅ "선택" 상태에서는 모든 하위 부서 표시
        subDepartmentSelect.innerHTML = '<option value="">선택</option>'; // 🔥 초기화
    } else {
        // ✅ 특정 부서를 선택하면 해당 부서의 하위 부서만 표시
        subDepartments = departments.filter(dept => String(dept.PARENT_DEPARTMENT_NUM) === String(parentCode));
    }

    console.log(`📌 선택한 부서: ${parentCode}, 표시할 하위 부서:`, subDepartments);

    // ✅ 하위 부서 옵션 추가
    subDepartments.forEach(dept => {
        subDepartmentSelect.innerHTML += `<option value="${dept.DEPARTMENT_NUM}">${dept.DEPARTMENT_NAME}</option>`;
    });

    console.log(`하위 부서 (${parentCode}) 목록:`, subDepartments);
    
    let lowDepartmentSelect = "";
    subDepartments.forEach(ele => {
		lowDepartmentSelect += ` <option value="${ele.DEPARTMENT_CODE}">${ele.DEPARTMENT_NAME}</option>`;
	});
	console.log("lowDepartmentSelect:::",lowDepartmentSelect);
    document.getElementById("modalSubDepartment").innerHTML = lowDepartmentSelect;
}

// 직급 목록 동적으로 추가
function populatePositionSelect(positions) {
	
    // 검색 필터용 (검색 화면)
    const searchPositionSelect = document.getElementById('searchPosition');
    searchPositionSelect.innerHTML = '<option value="">선택</option>';

    // 모달용 (사원 등록 화면)
    const modalPositionSelect = document.getElementById('modalPosition');
    modalPositionSelect.innerHTML = '<option value="">선택</option>';

    if (!positions) {
        console.error("⚠ positions 데이터가 없습니다.");
        return;
    }

    positions.forEach(pos => {
        let optionTag = `<option value="${pos.CMMNCODE}">${pos.CMMNNAME}</option>`;
        
        // 검색 필터에 추가
        searchPositionSelect.innerHTML += optionTag;
        
        // 모달에도 추가
        modalPositionSelect.innerHTML += optionTag;
    });

    console.log("📌 직급 목록 로드 완료:", positions);
}

// 재직 상태 버튼 동적으로 추가
function populateStatusButtons(statuses) {
    const statusGroup = document.getElementById('statusGroup');
    statusGroup.innerHTML = ''; // 기존 버튼 초기화

    // "전체" 버튼 추가
    statusGroup.innerHTML += `
        <input type="radio" class="btn-check" name="searchStatus" id="statusAll" checked value="">
        <label class="btn btn-outline-primary" for="statusAll">전체</label>
    `;
    
    // 상태 버튼 추가 (예상 데이터: ["재직", "퇴직", "휴직"])
    if (statuses && Array.isArray(statuses)) {
        statuses.forEach((status, index) => {
            const statusId = `status${index}`;
            statusGroup.innerHTML += `
                <input type="radio" class="btn-check" name="searchStatus" id="${statusId}" value="${status}">
                <label class="btn btn-outline-primary" for="${statusId}">${status}</label>
            `;
        });
    } else {
        console.error("⚠ 상태 목록 데이터가 비어있음:", statuses);
    }

    // 상태 필터 버튼 변경 시 자동 검색 실행
    document.querySelectorAll("input[name='searchStatus']").forEach(btn => {
        btn.addEventListener("change", function () {
            searchEmployees();
        });
    });
}

// ✅ 근무 유형 버튼 동적으로 추가 (검색 필터 & 모달용 구분)
function populateEmploymentButtons(employmentTypes) {
    // ✅ 검색 필터용 (검색 화면)
    const searchEmploymentGroup = document.getElementById('employmentTypeGroup');
    if (!searchEmploymentGroup) {
        console.error("❌ employmentTypeGroup 요소를 찾을 수 없습니다!");
        return;
    }
    searchEmploymentGroup.innerHTML = ''; // 초기화

    // ✅ 모달용 (사원 등록 화면)
    const modalEmploymentGroup = document.getElementById('modalEmploymentTypeGroup');
    if (!modalEmploymentGroup) {
        console.error("❌ modalEmploymentTypeGroup 요소를 찾을 수 없습니다!");
        return;
    }
    modalEmploymentGroup.innerHTML = ''; // 초기화

    console.log("📌 근무 유형 데이터 확인:", employmentTypes); // 🔥 데이터 확인

    // ✅ 데이터가 없거나 배열이 아닐 경우 예외 처리
    if (!Array.isArray(employmentTypes) || employmentTypes.length === 0) {
        console.warn("⚠ 근무 유형 데이터가 비어 있습니다!");
        return;
    }

    // ✅ "전체" 버튼 추가 (검색 필터용만 필요)
    searchEmploymentGroup.innerHTML += `
        <input type="radio" class="btn-check" name="employmentType" id="employmentTypeAll" checked value="">
        <label class="btn btn-outline-primary" for="employmentTypeAll">전체</label>
    `;

    // ✅ 근무 유형 목록을 검색 필터 & 모달용에 각각 추가
    employmentTypes.forEach(type => {
        if (!type.CMMNCODE || !type.CMMNNAME) {
            console.error("⚠ 근무 유형 데이터 오류!", type);
            return;
        }
        
        // ✅ 부트스트랩 스타일의 라디오 버튼 추가 (한 개만 선택되도록 name="employmentType" 설정)
        let radioTag = `
            <input type="radio" class="btn-check" name="employmentType" id="employmentType_${type.CMMNCODE}" value="${type.CMMNCODE}">
            <label class="btn btn-outline-primary" for="employmentType_${type.CMMNCODE}">${type.CMMNNAME}</label>
        `;

        // 🔹 검색 필터에 추가
        searchEmploymentGroup.innerHTML += radioTag;

        // 🔹 모달에도 추가 (별도 `name="modalEmploymentType"` 설정)
        let modalRadioTag = `
            <input type="radio" class="btn-check" name="modalEmploymentType" id="modalEmploymentType_${type.CMMNCODE}" value="${type.CMMNCODE}">
            <label class="btn btn-outline-primary" for="modalEmploymentType_${type.CMMNCODE}">${type.CMMNNAME}</label>
        `;
        modalEmploymentGroup.innerHTML += modalRadioTag;
    });

    console.log("✅ 근무 유형 목록 로드 완료!");

    // ✅ 100ms 지연 후 이벤트 리스너 등록 (즉시 실행 시 요소를 못 잡을 수 있음)
    setTimeout(() => {
        document.querySelectorAll("input[name='employmentType']").forEach(btn => {
            btn.addEventListener("change", function () {
                console.log("🔄 근무 유형 변경됨! 검색 필터 적용");
                searchEmployees(); // ✅ 자동 필터링 실행
            });
        });
    }, 100);
}

