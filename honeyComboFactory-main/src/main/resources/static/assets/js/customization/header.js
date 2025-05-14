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
			printSweetAlert("success", "재방문을 기다리겠습니다.", "메인페이지로 이동합니다.", "main.do");
		} else { // 로그아웃 실패 시
			console.log("로그아웃 실패");
			printSweetAlert("error", "로그아웃에 실패했습니다.", "지속될 시 관리자에게 문의하세요.");
		}
	} catch (error) {
		console.error("fetch 요청 에러 발생", error);
		printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
	}
}