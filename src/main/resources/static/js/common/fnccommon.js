/**
 * 공통 함수 사용 모달
 */

// 태백작성  문자 또는 숫자 -> 123,153,234,123 형태로 반환
function numberFormat(value) {
	if (value == 0) {
		return value;
	} else if (value == null || value == '') {
		return null;
	} else {
		return Number(value).toLocaleString();
	}
};

// ✅ 콤마 제거 후 숫자로 변환하는 함수 -> 123153234123 으로 int타입 변경
	  function removeCommas(value) {
	      if (typeof value === "string") {
	          return parseFloat(value.replace(/,/g, '')) || 0;
	      }
	      return value || 0;
	  }

//하연작성 sysdate 형태의 날짜를 -> 2025-12-12 12:12:12 형태로 반환
function subscriptday(value){
	const utcDate = new Date(value);
	// 한국 시간(KST)으로 변환 (24시간 형식 유지)
	const options = { 
	    year: 'numeric', month: '2-digit', day: '2-digit',
	    hour: '2-digit', minute: '2-digit', second: '2-digit',
	    hour12: false, timeZone: "Asia/Seoul"
	};
	// `toLocaleString` 사용하여 변환 후, 포맷을 `yyyy-MM-dd HH:mm:ss`로 조정
	const kstDate = new Intl.DateTimeFormat("ko-KR", options).format(utcDate)
	    .replace(/\./g, '-') // yyyy.MM.dd → yyyy-MM-dd 로 변환
	    .replace(/ /g, '')    // 공백 제거
	    .trim();
	return kstDate;
}

//나윤 날짜 yyyy-MM-dd
function formatDateToYYYYMMDD(value) {
    if (!value) return ""; // 값이 없으면 빈 문자열 반환
    const date = new Date(value);
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0');
}



