/**
 * department.js
 */

// ✅ 부서 목록 가져오기
function loadDepartments() {
    fetch('/hr/rest/emp/departments/'+sessionData.companyNum)
        .then(response => response.json())
        .then(data => {
            let departmentSelect = document.getElementById("modalDepartment");
            departmentSelect.innerHTML = '<option value="">선택</option>';
            data.forEach(dept => {
                departmentSelect.innerHTML += `<option value="${dept.departmentNum}">${dept.departmentName}</option>`;
            });
        })
        .catch(error => console.error("부서 목록 로딩 실패:", error));
}

// ✅ 하위 부서 목록 가져오기
function loadSubDepartments(parentDeptId) {
    fetch(`/hr/rest/emp/sub-departments?parentDeptId=${parentDeptId}`)
        .then(response => response.json())
        .then(data => {
			
            let subDeptSelect = document.getElementById("modalSubDepartment");
            subDeptSelect.innerHTML = '<option value="">선택</option>';
            data.forEach(dept => {
                subDeptSelect.innerHTML += `<option value="${dept.departmentNum}">${dept.departmentName}</option>`;
            });
        })
        .catch(error => console.error("하위 부서 목록 로딩 실패:", error));
}

// ✅ 부서 선택 시 하위 부서 로드
document.getElementById("modalDepartment").addEventListener("change", function () {
    let selectedDeptId = this.value;
    if (selectedDeptId) {
        loadSubDepartments(selectedDeptId);
    } else {
        document.getElementById("modalSubDepartment").innerHTML = '<option value="">선택</option>';
    }
});

// ✅ 페이지 로드 시 부서 목록 가져오기
document.addEventListener("DOMContentLoaded", function () {
    loadDepartments();
});