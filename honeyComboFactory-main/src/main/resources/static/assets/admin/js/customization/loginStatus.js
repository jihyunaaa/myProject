/**
 *  서버에서 로그인 정보 확인 js
 */
const isLoginUrl = "/api/session/member"; // 로그인 상태 확인 url

// 로그인된 사용자 번호
let loginedMemberNumber = null;
// 로그인된 사용자 관리자 여부
let loginedMemberIsAdmin = false;
// 로그인 상태 확인 여부
let loginStatusChecked = false;

// 서버에서 받은 로그인 정보 저장 기능
const initLoginStatus = async () => {
	try {
		const response = await fetch(isLoginUrl, {
			method: "GET",
			headers: {
				"Content-Type": "application/json"
			}
		});

		if (!response.ok) { // HTTP 상태 코드가 에러일 경우
			throw new Error("로그인 상태 확인 실패");
		}

		// 응답 저장
		const res = await response.json();

		// 로그인 중이라면
		if (res.authenticated) {
			loginedMemberNumber = res.loginedMemberNumber;
			loginedMemberIsAdmin = res.memberIsAdmin;
			console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");
			console.log("로그인한 회원 관리자여부 : [" + loginedMemberIsAdmin + "]");
		} else { // 비 로그인 중이라면
			loginedMemberNumber = null;
			loginedMemberIsAdmin = false;
			console.log("비 로그인 상태");
		}
		// 로그인 상태 확인 완료
		loginStatusChecked = true;
	} catch (error) { // 비동기 에러 발생 시
		console.error("서버와의 통신 실패:", error);
		printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
	}
};

// 로그인된 회원 정보 반환
const getLoginedMemberNumber = async () => {
	if (!loginStatusChecked) { // 로그인 상태 미확인 시
		await initLoginStatus(); // 로그인 상태 확인
	}
	
	// 로그인된 회원 정보를 객체 형태로 반환
	return {
		memberNumber: loginedMemberNumber,
		isAdmin: loginedMemberIsAdmin
	};
};

// 페이지 로드 시 로그인 상태 확인
$(document).ready(() => {
	// 서버에서 받은 로그인 정보 저장 함수 호출
	initLoginStatus();
});