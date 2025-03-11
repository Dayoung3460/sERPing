// ✅ 삭제 버튼 렌더러 (전역으로 이동)
class DeleteRenderer {
    constructor(props) {
		// 부모 div 추가
        const wrapper = document.createElement('div');
        wrapper.style.display = 'flex';
        wrapper.style.justifyContent = 'center';
        wrapper.style.alignItems = 'center';
        wrapper.style.height = '100%';
        
        const el = document.createElement("button");
        el.textContent = "삭제";
        el.className = 'btnDelete btn btn-outline-danger btn-sm';
        el.addEventListener("click", () => {
            // ✅ 올바른 그리드 객체에서 해당 행 삭제
            purchaseGrid.removeRow(props.rowKey);
        });
          wrapper.appendChild(el); // 버튼을 div 내부에 추가
		  this.el = wrapper; // wrapper를 요소로 설정
    }
    getElement() {
        return this.el;
    }
}


document.addEventListener("DOMContentLoaded", function () {
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf"]').content;
const companyNum = document.getElementById("companyNum").value;
    //console.log("✅ 발주서 등록 페이지 스크립트 실행됨");
	//console.log("✅ 현재 companyNum 값:", companyNum);
	

	
    // ✅ Toast Grid가 렌더링된 후 모달이 실행되도록 순서 조정
    if (!window.purchaseGrid) {
        initPurchaseGrid();
    }
	
	//모달 닫기 작업 
	const modalElement = document.getElementById("goodsModal");
	const closeButton = modalElement.querySelector('[data-bs-dismiss="modal"]');

	if (closeButton) {
	    closeButton.addEventListener("click", function () {
	        //console.log("✅ 창고 모달 닫기 버튼 클릭됨!");

	        try {
	        	let modalInstance = bootstrap.Modal.getInstance("#goodsModal") || new bootstrap.Modal("#goodsModal");
	            modalInstance.hide(); // ✅ Bootstrap 방식으로 모달 닫기
	            
	        } catch (error) {
	            console.warn("❌ Bootstrap 5가 로드되지 않았음. 대체 방식 사용");
	            modalElement.classList.remove("show");
	            modalElement.style.display = "none";
	            document.body.classList.remove("modal-open");

	            setTimeout(() => {
	                document.querySelectorAll(".modal-backdrop").forEach((element) => element.remove()); // 백그라운드 제거
	            }, 300);
				
	// ✅ 모달 닫힐 때 sessionStorage 값 가져와서 그리드에 저장
		    setupModalCloseEvent();
    
	          
	        }
	    });
	} else {
	    console.warn("❌ 창고 모달 닫기 버튼을 찾을 수 없습니다.");
	} 
	
	// ✅ 모달이 열릴 때 그리드 레이아웃을 새로고침
    const goodsNumModal = document.getElementById('goodsNumModal');

    if (goodsNumModal) {
        goodsNumModal.addEventListener('shown.bs.modal', function () {
            console.log("📢 상품 재고 조회 모달 열림");

            setTimeout(() => {
                if (window.productNumGrid) {
                    productNumGrid.refreshLayout();
                    //console.log("✅ productNumGrid 레이아웃 리프레시 완료");
                } else {
                    console.warn("❌ productNumGrid가 정의되지 않음");
                }
            }, 300); // ✅ 300ms 대기 후 실행 (모달 렌더링 완료될 시간 확보)
        });
    } else {
        console.warn("❌ goodsNumModal 요소를 찾을 수 없음");
    }
    
    // ✅ 모달이 열릴 때 그리드 레이아웃을 새로고침
    const orderlistModal = document.getElementById('orderlistModal');

    if (orderlistModal) {
        orderlistModal.addEventListener('shown.bs.modal', function () {
            //console.log("📢 주문서 조회 모달 열림");

            setTimeout(() => {
                if (window.orderListGrid) {
                    orderListGrid.refreshLayout();
                    console.log("✅ orderListGrid 레이아웃 리프레시 완료");
                } else {
                    console.warn("❌ orderListGrid가 정의되지 않음");
                }
            }, 300); // ✅ 300ms 대기 후 실행 (모달 렌더링 완료될 시간 확보)
        });
    } else {
        console.warn("❌ orderListGrid 요소를 찾을 수 없음");
    }
	
	
    
});

