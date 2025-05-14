/*
 *  꿀조합 게시판 js
 */
const loadMoreComboBoardUrl = "/boardCombo/order"; // 글 정보 불러오기 서블릿 url
const searchComboBoardTitleUrl = "/boardCombo/search"; // 글 제목 검색 서블릿 url
let boardOrderCondition = "ORDERUPTODATE"; // 게시판 글 정렬 조건
let boardPageNumber = 1; // 초기 페이지는 1로 설정
const boardContentCount = 5; // 한 번에 가져올 데이터 개수
const pageGroupSize = 5; // 한 번에 보여줄 페이지네이션 개수
let searchKeyword = ""; // 검색 제목
let searchKeywordPageFlag = true; // 검색 상품 페이지 1로 설정 여부

// 페이지네이션 페이지 이동 기능
const changePage = (element) => {
	console.log("페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === 'Previous') { // "<" 버튼 클릭 시
		boardPageNumber--;
	} else if (element.attr('id') === 'Next') { // ">" 버튼 클릭 시
		boardPageNumber++;
	} else { // 페이지 번호 클릭 시
		boardPageNumber = element.data('page');
	}

	if (searchKeyword) { // 검색어가 있다면
		// 꿀조합 게시글 제목 검색 함수 호출
		loadComboDatas();
	}
	else { // 검색어가 없다면
		// 회원 게시글 불러오기 함수 호출
		loadComboBoardDatas();
	}
}

