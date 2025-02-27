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