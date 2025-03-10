/**
 * employee.js
 */

let grid;
var employeeNum;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf"]').content;

document.addEventListener("DOMContentLoaded", function () {

    setTimeout(() => {
        populateFilters(); // 필터 로딩 함수 실행
    }, 100);
	
	//주민번호 입력값 검증
    // 첫 번째 주민번호 입력 필드
    const firstSsnInput = document.getElementById("firstSsn");
    firstSsnInput.addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 6); // 숫자가 아니면 제거, 6자리 제한
    });

    // 두 번째 주민번호 입력 필드
    const secondSsnInput = document.getElementById("secondSsn");
    secondSsnInput.addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 7); // 숫자가 아니면 제거, 7자리 제한
    });
	
	
const modalElement = document.getElementById("contractModal");
  const closeButton = modalElement.querySelector('[data-bs-dismiss="modal"]');
  if (closeButton) {
      closeButton.addEventListener("click", function () {
          console.log("✅ 인쇄 모달 닫기 버튼 클릭됨!");

          try {
          	let modalInstance = bootstrap.Modal.getInstance("#contractModal") || new bootstrap.Modal("#contractModal");
              modalInstance.hide(); // ✅ Bootstrap 방식으로 모달 닫기
              
          } catch (error) {
              console.warn("❌ Bootstrap 5가 로드되지 않았음. 대체 방식 사용");
              modalElement.classList.remove("show");
              modalElement.style.display = "none";
              document.body.classList.remove("modal-open");

              setTimeout(() => {
                  document.querySelectorAll(".modal-backdrop").forEach((element) => element.remove()); // 백그라운드 제거
              }, 300);
           
          }
      });
  } else {
      console.warn("❌ 근로계약서 모달 닫기 버튼을 찾을 수 없습니다.");
  }
	
	
	 //pdf다운
	  document.getElementById('pdfDown').addEventListener('click', function() {
		  // 다운로드 엔드포인트 URL 구성 (/purchs/report/reportDownload 엔드포인트 사용)
		  const contractDownloadUrl = `/hr/rest/contract/report/down?employeeNum=${employeeNum}`;
		  console.log("Download URL:", contractDownloadUrl);
		  // 브라우저를 해당 URL로 이동시켜 파일 다운로드를 유도
		  window.location.href = contractDownloadUrl;
		});
	
	//연락처 검증
	const phoneInput = document.getElementById("phone");
	
	if (!phoneInput) {
	    console.error("❌ 'phone' ID를 가진 요소를 찾을 수 없습니다!");
	} else {
	    phoneInput.addEventListener("input", function () {
	        let value = this.value.replace(/\D/g, ""); // 숫자 이외 문자 제거
	
	        if (value.length > 11) {
	            value = value.substring(0, 11); // 11자리까지만 입력 가능
	        }
	
	        // 📌 번호 유형에 따라 하이픈 적용
	        if (/^(010|011|016|017|018|019)/.test(value)) {
	            // 🔹 휴대폰 번호 (010, 011, 016, 017, 018, 019) - 11자리
	            if (value.length === 11) {
	                value = value.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3"); // 010-1234-5678 형식
	            } else if (value.length === 10) {
	                value = value.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3"); // 구형 10자리 번호 (예: 011-123-4567)
	            }
	        } else if (/^02/.test(value)) {
	            // 🔹 서울 지역번호 (02) - 9~10자리
	            if (value.length === 10) {
	                value = value.replace(/(\d{2})(\d{4})(\d{4})/, "$1-$2-$3"); // 02-1234-5678 형식
	            } else if (value.length === 9) {
	                value = value.replace(/(\d{2})(\d{3})(\d{4})/, "$1-$2-$3"); // 02-123-4567 형식
	            }
	        } else if (/^(0[3-9][0-9])/.test(value)) {
	            // 🔹 일반 지역번호 (031, 032, 051 등) - 10자리
	            if (value.length === 10) {
	                value = value.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3"); // 031-123-4567 형식
	            } else if (value.length === 9) {
	                value = value.replace(/(\d{3})(\d{2})(\d{4})/, "$1-$2-$3"); // 031-12-3456 형식 (드물게 존재)
	            }
	        }
	
	        this.value = value; // 변환된 값 적용
	    });
	}
	
	

	//이메일 검증
	const emailInput = document.getElementById("email");
	const emailErrorMsg = document.createElement("small"); // 오류 메시지 요소 생성
	emailErrorMsg.style.color = "red";
	emailInput.parentNode.appendChild(emailErrorMsg); // ㄹ 입력 필드 아래에 추가
	
	emailInput.addEventListener("blur", function () {
	    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	
	    if (!emailPattern.test(this.value)) {
	        emailErrorMsg.textContent = "올바른 이메일 주소를 입력하세요.";
	        this.classList.add("is-invalid"); // Bootstrap 스타일 적용 가능
	    } else {
	        emailErrorMsg.textContent = "";
	        this.classList.remove("is-invalid");
	    }
	});
	
	// ✅ 사원 ID 입력 후 중복 체크 실행
	const employeeIdInput = document.getElementById("employeeIdInput");
	employeeIdInput.addEventListener("blur", async function () {
	    const employeeId = this.value.trim();
	    if (employeeId === "") return; // 값이 없으면 검사하지 않음
	
	    const exists = await checkEmployeeIdExists(employeeId);
	    if (exists) {
	        Swal.fire({
	            icon: "error",
	            title: "사원 ID 중복",
	            text: "이미 등록된 사원 ID입니다. 다른 ID를 입력하세요.",
	            confirmButtonText: "확인",
	            customClass: { popup: 'custom-swal-popup' }
	        });
	
	        this.value = ""; // 중복 시 입력값 초기화
	        this.classList.add("is-invalid"); // Bootstrap 스타일 적용
	    } else {
	        this.classList.remove("is-invalid");
	    }
	});
	
	