// ✅ 발주서 등록 Toast Grid 초기화


function initPurchaseGrid() {
    //console.log("✅ 발주서 Grid 초기화");
    
    window.purchaseGrid = new tui.Grid({
        el: document.getElementById('grid'),
        scrollX :false,
        scrollY : false,
        heightFitToContent: true, // ✅ 추가된 행이 보이도록 자동 높이 조정
        minBodyHeight: 300, // ✅ 너무 작아지는 걸 방지하는 최소 높이 설정
       
       
        columns: [
            { header: '상품코드', name: 'goodsCode' ,align: "left" },
            { header: '상품명', name: 'goodsName',align: "left" },
            { header: '옵션코드', name: 'optionCode' ,hidden: true},
            { header: '옵션명', name: 'optionName' ,align: "left"},
			{ header: '옵션번호', name: 'optionNum' , hidden: true},
            { header: '공급처명', name: 'vendorName' ,align: "left"},
			{ header: '공급처번호', name: 'vendorId' , hidden: true},
            { header: '규격', name: 'goodsStandard' ,align: "center"},
            { header: '수량', name: 'purchaseQuantity',editor: { type: "text", useFormatter: false } ,align: "right"},
            { header: '단가', name: 'purchaseUnitPrice',editor: { type: "text", useFormatter: false },align: "right"},
            { header: '공급가격', name: 'purchaseSupplyPrice' ,align: "right"},
            { header: '부가세', name: 'purchaseVat' ,align: "right"},
            { header: '발주계획바디번호', name: 'orderPlanBodyNum' , hidden: true},
            {
                    header : "삭제"
                    ,name: "delete"
                    ,align: "center"
                    ,renderer: {
                    type: DeleteRenderer // 삭제버튼 정의 렌더러
                    }  
                    ,cellStyle: { textAlign: "center" }
                    ,className: "tui-grid-cell-readonly"
                }
        ],
        rowHeaders: ['checkbox'],
        data: [],
       
    });
	

	//상품 및 옵션 칸 클릭 하면 정보리스트 모달 출력 
	  window.purchaseGrid.on("click", (ev) => {
	    if (ev.columnName === "goodsName" || ev.columnName === "goodsCode" || ev.columnName === "optionName" || ev.columnName === "optionCode") {
	        openGoodsModal(ev.rowKey);
		    }
	});
	
	// ✅ 추가 버튼 기능 (새로운 행 추가)
	    document.getElementById("bttAdd").addEventListener("click", function () {
	        purchaseGrid.appendRow({}, { at: 0 });
	    });
	    
	    
	
	// ✅ 수량 또는 단가 변경 시 공급가격 자동 계산
	    purchaseGrid.on("afterChange", function (ev) {
	        ev.changes.forEach(change => {
	            if (change.columnName === "purchaseQuantity" || change.columnName === "purchaseUnitPrice") {
            let formattedValue = formatNumberWithCommas(change.value);
            purchaseGrid.setValue(change.rowKey, change.columnName, formattedValue);
            calculateSupplyPrice(change.rowKey);
	        }
	    });
	});
	    // ✅ 부가세 체크박스 이벤트
	    document.getElementById("vatUnchecked").addEventListener("change", updateVat);
	    document.getElementById("vatChecked").addEventListener("change", updateVat);
		
		// ✅ 부가세 체크박스 이벤트 핸들러 (하나만 선택 가능)
		document.getElementById("vatUnchecked").addEventListener("click", function () {
		    if (this.checked) {
		        document.getElementById("vatChecked").checked = false; // 다른 체크박스 해제
		        setTimeout(() => updateVat(), 10); // DOM 업데이트 반영 후 부가세 업데이트 실행
		    }
		});

		document.getElementById("vatChecked").addEventListener("click", function () {
		    if (this.checked) {
		        document.getElementById("vatUnchecked").checked = false; // 다른 체크박스 해제
		        setTimeout(() => updateVat(), 10); // DOM 업데이트 반영 후 부가세 업데이트 실행
		    }
		});
		
		
		document.getElementById("purchaseInsert").addEventListener("click",function(){
			purchaseRegister();
		})

	
}