// 회원 게시글 불러오기 기능
const loadComboBoardDatas = () => {
	$.ajax({ // 비동기
		url: loadMoreComboBoardUrl, // 보낼 주소
		type: 'GET', // 방식
		data: { // 보낼 값
			boardPageNumber: boardPageNumber,
			boardContentCount: boardContentCount,
			boardOrderCondition: boardOrderCondition
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 글 정보 데이터 : [" + response + "]"); // 로그 찍기

			// 회원 게시글 생성 함수 호출
			insertComboBoard(response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 페이지네이션 생성 기능
function makePageNation(totalNumber) {
	console.log("총 데이터 수 : [" + totalNumber + "]");
	const totalPageNumber = Math.ceil(totalNumber / boardContentCount); // 총 페이지 수
	const pageNation = $("#pageNation");
	let content = "";
	console.log("총 페이지 수 : [" + totalPageNumber + "]");

	// 페이지네이션 초기화
	pageNation.empty();

	let group = Math.ceil(boardPageNumber / pageGroupSize); // 현재 그룹
	let startPage = (group - 1) * pageGroupSize + 1;
	let endPage = Math.min(group * pageGroupSize, totalPageNumber);

	let prevClass = boardPageNumber <= 1 ? 'disabled-link' : '';
	let nextClass = boardPageNumber >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + boardPageNumber + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link `+ prevClass + `" href="javascript:void(0);" aria-label="Previous" id="Previous">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;

	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = boardPageNumber == i ? 'active' : '';
		content += `
	    	<li class="page-item `+ activeClass + `">
	    		<a class="page-link" href="javascript:void(0);" data-page=` + i + `>` + i + `</a>
	    	</li>
	    `;
	}

	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link `+ nextClass + `" href="javascript:void(0);" aria-label="Next" id="Next" >
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;

	// 페이지네이션 생성
	pageNation.append(content);
}

// 회원 게시글 정렬 기능
const orderComboBoard = (comboBoardOrderCondition) => {
	console.log("요청받은 게시글 정렬 조건 : [" + comboBoardOrderCondition + "]");
	const nowSearchKeyword = $("#searchKeyword").val();
	console.log("정렬 시 현재 검색된 게시글 제목 : [" + nowSearchKeyword + "]");

	if (boardOrderCondition === comboBoardOrderCondition) {
		console.log("정렬조건 및 검색 값 변동 없으므로 중단");
		return;
	}
	// 정렬 조건 변경
	boardOrderCondition = comboBoardOrderCondition;
	// 검색어 변경
	searchKeyword = nowSearchKeyword;
	// 페이지 번호 초기화
	boardPageNumber = 1;

	console.log("정렬 시 현재 검색된 게시글 정렬 조건 : [" + boardOrderCondition + "]");
	console.log("정렬 시 현재 검색된 검색어 : [" + searchKeyword + "]");

	if (!searchKeyword) { // 검색된 게시글 제목이 없다면
		// 회원 게시글 불러오기 함수 호출
		loadComboBoardDatas();
	}
	else { // 검색된 게시글 제목이 있다면
		// 첫 페이지 설정 필요
		searchKeywordPageFlag = true;
		// 게시글 제목 검색 함수 호출
		searchComboBoardTitle();
	}
}

// 게시글 제목 검색 기능
const searchComboBoardTitle = () => {
	console.log("게시글 제목 검색 실행");
	searchKeyword = $("#searchKeyword").val();
	console.log("검색한 게시글 제목 : [" + searchKeyword + "]");

	if (!searchKeyword) { // 빈 내용을 검색한다면
		// 회원 게시글 불러오기 함수 호출
		loadComboBoardDatas();
		// 첫 페이지 설정 필요
		searchKeywordPageFlag = true;
	}

	if (searchKeywordPageFlag) { // 처음 검색이라면
		// 페이지 번호 초기화
		boardPageNumber = 1;
		// 첫 페이지 설정 불필요
		searchKeywordPageFlag = false;
	}

	console.log("검색할 게시글 페이지 번호 : [" + boardPageNumber + "]");
	console.log("검색할 게시글 정렬 조건 : [" + boardOrderCondition + "]");

	$.ajax({ // 비동기
		url: searchComboBoardTitleUrl, // 보낼 주소
		type: 'GET', // 방식
		data: { // 보낼 값
			searchKeyword: searchKeyword,
			boardPageNumber: boardPageNumber,
			boardContentCount: boardContentCount,
			boardOrderCondition: boardOrderCondition
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 검색 글 정보 데이터 : [" + response + "]"); // 로그 찍기
			// 회원 게시글 생성 함수 호출
			insertComboBoard(response);
		},
		error: (xhr, status, error) => { // 실패라면
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 반복하며 회원 게시글 생성 기능
const insertComboBoard = (response) => {
	console.log("받은 회원 게시글 생성 정보 : [" + response + "]");
	const clientBoardWrapper = $("#boardTable tfoot");

	clientBoardWrapper.empty(); // 게시글 초기화

	if (response.boardComboDatas.length === 0) { // 응답이 비어있다면
		console.log("글 존재하지 않음");

		clientBoardWrapper.append(`
			<tr>
	        <td colspan="6">글이 존재하지 않습니다.</td>
			</tr>
		`);

		// 페이지네이션 생성 함수 호출
		makePageNation(1);
		return;
	}

	// 반복하며 회원 게시글 생성
	response.boardComboDatas.forEach(boardComboData => {
		// 탈퇴한 회원 여부에 따라 이름 표시 변경
		const memberName = boardComboData.memberIsWithdraw ? "탈퇴한 회원" : boardComboData.memberName;

		clientBoardWrapper.append(`
			<tr onclick="location.href='boardDetail.do?boardComboNumber=`+ boardComboData.boardComboNumber + `'">
		        <td>`+ boardComboData.boardComboNumber + `</td>
		        <td>`+ memberName + `</td>
		        <td>`+ boardComboData.boardComboTitle + `</td>
		        <td>`+ boardComboData.boardComboViewCount + `</td>
		        <td>`+ boardComboData.boardComboLikedCount + `</td>
		        <td>`+ boardComboData.boardComboRegisterDate + `</td>
			</tr>
		`);
	});

	// 페이지네이션 생성 함수 호출
	makePageNation(response.boardComboDatas[0].totalCountNumber);
}

// 초기 기능 실행 기능
const runPostLoginSetup = () => {
	// 회원 게시글 불러오기 함수 호출
	loadComboBoardDatas(); // 페이지 변경에 따라 데이터 로드
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 페이지네이션 클릭 이벤트 위임
	$(document).on("click", ".page-link", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage($(this)); // 페이지 이동
	});

	// 검색 엔터키 이벤트 위임
	$("#searchKeyword").on("keydown", function(event) {
		if (event.key === "Enter") { // Enter 키 감지
			searchComboBoardTitle(); // 검색 함수 실행
		}
	});
}

// 페이지 시작
$(document).ready(function() {
	// 초기 이벤트 리스너 등록 함수 호출
	initEventListeners();
	// 초기 기능 실행 함수 호출
	runPostLoginSetup();
});