// ✅ 주민번호 입력값 검증 (앞 6자리 + 뒤 7자리 필수 입력)
function validateResidentNumber() {
    const firstSsn = document.getElementById("firstSsn");
    const secondSsn = document.getElementById("secondSsn");

    // 앞자리 검증 (6자리)
    if (firstSsn.value.length !== 6) {
        Swal.fire({
            icon: "warning",
            title: "입력 오류",
            text: "주민등록번호 앞자리는 6자리여야 합니다.",
            confirmButtonText: "확인"
        });
        firstSsn.classList.add("is-invalid");
        return false;
    } else {
        firstSsn.classList.remove("is-invalid");
    }

    // 뒷자리 검증 (7자리)
    if (secondSsn.value.length !== 7) {
        Swal.fire({
            icon: "warning",
            title: "입력 오류",
            text: "주민등록번호 뒷자리는 7자리여야 합니다.",
            confirmButtonText: "확인"
        });
        secondSsn.classList.add("is-invalid");
        return false;
    } else {
        secondSsn.classList.remove("is-invalid");
    }

    return true;
}


	// 이름 검증
	const nameInput = document.getElementById("employeeName");
	const nameErrorMsg = document.createElement("small"); // 오류 메시지 요소 생성
	nameErrorMsg.style.color = "red";
	nameInput.parentNode.appendChild(nameErrorMsg); // 이름 입력 필드 아래에 추가
	
	nameInput.addEventListener("blur", function () {
	    const namePattern = /^[가-힣a-zA-Z\s'-.]+$/;
	
	    if (!namePattern.test(this.value) || this.value.trim() === "") {
	        nameErrorMsg.textContent = "한글 또는 영문만 입력 가능합니다.";
	        this.classList.add("is-invalid"); // Bootstrap 스타일 적용 가능
	    } else {
	        nameErrorMsg.textContent = "";
	        this.classList.remove("is-invalid");
	    }
	});

	
	document.body.addEventListener("click",function(event){
    if (event.target.classList.contains("contractBtn")) {
        employeeNum = event.target.getAttribute("data-id");
        console.log("📌 선택된 employeeNum:", employeeNum);

        const contractUrl = `/hr/rest/contract/report?employeeNum=${employeeNum}`;
        const contractModalEl = document.querySelector("#contractModal");

        // 📌 서버에서 계약서 존재 여부 확인
        fetch(contractUrl, { method: 'HEAD' })
            .then(response => {
                if (response.ok) {
                    console.log("✅ 근로 계약서 존재함. PDF 렌더링 시작");
                    renderPDF(contractUrl);
                } else {
                    console.warn("❌ 근로 계약서 없음!");
                    renderPDF(""); // PDF가 없을 경우 빈 값 전달하여 메시지 표시
                }

                // 📌 모달 표시
                if (contractModalEl) {
                    const contractModal = new bootstrap.Modal(contractModalEl);
                    contractModal.show();
                }
            })
            .catch(error => {
                console.error("🚨 근로 계약서 확인 중 오류 발생:", error);
                alert("⚠️ 근로 계약서 확인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
            });
    }
});
	
   let profileInputIMG = document.querySelector("#profileImage");
   let profileImgView = document.querySelector("#profilePreview");
   let file = null;

    profileInputIMG.addEventListener("change", function (event) {
	    let file = event.target.files[0];
	
	    if (file) {
	        // 파일 크기 확인 (2MB 제한)
	        if (file.size > 2 * 1024 * 1024) { 
	            alert("이미지 크기가 너무 큽니다! (최대 2MB)");
	            profileInputIMG.value = "";  // 파일 선택 취소
	            return;
	        }
	        
	        // 파일 확장자 제한 (jpg, jpeg, png만 허용)
	        let allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
	        if (!allowedExtensions.exec(file.name)) {
	            alert("jpg, jpeg, png 형식의 파일만 업로드 가능합니다.");
	            event.target.value = ""; // 파일 선택 취소
	            return;
	        }
	
	        const reader = new FileReader();
	        reader.onload = function (e) {
	            document.getElementById("profilePreview").src = e.target.result;
	            document.getElementById("profilePreview").style.width = "150px";
	            document.getElementById("profilePreview").style.height = "150px";
	            document.getElementById("profilePreview").style.objectFit = "cover";
	        };
	
	        reader.readAsDataURL(file);
	    }
    });
    
    

	
    initializeGrid();
    setupEventListeners();
    fetchNewEmployeeId(); // 모달 열릴 때 사원번호 자동 입력
    
    let registerBtn = document.getElementById("registerBtn");
    
    if (registerBtn) {
        registerBtn.addEventListener("click", async function (event) {
            console.log("🔍 등록 버튼 클릭됨!");

            // 🔹 입력값 검증 후 실행
            if (!validateEmployeeForm()) {
                console.warn("⚠️ 필수 입력값이 누락되었습니다. 등록을 중단합니다.");
                return;
            }
            
            
            
    const employeeId = document.getElementById("employeeIdInput")?.value.trim();
    if (!employeeId) {
        Swal.fire({ icon: "warning", title: "입력 오류", text: "사원 ID를 입력하세요.", confirmButtonText: "확인" });
        return;
    }

    // ✅ 사원 ID 중복 체크 실행
    const exists = await checkEmployeeIdExists(employeeId);
    if (exists) {
        Swal.fire({ icon: "error", title: "사원 ID 중복", text: "이미 등록된 사원 ID입니다. 다른 ID를 입력하세요.", confirmButtonText: "확인" });
        return; // 중복이면 등록 중단
    }

    // ✅ 검증 성공 시 등록 실행
    if (!validateEmployeeForm()) {
        console.warn("⚠️ 필수 입력값이 누락되었습니다. 등록을 중단합니다.");
        return;
    }

            // 🔹 사원 등록 실행
            registerEmployee();
        });

        console.log("✅ 등록 버튼 이벤트 리스너 연결 완료!");
    } else {
        console.error("❌ registerBtn 요소를 찾을 수 없습니다!");
    }
    
	document.getElementById("empRegisterModal").addEventListener("show.bs.modal", function () {
	    resetEmployeeForm();
	    fetchNewEmployeeId();
	    populateModalData();
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
                url: '/hr/rest/emp/list',
                method: 'GET',
                initParams: { page: 1 }
            }
        },
        contentType: 'application/json',
    };
    
	Grid.applyTheme('stripe', {
	    row: { fontSize: '16px' },
	    cell: { fontSize: '16px' }
	});

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
            { header: "사원ID", name: "employeeId", align: "center",sortable: true, width: 100 },
            { header: "사원명", name: "employeeName", align: "center", sortable: true, width: 150 },
            { header: "부서", name: "departmentName", align: "center", sortable: true, width: 100 },
            { header: "직급", name: "position", align: "center", sortable: true, width: 100, formatter: formatCommonCode('position') },
            { header: "재직 상태", name: "status", align: "center", sortable: true, width: 120, formatter: formatCommonCode('status') },
            { header: "근무 유형", name: "employmentType", align: "center", sortable: true, width: 120, formatter: formatCommonCode('employmentType') },
            { header: "입사일", name: "hireDate", align: "center", sortable: true, width: 150, formatter: ({ value }) => value?.split('T')[0] || '' },
            { header: "연락처", name: "phone", align: "center", sortable: true, width: 150, formatter: ({ value }) => formatPhoneNumberForDB(value) },
            { header: "이메일", name: "email", align: "left", sortable: true, width: 200 },
            { header: "근로계약서", name: "employeeContract", align: "center", sortable: true, width: 120,
					formatter: function({ row }) {
					    if (row.contractStatus === "보기") {
					        return `<button class="btn btn-outline-info btn-sm contractBtn" data-id="${row.employeeNum}">보기</button>`;
					    } else {
					        return `<span class="text-danger">미계약</span>`;
					    }
					},
            }
        ],
        data: dataSource,
        rowHeaders: ['checkbox'],
        	
    });
}