//모달 열기 함수 
// ✅ 모달 열기 함수
	function openGoodsModal(rowKey) {
	    //console.log("📢 선택한 행(rowKey):", rowKey);
	
	    const modalElement = document.getElementById('goodsModal');
	    if (!modalElement) {
	        //console.error("❌ 모달 요소를 찾을 수 없습니다.");
	        return;
	    }
	
	    if (typeof productGrid !== 'undefined' && productGrid !== null) {
	        //console.log("📢 상품 조회 그리드 데이터 갱신 시작");
	
	        // 데이터를 먼저 불러온 후, 모달을 표시
	        productGrid.readData();
	        
	        // ✅ 데이터 갱신 후 모달을 표시하도록 이벤트 리스너 추가
	        productGrid.on("onGridUpdated", function () {
	            //console.log("📢 상품 조회 그리드 데이터 갱신 완료");
	            
	            // ✅ 모달 표시
	            const modalInstance = new bootstrap.Modal(modalElement);
	            modalInstance.show();
	            //console.log("📢 상품 조회 모달 열림:", rowKey);
	
	            // ✅ 데이터 반영 후 레이아웃 새로고침 (지연 실행)
	            setTimeout(() => {
	                productGrid.refreshLayout();
	                //console.log("📢 상품 조회 그리드 리프레시 완료");
	            }, 500);
	        }); 
	    } else {
	        console.warn("❌ productGrid가 정의되지 않았습니다.");
	    }
	}
	// ✅ 모달이 완전히 열린 후 동작
	document.getElementById('goodsModal').addEventListener('shown.bs.modal', function () {
	    console.log("📢 상품 조회 모달이 완전히 열림");

	    if (typeof productGrid !== 'undefined' && productGrid !== null) {
	        setTimeout(() => {
	            productGrid.refreshLayout();
	            //console.log("📢 상품 조회 그리드 리프레시 실행됨");
	        }, 500);
	    } else {
	        console.warn("❌ productGrid가 정의되지 않았습니다.");
	    }
	});
 	



//모달 검은 화면 모두 제거 
 
   document.querySelectorAll('[data-bs-toggle="modal"]').forEach(function (modalTrigger) {
	    modalTrigger.addEventListener("click", function () {
	        document.querySelectorAll('.modal-backdrop').forEach(function (element) {
	            element.remove(); // 중복 생성 방지
	        });
	    });
	});

		
		


// ✅ 모달 닫힐 때 sessionStorage 값 가져와서 그리드에 저장
function setupModalCloseEvent() {
	//console.log("✅ 모달 닫힘 이벤트 실행");

	 
  

 const rowKey = purchaseGrid.getFocusedCell()?.rowKey;
        if (rowKey === null || rowKey === undefined) {
            //console.warn("❌ 먼저 행을 선택해야 합니다.");
            return;
        }
		
		
//✅ sessionStorage의 key와 그리드의 name을 매칭
		 const dataMap = {
		            selectedGoodsCode: "goodsCode",
		            selectedGoodsName: "goodsName",
		            selectedOptionCode: "optionCode",
		            selectedOptionName: "optionName",
					selectedOptionNum : "optionNum",
		            selectedVendorName: "vendorName",
					selectedVendorId: "vendorId",
		            selectedGoodsStandard: "goodsStandard",
		            selectedGoodsSupplyPrice: "purchaseUnitPrice"
		        };

		        Object.keys(dataMap).forEach(storageKey => {
		            const gridColumn = dataMap[storageKey];
		            const value = sessionStorage.getItem(storageKey);
		            if (value) {
		                purchaseGrid.setValue(rowKey, gridColumn, value);
		                sessionStorage.removeItem(storageKey); // ✅ 사용한 데이터 삭제
		            }
		        });
		
	

        //console.log("✅ 발주 그리드에 데이터 저장 완료");
  
}



