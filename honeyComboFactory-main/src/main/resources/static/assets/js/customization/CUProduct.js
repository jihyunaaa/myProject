/**
 *  CU 상품 js
 */
const insertCartUrl = "/member/cart/add"; // 장바구니 상품 담기 서블릿 url
const loadMoreHotUrl = "/product/hotIssue"; // 핫이슈 불러오기 서블릿 url
const loadMorePlusUrl = "/product/plusOne"; // +1증정 불러오기 서블릿 url
const loadMoreCUUrl = "/product/order"; // CU상품 불러오기 서블릿 url
const searchCUProductNameUrl = "/product/search"; // CU상품 이름 검색 서블릿 url
// 전역 객체 선언
const productState = {
	pageGroupSize: 5, // 한 번에 보여줄 페이지네이션 개수
	pageNumber: {
		hotIssue: 1,
		plus: 1,
		CU: 1
	},
	contentCount: {
		hotIssue: 3,
		plus: 3,
		CU: 6
	},
	pageNation: {
		hotIssue: "hotPageNation",
		plus: "plusPageNation",
		CU: "CUPageNation"
	},
	// CU 전용
	orderCondition: "ORDERPOPULAR", // 상품 정렬 조건
	category: "ALLPRODUCT", // 상품 카테고리
	searchKeyword: "", // 검색 상품이름
	searchKeywordPageFlag: true // 검색 상품 페이지 1로 설정 여부
};

// 상품 카테고리 변경 기능
const setCUProductCategory = (category) => {
	console.log("상품 카테고리 변경 : [" + category + "]");

	// 상품 검색한 상황 혹은 카테고리 변동이 없다면
	if (productState.category === category) {
		console.log("상품 검색한 상황 혹은 카테고리 변동이 없음");
		return;
	}

	// 검색어 비우기
	$("#searchKeyword").val("");
	// 검색어 정보 비우기
	productState.searchKeyword = "";
	// 페이지 번호 초기화
	productState.pageNumber["CU"] = 1;
	// 카테고리 설정
	productState.category = category;

	// CU 상품 불러오기 함수 호출
	loadDatas("CU");
}

// CU상품 정렬 기능
const setCUProductOrderCondition = (orderCondition) => {
	console.log("요청받은 상품 정렬 조건 : [" + orderCondition + "]");

	if (productState.orderCondition === orderCondition) {
		console.log("변동 없으므로 중단");
		return;
	}

	// 정렬 조건 변경
	productState.orderCondition = orderCondition;
	// 검색어 변경
	const nowSearchKeyword = $("#searchKeyword").val();
	productState.searchKeyword = nowSearchKeyword;
	// 페이지 번호 초기화
	productState.pageNumber["CU"] = 1;

	console.log("정렬 시 현재 검색된 CU 상품 정렬 조건 : [" + productState.orderCondition + "]");
	console.log("정렬 시 현재 검색된 검색어 : [" + productState.searchKeyword + "]");

	if (!productState.searchKeyword) { // 검색된 상품 이름이 없다면
		// CU상품 불러오기 함수 호출
		loadDatas("CU");
	}
	else { // 검색된 CU상품 이름이 있다면
		// 첫 페이지 설정 필요
		productState.searchKeywordPageFlag = true;
		// CU상품 이름 검색 함수 호출
		searchCUProductName();
	}
}

