
let mypageDataSource = {}
let employeeNum = document.getElementById("sessionEmployeeNum").value;
let mypageGrid = {}
if(employeeNum) {
    mypageDataSource = {
        api: {
            readData: {
                url: `/api/mainpage/approval/list/${employeeNum}`,
                method: 'GET',
                initParams: { page: 1 }
            },
            contentType: 'application/json'
        },
    };

    mypageGrid = new tui.Grid({
        el: document.querySelector('#grid'),
        scrollX: false,
        scrollY: false,
        pageOptions: {
            useClient: false,
            perPage: 10,
        },
        columns: [
            {header: "번호", name: "inApprovalId", sortable: true, align: 'right'},
            {header: "요청 구분", name: "documentType", sortable: true},
            {
                header: "결재요청 날짜",
                name: "inApprovalRequestDate",
                sortable: true,
                formatter: ({value}) => {
                    return formatDateTime(value)
                },
                align: 'center'
            },
            {
                header: "처리",
                name: "process",
                hidden: true,
                formatter: ({row}) => {
                    return `
                    <button class="btn btn-approve" data-bs-toggle="modal" data-bs-target="#commonModal" data-in-approval-id="${row.inApprovalId}">승인</button>
                    <button class="btn btn-reject" data-bs-toggle="modal" data-bs-target="#commonModal" data-in-approval-id="${row.inApprovalId}">반려</button>`
                }
            },
            {
                header: "처리 상태",
                name: "inApprovalStatus",
                sortable: true,
                rowClassName: 'center',
                align: 'center',
                formatter: ({value}) => {
                    const statusMap = {
                        WAITING: {text: "대기중", color: "gray"},
                        APPROVED: {text: "승인", color: "green"},
                        REJECTED: {text: "반려", color: "red"}
                    };
                    const status = statusMap[value] || {text: "알 수 없음", color: "black"};
                    return `<span class="status-label"
                                style="background-color: ${status.color};
                            ">${status.text}</span>`;
                }
            },
            {header: "요청 내용", name: "inApprovalRequestContent", sortable: true, hidden: true},
            {header: "요청 내용 확인", name: "moveToPage", sortable: true,
                hidden: true,
                formatter: ({ row }) => {
                    return `<button class="move-btn" data-id="${row.inApprovalId}">
                          <i class="mdi mdi-arrow-right-bold"></i>
                        </button>`;
                }
            },
            {
                header: "다운로드",
                name: "download",
                sortable: true,
                hidden: true,
                formatter: ({ row }) => {
                    return `<button class="download-btn" data-content="${row.inApprovalRequestContent}">
                    <i class="mdi mdi-folder-download"></i>
                  </button>`;
                }
            }  ],
        data : mypageDataSource,
    });
} else {
    setTimeout(() => {
        // showAlert("세션이 만료되었습니다. 로그인 페이지로 이동합니다.", 'danger');
    }, 100)
    setTimeout(() => {
        window.location.href = "/login";
    }, 1000);
}

let inApprovalStatus = ''
let filterBtns = document.getElementsByClassName('filter-btn')
filterBtns = Array.from(filterBtns)
filterBtns.forEach((btn) => {

    btn.addEventListener('click', (event) => {
        filterBtns.forEach((btn) => {
            btn.classList.remove('active')
        })
        inApprovalStatus = event.target.dataset.status
        event.target.classList.add('active')
    })
})
function reset(){
    let documentType = document.querySelector('#documentType');
    let inApprovalRequestDateStart = document.querySelector('#inApprovalRequestDateStart');
    let inApprovalRequestDateEnd = document.querySelector('#inApprovalRequestDateEnd');

    documentType.value = '';
    inApprovalRequestDateStart.value = '';
    inApprovalRequestDateEnd.value = '';
    inApprovalStatus = ''

    mypageGrid.setRequestParams({
        "documentType" : documentType.value,
        "inApprovalStatus" : inApprovalStatus,
        "inApprovalRequestDateStart" : inApprovalRequestDateStart.value,
        "inApprovalRequestDateEnd" : inApprovalRequestDateEnd.value
    })
    mypageGrid.readData();
}

function search(){
    let documentType = document.querySelector('#documentType').value.toString();
    let inApprovalRequestDateStart = document.querySelector('#inApprovalRequestDateStart').value.toString();
    let inApprovalRequestDateEnd = document.querySelector('#inApprovalRequestDateEnd').value.toString();
    mypageGrid.setRequestParams({
        "documentType" : documentType,
        "inApprovalStatus" : inApprovalStatus,
        "inApprovalRequestDateStart" : inApprovalRequestDateStart,
        "inApprovalRequestDateEnd" : inApprovalRequestDateEnd
    })
    mypageGrid.readData();
}

function changeDisplay() {
    let gap = parseInt(document.querySelector('#display_amount').value);
    mypageGrid.setPerPage(gap, mypageDataSource)
}

let typeFilter = document.getElementById("documentType");
let typeSelected = document.getElementById("documentTypeSelected");

const getMyApprovalType = () => {
    const url = `/api/mainpage/approval/type`;
    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(data => {
            return data.json()
        })
        .then(data => {
            const docType = data.map((doc) => {
                return {
                    documentType: doc.documentType,
                    documentId: doc.documentId
                }
            })

            docType.forEach(optionText => {
                const option1 = document.createElement("option");
                option1.value = optionText.documentType;
                option1.textContent = optionText.documentType;

                const option2 = document.createElement("option");
                option2.value = optionText.documentId;
                option2.textContent = optionText.documentType;

                typeFilter.appendChild(option1);
                typeSelected.appendChild(option2);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

getMyApprovalType()

let selectedDocType = ''
typeSelected.addEventListener('change', (e) => {
    selectedDocType = e.target.value
});

const requestApproval = document.getElementById('requestApproval')
requestApproval.addEventListener('click', () => {
    if(selectedDocType) {
        window.location = `/mypage/approval/${selectedDocType}?menu=mypage`
    } else {
        showAlert('요청 구분을 선택해주세요.', 'danger')
    }
})

