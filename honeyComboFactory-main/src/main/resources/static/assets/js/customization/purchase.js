/**
 *  주문 js
 */
const kakaoPayReadyUrl = "/purchase/kakao/ready"; // 카카오페이 결제 준비 요청 서블릿 URL

// 카카오페이 결제 준비 요청 기능
const purchaseCartProduct = () => {
	console.log("카카오페이 결제 준비");

	let cartProductNumberDatas = "";

	// 체크된 상품들의 번호 저장
	$('.productCheckBox:checked').each(function() {
		const productNumber = $(this).val(); // 상품 번호

		// 상품 번호를 저장 (처음이 아니면 + 추가)
		if (cartProductNumberDatas.length > 0) {
			cartProductNumberDatas += "+";
		}
		cartProductNumberDatas += productNumber;
	});
	console.log("체크된 상품들의 번호 정보 : [" + cartProductNumberDatas + "]");

	if (cartProductNumberDatas.length === 0) { // 선택한 결제상품이 없다면
		console.log("선택한 결제상품 없음");
		// 스위트 알럿창 출력
		printSweetAlert("info", "선택하신 결제상품이 없습니다.", "결제할 상품을 선택해주세요.");
		return;
	}

	$.ajax({
		type: "POST", // POST 방식
		url: kakaoPayReadyUrl, // 카카오페이 결제 준비 서블릿
		data: { cartProductNumberDatas: cartProductNumberDatas }, // 보낼 값
		dataType: "json", // 응답 형식: JSON
		success: (response) => {
			console.log("카카오페이 결제 준비 완료 : " + JSON.stringify(response));

			if (response && response.next_redirect_pc_url) {
				// 결제 대기 화면을 PC 환경에 맞게 띄운 후, 결제 수단을 선택하도록 유도
				location.href = response.next_redirect_pc_url;
			} else if (response && response.message === "재고 부족") {
				// 재고 부족 메시지가 있을 때 스위트 알럿창 출력
				printSweetAlert("error", "재고가 부족합니다.");
			} else {
				// 스위트 알럿창 출력
				printSweetAlert("error", "결제 준비 요청에 실패했습니다.", "지속될 시 관리자에게 문의하세요.");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
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

	if (!loginedMemberNumber) { // 비 로그인 상태라면
		// 스위트 알럿창 출력
		printSweetAlert("error", "로그인이 필요한 페이지입니다.", "로그인 페이지로 이동합니다.", "login.do");
	}
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});