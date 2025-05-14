/**
 *  헤더 js
 */
const logoutUrl = "/member/logout"; // 로그아웃 서블릿 URL

// 로그아웃 기능
async function logout() {
	console.log("로그아웃 실행");

	try {
		const response = await fetch(logoutUrl, {  // 주소
			method: "POST", // 방식
			headers: {
				"Content-Type": "application/x-www-form-urlencoded"
			},
			body: new URLSearchParams({}) // 보낼 값
		});

		// 응답 저장
		const text = await response.text();

		// 로그아웃 성공 시
		if (text === "true") {
			console.log("로그아웃 성공");
			sessionStorage.removeItem("loginedMemberNumber");
			printSweetAlert("success", "로그아웃 확인", "메인페이지로 이동합니다.", "main.do");
		} else { // 로그아웃 실패 시
			console.log("로그아웃 실패");
			printSweetAlert("error", "로그아웃에 실패했습니다.", "비동기 반환 값 처리 불가");
		}
	} catch (error) {
		console.error("fetch 요청 에러 발생", error);
		printSweetAlert("error", "서버에 문제가 발생했습니다.", "비동기 호출 문제 발생", "error.do");
	}
}

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원정보 반환 함수 호출
	const memberInfo = await getLoginedMemberNumber();
	console.log("관리자 페이지 접근한 로그인한 회원정보 : [" + memberInfo + "]");
	
	loginedMemberNumber = memberInfo.memberNumber;
	loginedMemberIsAdmin = memberInfo.isAdmin;
	
	// 옳지 않은 페이지 접근 시
	if(!loginedMemberNumber || !loginedMemberIsAdmin){
		printSweetAlert("error", "옳지 않은 접근입니다.", "메인 페이지로 이동합니다.","main.do");
	}
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});