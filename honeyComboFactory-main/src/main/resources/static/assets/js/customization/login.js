/**
 *  로그인 js
 */
// 중요 값 상수화
const loginUrl = "/member/login"; // 로그인 서블릿 url
const kakaoLoginUrl = "/member/login/kakao"; // 카카오 로그인 서블릿 url
const javaScriptKey = "api키"; // SDK 카카오 로그인 키
const kakaoServerUrl = "/v2/user/me"; // 카카오 로그인용 서버 url

// 로그인 기능
$("#loginForm").on('submit', function(event) {
	console.log("로그인 실행");
	event.preventDefault(); // 기본 폼 제출 동작을 방지

	const memberId = $("#memberId").val();
	const memberPassword = $("#memberPassword").val();

	console.log("입력받은 로그인 아이디 : [" + memberId + "]");
	console.log("입력받은 로그인 비밀번호 : [" + memberPassword + "]");

	// AJAX 요청
	$.ajax({
		type: "POST", // 방식
		url: loginUrl, // 서버로 데이터를 보낼 URL
		data: { // 보낼 값
			memberId: memberId,
			memberPassword: memberPassword
		},
		dataType: "json", // 받을 타입
		success: function(response) {
			console.log("받은 로그인 데이터 : [" + response.member + "]");
			if (!response.member) { // 로그인 실패 시
				// 스위트 알럿창 출력
				printSweetAlert("error", "존재하지 않는 계정입니다.", "아이디 혹은 비밀번호를 확인하세요.");
			} else { // 로그인 성공 시
				// 관리자 계정이라면
				if (response.member.memberIsAdmin) {
					// 스위트 알럿창 출력
					printSweetAlert("success", "관리자 계정 확인", "회원관리 페이지로 이동합니다.", "manageMember.do");
				} else { // 일반 회원이라면
					// 스위트 알럿창 출력
					printSweetAlert("success", "방문을 환영합니다.", "", "main.do");
				}
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
});



// 카카오 로그인 초기화
Kakao.init(javaScriptKey);
// 카카오 로그인 기능
const kakaoLogin = () => {
	console.log("카카오 로그인 실행");

	Kakao.Auth.login({
		success: () => {
			// 로그인 성공 후 이메일 정보 요청
			Kakao.API.request({
				url: kakaoServerUrl,
				success: (response) => {
					console.log("카카오 로그인 성공");
					// AJAX 요청
					$.ajax({
						type: "POST", // 방식
						contentType: "application/json", // 서버에 보내는 데이터 타입
						url: kakaoLoginUrl, // 서버로 데이터를 보낼 URL
						data: JSON.stringify(response), // 보낼 값
						dataType: "text", // 받을 타입
						success: function(response) {
							console.log("받은 로그인 데이터 : [" + response + "]");
							if (response === "false") { // 로그인 실패 시
								// 스위트 알럿창 출력
								printSweetAlert("error", "카카오 정보 조회에 실패했습니다.", "지속될 시 관리자에게 문의하세요.");
							} else { // 로그인 성공 시
								// 스위트 알럿창 출력
								printSweetAlert("success", "방문을 환영합니다.", "", "main.do");
							}
						},
						error: (xhr, status, error) => { // 에러 처리
							console.error("AJAX 요청 에러 발생", xhr.status, status, error);
							// 스위트 알럿창 출력
							printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
						}
					});
				},
				fail: (xhr, status, error) => {
					console.error("AJAX 요청 에러 발생", xhr.status, status, error);
					// 스위트 알럿창 출력
					printSweetAlert("error", "카카오 서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
				}
			});
		},
		fail: (xhr, status, error) => {
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "카카오 서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");

	if (loginedMemberNumber) { // 로그인 상태라면
		// 스위트 알럿창 출력
		printSweetAlert("error", "로그인이 필요한 페이지입니다.", "로그인 페이지로 이동합니다.", "login.do");
	}
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});