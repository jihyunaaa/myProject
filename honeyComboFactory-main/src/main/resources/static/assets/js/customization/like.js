/**
 *  좋아요 js
 */
const checkLikedUrl = "/boardComboLiked/isLiked"; // 좋아요 여부 받아오기 서블릿 url
const clickLikedUrl = "/boardComboLiked/like"; // 좋아요 여부 변경 서블릿 url
const updateBoardUrl = "/boardCombo/insert"; // 게시글 등록 서블릿 url
const boardComboNumber = document.body.dataset.boardNumber; // jsp 파일의 게시글 번호 가져오기
let boardLikedCount = document.body.dataset.boardLikedCount; // jsp 파일의 게시글 좋아요 수 가져오기
let isLiked = false; // 게시글 좋아요 여부

// 좋아요 여부 받아오기 기능
const checkLiked = () => {
	console.log("좋아요 여부 받아올 게시글 번호 : [" + boardComboNumber + "]");

	if (!loginedMemberNumber) { // 비회원이라면
		return;
	}

	$.ajax({ // 비동기
		url: checkLikedUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { boardComboNumber: boardComboNumber }, // 보낼 값
		dataType: 'text', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 글 좋아요 여부 정보 : [" + response + "]"); // 로그 찍기
			if (response === "true") {
				isLiked = true;
			}
			// 좋아요 버튼 생성 함수 호출
			createLikedBtn();
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 좋아요 버튼 생성 기능
const createLikedBtn = () => {
	console.log("좋아요 버튼 생성 시 좋아요 여부 : [" + isLiked + "]");
	console.log("좋아요 버튼 생성 시 좋아요 수 : [" + boardLikedCount + "]");
	// 좋아요 버튼 내용 비우기
	$("#likedBtnWrapper").empty();
	let contentText = "";

	if (isLiked) { // 좋아요 상태라면
		contentText = "좋아요 취소";
	}
	else { // 좋아요 상태가 아니라면
		contentText = `좋아요`;
	}

	// 화면에 생성
	$("#likedBtnWrapper").append(`
		<a href="javascript:void(0);" 
		id="likeBtn" class="genric-btn primary-border e-large danger-border">
			<i class="fa fa-heart" aria-hidden="true"></i>
			<span>
			`+ contentText + `&nbsp;` + boardLikedCount + `
			</span>
		</a>
	`);
}

// 좋아요 여부 변경 기능
const clickLiked = () => {
	let likedCondition = "";
	console.log("좋아요 버튼 클릭");
	console.log("좋아요 버튼에 요청받은 글 번호 : [" + boardComboNumber + "]");
	console.log("좋아요 버튼에 요청받은 회원 번호 : [" + loginedMemberNumber + "]");
	console.log("좋아요 버튼에 요청받은 좋아요 여부 : [" + isLiked + "]");

	if (!loginedMemberNumber) { // 비회원이라면
		// 스위트 알럿창 출력
		printSweetAlert("warning", "로그인이 필요한 기능입니다.");
		return;
	}

	if (isLiked) { // 좋아요 글이라면
		likedCondition = "DELETELIKED";
	}
	else { // 좋아요 글이 아니라면
		likedCondition = "INSERTLIKED";
	}
	console.log("좋아요 등록/취소 조건 : [" + likedCondition + "]");

	$.ajax({ // 비동기
		url: clickLikedUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			boardComboNumber: boardComboNumber,
			likedCondition: likedCondition
		},
		dataType: 'text', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 좋아요 여부 : [" + response + "]"); // 로그 찍기

			if (response === "false") { // 좋아요 여부 변경 실패라면
				console.log("좋아요 여부 변경 실패");
				// 스위트 알럿창 출력
				printSweetAlert("error", "좋아요 변경이 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
				return;
			}

			isLiked = !isLiked; // 회원 좋아요 여부 값 변경
			boardLikedCount = response // 글 좋아요 수 값 변경
			// 좋아요 버튼 생성 함수 호출
			createLikedBtn();
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
	// 좋아요 여부 불러오기
	checkLiked();
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 좋아요 버튼 클릭 시
	$(document).on("click", "#likeBtn", function(event) {
		event.preventDefault(); // 기본 이벤트 방지

		clickLiked(); // 좋아요 여부 변경 함수 호출
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