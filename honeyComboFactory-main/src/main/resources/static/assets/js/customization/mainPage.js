/**
 *  장바구니 상품 담기(1개 수량) js
 */
const insertCartUrl = "/member/cart/add"; // 장바구니 상품 담기 서블릿 url

// 장바구니 상품 담기 기능
const insertCart = (productNumber, cartProductCount, isComboProduct, productStock) => {
	console.log("상품 상세에서 추천 상품 담기 실행");
	if (!loginedMemberNumber) { // 로그인하지 않은 경우
		console.log("로그인 없이 장바구니 담기 요청");
		// 스위트 알럿창 출력
		printSweetAlert("warning", "로그인이 필요한 기능입니다!");
		return;
	}

	console.log("장바구니 담을 상품 재고 : [" + productStock + "]");
	console.log("장바구니 담을 상품 번호 : [" + productNumber + "]");
	console.log("장바구니 담을 상품 개수 : [" + cartProductCount + "]");
	console.log("장바구니 담을 꿀조합 상품 여부 : [" + isComboProduct + "]");

	if (productStock <= 0) { // 품절 상품이라면
		console.log("장바구니 담을 상품 재고 부족");
		// 스위트 알럿창 출력
		printSweetAlert("warning", "품절 상품입니다.");
		return;
	}

	$.ajax({
		type: "POST", // 방식
		url: insertCartUrl, // 찾아갈 주소
		data: { // 보낼 값
			productNumber: productNumber,
			cartProductCount: cartProductCount,
			isComboProduct: isComboProduct
		},
		dataType: "text", // 받을 타입
		success: (response) => { // 성공 시 처리
			if (response === "true") { // 잘 담겼다면
				console.log("장바구니 상품 담기 성공");
				// 스위트 알럿창 출력
				printSweetAlert("success", "선택하신 상품이 장바구니에 담겼습니다.");
			} else { // 안 담겼다면
				console.log("장바구니 담기 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "선택하신 상품을 장바구니에 담기 실패했습니다.", "지속될 시 관리자에게 문의하세요.");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
};

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});