//수량과 단가 변경 시 공급가격 자동 계산
function calculateSupplyPrice(rowKey) {
    let quantity = purchaseGrid.getValue(rowKey, "purchaseQuantity") || "0";
    let unitPrice = purchaseGrid.getValue(rowKey, "purchaseUnitPrice") || "0";

    // ✅ 문자열에 포함된 콤마(,) 제거 후 숫자로 변환
    quantity = parseFloat(quantity.toString().replace(/,/g, '')) || 0;
    unitPrice = parseFloat(unitPrice.toString().replace(/,/g, '')) || 0;

    let supplyPrice = quantity * unitPrice;

    // ✅ 계산된 공급가격을 Grid에 업데이트 (실제 값은 숫자, 화면에 표시할 때만 `,` 추가)
    purchaseGrid.setValue(rowKey, "purchaseSupplyPrice", formatNumberWithCommas(supplyPrice));

    updateVat(); // ✅ 부가세 즉시 업데이트
}



// ✅ 부가세 체크박스 적용 기능 (모든 행을 업데이트)
function updateVat() {
    const applyVat = document.getElementById("vatChecked").checked; // ✅ 부가세 적용 여부

    purchaseGrid.getData().forEach((row, rowIndex) => {
        let supplyPrice = purchaseGrid.getValue(rowIndex, "purchaseSupplyPrice") || "0";

        // ✅ `,` 제거 후 숫자로 변환
        supplyPrice = parseFloat(supplyPrice.toString().replace(/,/g, '')) || 0;

        let vat = applyVat ? supplyPrice * 0.1 : 0; // ✅ 부가세 계산

        // ✅ 부가세 업데이트 (화면에 표시할 때 `,` 추가)
        purchaseGrid.setValue(rowIndex, "purchaseVat", formatNumberWithCommas(vat));
    });

    //console.log("✅ 부가세 적용 여부:", applyVat ? "적용됨" : "미적용");
}



// ✅ 3자리마다 콤마 추가하는 함수
function formatNumberWithCommas(value) {
    if (!value) return "0";
    let num = value.toString().replace(/,/g, ''); // 기존 콤마 제거 후 숫자로 변환
    return num.replace(/\B(?=(\d{3})+(?!\d))/g, ","); // 3자리마다 콤마 추가
}



