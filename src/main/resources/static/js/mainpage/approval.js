let Grid = tui.Grid;
let sessionAuthority = document.getElementById("sessionAuthority").value;
const dataSource = {
    api: {
        readData: {
            url: '/api/mainpage/approval/list',
            method: 'GET',
            initParams: { page: 1 }
        },
        contentType: 'application/json'
    },
};

const approvalGrid = new Grid({
    el: document.querySelector('#approvalGrid'),
    scrollX: false,
    scrollY: false,
    pageOptions: {
        useClient: false,
        perPage: 10,
    },
    columns: [
        {header: "번호", name: "inApprovalId", sortable: true, align: 'right'},
        {header: "요청 구분", name: "documentType", sortable: true},
        {header: "요청자", name: "employeeName", sortable: true},
        {
            header: "결재요청 날짜",
            name: "inApprovalRequestDate",
            sortable: true,
            align: 'center',
            width: 150,
            formatter: ({value}) => {
                return formatDateTime(value)
            },
        },
        {
            header: "처리",
            name: "process",
            align: 'center',
            formatter: ({row}) => {
                return `
                    <button class="btn btn-sm btn-outline-success btn-approve " data-bs-toggle="modal" data-bs-target="#commonModal" data-in-approval-id="${row.inApprovalId}">승인</button>
                    <button class="btn btn-sm btn-outline-danger btn-reject " data-bs-toggle="modal" data-bs-target="#commonModal" data-in-approval-id="${row.inApprovalId}">반려</button>`
            }
        },
        {
            header: "상태",
            name: "inApprovalStatus",
            sortable: true,
            rowClassName: 'center',
            width: 80,
            hidden: true,
            formatter: ({value}) => {
                const statusMap = {
                    WAITING: {text: "대기중", color: "gray"},
                    APPROVED: {text: "승인", color: "green"},
                    REJECTED: {text: "거절", color: "red"}
                };
                const status = statusMap[value] || {text: "알 수 없음", color: "black"};
                return `<span class="status-label"
                                style="background-color: ${status.color};
                            ">${status.text}</span>`;
            }
        },
        {header: "요청 내용", name: "inApprovalRequestContent", sortable: true, hidden: true},
        {header: "요청 내용 확인", name: "moveToPage", sortable: true,
            formatter: ({ row }) => {
                return `<button class="mainpage-move-btn btn btn-outline-success p-0" data-id="${row.inApprovalId}">
                          <i class="mdi mdi-arrow-right-bold"></i>
                        </button>`;
            },
            align: 'center',
        },
        {
            header: "다운로드",
            name: "download",
            sortable: true,
            formatter: ({ row }) => {
                return `<button class="download-btn btn btn-outline-secondary p-0" data-content='${row.inApprovalRequestContent}'>
                    <i class="mdi mdi-folder-download"></i>
                  </button>`;
            },
            align: 'center',
        }  ],
    data : dataSource,
});
// !최고관리자 && !관리자
let isAdmin = sessionAuthority === 'AU001' || sessionAuthority === 'AU002'
if(!isAdmin) {
    document.getElementById('approval-tab').classList.add('lock')
}
document.getElementById('approval-tab').addEventListener('click', () => {
    approvalGrid.refreshLayout()
}, {once: isAdmin})

document.addEventListener("click", function (event) {
    let buttonApprove = event.target.closest(".btn-approve");
    let buttonReject = event.target.closest(".btn-reject");

    if (buttonApprove) {
        setModalContent('전자 결재 처리', 'approval-approved', '취소', '승인', () => doApprove(buttonApprove))
    } else if(buttonReject) {
        setModalContent('전자 결재 처리', 'approval-rejected', '취소', '반려', () => doReject(buttonReject))
    }
});

document.addEventListener("click", function (event) {
    const button = event.target.closest(".mainpage-move-btn");
    if (button) {
        const dataset = button.dataset; // 버튼에서 data-id 값 가져오기
        window.location.href = `/mainpage/approval?inApprovalId=${dataset.id}`;

    }
});

document.addEventListener("click", function (event) {
    const button = event.target.closest(".download-btn");
    if (button) {
        const dataset = button.dataset;
        downloadPDF(dataset);
    }
});

