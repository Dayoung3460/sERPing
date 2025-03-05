// HTML이 로드된 후 실행
document.addEventListener("DOMContentLoaded", async function () {
    if (sessionData.companyNum) {
        try {
            showLoadingIndicator(); // 로딩 표시

            // 모든 데이터를 병렬로 요청 후 대기
            await Promise.all([
                subscriptionlist(sessionData.companyNum),
                subcontactlist(sessionData.companyNum),
                subgpoptionlist(sessionData.companyNum)
            ]);

            updateUI(); // 데이터가 로드된 후 UI 업데이트

        } catch (error) {
            console.error("데이터 로드 중 오류 발생:", error);
        } finally {
            hideLoadingIndicator(); // 로딩 완료 후 숨김
        }
    } else {
        console.error("회사 번호가 없습니다.");
    }
});

var subscriptionState;

// 로딩 표시 함수
function showLoadingIndicator() {
    document.body.insertAdjacentHTML("afterbegin", '<div id="loading">Loading...</div>');
}

// 로딩 숨김 함수
function hideLoadingIndicator() {
    let loadingElement = document.getElementById("loading");
    if (loadingElement) {
        loadingElement.remove();
    }
}

// UI 업데이트 함수
function updateUI() {
    //console.log("구독 정보:", subscriptionState);
    // 화면 업데이트 로직 추가 가능
}

// 그룹웨어 옵션 현황 불러오기 (재시도 기능 추가)
async function subgpoptionlist(companyNum, retryCount = 3) {
    const url = "/erp/rest/subgp/" + companyNum;
    
    for (let attempt = 1; attempt <= retryCount; attempt++) {
        try {
            //console.log(`그룹웨어 옵션 데이터 요청 (시도 ${attempt}/${retryCount})`);

            let response = await fetch(url, { method: "GET", headers: { "Content-Type": "application/json" } });
            
            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }

            let data = await response.text();

            //console.log("그룹웨어 옵션 데이터 응답:", data);

            if (!data || data === "0") {
                console.warn("그룹웨어 옵션 데이터가 없음, 기본값 50 설정");
                data = "50"; // 기본값 설정
            }

            subscriptionState = subscriptionState || {};
            subscriptionState.gpOption = data;

            //console.log("최종 설정된 gpOption 값:", subscriptionState.gpOption);
            return; // 성공 시 종료

        } catch (error) {
            console.error(`그룹웨어 옵션 불러오기 실패 (시도 ${attempt}/${retryCount}):`, error);

            if (attempt === retryCount) {
                console.error("최대 재시도 횟수 도달. 기본값 50 적용");
                subscriptionState = subscriptionState || {};
                subscriptionState.gpOption = "50";
            }
        }
        
        await new Promise(resolve => setTimeout(resolve, 1000)); // 재시도 전 1초 대기
    }
}

// 계약서 서명 정보 불러오기 (재시도 기능 추가)
async function subcontactlist(companyNum, retryCount = 3) {
    const url = "/erp/rest/subcontact/" + companyNum;

    for (let attempt = 1; attempt <= retryCount; attempt++) {
        try {
            let response = await fetch(url, { method: "GET", headers: { "Content-Type": "application/json" } });
            let data = await response.text();

            if (!data || data === "0") {
                //console.warn("계약서 서명 데이터 없음, 기본값 0 설정");
                data = "0";
            }

            subscriptionState = subscriptionState || {};
            subscriptionState.subcon = data;

            //console.log("계약서 서명 정보:", subscriptionState.subcon);
            return;

        } catch (error) {
            console.error(`계약서 서명 데이터 불러오기 실패 (시도 ${attempt}/${retryCount}):`, error);

            if (attempt === retryCount) {
                console.error("최대 재시도 횟수 도달. 기본값 0 적용");
                subscriptionState = subscriptionState || {};
                subscriptionState.subcon = "0";
            }
        }

        await new Promise(resolve => setTimeout(resolve, 1000)); // 재시도 전 1초 대기
    }
}

