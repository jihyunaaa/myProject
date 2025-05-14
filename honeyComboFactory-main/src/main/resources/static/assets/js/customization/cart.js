/**
 *  장바구니 js
 */
const kakaoPayReadyUrl = "/purchase/kakao/ready"; // 카카오페이 결제 준비 요청 서블릿 URL
const changeCartProductCountUrl = "/member/cart/change"; // 장바구니 상품 개수 변경 서블릿 url
const deleteCartProductUrl = "/member/cart/delete"; // 장바구니 상품 삭제 서블릿 url
const checkStockPurchaseCartProductUrl = "/member/cart/delete"; // 결제 상품 재고 검사 서블릿 url

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

// 장바구니 각 상품 구매 가격 변경 기능
const calculationPrice = (cartProductNumber) => {
	console.log("장바구니 각 상품 구매 가격 변경 실행");

	// 할인된 가격이 있으면 그걸 사용, 없으면 일반 가격 사용
	let priceText = $("#cartProductDiscountedPrice-" + cartProductNumber).text() || $("#cartProductPrice-" + cartProductNumber).text();
	// 상품 가격
	const cartProductPrice = parseInt(priceText.replace(/,/g, '').replace(/,/g, ''));
	// 상품 수량
	const cartProductCount = parseInt($("#count-" + cartProductNumber).val());

	console.log("장바구니 각 상품 구매 가격 변경할 번호 : " + cartProductNumber + "]");
	console.log("상품 개수변경 가격 : [" + cartProductPrice + "]");
	console.log("상품 개수변경 수량 : [" + cartProductCount + "]");

	const formattedPrice = (cartProductPrice * cartProductCount).toLocaleString();
	console.log("변경할 상품 가격 : [" + formattedPrice + "]");
	// 화면 가격 변경
	$("#cartProductSumPrice-" + cartProductNumber).text(formattedPrice);
}

// 장바구니 각 체크박스 클릭 기능
const clickOneCheckBox = (isChecked) => {
	console.log("체크박스 해제 여부 : [" + isChecked + "]");

	if (isChecked) { // 체크 실행이라면
		if (checkAllChecked()) { // 체크박스가 다 체크상태라면
			// productCheckBox 클래스의 모든 체크박스 상태 변경
			$("#cartAllCheckBox").prop('checked', true);
		}
	}
	else { // 체크 해제라면
		// productCheckBox 클래스의 모든 체크박스 상태 변경
		$("#cartAllCheckBox").prop('checked', false);
	}

	// 장바구니 상품 가격 총합 계산 함수 호출
	calculationTotalAmount();
}

// 전체 체크박스 체크 상태 여부 검사 기능
const checkAllChecked = () => {
	console.log("전체 체크박스 체크 상태 여부 검사 실행");
	const totalCheckboxes = $('.productCheckBox').length; // 전체 체크박스 개수
	const checkedCheckboxes = $('.productCheckBox:checked').length; // 체크된 체크박스 개수
	console.log("전체 체크박스 개수 : [" + totalCheckboxes + "]");
	console.log("체크된 체크박스 개수 : [" + checkedCheckboxes + "]");

	if (totalCheckboxes == checkedCheckboxes) { // 체크박스가 다 체크상태라면
		console.log("체크박스 전부 체크 됨")
		return true;
	}
	return false;
}

// 장바구니 상품 가격 총합 계산 기능
const calculationTotalAmount = () => {
	console.log("종바구니 상품 총합 계산 실행");
	let totalAmount = 0;

	// 체크된 상품들의 가격을 합산
	$('.productCheckBox:checked').each(function() {
		const productNumber = $(this).val();  // 개별 상품 번호

		// 할인된 가격이 있으면 그걸 사용, 없으면 일반 가격 사용
		let priceText = $("#cartProductDiscountedPrice-" + productNumber).text() || $("#cartProductPrice-" + productNumber).text();
		// 상품 가격
		const price = parseInt(priceText.replace(/,/g, '').replace(/,/g, ''));
		const count = parseInt($("#count-" + productNumber).val()); // 상품 수량

		totalAmount += price * count; // 총 가격 계산
	});
	console.log("체크된 상품들의 합산 가격 : [" + totalAmount + "]");

	// 총합을 페이지에 표시
	$('#totalAmount').text(totalAmount.toLocaleString());
}

// 장바구니 전체 선택/비선택 기능
const selectAllProduct = (isChecked) => {
	console.log("장바구니 상품 전체 체크 여부 : [" + isChecked + "]");

	// productCheckBox 클래스의 모든 체크박스를 선택하여 상태 변경
	$(".productCheckBox").prop('checked', isChecked);

	// 장바구니 상품 가격 총합 계산 함수 호출
	calculationTotalAmount();
}