// ✅ 발주 등록 함수
function purchaseRegister() {
    // ✅ 체크된 행의 그리드 값만 가져오기
    const gridData = purchaseGrid.getCheckedRows();

    if (gridData.length === 0) {
        showAlert("발주할 상품이 없습니다.","danger");
        return;
    }
    
 
    // ✅ 납기일자 선택 여부 확인
	const purchaseDueDate = document.getElementById("purchaseDueDate").value;
	if (!purchaseDueDate) {
	    showAlert("납기일자를 등록하세요.", "danger");
	    return;
	}
	
	// ✅ 발주일 가져오기
	const purchaseDate = document.getElementById("purchaseDate").value;
	
	// ✅ 발주일과 납기일 비교
	const purchaseDateObj = new Date(purchaseDate);
	const dueDateObj = new Date(purchaseDueDate);
	
	// ✅ 날짜 유효성 검사
	if (dueDateObj < purchaseDateObj) {
	    showAlert("납기일은 발주일보다 이후 날짜여야 합니다.", "danger");
	    return;
	}

    
     // ✅ 수량이 입력되지 않았거나 0인 경우 알림
	for (let item of gridData) {
	    let quantity = item.purchaseQuantity ? parseInt(item.purchaseQuantity.toString().replace(/,/g, '')) || 0 : 0; // 안전하게 처리
	    if (quantity <= 0) {
	        showAlert("수량을 입력하세요.", "danger");
	        return;
	    }
	}


    // ✅ VAT 체크박스 상태에 따라 플래그 설정
    const vatFlag = document.getElementById("vatChecked").checked ? 1 : 0;

    // ✅ 공급처 ID 기준으로 그룹화
    const groupedData = {};
    gridData.forEach((item) => {
        const vendorId = parseInt(item.vendorId) || 0;

        if (!groupedData[vendorId]) {
            groupedData[vendorId] = {
                employeeNum: parseInt(document.getElementById("employeeNum").value) || 0,
                companyNum: parseInt(document.getElementById("companyNum").value) || 0,
                purchaseDate: document.getElementById("purchaseDate").value,
                purchaseDueDate: document.getElementById("purchaseDueDate").value,
                purchaseVatFlag: vatFlag,
                vendorId: vendorId,
                files: []
            };
        }

        // ✅ orderPlanBodyNum이 없으면 null 처리
        const orderPlanBodyNum = item.orderPlanBodyNum ? parseInt(item.orderPlanBodyNum) : null;

        // ✅ 발주서 바디 추가 (숫자로 변환)
        groupedData[vendorId].files.push({
		    companyNum: parseInt(document.getElementById("companyNum").value.replace(/,/g, '')) || 0,  // ✅ 정수 변환
		    goodsStandard: item.goodsStandard,
		    optionNum: parseInt(item.optionNum.replace(/,/g, '')) || 0,  // ✅ 정수 변환
		    orderPlanBodyNum: orderPlanBodyNum ? parseInt(orderPlanBodyNum.replace(/,/g, '')) : null , // ✅ 정수 변환 (nullable)
		    purchaseQuantity: parseInt(item.purchaseQuantity.replace(/,/g, '')) || 0,  // ✅ 정수 변환
		    purchaseSupplyPrice: parseFloat(item.purchaseSupplyPrice.replace(/,/g, '')) || 0,  // ✅ 실수 변환 (문자열 제거)
		    purchaseUnitPrice: parseFloat(item.purchaseUnitPrice.replace(/,/g, '')) || 0,  // ✅ 실수 변환
		    purchaseVat: parseFloat(item.purchaseVat.replace(/,/g, '')) || 0,  // ✅ 실수 변환 (문자열 제거)
		});


    });

	// groupedData를 배열로 변환 (각 그룹별 발주 데이터)
	    const purchaseArray = Object.values(groupedData);
	    //console.log("서버로 전송할 데이터:", purchaseArray);
		
		// CSRF 토큰 및 헤더 정보 (meta 태그에서 미리 읽어옴)
		    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
		    const csrfToken = document.querySelector('meta[name="_csrf"]').content;

		    // 컨트롤러의 엔드포인트로 POST 요청 전송 (예: /purchs/rest/purchase/insert)
		    fetch("/purchs/rest/purchase/insert", {
		        method: "POST",
		        headers: {
		            "Content-Type": "application/json",
		            [csrfHeader]: csrfToken
		        },
		        body: JSON.stringify(purchaseArray)
		    })
		    .then(response => response.json())
		    .then(data => {
		        if (data.status === "success") {
		            showAlert("발주 등록 성공 하였습니다.", "success");
		            setTimeout(() => location.reload(), 1000);
		        } else {
		            showAlert("발주 등록 실패 하였습니다.", "danger");
		        }
		    })
		    .catch(error => {
		        console.error("발주 등록 오류:", error);
		        
		    });
	
	
}




	