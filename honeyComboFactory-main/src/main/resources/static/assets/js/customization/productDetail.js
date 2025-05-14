/**
 *  상품 상세 js
 */
const loadMoreReviewUrl = "/review/load"; // 리뷰 불러오기 서블릿 url
const insertCartUrl = "/member/cart/add"; // 장바구니 상품 담기 서블릿 url
const insertReviewUrl = "/review/insert"; // 리뷰 등록 서블릿 url
const updateReviewUrl = "/review/update"; // 리뷰 수정 서블릿 url
const deleteReviewUrl = "/review/delete"; // 리뷰 삭제 서블릿 url
let reviewPageNumber = 1; // 초기 페이지는 1로 설정
const reviewContentCount = 5; // 한 번에 가져올 데이터 개수
const productNumber = document.body.dataset.productNumber; // jsp 파일의 상품 번호 가져오기
let firstLoadReview = false; // 처음 리뷰 불러오기 여부

// 리뷰 불러오기 기능
const loadMoreReview = () => {
	console.log("불러올 리뷰 페이지 수 : [" + reviewPageNumber + "]");
	console.log("불러올 상품 번호 : [" + productNumber + "]");

	if (firstLoadReview) { // 처음 실행이 아니라면
		// 리뷰 페이지 1 증가
		reviewPageNumber++;
	}

	// 처음 실행 여부 값 변경
	firstLoadReview = true;

	$.ajax({ // 비동기
		url: loadMoreReviewUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			productNumber: productNumber,
			reviewPageNumber: reviewPageNumber,
			reviewContentCount: reviewContentCount
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 리뷰 정보 : [" + response + "]"); // 로그 찍기

			// 리뷰 생성 함수 호출
			createReview(response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 리뷰 생성 기능
const createReview = (response) => {
	console.log("리뷰 글 생성");
	if (!response.reviewDatas || response.reviewDatas.length === 0) { // 리뷰 정보가 비어있다면
		console.log("리뷰 없음");

		if ($("#reviewWrapper").is(':empty')) { // 화면에 리뷰가 없다면
			// 총 리뷰 수 0으로 설정
			$("#totalReviewCount").text(0);
			// 리뷰 없음 글 설정
			makeNoReviewText();
		}
		return;
	}

	// 총 리뷰 수 설정
	console.log("총 리뷰 수 : [" + response.reviewDatas[0].totalCountNumber + "]");
	$("#totalReviewCount").text(response.reviewDatas[0].totalCountNumber);

	// 반복하며 리뷰 글 화면에 생성
	response.reviewDatas.forEach(reviewData => {
		// 리뷰 작성 내용
		let content = "";

		// 탈퇴한 회원 여부에 따라 이름 표시 변경
		const memberName = reviewData.memberIsWithdraw ? "탈퇴한 회원" : reviewData.memberName;

		content = `
			<div id="review-`+ reviewData.reviewNumber + `" class="comment-list">
				<div class="single-comment justify-content-between d-flex">
					<div class="user justify-content-between d-flex">
						<div class="thumb">
							<img src="assets/img/logo/mainLogo.jpg" alt="로고 이미지">
						</div>
						<div class="desc">
							<p class="comment"><input id="reviewComment-`+ reviewData.reviewNumber + `" class="readonlyReview" type="text" value="` + reviewData.reviewContent + `" readonly></p>
							<div class="d-flex justify-content-between">
								<div class="d-flex align-items-center">
									<p id="rating-reply-`+ reviewData.reviewNumber + `"></p>
									<p class="date">
										<span>`+ memberName + `</span>&nbsp;&nbsp;&nbsp;` + reviewData.reviewRegisterDate + `
									</p>
								</div>
		`;

		if (reviewData.memberNumber === Number(loginedMemberNumber)) { // 로그인 회원과 리뷰 작성자가 같다면
			content += `
			<div class="reply-btn">
				&nbsp;
				<button class="genric-btn warning radius" id="updateReviewBtn-`+ reviewData.reviewNumber + `">수정</button>
				<button class="genric-btn warning radius" id="deleteReviewBtn-`+ reviewData.reviewNumber + `">삭제</button>
			</div>
			`;
		}

		content += `
							</div>
						</div>
					</div>
				</div>
			</div>
		`;

		// 리뷰 추가
		$("#reviewWrapper").prepend(content);
	});

	// 리뷰 없음 글 설정
	makeNoReviewText();
	// 리뷰 별점 생성 함수 호출
	createRating(response);
}

// 리뷰 별점 생성 기능
const createRating = (response) => {
	console.log("리뷰 별점 생성");
	response.reviewDatas.forEach(reviewData => {
		$('#rating-reply-' + reviewData.reviewNumber).starRating({
			initialRating: reviewData.reviewScore, // JSON 데이터에서 받은 값 사용
			strokeColor: '#894A00',
			strokeWidth: 10,
			starSize: 25,
			useFullStars: true, // 별 하나 단위
			readOnly: true // 값 고정
		});
	});
}

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
				printSweetAlert("error", "선택하신 상품을 장바구니에 담기 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
};

// 리뷰 등록 기능
const insertReview = () => {
	if (!loginedMemberNumber) { // 비회원이라면
		// 스위트 알럿창 출력
		printSweetAlert("warning", "로그인이 필요한 기능입니다.");
		return;
	}

	console.log("리뷰 등록");
	let reviewContent = $("#comment").val().trim();
	console.log("리뷰 등록할 내용 : [" + reviewContent + "]");
	let reviewScore = parseInt($("#my-rating").starRating('getRating'));
	console.log("리뷰 등록할 별점 수 : [" + reviewScore + "]");

	// 별점, 내용 둘 중 하나라도 미입력 시
	if (!reviewContent || reviewScore == 0) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "별점과 내용을 모두 입력해주세요.");
		return;
	}

	$.ajax({
		type: "POST", // 방식
		url: insertReviewUrl, // 찾아갈 주소
		data: { // 보낼 값
			productNumber: productNumber,
			reviewScore: reviewScore,
			reviewContent: reviewContent
		},
		dataType: "json", // 받을 타입
		success: (response) => { // 성공 시 처리
			console.log("받은 리뷰등록 데이터 : [" + response.reviewData + "]");
			if (response.reviewData === 0) { // 중복 등록 시
				console.log("리뷰 중복 등록 시도");
				// 스위트 알럿창 출력
				printSweetAlert("info", "이미 작성된 리뷰가 있습니다.");
			} else if (response.reviewData === 1) { // 등록 실패 시
				console.log("리뷰 등록 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "리뷰 등록이 실패하였습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			} else { // 등록 성공 시
				console.log("리뷰 등록 성공");
				// 리뷰 최상단에 추가 함수 호출
				prependReview(response);
				// 총 리뷰 수 +1으로 설정
				$("#totalReviewCount").text(Number($("#totalReviewCount").text()) + 1);
				// 리뷰 없음 글 생성 여부
				makeNoReviewText();
				// 스위트 알럿창 출력
				printSweetAlert("success", "리뷰가 등록되었습니다.");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 리뷰 최상단 추가 기능
const prependReview = (response) => {
	console.log("최상단 추가할 내용 : [" + response + "]");

	$("#reviewWrapper").prepend(`
		<div id="review-`+ response.reviewNumber + `" class="comment-list">
			<div class="single-comment justify-content-between d-flex">
				<div class="user justify-content-between d-flex">
					<div class="thumb">
						<img src="assets/img/logo/mainLogo.jpg" alt="로고 이미지">
					</div>
					<div class="desc">
						<p class="comment"><input id="reviewComment-`+ response.reviewNumber + `" type="text" class="readonlyReview" value="` + response.reviewContent + `" readonly></p>
						<div class="d-flex justify-content-between">
							<div class="d-flex align-items-center">
								<p id="rating-reply-`+ response.reviewNumber + `"></p>
								<p class="date">
									<span>`+ response.memberName + `</span>&nbsp;&nbsp;&nbsp;` + response.reviewRegisterDate + `
								</p>
							</div>
							<div class="reply-btn">
								&nbsp;
								<button class="genric-btn warning radius" id="updateReviewBtn-`+ response.reviewNumber + `">수정</button>
								<button class="genric-btn warning radius" id="deleteReviewBtn-`+ response.reviewNumber + `">삭제</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	`);

	// 별점 박스 완전 교체 (초기화 꼼수)
	$("#rating-reply-" + response.reviewNumber).replaceWith('<div id="rating-reply-' + response.reviewNumber + '"></div>');

	// 이제 새로 생성된 div에 별점 플러그인 적용
	$("#rating-reply-" + response.reviewNumber).starRating({
		strokeColor: '#894A00',
		strokeWidth: 10,
		starSize: 25,
		useFullStars: true,
		initialRating: response.reviewScore,
		readOnly: false,
		disableAfterRate: false
	});
}

// 리뷰 삭제 기능
const deleteReview = (reviewNumber) => {
	console.log("삭제할 리뷰 번호 : " + reviewNumber + "]");

	Swal.fire({
		title: "정말로 삭제하시겠습니까?",
		text: "삭제된 리뷰는 복구하실 수 없습니다.",
		showCancelButton: true, // 취소 버튼 표시
		confirmButtonColor: "#d33", // 붉은색 강조
	}).then((result) => {
		if (result.isConfirmed) { // 예 클릭 시
			console.log("리뷰 삭제 확인");

			$.ajax({
				type: "POST", // 방식
				url: deleteReviewUrl, // 찾아갈 주소
				data: { reviewNumber: reviewNumber }, // 보낼 값
				dataType: "text", // 받을 타입
				success: (response) => { // 성공 시 처리
					if (response === "true") { // 삭제 완료 시
						console.log("리뷰 삭제 성공");
						// 해당 리뷰 삭제
						$("#review-" + reviewNumber).remove();
						// 총 리뷰 수 -1으로 설정
						$("#totalReviewCount").text(Number($("#totalReviewCount").text()) - 1);
						// 리뷰 없음 글 설정
						makeNoReviewText();
						// 스위트 알럿창 출력
						printSweetAlert("success", "리뷰가 삭제됐습니다.");
					} else { // 삭제 실패 시
						console.log("리뷰 삭제 실패");
						// 스위트 알럿창 출력
						printSweetAlert("error", "리뷰 삭제에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
					}
				},
				error: (xhr, status, error) => { // 에러 처리
					console.error("AJAX 요청 에러 발생", xhr.status, status, error);
					// 스위트 알럿창 출력
					printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
				}
			});
		} else {
			console.log("리뷰 삭제 취소");
		}
	});
}

// 리뷰 존재 여부 안내 문구 기능
const makeNoReviewText = () => {
	console.log("리뷰 존재 여부 확인");
	if ($("#reviewWrapper").children().length === 0) {
		console.log("리뷰가 화면에 없음");
		$("#noReview").show();
	}
	else {
		console.log("리뷰가 화면에 있음");
		$("#noReview").hide();
	}
}

// 리뷰 수정 기능
const updateReview = (reviewNumber) => {
	const updateReviewBtn = $("#updateReviewBtn-" + reviewNumber);

	if (updateReviewBtn.text() === "수정") { // 수정 버튼 클릭 시
		console.log("수정할 리뷰 번호 : " + reviewNumber + "]");
		//버튼 내용 완료로 변경
		updateReviewBtn.text("완료");
		console.log($("#reviewComment-" + reviewNumber)); // 선택된 요소 확인
		// 수정할 리뷰의 readonly 속성 제거
		$("#reviewComment-" + reviewNumber).prop("readonly", false);
		// 읽기 전용 css 제거
		$("#reviewComment-" + reviewNumber).removeClass("readonlyReview");
		// 기존 별점 수 대피
		const initialStar = $("#rating-reply-" + reviewNumber).starRating('getRating');
		console.log("기존 별점 수 : [" + initialStar + "]");
		// 별점 박스 완전 교체
		$("#rating-reply-" + reviewNumber).replaceWith('<p id="rating-reply-' + reviewNumber + '"></p>');

		// 이제 새로 생성된 div에 별점 플러그인 적용
		$("#rating-reply-" + reviewNumber).starRating({
			strokeColor: '#894A00',
			strokeWidth: 10,
			starSize: 25,
			useFullStars: true,
			initialRating: initialStar,
			readOnly: false,
			disableAfterRate: false
		});
		return;
	}

	reviewScore = parseInt($('#rating-reply-' + reviewNumber).starRating('getRating'));
	reviewContent = $("#reviewComment-" + reviewNumber).val();
	console.log("수정 완료할 리뷰 번호 : " + reviewNumber + "]");
	console.log("수정할 리뷰 별점 수 : " + reviewScore + "]");
	console.log("수정할 리뷰 내용 : " + reviewContent + "]");

	$.ajax({
		type: "POST", // 방식
		url: updateReviewUrl, // 찾아갈 주소
		data: { // 보낼 값
			reviewNumber: reviewNumber,
			reviewScore: reviewScore,
			reviewContent: reviewContent,
		},
		dataType: "text", // 받을 타입
		success: (response) => { // 성공 시 처리
			if (response === "true") { // 삭제 완료 시
				console.log("리뷰 수정 성공");
				// 스위트 알럿창 출력
				printSweetAlert("success", "리뷰가 수정됐습니다.");
				//버튼 내용 수정으로 변경
				updateReviewBtn.text("수정");
				// 수정한 리뷰 내용 readonly 속성 설정
				$("#reviewComment-" + reviewNumber).prop("readonly", true);
				// 수정한 리뷰의 별점 고정 설정
				$("#reviewComment-" + reviewNumber).addClass("readonlyReview");

				// 별점 박스 완전 교체
				$("#rating-reply-" + reviewNumber).replaceWith('<p id="rating-reply-' + reviewNumber + '"></p>');
				// 이제 새로 생성된 p에 별점 플러그인 적용
				$("#rating-reply-" + reviewNumber).starRating({
					strokeColor: '#894A00',
					strokeWidth: 10,
					starSize: 25,
					useFullStars: true,
					initialRating: reviewScore,
					readOnly: true,
					disableAfterRate: true
				});
			} else { // 삭제 실패 시
				console.log("리뷰 수정 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "리뷰 수정에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 초기 기능 실행 기능
const runPostLoginSetup = () => {
	// 리뷰 불러오기 함수 호출
	loadMoreReview();
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 이벤트 위임: 리뷰 수정 버튼 클릭 처리
	$(document).on('click', '[id^="updateReviewBtn-"]', function() {
		const reviewNumber = $(this).attr('id').split('-')[1];
		updateReview(reviewNumber);  // 해당 리뷰 번호로 수정 기능 실행
	});

	// 이벤트 위임: 리뷰 삭제 버튼 클릭 처리
	$(document).on('click', '[id^="deleteReviewBtn-"]', function() {
		const reviewNumber = $(this).attr('id').split('-')[1];
		deleteReview(reviewNumber);  // 해당 리뷰 번호로 삭제 기능 실행
	});

	// 이벤트 위임: 리뷰 더보기 버튼 클릭 처리
	$(document).on('click', '#loadMoreReviewBtn', function() {
		loadMoreReview();
	});

	// 이벤트 위임: 장바구니 담기 버튼 클릭 처리
	$(document).on("click", ".insertCartBtn", function() {
		console.log("장바구니 담기 실행");
		const productNumber = $(this).data("number");
		const isComboProduct = $(this).data("combo");
		const cartProductCount = $("#cartProductCount").val();
		const productStock = $(this).data("stock");

		insertCart(productNumber, cartProductCount, isComboProduct, productStock);
	});
}

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) { // 비 로그인 상태라면
		$("#my-rating").starRating({ // 별점 고정
			initialRating: 5.0,
			readOnly: true, // 값 고정
		});
	}
	else { // 로그인 상태라면
		// 리뷰 내용 입력 disabled 속성 제거
		$("#comment").prop("disabled", false);

		$("#my-rating").starRating({ // 별점 고정 풀기
			initialRating: 5.0,
			disableAfterRate: false,
			useFullStars: true, // 별 하나 단위
			readOnly: false, // 값 고정
		});

		// 안내글 바꾸기
		$("#comment").attr("placeholder", "리뷰 내용을 입력하세요");
	}

	// 초기 이벤트 리스너 등록 함수 호출
	initEventListeners();
	// 초기 기능 실행 함수 호출
	runPostLoginSetup();
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});