// 장바구니 상품 수량 표시 변경 기능
const changeInputCartProductCount = (cartProductNumber, cartProductCondition, productCount) => {
	console.log("장바구니 상품 수량 표시 변경할 상품 번호 : [" + cartProductNumber + "]");
	console.log("장바구니 상품 수량 표시 변경할 상품 증감 조건 : [" + cartProductCondition + "]");
	console.log("장바구니 상품 수량 표시 변경할 상품 변경 수량 : [" + productCount + "]");

	const beforeCount = parseInt($("#count-" + cartProductNumber).val(), 10);
	console.log("장바구니 상품 수량 표시 변경 전 수량 : [" + beforeCount + "]");

	const nowCount = parseInt($("#count-" + cartProductNumber).val());
	if (cartProductCondition === "downCartProductCount") { // 상품 수량 감소라면
		console.log("장바구니 수량 감소 표시");
		$("#count-" + cartProductNumber).val(nowCount - productCount);
	} else { // 상품 수량 증가라면
		console.log("장바구니 수량 증가 표시");
		$("#count-" + cartProductNumber).val(nowCount + productCount);
	}
}

// 장바구니 상품 개수 증가/감소 기능
const changeCartProductCount = (cartProductNumber, cartProductCondition) => {
	// 장바구니 증감할 개수
	const productCount = 1;

	console.log("개수 변경 장바구니 상품 실행");
	console.log("개수 변경 장바구니 상품 번호 : [" + cartProductNumber + "]");
	console.log("개수 변경 장바구니 상품 조건 : [" + cartProductCondition + "]");

	const nowCount = parseInt($("#count-" + cartProductNumber).val());
	// 상품 수량 개수 감소 버튼 클릭 시
	if (cartProductCondition === "downCartProductCount") {
		if (nowCount - parseInt(productCount) < 1) { // 1보다 작아진다면
			console.log("1보다 작을 수 없음");
			return;
		}
	}

	$.ajax({
		type: "POST", // 방식
		url: changeCartProductCountUrl, // 찾아갈 주소
		data: { // 보낼 값
			cartProductNumber: cartProductNumber,
			cartProductCondition: cartProductCondition,
			productCount: productCount
		},
		dataType: "text", // 받을 타입
		success: (response) => { // 성공적이라면
			if (response === "true") { // 상품 개수 변경 성공 시
				console.log("장바구니 상품 개수 변경 성공");

				// 장바구니 상품 수량 표시 변경 함수 호출
				changeInputCartProductCount(cartProductNumber, cartProductCondition, productCount);

				// 장바구니 각 상품 구매 가격 변경 함수 호출
				calculationPrice(cartProductNumber);

				console.log("증감 상품 장바구니 선택 여부 : [" +
					$("#productCheckBox-" + cartProductNumber).prop("checked") + "]");
				if ($("#productCheckBox-" + cartProductNumber).prop("checked")) { // 선택된 상품이었다면
					// 장바구니 상품 가격 총합 계산 함수 호출
					calculationTotalAmount();
				}
			}
			else { // 상품 개수 변경 실패 시
				console.log("장바구니 상품 개수 변경 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "장바구니 개수 변경에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 장바구니 상품 삭제 기능
const deleteCartProduct = () => {
	console.log("장바구니 상품 삭제 실행");

	let cartProductNumberDatas = ""; // 선택된 상품 번호들을 저장할 문자열

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

	if (cartProductNumberDatas.length === 0) { // 선택한 삭제상품이 없다면
		console.log("선택한 삭제상품 없음");
		// 스위트 알럿창 출력
		printSweetAlert("info", "선택하신 삭제상품이 없습니다.");
		return;
	}

	$.ajax({
		type: "POST", // 방식
		url: deleteCartProductUrl, // 찾아갈 주소
		data: { cartProductNumberDatas: cartProductNumberDatas }, // 보낼 값
		dataType: "text", // 받을 타입
		success: (response) => { // 성공적이라면
			if (response === "true") { // 상품 삭제 성공 시
				console.log("장바구니 상품 삭제 성공");

				// "+"를 기준으로 모든 값을 배열로 분리
				let deleteParts = cartProductNumberDatas.split("+");
				console.log("총 개수:", deleteParts.length); // 몇 개인지 확인

				// 배열 반복문으로 행 삭제 처리
				deleteParts.forEach((deletePart) => {
					// 해당 상품 행 삭제
					$("#cartRow-" + deletePart).remove();
				});

				// 장바구니 상품 가격 총합 계산 함수 호출
				calculationTotalAmount();

				if ($('.productCheckBox').length == 0) { // 장바구니에 상품이 없다면
					$("table.table tbody").prepend(`
						<tr>
							<td>장바구니에 담긴 상품이 없습니다.</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					`);
				}

				// 스위트 알럿창 출력
				printSweetAlert("success", "선택하신 상품이 삭제됐습니다.");
			}
			else { // 상품 삭제 실패 시
				console.log("상품 삭제 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "장바구니 상품 삭제에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
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