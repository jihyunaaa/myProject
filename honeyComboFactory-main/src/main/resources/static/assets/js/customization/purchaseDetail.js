/**
 *  주문 상세 js
 */
// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) { // 비 로그인 상태라면
		// 스위트 알럿창 출력
		printSweetAlert("error", "로그인이 필요한 페이지입니다.", "로그인 페이지로 이동합니다.", "login.do");
	}
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});