const getApprovalType = () => {
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
                return doc.documentType
            })

            const selectElement = document.getElementById("documentType");

            // populate는 "채우다", "입력하다", "삽입하다" 등의 의미
            function populateSelect(options, select) {
                options.forEach(optionText => {
                    const option = document.createElement("option");
                    option.value = optionText;  // value 값 설정
                    option.textContent = optionText; // 표시할 텍스트
                    select.appendChild(option); // select 요소에 추가
                });
            }

            populateSelect(docType, selectElement);
        })
        .catch(error => console.error("Error fetching data:", error));
}

getApprovalType()

function reset(){
    let documentType = document.querySelector('#documentType');
    let employeeName = document.querySelector('#employeeName');
    let inApprovalRequestDateStart = document.querySelector('#inApprovalRequestDateStart');
    let inApprovalRequestDateEnd = document.querySelector('#inApprovalRequestDateEnd');

    documentType.value = '';
    employeeName.value = '';
    inApprovalRequestDateStart.value = '';
    inApprovalRequestDateEnd.value = '';

    approvalGrid.setRequestParams({
        "documentType" : documentType.value,
        "employeeName" : employeeName.value,
        "inApprovalRequestDateStart" : inApprovalRequestDateStart.value,
        "inApprovalRequestDateEnd" : inApprovalRequestDateEnd.value
    })
    approvalGrid.readData();
}

function search(){
    let documentType = document.querySelector('#documentType').value.toString();
    let employeeName = document.querySelector('#employeeName').value.toString();
    let inApprovalRequestDateStart = document.querySelector('#inApprovalRequestDateStart').value.toString();
    let inApprovalRequestDateEnd = document.querySelector('#inApprovalRequestDateEnd').value.toString();
    approvalGrid.setRequestParams({
        "documentType" : documentType,
        "employeeName" : employeeName,
        "inApprovalRequestDateStart" : inApprovalRequestDateStart,
        "inApprovalRequestDateEnd" : inApprovalRequestDateEnd
    })
    approvalGrid.readData();
}

function changeDisplay() {
    let gap = parseInt(document.querySelector('#display_amount').value);
    approvalGrid.setPerPage(gap, dataSource)
}

async function downloadPDF(dataset) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const container = document.createElement("div"); // 임시 컨테이너 생성
    container.innerHTML = dataset.content;
    const paragraphElement = container.firstElementChild;
    document.body.appendChild(paragraphElement); // 요소를 body에 추가

    // html2canvas로 요소 캡처
    const canvas = await html2canvas(paragraphElement, { scale: 2 });
    const imgData = canvas.toDataURL('image/png');

    // PDF에 이미지 추가
    doc.addImage(imgData, 'PNG', 10, 10, 180, 0);
    doc.save("결재 요청 서류.pdf"); // PDF 저장
}

const processApproval = (inApprovalId, processStr) => {
    const reason = document.getElementById('rejectReason').value
    const url = `/api/mainpage/approval/process`;

    fetch(url, {
        method: "POST",
        headers: {
            'header': header_csrf,
            "Content-Type": "application/json",
            'X-CSRF-Token': token_csrf
        },
        body: JSON.stringify({
            inApprovalStatus: processStr === 'approve' ? 'APPROVED' : 'REJECTED',
            inApprovalId: inApprovalId,
            inApprovalRejectedContent: reason
        })
    })
        .then(result => result.json())
        .then(data => {
            if(data.message === 'success') {
                if(processStr === 'approve') {
                    showAlert('결재 승인처리 되었습니다.', 'success')
                } else {
                    showAlert('결재 반려처리 되었습니다.', 'success')
                }
            } else {
                showAlert('결재 처리 실패', 'danger')
            }
            approvalGrid.reloadData();
        })
        .catch(error => console.error("Error fetching data:", error));
}

const doApprove = (buttonApprove) => {
    const inApprovalId = buttonApprove.dataset.inApprovalId;
    processApproval(inApprovalId, 'approve')
}

const doReject = (buttonReject) => {
    const inApprovalId = buttonReject.dataset.inApprovalId;
    processApproval(inApprovalId, 'reject')
}