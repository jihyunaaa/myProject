/**
 *  게시글 상세 js
 */
const checkLikedUrl = "/boardComboLiked/isLiked"; // 좋아요 여부 받아오기 서블릿 url
const clickLikedUrl = "/boardComboLiked/like"; // 좋아요 여부 변경 서블릿 url
const deleteComboBoardUrl = "/boardCombo/delete"; // 게시글 삭제 서블릿 url
const updateComboBoardUrl = "/boardCombo/update"; // 게시글 수정 서블릿 url
const boardComboNumber = document.body.dataset.boardNumber; // jsp 파일의 게시글 번호 가져오기
let boardLikedCount = Number(document.body.dataset.boardLikedCount); // jsp 파일의 게시글 좋아요 수 가져오기
let isLiked = false; // 게시글 좋아요 여부
// 이미지 업로드 어댑터 생성 함수
// CKEditor5의 커스텀 이미지 업로더를 생성함
const contentImgUploadUrl = "/boardCombo/CKEditor5/uploadImage";
const contentImageUploader = (loader) => {
	return {
		upload: () => {
			return loader.file.then(file => {
				const formData = new FormData();
				formData.append("upload", file); // 업로드 파일 추가

				// 서버로 업로드 요청
				return fetch(contentImgUploadUrl, {
					method: "POST",
					body: formData
				})
					.then(res => res.json())
					.then(data => {
						// 업로드 성공 시 반환된 이미지 URL을 에디터에 삽입
						if (data.url) return { default: data.url };
						printSweetAlert("error", "이미지 업로드 실패", "잠시 후 다시 시도해주세요.");
						throw new Error("Upload failed");
					})
					.catch(error => {
						printSweetAlert("error", "서버 오류", "이미지 업로드 중 문제가 발생했습니다.");
						throw error;
					});
			});
		}
	};
};

// 게시글 수정 버튼 클릭 이벤트
// 버튼의 상태(mode)에 따라 수정 모드 진입 또는 저장 요청을 처리함
$(document).on("click", "#updateBtn", function(e) {
	e.preventDefault(); // 기본 이벤트(페이지 이동 등) 차단

	const btn = $(this);
	const mode = btn.attr("data-mode"); // 현재 버튼 상태값 확인 (edit 또는 save)

	if (mode === "edit") {
		// 수정 모드로 전환

		const titleText = $("#titleArea").text().trim(); // 기존 제목 추출
		const contentHtml = $("#boardDetailContent").html().trim(); // 기존 내용 추출

		// 제목을 input 태그로 바꿔 수정 가능하게 처리
		$("#titleArea").html(`<input type="text" id="updateBoardTitle" class="form-control" value="${titleText}" />`);

		// 내용을 CKEditor5 에디터 영역으로 변경
		$("#boardDetailContent").html(`<div id="CKEditor5">${contentHtml}</div>`);

		// CKEditor5 초기화 및 업로더 어댑터 등록
		ClassicEditor
			.create(document.querySelector("#CKEditor5"))
			.then(editor => {
				window.editor = editor; // 전역 참조 저장
				editor.plugins.get('FileRepository').createUploadAdapter = loader => contentImageUploader(loader);

				// 버튼 상태 및 텍스트를 저장 모드로 변경
				btn.text("저장").attr("data-mode", "save");
			})
			.catch(error => console.error("CKEditor 로딩 실패", error));

	} else if (mode === "save") {
		// 저장 요청

		const newTitle = $("#updateBoardTitle").val(); // 변경된 제목
		const newContent = window.editor.getData(); // 변경된 내용 (HTML)

		// 유효성 검사
		if (!newTitle || !newContent.trim()) {
			printSweetAlert("warning", "제목과 내용을 모두 입력해주세요.");
			return;
		}

		// 서버로 수정 요청 전송
		$.ajax({
			url: updateComboBoardUrl, // POST 요청 URL
			type: "POST",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			data: {
				boardComboNumber: boardComboNumber,
				boardComboTitle: newTitle,
				boardComboContent: newContent
			},
			dataType: "text",
			success: function(response) {
				// 수정 성공 시 상세페이지로 이동
				if (response === "true") {
					printSweetAlert("success", "수정 완료", "", `boardDetail.do?boardComboNumber=${boardComboNumber}`);
				} else {
					printSweetAlert("error", "수정 실패", "지속될 시 관리자에게 문의해주세요.");
				}
			},
			error: function(xhr, status, error) {
				console.error("수정 에러", error);
				printSweetAlert("error", "서버 오류", "잠시 후 다시 시도해주세요.");
			}
		});
	}
});

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
			// 좋아요 상태 쿠기 설정
			document.cookie = "boardComboIsLiked=" + isLiked;

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
	console.log("좋아요 버튼 클릭");
	console.log("좋아요 버튼에 요청받은 글 번호 : [" + boardComboNumber + "]");
	console.log("좋아요 버튼에 요청받은 회원 번호 : [" + loginedMemberNumber + "]");
	console.log("좋아요 버튼에 요청받은 좋아요 여부 : [" + isLiked + "]");

	if (!loginedMemberNumber) { // 비회원이라면
		// 스위트 알럿창 출력
		printSweetAlert("warning", "로그인이 필요한 기능입니다.");
		return;
	}

	// 회원 좋아요 여부 값 반전
	isLiked = !isLiked;

	// 글 좋아요 수 값 변경
	// 좋아요 등록상태라면
	if (isLiked) {
		boardLikedCount += 1;
		console.log("좋아요 등록된 총 수 : [" + boardLikedCount + "]");
	}
	// 좋아요 취소 상태라면
	else {
		boardLikedCount -= 1;
		console.log("좋아요 취소된 총 수 : [" + boardLikedCount + "]");
	}

	// 좋아요 버튼 생성 함수 호출
	createLikedBtn();
}

