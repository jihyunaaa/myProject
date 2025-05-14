/**
 *  카카오 결제 승인 js
 */
//카카오페이 결제 승인 요청을 처리하는 서블릿 URL
const kakaoPayApproveUrl = "/purchase/kakao/approval";

// 카카오페이 결제 승인 API 호출 기능
const requestKakaoPayApprove = (pg_token) => {
	console.log("결제 승인할 pg_token : [" + pg_token + "]");

	// AJAX 요청을 통해 결제 승인 API 호출
	$.ajax({
		type: "POST",
		url: kakaoPayApproveUrl, // 결제 승인 처리를 담당하는 서블릿 URL
		data: { pg_token: pg_token }, // 카카오페이에서 제공한 승인 토큰
		dataType: "json",
		success: (response) => {
			console.log("결제 승인 완료 : " + JSON.stringify(response));

			if (response && response.message === "재고 부족") {
				// 재고 부족 메시지가 있을 때 스위트 알럿창 출력
				Swal.fire({
					icon: "error",
					title: "재고가 부족합니다."
				}).then(() => {
					// 장바구니로 이동
					window.location.replace("cart.do");
				});
			} else if (response && response.approved_at) {
				// 결제 승인 응답이 정상적으로 도착했는지 확인
				Swal.fire({
					icon: "success",
					title: "결제가 완료되었습니다."
				}).then(() => {
					// 결제 완료 후 장바구니로 이동
					window.location.replace("cart.do");
				});
			} else {
				console.log("결제 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "카카오 결제 승인에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 스위트 알럿창 출력 기능
const printSweetAlert = (icon, title, text, pageLink) => {
	console.log("스위트알럿 출력할 아이콘 : [" + icon + "]");
	console.log("스위트알럿 출력할 제목 글 : [" + title + "]");
	console.log("스위트알럿 출력할 내용 글 : [" + text + "]");
	console.log("스위트알럿 후 이동할 링크 : [" + pageLink + "]");

	Swal.fire({
		icon: icon,
		title: title,
		text: text
	}).then(() => {
		if (pageLink) { // 페이지 이동이 필요하다면
			// 페이지 이동
			location.replace = pageLink;
		}
	});
}

$(document).ready(function() {
	// 현재 페이지의 URL에서 pg_token 파라미터 값을 추출
	const urlParams = new URLSearchParams(window.location.search);
	const pg_token = urlParams.get("pg_token");

	// pg_token이 존재하면 결제 승인 요청을 진행
	if (pg_token) {
		requestKakaoPayApprove(pg_token);
	}
	else { // 존재하지 않는다면
		console.log("pg_token 비존재");
		// 스위트 알럿창 출력
		printSweetAlert("error", "올바르지 않은 접근입니다.", "메인페이지로 이동합니다.", "main.do");
	}
});