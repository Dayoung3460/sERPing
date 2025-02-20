
document.addEventListener("DOMContentLoaded", function () {
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf"]').content;
const companyNum = document.getElementById("companyNum").value;
    console.log("✅ 발주서 등록 페이지 스크립트 실행됨");
	console.log("✅ 현재 companyNum 값:", companyNum);
	
    // ✅ Toast Grid가 렌더링된 후 모달이 실행되도록 순서 조정
    if (!window.purchaseGrid) {
        initPurchaseGrid();
    }
	
	//모달 닫기 작업 
	const modalElement = document.getElementById("goodsModal");
	const closeButton = modalElement.querySelector('[data-bs-dismiss="modal"]');

	if (closeButton) {
	    closeButton.addEventListener("click", function () {
	        console.log("✅ 창고 모달 닫기 버튼 클릭됨!");

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
	
	
    
});

// ✅ 발주서 등록 Toast Grid 초기화


function initPurchaseGrid() {
    console.log("✅ 발주서 Grid 초기화");

    window.purchaseGrid = new tui.Grid({
        el: document.getElementById('grid'),
        columns: [
            { header: '상품코드', name: 'goodsCode' },
            { header: '상품명', name: 'goodsName' },
            { header: '옵션코드', name: 'optionCode' },
            { header: '옵션명', name: 'optionName' },
			{ header: '옵션번호', name: 'optionNum' , hidden: true},
            { header: '거래처명', name: 'vendorName' },
			{ header: '거래처번호', name: 'vendorId' , hidden: true},
            { header: '규격', name: 'goodsStandard' },
            { header: '수량', name: 'puchaseQuantity',editor: "text" },
            { header: '단가', name: 'puchaseUnitPrice',editor: "text" },
            { header: '공급가격', name: 'purchaseSupplyPrice' },
            { header: '부가세', name: 'puchaseVat' }
        ],
        rowHeaders: ['checkbox'],
        data: [],
        scrollX: true,
        scrollY: 300
    });
	
	//추가 버튼 기능 (새로운 행 추가) 
    document.getElementById("bttAdd").addEventListener("click", function () {
        purchaseGrid.appendRow({}, { at: 0 });
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
	            if (change.columnName === "puchaseQuantity" || change.columnName === "puchaseUnitPrice") {
					purchaseGrid.setValue(change.rowKey, change.columnName, formatNumber(change.value));
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

	
}
	
//모달 열기 함수 
// ✅ 모달 열기 함수
	function openGoodsModal(rowKey) {
	    console.log("📢 선택한 행(rowKey):", rowKey);
	
	    const modalElement = document.getElementById('goodsModal');
	    if (!modalElement) {
	        console.error("❌ 모달 요소를 찾을 수 없습니다.");
	        return;
	    }
	
	    if (typeof productGrid !== 'undefined' && productGrid !== null) {
	        console.log("📢 상품 조회 그리드 데이터 갱신 시작");
	
	        // 데이터를 먼저 불러온 후, 모달을 표시
	        productGrid.readData();
	        
	        // ✅ 데이터 갱신 후 모달을 표시하도록 이벤트 리스너 추가
	        productGrid.on("onGridUpdated", function () {
	            console.log("📢 상품 조회 그리드 데이터 갱신 완료");
	            
	            // ✅ 모달 표시
	            const modalInstance = new bootstrap.Modal(modalElement);
	            modalInstance.show();
	            console.log("📢 상품 조회 모달 열림:", rowKey);
	
	            // ✅ 데이터 반영 후 레이아웃 새로고침 (지연 실행)
	            setTimeout(() => {
	                productGrid.refreshLayout();
	                console.log("📢 상품 조회 그리드 리프레시 완료");
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
	            console.log("📢 상품 조회 그리드 리프레시 실행됨");
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
  

        const rowKey = purchaseGrid.getFocusedCell()?.rowKey;
        if (rowKey === null || rowKey === undefined) {
            console.warn("❌ 먼저 행을 선택해야 합니다.");
            return;
        }
		
		
		// ✅ sessionStorage의 key와 그리드의 name을 매칭
		        const dataMap = {
		            selectedGoodsCode: "goodsCode",
		            selectedGoodsName: "goodsName",
		            selectedOptionCode: "optionCode",
		            selectedOptionName: "optionName",
					selectedOptionNum : "optionNum",
		            selectedVendorName: "vendorName",
					selectedVendorId: "vendorId",
		            selectedGoodsStandard: "goodsStandard",
		            selectedGoodsSupplyPrice: "puchaseUnitPrice"
		        };

		        Object.keys(dataMap).forEach(storageKey => {
		            const gridColumn = dataMap[storageKey];
		            const value = sessionStorage.getItem(storageKey);
		            if (value) {
		                purchaseGrid.setValue(rowKey, gridColumn, value);
		                sessionStorage.removeItem(storageKey); // ✅ 사용한 데이터 삭제
		            }
		        });
		
		

        console.log("✅ 발주 그리드에 데이터 저장 완료");
  
}
//수량과 단가 변경 시 공급가격 자동 계산
function calculateSupplyPrice(rowKey) {
    let quantity = purchaseGrid.getValue(rowKey, "puchaseQuantity") || "0";
    let unitPrice = purchaseGrid.getValue(rowKey, "puchaseUnitPrice") || "0";

    // ✅ 문자열에 포함된 콤마(,) 제거 후 숫자로 변환
    quantity = parseFloat(quantity.replace(/,/g, '')) || 0;
    unitPrice = parseFloat(unitPrice.replace(/,/g, '')) || 0;

    let supplyPrice = quantity * unitPrice;

    // ✅ 계산된 공급가격을 Grid에 업데이트 (실제 값은 숫자, 화면에 표시할 때만 `,` 추가)
    purchaseGrid.setValue(rowKey, "purchaseSupplyPrice", formatNumber(supplyPrice));

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
        purchaseGrid.setValue(rowIndex, "puchaseVat", formatNumber(vat.toFixed(2)));
    });

    console.log("✅ 부가세 적용 여부:", applyVat ? "적용됨" : "미적용");
}


// 숫자를 3자리마다 , 로 구분하는 함수
function formatNumber(value) {
    if (!value) return "0"; 
    return parseFloat(value).toLocaleString('ko-KR'); 
}



	