/*
// 좋아요 여부 변경 기능 <= 쿠키 적용 전
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
*/

// 꿀조합 게시글 삭제 기능
const deleteComboBoard = (boardComboNumber) => {
	console.log("삭제할 게시글 번호 : [" + boardComboNumber + "]");

	// 삭제 여부 확인
	Swal.fire({
		title: "정말로 작성하신 글을 삭제하시겠습니까?",
		text: "삭제된 글은 복구하실 수 없습니다.",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#d33",
		showLoaderOnConfirm: true
	}).then((result) => {
		if (!result.isConfirmed) { // 글 삭제 취소라면
			console.log("글 삭제 취소");
			return;
		}
		$.ajax({ // 비동기
			url: deleteComboBoardUrl, // 보낼 주소
			type: 'POST', // 방식
			data: { boardComboNumber: boardComboNumber }, // 보낼 값
			dataType: 'text', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 글 삭제 성공 정보 : [" + response + "]"); // 로그 찍기
				if (response === "true") { // 게시글 삭제 성공 시
					console.log("게시글 삭제 성공");
					// 스위트 알럿창 출력
					printSweetAlert("success", "게시글이 삭제됐습니다.", "", "comboBoard.do");
				}
				else { // 게시글 삭제 실패 시
					console.log("게시글 삭제 실패");
					// 스위트 알럿창 출력
					printSweetAlert("error", "게시글 삭제가 실패했습니다.", "", "error.do");
				}
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
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

	// 수정 버튼 클릭 시
	$(document).on("click", "#updateBtn", function() {
		updateComboBoard(boardComboNumber);
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

// 쿠키 값 가져오는 기능
const getCookie = (name) => {
	console.log("값 가져올 쿠키 이름 : [" + name + "]");
	const value = `; ${document.cookie}`;
	const parts = value.split(`; ${name}=`);
	if (parts.length === 2) return parts.pop().split(';').shift();
	return null;
}

// 페이지를 벗어날 시
$(window).on("beforeunload", function() {
	console.log("페이지 벗어남 확인-게시글 좋아여 등록/취소 실행");
	// 현재 쿠키 값 가져오는 함수 호출
	const likeStatus = getCookie("boardComboIsLiked");
	// 쿠키 비우기
	document.cookie = "boardComboIsLiked=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";

	// isLiked와 비교하기 위해 형변환
	// "true"일 경우에만 true로 변환
	const likeStatusBoolean = (likeStatus === "true");

	console.log("쿠키에서 가져온 boolean 형변환한 값 : ["+likeStatusBoolean+"]");
	// 쿠키값과 isLiked가 같다면
	if (likeStatusBoolean === isLiked) {
		console.log("쿠키값과 isLiked 동일");
		return;
	}

	console.log("쿠키값과 isLiked 비 동일");
	// 쿠키값과 isLiked가 다르다면
	// 좋아요 등록/취소 조건명 설정
	let likedCondition = isLiked ? "INSERTLIKED" : "DELETELIKED";

	// 보낼 값 세팅
	const data = JSON.stringify({
		boardComboNumber: boardComboNumber,
		likedCondition: likedCondition
	});
	// JSON 타입 명시적으로 지정
	const blob = new Blob([data], { type: 'application/json' });

	// beforeunload 이벤트에서는 비동기 호출이 제한되므로
	// 페이지가 닫히기 전에 동기식으로 모든 작업을 완료해야함
	// sendBeacon을 사용하여 서버로 데이터를 보내기
	navigator.sendBeacon(clickLikedUrl, blob);
});

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});