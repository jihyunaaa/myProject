/**
 *  사용자 정의 글 등록 js
 */
const updateBoardUrl = "/boardCombo/insert"; // 게시글 등록 서블릿 url
const contentImgUploadUrl = "/boardCombo/CKEditor5/uploadImage"; // 게시글 내용 이미지 업로드 서블릿 url

// CKEditor에서 커스텀 이미지 업로드 기능
const contentImageUploader = (loader) => {
	return {
		upload: () => {
			// CKEditor가 업로드할 파일을 가져옴
			return loader.file.then(file => {
				// 업로드할 파일을 담을 FormData 생성
				const formData = new FormData();
				// 파일을 'upload'라는 key로 첨부
				formData.append('upload', file);

				// 실제 서버로 이미지 업로드 요청
				return fetch(contentImgUploadUrl, {
					// 전송 방식: POST
					method: 'POST',
					// 이미지 파일 데이터 전송
					body: formData
				})
					// 서버 응답(JSON 형태)을 파싱
					.then(res => {
						if (res.status === 413) {
							// 파일 크기 초과 에러 처리
							printSweetAlert("error", "파일 크기 초과", "50MB 이하의 파일만 업로드 가능합니다.");
							return Promise.reject("파일 크기 초과");
						}
						if (!res.ok) {
							printSweetAlert("error", "이미지 업로드 실패", "서버 오류가 발생했습니다.");
							return Promise.reject("서버 오류");
						}
						return res.json();
					})
					.then(data => {
						// 업로드 성공 시 이미지 경로 반환 → 에디터에 <img src="경로"> 로 삽입
						if (data.url) {
							return { default: data.url };
						}
						// 실패 시 에러 처리
						printSweetAlert("error", "이미지 업로드 실패", "잠시 후 다시 시도해주세요.");
						return Promise.reject("업로드 실패");
					})
					// 서버 오류 처리
					.catch(error => {
						// 크기 초과 감지: 메시지 문자열 또는 타입까지 폭넓게 검사
						const errorMsg = error.message || error.toString();
						if (errorMsg.includes("413") || errorMsg.includes("Payload") || errorMsg.includes("too large") || errorMsg.includes("크기")) {
							printSweetAlert("error", "파일 크기 초과", "50MB 이하의 파일만 업로드 가능합니다.");
							return Promise.reject();
						}

						// 그 외 오류
						printSweetAlert("error", "이미지 업로드 중 오류", "서버와의 연결에 문제가 있습니다.");
						return Promise.reject();
					});
			});
		}
	};
}

// CKEditor 초기화 및 설정
ClassicEditor
	.create(document.querySelector('#CKEditor5'))

	.then(editor => {
		// 에디터 객체를 전역 변수로 저장해서 이후 getData() 등에 사용 가능
		window.editor = editor;
		// 이미지 업로더 커스터마이징
		// CKEditor의 파일 업로드 처리 객체인 FileRepository에서
		// 각 업로드 요청(loader)마다 우리가 만든 contentImageUploader()를 사용하도록 설정함
		// 사용자가 에디터에 이미지를 삽입할 때마다 이 로직이 동작함
		editor.plugins.get('FileRepository').createUploadAdapter = loader => contentImageUploader(loader);

	})
	.catch(error => {
		// 에디터 로딩 실패시 오류 메시지
		console.error('Editor loading failed:', error);
	});

// 게시글 등록 기능
const updateBoard = () => {
	console.log("게시글 등록 실행");
	let updateBoardTitle = $("#updateBoardTitle").val();  // 제목 입력값을 가져옴
	let updateBoardContent = editor.getData();
	$("#updateBoardContent").val(updateBoardContent);  // CKEditor5에서 입력된 HTML 형식의 콘텐츠를 가져옴

	console.log("등록할 제목 : [" + updateBoardTitle + "]");
	console.log("등록할 내용 : [" + updateBoardContent + "]");

	if (!updateBoardTitle || !updateBoardContent.trim()) { // 제목이나 내용이 비어있다면
		// 스위트 알럿창 출력
		printSweetAlert("warning", "제목과 내용을 모두 입력해주세요.");
		return;
	}

	// Ajax를 사용하여 데이터 서버로 전송
	$.ajax({
		url: updateBoardUrl,  // 서버로 데이터를 전송할 API 경로
		type: "POST",  // HTTP 메서드: POST
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",  // 콘텐츠 타입 설정
		data: {
			updateBoardTitle: updateBoardTitle,  // 제목 데이터 전송
			updateBoardContent: updateBoardContent,  // 내용 데이터 전송
		},
		dataType: "text",  // 서버 응답 형식 (여기서는 텍스트로 응답)
		success: function(response) {  // 서버로부터의 응답 처리
			if (response === "true") {  // 서버 응답이 "true"일 경우
				console.log("게시글 등록 성공");
				// 스위트 알럿창 출력
				printSweetAlert("success", "게시글이 등록됐습니다.", "", "comboBoard.do");
			} else {
				console.log("게시글 등록 실패");  // 콘솔에 저장 실패 로그 출력
				// 스위트 알럿창 출력
				printSweetAlert("error", "게시글 등록이 실패했습니다.", "지속될 시 관리자에게 문의하세요.");
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 글 등록 버튼 이벤트 위임
	$("#updateBoardBtn").on("click", function() {
		updateBoard();
	});
}

// 로그인 상태 확인 기능
const checkLoginStatus = () => {
	// 서버에서 로그인 정보 확인
	fetch(isLoginUrl)
		.then(response => {
			if (!response.ok) {
				throw new Error('로그인 상태 확인 실패');
			}
			return response.json();
		})
		.then(res => {
			if (!res.authenticated) { // 비 로그인 중이라면
				// 스위트 알럿창 출력
				printSweetAlert("error", "로그인이 필요한 페이지입니다.", "로그인 페이지로 이동합니다.", "login.do");
			}
			else { // 로그인 중이라면
				// 초기 이벤트 리스너 등록 함수 호출
				initEventListeners();
			}
		})
		.catch(() => { // 에러 발생 시
			console.error("서버와의 통신 실패");
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
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