// 공통 코드 목록 불러오기 (동적 적용)
let commonCodes = {}; // 🔹 공통 코드 저장 변수
function loadCommonCodes() {
    fetch("/hr/rest/emp/common-codes")
        .then(response => response.json())
        .then(data => {
            commonCodes = data;
            //populateFilters(); // 필터 UI 업데이트
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
 
	console.log("🔎 positionSelect:", document.getElementById("positionFilter"));
	console.log("🔎 statusSelect:", document.getElementById("statusFilter"));
	console.log("🔎 employmentTypeSelect:", document.getElementById("employmentTypeFilter"));
	console.log("🔎 departmentSelect:", document.getElementById("departmentFilter")); 
	    
    if (!positionSelect || !statusSelect || !employmentTypeSelect || !departmentSelect) {
        console.error("❌ populateFilters() 실행 실패! 필터 요소 중 일부가 존재하지 않습니다.");
        return; // 요소가 없으면 함수 실행 중단
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


    const employeeIdInput = document.getElementById("employeeIdInput");
    const toggleButton = document.getElementById("toggleAutoGenerate");
    
	employeeIdInput.addEventListener("blur", async function () {
	    const employeeId = this.value.trim();
	    if (employeeId === "") return; // 값이 없으면 검사하지 않음
	
	    const exists = await checkEmployeeIdExists(employeeId);
	    if (exists) {
	        Swal.fire({
	            icon: "error",
	            title: "사원 ID 중복",
	            text: "이미 등록된 사원 ID입니다. 다른 ID를 입력하세요.",
	            confirmButtonText: "확인",
	            customClass: { popup: 'custom-swal-popup' }
	        });
	
	        this.value = ""; // 중복 시 입력값 초기화
	        this.classList.add("is-invalid"); // Bootstrap 스타일 적용
	    } else {
	        this.classList.remove("is-invalid");
	    }
	});

    if (toggleButton) {
        toggleButton.addEventListener("click", function () {
            if (employeeIdInput.disabled) {
                // 🔹 '수동 입력' 활성화
                employeeIdInput.disabled = false;
                employeeIdInput.value = ""; // 기존 자동 생성된 값 제거
                toggleButton.textContent = "자동 생성";
            } else {
                // 🔹 '자동 생성' 활성화 (기존 방식 유지)
                employeeIdInput.disabled = true;
                fetchNewEmployeeId();
                toggleButton.textContent = "수동 입력";
            }
        });
    } else {
        console.error("❌ 'toggleAutoGenerate' 버튼을 찾을 수 없습니다!");
    };


// 🔹 새 사원번호 가져오기
function fetchNewEmployeeId() {
    fetch("/hr/rest/emp/new-employee-id")
        .then(response => response.text())
        .then(data => {
            const employeeIdInput = document.getElementById("employeeIdInput");
            if (employeeIdInput && employeeIdInput.disabled) {
                employeeIdInput.value = data; // 자동 생성된 사원번호 입력
            }
        })
        .catch(error => console.error("❌ 사원번호 생성 오류:", error));
}

/**
 * 📌 입력값 검증 함수
 */
function validateEmployeeForm() {
    const fields = [
        { id: "employeeName", message: "사원명을 입력하세요." },
        { id: "email", message: "이메일을 입력하세요." },
        { id: "phone", message: "연락처를 입력하세요." },
        { id: "hireDate", message: "입사일을 선택하세요." },
        { id: "modalDepartment", message: "부서를 선택하세요." },
        { id: "modalPosition", message: "직급을 선택하세요." },
        { id: "modalAutority", message: "권한을 선택하세요." }
    ];

    for (let field of fields) {
        let value = document.getElementById(field.id)?.value.trim();
        if (!value) {
            Swal.fire({ icon: "warning", title: "입력 오류", text: field.message, confirmButtonText: "확인", customClass: { popup: 'custom-swal-popup' } });
            return false;
        }
    }

    if (!document.querySelector("input[name='modalEmploymentType']:checked")) {
        Swal.fire({ icon: "warning", title: "입력 오류", text: "근무 유형을 선택하세요.",customClass: { popup: 'custom-swal-popup' }, confirmButtonText: "확인" });
        return false;
    }

    return true;
}


function registerEmployee() {
	
	let employmentId = document.querySelector("input[name='modalEmploymentType']:checked")?.id;
	let employmentValue = employmentId ? employmentId.substring(employmentId.lastIndexOf("_") + 1) : "";
	let phone = document.getElementById("phone")?.value || "";
    let formattedPhone = formatPhoneNumberForDB(phone); // 변환된 전화번호
    let bankSelect = document.getElementById("bankSelect");
    
    // 🔹 은행 선택이 "선택"이면 값을 null로 변경
    let bankValue = bankSelect.value === "" ? "" : bankSelect.options[bankSelect.selectedIndex].text.trim();
	
	
	let profileInputIMG = document.querySelector("#profileImage");
    const file = profileInputIMG.files[0];
    const formData = new FormData();
	if (file) {
	    formData.append("image", file);  // 파일이 있을 때만 추가
	} else {
	    console.warn("📌 프로필 이미지 파일이 선택되지 않음, 서버로 전송되지 않음");
	}
    
    formData.append("employeeId", document.getElementById("employeeIdInput")?.value || "");
	formData.append("employeeName",document.getElementById("employeeName")?.value || "");
	formData.append("email",document.getElementById("email")?.value || "");
	formData.append("phone", formattedPhone);
	formData.append("hireDate",document.getElementById("hireDate")?.value || "");
	formData.append("departmentNum",document.getElementById("modalSubDepartment")?.value || 0);
	formData.append("position",document.getElementById("modalPosition")?.value || "");
	formData.append("status","ST001");
	formData.append("employmentType",employmentValue || "");
	formData.append("bankName",bankValue);
	formData.append("accountNum",document.getElementById("accountNumber")?.value || "");
	formData.append("zipCode",document.getElementById("zipcode")?.value || "");
	formData.append("address",document.getElementById("address")?.value || "");
	formData.append("addressDetail",document.getElementById("addressDetail")?.value || "");
	formData.append("memo",document.getElementById("memo")?.value || "");
	formData.append("parentDeptNum",document.getElementById("modalDepartment")?.value || "");
	formData.append("companyNum",document.getElementById("companyNumSJ")?.value || "");
	formData.append("firstSsn",document.getElementById("firstSsn")?.value || "");
	formData.append("secondSsn",document.getElementById("secondSsn")?.value || "");
	formData.append("authority",document.getElementById("modalAutority")?.value);
		
    
/*    formData.append("employeeId", "20250227025");
	formData.append("employeeName", "길동길동");
	formData.append("email", "sdsds2233dsd@gmail.com");
	formData.append("phone","01051005151");
	formData.append("hireDate","2025-02-02");
	formData.append("departmentNum" ,"3");
	formData.append("position", "PO001");
	formData.append("status","ST001");
	formData.append("employmentType","ET001");
	formData.append("bankName","국민은행");
	formData.append("accountNum","202202020");
	formData.append("zipCode","05222");
	formData.append("address","대구");
	formData.append("addressDetail","2층");
	formData.append("memo","메모");
	formData.append("parentDeptNum","1");
	formData.append("companyNum", "1");
	formData.append("firstSsn", "202202");
	formData.append("secondSsn","2020222");
	formData.append("authority","AU001");*/	
		
	console.log("file:::",file);
	

/*    let empData = {
		employeeId: document.getElementById("employeeIdInput")?.value || "",
        employeeName: document.getElementById("employeeName")?.value || "",
        email: document.getElementById("email")?.value || "",
        phone: document.getElementById("phone")?.value || "",
        hireDate: document.getElementById("hireDate")?.value || "",
        departmentNum: document.getElementById("modalSubDepartment")?.value || "",
        position: document.getElementById("modalPosition")?.value || "",
        status: "ST001",
        employmentType: employmentValue || "",
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
    };*/
    
/*    
    	empData = {
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



	//console.log("empData::::::",empData);

    fetch("/hr/rest/emp/register", {
        method: "POST",
        headers: {
            'header': header,
            'X-CSRF-Token': token
        },
        body: formData
    })
	.then(async (response) => {
	    if (!response.ok) {
	        let errorData;
	        try {
	            errorData = await response.json();
	        } catch (jsonError) {
	            console.error("❌ JSON 파싱 오류:", jsonError);
	            throw new Error(`서버 오류: ${response.status} ${response.statusText}`);
	        }
	
	        throw new Error(errorData?.error || `서버 오류: ${response.status} ${response.statusText}`);
	    }
	    return response.json();
	})
	.then(data => {
	    if (data.error) {
	        if (data.error.includes("이미 등록된 이메일")) {
	            // ✅ 이메일 중복 처리
	            let emailInput = document.getElementById("email");
	            emailInput.value = "";
	            emailInput.classList.add("is-invalid");
	
	            Swal.fire({
	                icon: 'error',
	                title: '이메일 중복',
	                text: '이미 등록된 이메일입니다. 다른 이메일을 입력해주세요.',
	                confirmButtonText: '확인',
	                customClass: { popup: 'custom-swal-popup' }
	            });
	
	            return;
	        } else if (data.error.includes("이미 등록된 사원 ID")) {
	            // ✅ 사원 ID 중복 처리
	            let employeeIdInput = document.getElementById("employeeIdInput");
	            employeeIdInput.value = "";
	            employeeIdInput.classList.add("is-invalid");
	
	            Swal.fire({
	                icon: 'error',
	                title: '사원 ID 중복',
	                text: '이미 등록된 사원 ID입니다. 다른 ID를 입력해주세요.',
	                confirmButtonText: '확인',
	                customClass: { popup: 'custom-swal-popup' }
	            });
	
	            return;
	        }
	
	        // 기타 오류 처리
	        Swal.fire({
	            icon: 'error',
	            title: '등록 실패',
	            text: data.error,
	            confirmButtonText: '확인',
	            customClass: { popup: 'custom-swal-popup' }
	        });
	    } else {
	        // ✅ 등록 성공 처리
	        Swal.fire({
	            icon: 'success',
	            title: '등록 완료',
	            text: '사원 등록이 성공적으로 완료되었습니다!',
	            confirmButtonText: '확인',
	            customClass: { popup: 'custom-swal-popup' }
	        }).then(() => {
	            location.reload(); // 페이지 새로고침
	        });
	    }
	})
	.catch(error => {
	    console.error("❌ 등록 실패:", error);
	
	    Swal.fire({
	        icon: 'error',
	        title: '등록 중 오류 발생',
	        text: error.message || '등록 중 문제가 발생했습니다. 다시 시도해주세요.',
	        confirmButtonText: '확인',
	        customClass: { popup: 'custom-swal-popup' }
	    });
	});
}
    
let globalDepartments = [];
let globalSubDepartments = [];
    

// ✅ 모달 공통 코드 데이터 로드
function populateModalData() {
    console.log("🔹 모달 공통 코드 데이터 불러오는 중..."+sessionData.companyNum);
	
    fetch("/hr/rest/emp/common-codes/"+sessionData.companyNum)
        .then(response => response.json())
        .then(data => {
            if (!data) {
                console.error("❌ 공통 코드 데이터를 불러오지 못함.");
                return;
            }

            console.log("📥 불러온 공통 코드 데이터123123:", data);

            // ✅ 전역 변수에 부서 및 하위 부서 저장
            globalDepartments = data.departments.filter(dept => dept.PARENT_DEPARTMENT_NUM == 0 );  
            console.log("globalDepartments",globalDepartments);
            
            let tag = `<option value="">선택</option>`;
            globalDepartments.forEach(glovalDept => {
				console.log("glovalDept",glovalDept);
				tag += `<option value="${glovalDept.DEPARTMENT_NUM}">${glovalDept.DEPARTMENT_NAME}</option>`;
			})
            globalSubDepartments = data.departments.filter(dept => dept.PARENT_DEPARTMENT_NUM !== null); // 하위 부서만 저장

            // ✅ 부서 (Department) 선택 리스트 설정
            const departmentSelect = document.getElementById("modalDepartment");
            console.log("tag",tag);
            departmentSelect.innerHTML = tag;
/*             `
                <option value="">선택</option>
                <option value="1">본사</option>
                <option value="8">지점</option>
            `;*/

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


console.log("file:::::",file);



querySelector("#contractBtn").addEventListener("click", ()=>{
			alert("계약보기 클릭");
		});

		
// (1) PDF.js로 PDF 파일을 캔버스에 렌더링
	function renderPDF(contractUrl) {
	  const canvas = document.getElementById('pdfCanvas');
	  const context = canvas.getContext('2d');
	
	  // PDF 로드
	  pdfjsLib.getDocument(contractUrl).promise.then(pdf => {
	    console.log('PDF loaded, total pages:', pdf.numPages);
	    // 첫 페이지만 렌더링 (필요시 여러 페이지 지원 가능)
	    pdf.getPage(1).then(page => {
	      const scale = 1.5; // 확대/축소 비율
	      const viewport = page.getViewport({ scale });
	      // 캔버스 크기 설정
	      canvas.width = viewport.width;
	      canvas.height = viewport.height;
	
	      // 페이지 렌더링
	      const renderContext = {
	        canvasContext: context,
	        viewport: viewport
	      };
	      page.render(renderContext).promise.then(() => {
	        console.log('Page rendered');
	      });
	    });
	  }).catch(error => {
	    console.error('PDF 로드 오류:', error);
	  });
	}
	
	
	
 
	



function formatPhoneNumberForDB(value) {
    if (!value) return "";
    value = value.replace(/\D/g, ""); // 숫자 이외 문자 제거

    if (value.length === 11) {
        return value.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
    } else if (value.length === 10) {
        return value.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
    }
    return value;
};


// 사원 ID 중복 체크 함수
function checkEmployeeIdExists(employeeId) {
    return fetch(`/hr/rest/emp/check-id?employeeId=${employeeId}`)
        .then(response => response.json())
        .then(data => data.exists) // 서버에서 true/false 반환
        .catch(error => {
            console.error("❌ 사원 ID 중복 체크 오류:", error);
            return false; // 에러 발생 시 기본적으로 중복 없음 처리
        });
}