// CU상품 이름 검색 기능
const searchCUProductName = () => {
	console.log("CU상품 이름 검색 실행");
	productState.searchKeyword = $("#searchKeyword").val();
	console.log("검색한 CU상품 이름 : [" + productState.searchKeyword + "]");

	if (!productState.searchKeyword) { // 빈 내용을 검색한다면
		// CU상품 불러오기 함수 호출
		loadDatas("CU");
		// 첫 페이지 설정 필요
		productState.searchKeywordPageFlag = true;
	}

	if (productState.searchKeywordPageFlag) { // 처음 검색이라면
		// 페이지 번호 초기화
		productState.pageNumber["CU"] = 1;
		// 첫 페이지 설정 불필요
		productState.searchKeywordPageFlag = false;
	}

	console.log("검색할 조건 : [" + productState.orderCondition + "]");

	$.ajax({ // 비동기
		url: searchCUProductNameUrl, // 보낼 주소
		type: 'GET', // 방식
		data: { // 보낼 값
			searchKeyword: productState.searchKeyword,
			CUProductPageNumber: productState.pageNumber["CU"],
			CUProductContentCount: productState.contentCount["CU"],
			CUProductOrderCondition: productState.orderCondition
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 검색 CU상품 데이터 : [" + response + "]"); // 로그 찍기
			// 카테고리는 전부로 설정
			productState.category = "ALLPRODUCT";

			// CU 상품 생성 함수 호출
			insert("CU", response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 상품 페이지네이션 페이지 이동 기능
const changePage = (type, element) => {
	console.log("상품 페이지네이션 클릭 타입 : [" + type + "]");
	console.log("상품 페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("상품 페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === "Previous") { // "<" 버튼 클릭 시
		productState.pageNumber[type]--;
	} else if (element.attr('id') === "Next") { // ">" 버튼 클릭 시
		productState.pageNumber[type]++;
	} else { // 페이지 번호 클릭 시
		productState.pageNumber[type] = element.data('page');
	}

	if (productState.searchKeyword) { // 검색어가 있다면
		// CU상품 이름 검색 함수 호출
		searchCUProductName();
	}
	else { // 검색어가 없다면
		// 상품 불러오기 함수 호출
		loadDatas(type);
	}
}

// 상품 불러오기 기능
const loadDatas = (type) => {
	console.log("불러올 상품 타입 : [" + type + "]");

	if (type === "hotIssue") { // 핫이슈 상품이라면
		$.ajax({ // 비동기
			url: loadMoreHotUrl, // 보낼 주소
			type: 'GET', // 방식
			data: { // 보낼 값
				hotCUProductPageNumber: productState.pageNumber[type],
				hotCUProductContentCount: productState.contentCount[type]
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 핫이슈 상품 데이터 : [" + response + "]"); // 로그 찍기

				// 핫이슈 상품 생성 함수 호출
				insert(type, response);
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
	}
	else if (type === "plus") { // +1 증정 상품이라면
		$.ajax({ // 비동기
			url: loadMorePlusUrl, // 보낼 주소
			type: 'GET', // 방식
			data: { // 보낼 값
				plusCUProductPageNumber: productState.pageNumber[type],
				plusCUProductContentCount: productState.contentCount[type]
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 +1증정 상품 데이터 : [" + response + "]"); // 로그 찍기

				// +1증정 상품 생성 함수 호출
				insert("plus", response);
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
	}
	else if (type === "CU") { // CU 상품이라면
		$.ajax({ // 비동기
			url: loadMoreCUUrl, // 보낼 주소
			type: 'GET', // 방식
			data: { // 보낼 값
				CUProductPageNumber: productState.pageNumber[type],
				CUProductContentCount: productState.contentCount[type],
				CUProductOrderCondition: productState.orderCondition,
				CUProductCategory: productState.category
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 CU 상품 데이터 : [" + response + "]"); // 로그 찍기

				// CU 상품 생성 함수 호출
				insert(type, response);
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
	}
}

// 반복하며 상품 생성 기능
const insert = (type, response) => {
	console.log("받은 상품 생성 타입 : [" + type + "]");
	console.log("받은 상품 생성 정보 : [" + response + "]");

	const wrapper = $("#" + type + "Wrapper");

	// 상품 초기화
	wrapper.empty();

	if (response.length === 0) { // 응답이 비어있다면
		console.log(type + " 상품 존재하지 않음");

		// 화면에 상품 준비중 생성
		wrapper.append(`
			<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
				<div class="single-popular-items mb-50 text-center"></div>
			</div>
			<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
				<div class="single-popular-items mb-50 text-center"><h1>상품 준비중입니다.</h1></div>
			</div>
			<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
				<div class="single-popular-items mb-50 text-center"></div>
			</div>
		`);

		// 상품 페이지네이션 생성 함수 호출
		makePageNation(type, 1);
		return;
	}

	// 반복하며 화면에 상품 생성
	response.forEach(productData => {
		console.log("화면 생성할 상품 이름 : [" + productData.productSingleName + "]");
		let content = "";
		content += `
			<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
				<div class="single-popular-items mb-50 text-center">
					<div class="popular-img">
						<img src="`+ productData.productSingleImage + `"
							alt="`+ productData.productSingleName + ` 상품 이미지">
						<div class="img-cap"
						data-number="`+ productData.productSingleNumber + `"
						data-combo="false" data-stock="`+ productData.productSingleStock + `">
							<span>장바구니 담기</span>
						</div>
					</div>
					<div class="popular-caption">
						<h3>
							<a
								href="productDetail.do?productSingleNumber=`+ productData.productSingleNumber + `">` + productData.productSingleName + `</a>
						</h3>
						<span>`+ productData.productSinglePrice.toLocaleString() + `원</span>
		`;

		// 할인 상품이라면
		if (0 < productData.productSingleDiscount) {
			content += `
				<span style="color:red;">-`+ productData.productSingleDiscount + `%</span>
				<span style="color:blue;">`+ productData.productSingleDiscountedPrice.toLocaleString() + `원</span>
			`
		}

		content += `</div></div></div>`;

		// content내용 화면 생성
		wrapper.append(content);
	});

	// 페이지네이션 생성 함수 호출
	makePageNation(type, response[0].totalCountNumber);
}

// 페이지네이션 생성 기능
function makePageNation(type, totalNumber) {
	console.log("페이지네이션 생성 타입 : [" + type + "]");
	console.log("총 데이터 수 : [" + totalNumber + "]");

	// 총 페이지 수
	const totalPageNumber = Math.ceil(totalNumber / productState.contentCount[type]);
	console.log("총 페이지 수 : [" + totalPageNumber + "]");

	// 현재 그룹
	let group = Math.ceil(productState.pageNumber[type] / productState.pageGroupSize);
	// 그룹의 처음 페이지 수
	let startPage = (group - 1) * productState.pageGroupSize + 1;
	// 그룹의 마지막 페이지 수
	let endPage = Math.min(group * productState.pageGroupSize, totalPageNumber);

	// 1 혹은 마지막 페이지면 클래스 추가
	let prevClass = productState.pageNumber[type] <= 1 ? 'disabled-link' : '';
	let nextClass = productState.pageNumber[type] >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + productState.pageNumber[type] + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 작성할 내용 초기화
	let content = "";

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link `+ type + ` ` + prevClass + `" href="javascript:void(0);" aria-label="Previous" id="Previous">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;
	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = productState.pageNumber[type] == i ? 'active' : '';
		content += `
	    	<li class="page-item `+ activeClass + `">
	    		<a class="page-link `+ type + `" href="javascript:void(0);" data-page=` + i + `>` + i + `</a>
	    	</li>
	    `;
	}
	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link `+ type + ` ` + nextClass + ` href="javascript:void(0);" aria-label="Next" id="Next" >
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;
	// 페이지네이션 비우고 새로 생성
	$("#" + productState.pageNation[type]).empty().append(content);
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

// 초기 기능 실행 기능
const runPostLoginSetup = () => {
	// 상품 불러오기 함수 호출
	loadDatas("hotIssue");
	loadDatas("plus");
	loadDatas("CU");
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 검색 엔터키 이벤트 위임
	$("#searchKeyword").on("keydown", function(event) {
		if (event.key === "Enter") { // Enter 키 감지
			searchCUProductName(); // 검색 함수 실행
		}
	});

	// 장바구니 담기 기능 이벤트 위임
	$(document).on("click", ".img-cap", function() {
		console.log("장바구니 담기 실행");
		const productNumber = $(this).data("number");
		const isComboProduct = $(this).data("combo");
		const cartProductCount = 1;
		const productStock = $(this).data("stock");
		insertCart(productNumber, cartProductCount, isComboProduct, productStock);
	});

	// 핫이슈 상품 페이지네이션 이벤트 등록
	$(document).on("click", ".hotIssue", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("hotIssue", $(this)); // 페이지 이동
	});

	// +1증정 상품 페이지네이션 이벤트 등록
	$(document).on("click", ".plus", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("plus", $(this)); // 페이지 이동
	});

	// CU 상품 페이지네이션 이벤트 등록
	$(document).on("click", ".CU", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("CU", $(this)); // 페이지 이동
	});
}

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");

	// 초기 이벤트 리스너 등록 함수 호출
	initEventListeners();
	// 초기 기능 실행 함수 호출
	runPostLoginSetup();
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});