// 구독 정보 불러오기 (재시도 기능 추가)
async function subscriptionlist(companyNum, retryCount = 3) {
    const url = "/erp/rest/subList/" + companyNum;

    for (let attempt = 1; attempt <= retryCount; attempt++) {
        try {
            let response = await fetch(url, { method: "GET", headers: { "Content-Type": "application/json" } });
            let data = await response.json();
            subManagement(data);
            return;

        } catch (error) {
            console.error(`구독 정보 불러오기 실패 (시도 ${attempt}/${retryCount}):`, error);

            if (attempt === retryCount) {
                console.error("최대 재시도 횟수 도달, 기본값 적용");
                subscriptionState = subscriptionState || {};
            }
        }

        await new Promise(resolve => setTimeout(resolve, 1000)); // 재시도 전 1초 대기
    }
}

// 남은 기간 반환 함수
function modDate(endDateStr) {
    if (!endDateStr) return 0;

    let today = new Date();
    let endDate = new Date(endDateStr);

    if (isNaN(endDate.getTime())) {
        console.error("유효하지 않은 날짜 값:", endDateStr);
        return 0;
    }

    let timeDiff = endDate.getTime() - today.getTime();
    return Math.ceil(timeDiff / (1000 * 60 * 60 * 24));
}

// 구독정보 처리
function subManagement(data) {
    const subscriptionMap = [
        null,
        { name: "구독형태", key: "subObj", dateKey: "subObjdate" },
        { name: "그룹웨어", key: "groupObj", dateKey: "groupObjdate" },
        { name: "인사관리", key: "hrObj", dateKey: "hrObjdate" },
        { name: "지점관리", key: "pointObj", dateKey: "pointObjdate" },
        { name: "회계관리", key: "accountObj", dateKey: "accountObjdate" }
    ];

    // 🚨 subscriptionState가 undefined면 빈 객체로 초기화
    subscriptionState = subscriptionState || {};

    // 기존 subscriptionState를 유지하면서 값 업데이트
    subscriptionState = {
        ...subscriptionState,  // 기존 값 유지
        subObj: "date",
        subObjdate: 0,
        groupObj: "date",
        groupObjdate: 0,
        hrObj: "date",
        hrObjdate: 0,
        pointObj: "date",
        pointObjdate: 0,
        accountObj: "date",
        accountObjdate: 0,
        subcon: subscriptionState.subcon || 0, // 기존 subcon 값 유지
        gpOption: subscriptionState.gpOption || 50, // 기존 gpOption 값 유지
    };

    data.forEach(ele => {
        let baseNum = (ele.subscriptionNameNum - 1) % 5 + 1;
        let isPeriodic = ele.subscriptionNameNum > 5;
        let remainingDays = modDate(ele.subscriptionEndDate);

        if (subscriptionMap[baseNum]) {
            let { key, dateKey } = subscriptionMap[baseNum];

            subscriptionState[key] = isPeriodic ? "per" : "date";
            subscriptionState[dateKey] = remainingDays;
        }
    });
    //console.log("최종 구독 상태:", subscriptionState);

	/*============ 구독에 따른 권한 처리 ===============*/
	setTimeout(function(){
		//console.log("현재주소 : "+window.location.href);
		//console.log(sessionData);
		
	    //계약정보가 없을경우 페이지로 리다액션
	    if(!window.location.href.includes("erp/usercontact")){
			//마스터계정아니라도 갈수있음 이유는 계정 생성하면 다른계정이 없고 그계정이 서명해야지만 사용가능
			if(subscriptionState.subcon != "1"){
				showAlert('사용계약 서명을 해주세요.', 'danger');
				window.location.href = "/erp/usercontact?menu=mypage";
			}
		}
		
		//최고 관리자인 경우에만
		if(sessionData.authority == "AU001"){
			//서비스 계약 만료 된 최고 관리자
			if(!window.location.href.includes("erp/fppay")){
				if(parseInt(subscriptionState.subObjdate) <= -10){
					showAlert('결제 후 이용가능합니다.', 'danger');
					window.location.href = "/erp/fppay?menu=mypage";
				}
			}
			
			//페이지 접속했을 경우
			//지점 관리 기능
			if(window.location.href.includes("/bhf/")){
				if(parseInt(subscriptionState.pointObjdate) <= -10){
					showAlert('결제 후 이용가능합니다.', 'danger');
					window.location.href = "/erp/fppay?menu=mypage";
				}
			}
			
			//인사 관리 기능
			if(window.location.href.includes("/hr/")){
				if(!window.location.href.includes("/hr/employee")){
					if(parseInt(subscriptionState.hrObjdate) <= 0){
						showAlert('결제 후 이용가능합니다.', 'danger');
						window.location.href = "/erp/fppay?menu=mypage";
					}
				}
			}
			//회계처리
			if(window.location.href.includes("/accnut/")){
				if(!window.location.href.includes("/accnut/selling_select")){
					if(parseInt(subscriptionState.hrObjdate) <= 0){
						showAlert('결제 후 이용가능합니다.', 'danger');
						window.location.href = "/accnut/selling_select";
					}
				}
			}
			
			//페이지 링크 삭제
			//지점
			if(parseInt(subscriptionState.pointObjdate) <= 0){
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    if (branchLink) {
			        branchLink.removeAttribute("href");
			    }
		    }
		    
		    var currentUrl = window.location.href;
		    //인사관리
		    if(parseInt(subscriptionState.hrObjdate) <= 0){
			    if (currentUrl.includes("/hr/")) {
				    document.querySelectorAll(".nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && href.includes("/hr/") && !href.includes("/hr/employee")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
			    }
		    }
		    
		    //회계관리
		    if(parseInt(subscriptionState.accountObjdate) <= 0){
			    if (currentUrl.includes("/accnut/") || currentUrl.includes("menu=account")) {
				    document.querySelectorAll(".nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && href.includes("/accnut/") && !href.includes("/accnut/selling_select")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
			    }
		    }
			
		//일반 사용자인 경우에만
		}else{
			
			//페이지 링크 접근시
			//서비스 계약 만료된 일반 사용자
			if (window.location.href !== window.location.origin + "/") { 
			    if (parseInt(subscriptionState.subObjdate) <= -10) {
			        showAlert('결제 후 이용가능합니다.', 'danger');
			        window.location.href = "/";
			    }
			}
			
			//지점 관리 기능
			if(window.location.href.includes("/bhf/")){
				if(parseInt(subscriptionState.pointObjdate) <= 0){
					showAlert('결제 후 이용가능합니다.', 'danger');
					window.location.href = "/";
				}
			}
			
			//인사 관리 기능
			if(window.location.href.includes("/hr/")){
				if(!window.location.href.includes("/hr/employee")){
					if(parseInt(subscriptionState.hrObjdate) <= 0){
						showAlert('결제 후 이용가능합니다.', 'danger');
						window.location.href = "/";
					}
				}
			}
			//회계처리
			if(window.location.href.includes("/accnut/")){
				if(!window.location.href.includes("/accnut/selling_select")){
					if(parseInt(subscriptionState.hrObjdate) <= 0){
						showAlert('결제 후 이용가능합니다.', 'danger');
						window.location.href = "/accnut/selling_select";
					}
				}
			}
			
			//페이지 링크 삭제
			//지점
			if(parseInt(subscriptionState.pointObjdate) <= 0){
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    if (branchLink) {
			        branchLink.removeAttribute("href");
			    }
		    }
		    
		    var currentUrl = window.location.href;
		    //인사관리
		    if(parseInt(subscriptionState.hrObjdate) <= 0){
			    if (currentUrl.includes("/hr/")) {
				    document.querySelectorAll(".nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && href.includes("/hr/") && !href.includes("/hr/employee")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
			    }
		    }
		    
		    //회계관리
		    if(parseInt(subscriptionState.accountObjdate) <= 0){
			    if (currentUrl.includes("/accnut/") || currentUrl.includes("menu=account")) {
				    document.querySelectorAll(".nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && href.includes("/accnut/") && !href.includes("/accnut/selling_select")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
			    }
		    }
		}
	}, 500);
    
}
document.addEventListener("DOMContentLoaded", function () {
    //관리자이면서 마이페이지이면 메뉴 생기게
    var currentUrl = window.location.href;
    if ( currentUrl.includes("/mypage/") || currentUrl.includes("/erp/erpsubinfo") || currentUrl.includes("/erp/usercontact") || currentUrl.includes("/erp/fppay") ) {
		if(sessionData.authority != "AU001"){
			 document.querySelectorAll(".nav-item.autag").forEach(li => {
			    li.remove();
			});
		}
	}
    /*
    if(sessionData.authority == "AU001"){
		if ( currentUrl.includes("/mypage/") || currentUrl.includes("/erp/erpsubinfo") || currentUrl.includes("/erp/usercontact") 
		 || currentUrl.includes("/erp/fppay") ) {
			let adminDomTag = `
	        <li class="nav-item"><a class="nav-link" href="/erp/usercontact?menu=mypage">
	            <i class="icon-grid menu-icon"></i> <span class="menu-title">사용계약서</span></a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link" href="/erp/erpsubinfo?menu=mypage">
	                <i class="icon-grid menu-icon"></i>
	                <span class="menu-title">구독</span>
	            </a>
	        <div class="collapse" id="ui-basic-1">
	                <ul class="nav flex-column sub-menu rounded-3">
	                    <li class="nav-item"><a class="nav-link" href="/erp/erpsubinfo?menu=mypage">구독조회</a></li>
	                    <li class="nav-item"><a class="nav-link" href="/erp/fppay?menu=mypage">구독결제</a></li>
	                </ul>
	            </div>
	        </li>
	        `;
			document.querySelector(".admin").insertAdjacentHTML("beforeend", adminDomTag);
		}
	}
	*/
	
	if(sessionData.authority != "AU001"){
		//부서에대한 권한처리
		//console.log("현재주소 : "+currentUrl);
		//console.log("사용할세션 : ",sessionData);
		const url = `/erp/rest/userDepartmentName/`+sessionData.departmentNum;
					
		fetch(url, {
		    method: "GET",
		    headers: {
		        "Content-Type": "application/json"
		    }
		})
		.then(response => response.json())  // JSON 변환
		.then(result => {
		    //console.log("API 응답:", result);  // API 응답을 확인
		    
		    //인사부서 링크제거 처리
		   	if (result.childDepartmentName && result.childDepartmentName.includes("인사")) {
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    let bsnLink = document.querySelector('nav.menu a[data="sales"]');
			    let purchsLink = document.querySelector('nav.menu a[data="inventory"]');
			    let hrLink = document.querySelector('nav.menu a[data="hr"]');
			    let accnutLink = document.querySelector('nav.menu a[data="account"]');
			    if (branchLink) branchLink.removeAttribute("href");
			    //if (bsnLink) bsnLink.removeAttribute("href");
			    if (purchsLink) purchsLink.removeAttribute("href");
			    //if (hrLink) hrLink.removeAttribute("href");
			    if (accnutLink) accnutLink.removeAttribute("href");
			    
			    //부서내부 등급 권한
			    if(sessionData.authority === "AU002" || sessionData.authority === "AU001"){
				}else{
					document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href.includes("/hr/emp_contract?")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
				
				//다른부서인데 모든사원이 접근 가능한 페이지 (영업부서)
			    if (currentUrl.includes("/bsn/") || currentUrl.includes("menu=sales")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href.includes("/bsn/order/Regist") || href.includes("/bsn/delivery?") || href.includes("/bsn/order/cs/returning?")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
			}
			
			//영업부서 링크제거 처리
		   	if (result.childDepartmentName && result.childDepartmentName.includes("영업")) {
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    let bsnLink = document.querySelector('nav.menu a[data="sales"]');
			    let purchsLink = document.querySelector('nav.menu a[data="inventory"]');
			    let hrLink = document.querySelector('nav.menu a[data="hr"]');
			    let accnutLink = document.querySelector('nav.menu a[data="account"]');
			    if (branchLink) branchLink.removeAttribute("href");
			    //if (bsnLink) bsnLink.removeAttribute("href");
			    if (purchsLink) purchsLink.setAttribute("href", "/purchs/goodsLots?menu=inventory");
			    if (hrLink) hrLink.setAttribute("href", "/hr/organization_list?menu=hr");
			    if (accnutLink) accnutLink.removeAttribute("href");
			    
			    //다른부서인데 모든사원이 접근 가능한 페이지 (인사부서)
			    if (currentUrl.includes("/hr/") || currentUrl.includes("menu=hr")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && !href.includes("/hr/organization_list")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
				//재고부서에 영업팀이 접근 가능
				if (currentUrl.includes("/purchs/") || currentUrl.includes("menu=inventory")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && !href.includes("/purchs/goodsLots")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
			}
			
			//재고부서 링크제거 처리
		   	if (result.childDepartmentName && result.childDepartmentName.includes("재고")) {
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    let bsnLink = document.querySelector('nav.menu a[data="sales"]');
			    let purchsLink = document.querySelector('nav.menu a[data="inventory"]');
			    let hrLink = document.querySelector('nav.menu a[data="hr"]');
			    let accnutLink = document.querySelector('nav.menu a[data="account"]');
			    if (branchLink) branchLink.removeAttribute("href");
			    //if (bsnLink) bsnLink.removeAttribute("href");
			    //if (purchsLink) purchsLink.removeAttribute("href");
			    if (hrLink) hrLink.setAttribute("href", "/hr/organization_list?menu=hr");
			    if (accnutLink) accnutLink.removeAttribute("href");
			    
			    //다른부서인데 모든사원이 접근 가능한 페이지 (인사)
			    if (currentUrl.includes("/hr/") || currentUrl.includes("menu=hr")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && !href.includes("/hr/organization_list")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
				
				//다른부서인데 모든사원이 접근 가능한 페이지 (영업부서)
			    if (currentUrl.includes("/bsn/") || currentUrl.includes("menu=sales")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href.includes("/bsn/order/Regist") || href.includes("/bsn/delivery?") || href.includes("/bsn/order/cs/returning?")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
			}
			
			//회계부서 링크제거 처리
		   	if (result.childDepartmentName && result.childDepartmentName.includes("회계")) {
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    let bsnLink = document.querySelector('nav.menu a[data="sales"]');
			    let purchsLink = document.querySelector('nav.menu a[data="inventory"]');
			    let hrLink = document.querySelector('nav.menu a[data="hr"]');
			    let accnutLink = document.querySelector('nav.menu a[data="account"]');
			    if (branchLink) branchLink.removeAttribute("href");
			    //if (bsnLink) bsnLink.removeAttribute("href");
			    if (purchsLink) purchsLink.removeAttribute("href");
			    if (hrLink) hrLink.setAttribute("href", "/hr/organization_list?menu=hr");
			    //if (accnutLink) accnutLink.removeAttribute("href");
			    
			    //다른부서인데 모든사원이 접근 가능한 페이지 (인사)
			    if (currentUrl.includes("/hr/") || currentUrl.includes("menu=hr")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && !href.includes("/hr/organization_list")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
				
				//다른부서인데 모든사원이 접근 가능한 페이지 (영업부서)
			    if (currentUrl.includes("/bsn/") || currentUrl.includes("menu=sales")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href.includes("/bsn/order/Regist") || href.includes("/bsn/delivery?") || href.includes("/bsn/order/cs/returning?")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
			}
			
			//지점부서 링크제거 처리
		   	if (result.childDepartmentName && result.parentDepartmentName.includes("지점")) {
			    let branchLink = document.querySelector('nav.menu a[data="branch"]');
			    let bsnLink = document.querySelector('nav.menu a[data="sales"]');
			    let purchsLink = document.querySelector('nav.menu a[data="inventory"]');
			    let hrLink = document.querySelector('nav.menu a[data="hr"]');
			    let accnutLink = document.querySelector('nav.menu a[data="account"]');
			    //if (branchLink) branchLink.removeAttribute("href");
			    //if (bsnLink) bsnLink.removeAttribute("href");
			    if (purchsLink) purchsLink.removeAttribute("href");
			    if (hrLink) hrLink.setAttribute("href", "/hr/organization_list?menu=hr");
			    if (accnutLink) accnutLink.removeAttribute("href");
			    
			    //다른부서인데 모든사원이 접근 가능한 페이지 (인사)
			    if (currentUrl.includes("/hr/") || currentUrl.includes("menu=hr")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href && !href.includes("/hr/organization_list")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
				
				//다른부서인데 모든사원이 접근 가능한 페이지 (영업부서)
			    if (currentUrl.includes("/bsn/") || currentUrl.includes("menu=sales")) {
				    document.querySelectorAll("#sidebar .nav-item a.nav-link").forEach(link => {
				        let href = link.getAttribute("href");
				        if (href.includes("/bsn/order/Regist") || href.includes("/bsn/delivery?") || href.includes("/bsn/order/cs/returning?")) {
				            link.removeAttribute("href");
				            link.style.pointerEvents = "none";
				            link.style.color = "#aaa";
				            link.style.cursor = "default";
				        }
				    });
				}
			}
			
			//관리자 권한만 접근 가능
		    if(sessionData.authority === "AU002" || sessionData.authority === "AU001"){
			}else{
				let standardLink = document.querySelector('nav.menu a[data="standard"]')
			    if (standardLink) standardLink.removeAttribute("href");
			}
		});
	}
	
	
});