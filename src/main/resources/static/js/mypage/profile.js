let regular = document.getElementsByName('regular')[0]
let contract = document.getElementsByName('contract')[0]
let employeeId = document.getElementById('employeeId')
let employeeName = document.getElementById('employeeName')
let hireDate = document.getElementById('hireDate')
let departmentName = document.getElementById('departmentName')
let email1 = document.getElementById('email1')
let email2 = document.getElementById('email2')
let address = document.getElementById('address')
let zipCode = document.getElementById('zipCode')
let phone = document.getElementById('phone')
let position = document.getElementById('position')
// let salary = document.getElementById('salary')
let bankName = document.getElementById('bankName')
let accountNum = document.getElementById('accountNum')
let imgUpdateBtn = document.getElementById('imgUpdateBtn')
let profileImg = document.getElementById('profileImg')
const fileInput = document.getElementById("imgUpdate");

document.addEventListener("DOMContentLoaded", function () {
    fileInput.addEventListener("change", function (event) {
        const file = event.target.files[0];

        if (file) {
            const reader = new FileReader();

            reader.onload = function (e) {
                profileImg.src = e.target.result;
            };

            reader.readAsDataURL(file); // 파일을 읽어 base64 URL로 변환
        }
    });
});

imgUpdateBtn.addEventListener('click', () => {
    const url = '/api/mypage/profile/img'

    if(!fileInput) {
        showAlert('사진을 업로드해주세요.', 'danger')
        return
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("image", file);

    fetch(url, {
        method: 'post',
        headers: {
            'header': header_csrf,
            'X-CSRF-Token': token_csrf
        },
        body: formData
    }).then((result) => {
        return result.json()
    }).then((data) => {
        if(data.message === 'success') {
            showAlert('프로필 사진이 변경되었습니다.', 'success')
            // to update header profile img
            window.location.reload();
        } else {
            showAlert('프로필 사진 변경 실패', 'danger')
        }
    })
})

const setInfo = (data) => {
    if(data.employmentType === 'ET001') {
        regular.checked = true
    } else {
        contract.checked = true
    }

    employeeId.value = data.employeeId
    employeeName.value = data.employeeName
    hireDate.value = data.hireDate.split('T')[0]
    departmentName.value = data.departmentName
    email1.value = data.email.split('@')[0]
    email2.value = data.email.split('@')[1]
    address.value = data.address
    zipCode.value = data.zipCode
    phone.value = data.phone
    position.value = data.position
    // salary.value = data.salary || '-'
    bankName.value = data.bankName
    accountNum.value = data.accountNum
    profileImg.src = data.profileImage || '/file/image/mypage/profile/noProfileImg.jpg';

    profileImg.onerror = function () {
        this.onerror = null; // 무한 루프 방지
        this.src = '/file/image/mypage/profile/noProfileImg.jpg';
    };

}

const setEmptyImg = () => {
    profileImg.src = '/file/image/mypage/profile/noProfileImg.jpg';
}

const getProfileInfo = () => {
    const url = '/api/mypage/profile'
    fetch(url, {
        method: 'get',
        headers: {
            "Content-Type": "application/json",
        }
    }).then((data) => {
        return data.json()
    }).then((data) => {
        setInfo(data)
    }).catch((e) => {
        setEmptyImg()
    })
}

getProfileInfo()
