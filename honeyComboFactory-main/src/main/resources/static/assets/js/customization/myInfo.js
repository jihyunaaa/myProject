/**
 *  내정보 js
 */
const withdrawUrl = "/member/withdraw"; // 회원탈퇴 서블릿 url
const loadMoreMyBoardUrl = "/member/myInfo/myBoard"; // 본인 작성 글 불러오기 서블릿 url
const loadMorePurchaseUrl = "/member/myInfo/purchase"; // 주문 내역 불러오기 서블릿 url
const loadMoreLikedBoardUrl = "/member/myInfo/likedBoard"; // 좋아요 글 불러오기 서블릿 url
const checkLoginedMemberUrl = "/member/password/confirm"; // 본인확인 서블릿 url
const updateMemberInfoUrl = "/member/myInfo/update"; // 회원 정보수정 서블릿 url
const updateMemberPasswordUrl = "/member/password/update"; // 비밀번호 변경 서블릿 url
const sendSmsCodeUrl = "/member/sendSmsCode";//sms 발신 서블릿 url
const checkSmsCodeUrl = "/member/checkSmsCode"; //sms 발신번호 확인 서블릿 url
let confirmNumber = 0; // 발급 받은 핸드폰 인증 번호
let timer; // 핸드폰 인증번호 타이머 ID 저장용 변수
// 전역 객체 선언
const myInfoState = {
	pageGroupSize: 5, // 한 번에 보여줄 페이지 개수
	pageNumber: { // 페이지 수
		myBoard: 1,
		likedBoard: 1,
		purchaseList: 1
	},
	contentCount: { // 한 번에 보여줄 데이터 수
		myBoard: 3,
		likedBoard: 3,
		purchaseList: 3
	},
	pageNation: { // 페이지네이션 생성할 영역 id
		myBoard: "myBoardPageNation",
		likedBoard: "likedBoardPageNation",
		purchaseList: "purchaseListPageNation"
	}
};

// 정보수정 입력 form 잠금 해제 기능
const unlockMemberInfoForm = () => {
	console.log("정보수정 form 잠금 해제 실행");
	// disabled 속성 제거 및 정보 *표시 제거
	$("#memberId").prop("type", "text");
	$("#memberName").prop("disabled", false).prop("type", "text");
	$("#memberPhoneNumber").prop("disabled", false).prop("type", "tel");
	$("#confirmPhoneNumberBtn").prop("type", "button");
	$("#memberAddressBtn").prop("type", "button");
	$("#memberAddressMain").prop("disabled", false).prop("readonly", true).prop("type", "text");
	$("#memberAddressDetail").prop("disabled", false).prop("readonly", true).prop("type", "text");
	$("#memberEmailId").prop("disabled", false).prop("type", "text");
	$("#memberEmailDomain").prop("disabled", false).prop("type", "text");
	$("#memberBirth").prop("type", "date");
}

// 비밀번호 변경 기능
const changeMemberPassword = () => {
	console.log("비밀번호 변경 실행");

	// 간편 로그인 계정이라면
	if (20000 <= loginedMemberNumber) {
		console.log("간편 로그인 계정 확인");
		// 스위트 알럿창 출력
		printSweetAlert("warning", "간편 로그인 계정입니다.");
		return;
	}

	// 본인 인증 확인
	checkLoginedMember().then((isAuthenticated) => {
		if (!isAuthenticated) { // 본인 인증 실패 시
			return;
		}
		const passwordPattern = /^(?!([\d])\1{4,})(?=.*[\W_]).{6,}$/;
		Swal.fire({
			title: "변경할 비밀번호를 입력하세요.",
			text: "(6~15자, 같은 숫자 연속 6개 이상 X, 특수문자 1개 이상 포함)",
			input: "password",
			inputAttributes: {
				autocapitalize: "off"
			},
			showCancelButton: true,
			preConfirm: (value) => {
				// 빈칸 입력이라면
				if (!value.trim()) {
					Swal.showValidationMessage("비밀번호를 입력해주세요");
					return false;
				}
				else if (!passwordPattern.test(value.trim())) { // 유요한 비밀번호 패턴인지 검사
					Swal.showValidationMessage("비밀번호 형식이 올바르지 않습니다.");
					return false;
				}
				return value.trim();
			}
		}).then((result) => {
			console.log("isConfirmed:", result.isConfirmed);
			console.log("isDismissed:", result.isDismissed);
			console.log("result.value:", result.value);
			if (!result.isConfirmed) { // 취소 버튼이라면
				printSweetAlert("info", "비밀번호 재설정이 취소되었습니다.");
				return;
			}

			// 최종 확인
			Swal.fire({
				title: "입력하신 비밀번호로 재설정하시겠습니까?",
				icon: "question",
				showCancelButton: true,
				confirmButtonColor: "#3085d6",
				cancelButtonColor: "#d33",
			}).then((confirmResult) => {
				if (!confirmResult.isConfirmed) {
					console.log("비밀번호 변경 취소");
					return;
				}
				console.log("비밀번호 변경 비동기 실행");

				// AJAX 요청
				$.ajax({
					type: "POST",
					url: updateMemberPasswordUrl,
					data: {
						memberPassword: result.value.trim()
					},
					dataType: "text",
					success: (response) => {
						if (response === "true") {
							sessionStorage.removeItem("loginedMemberNumber");
							printSweetAlert("info", "비밀번호가 변경되었습니다.", "다시 로그인하세요.", "logout.did");
						} else {
							printSweetAlert("error", "비밀번호 변경에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
						}
					},
					error: (xhr, status, error) => {
						console.error("AJAX 에러 발생", xhr.status, status, error);
						printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
					}
				});
			});
		});
	});
}
// 휴대폰번호 변경 검사 기능
const checkMemberPhoneNumber = (event) => {
	console.log("핸드폰번호 변경 검사 실행");
	// 행동이 일어난 태그의 값 받아오기
	let memberPhoneNumber = $(event.target).val().trim();
	console.log("핸드폰번호 변경 검사를 위해 받아온 핸드폰 번호 : [" + memberPhoneNumber + "]");

	if (memberPhoneNumber === $(event.target).data('original')) {
		console.log("핸드폰번호 변경 없음");
		$("#confirmPhoneNumberBtn").val("인증 완료");
		$("#phoneNumberConfirmNumber").prop("disabled", true);
		return;
	};

	console.log("핸드폰번호 변경 있음");
	$("#confirmPhoneNumberBtn").val("인증");
}

// 휴대폰번호 인증 기능
const confirmPhoneNumber = () => {
	console.log("휴대폰번호 인증");
	if ($("#confirmPhoneNumberBtn").val() === "인증 완료") { // 인증 완료 상태라면
		// 스위트 알럿창 출력
		printSweetAlert("info", "현재 번호는 인증이 완료된 번호입니다.");
	}
	else if ($("#confirmPhoneNumberBtn").val() === "인증") { // 인증 시작이라면
		// 버튼명 변경
		$("#confirmPhoneNumberBtn").val("취소");

		// 핸드폰번호 공백들 제거값 설정
		$("#memberPhoneNumber").val($("#memberPhoneNumber").val().replace(/\s+/g, ""));

		// 숫자만 포함되어 있는지 확인 (정규식 사용)
		const phoneNumberPattern = /^(?!^(\d)\1{10}$)\d{11}$/;
		if (!(phoneNumberPattern.test($("#memberPhoneNumber").val()))) {
			console.log("핸드폰번호 숫자 이외의 것 포함.");
			// 스위트 알럿창 출력
			printSweetAlert("warning", "올바른 핸드폰 번호 형식이 아닙니다.", "동일하지 않은 숫자로 11자리를 입력하세요.");
			return;
		}

		// 핸드폰번호 입력칸 disabled 속성 추가
		$("#memberPhoneNumber").prop("disabled", true);
		// 핸드폰 인증번호 입력칸 disabled 속성 제거
		$("#phoneNumberConfirmNumber").prop("disabled", false);
		// 인증번호 확인 버튼 hidden 속성 변경
		$("#checkConfirmPhoneNumberBtn").prop("type", "button");
		// 발급 받은 핸드폰 인증 번호
		//confirmNumber = 1234;
		console.log("발급 받은 핸드폰 인증 번호 : [" + confirmNumber + "]");
		// 타이머 시작
		startPhoneNumberConfirmTimer();
		// 타이머 표시
		$("#confirmTimer").show();

		// 실제 인증번호 문자 전송 요청
		$.ajax({
			type: "POST",
			url: sendSmsCodeUrl,
			data: { phoneNumber: $("#memberPhoneNumber").val() },
			dataType: "json",
			success: function(response) {
				console.log("인증번호 전송 응답:", response);
				if (response.status === "success") {
					printSweetAlert("success", "인증번호가 발송되었습니다.");
				} else {
					printSweetAlert("error", "인증번호 전송 실패", "서버에서 오류가 발생했습니다.");
				}
			},
			error: function(status, error) {
				console.error("인증번호 전송 중 오류 발생", status, error);
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.");
			}
		});
	}
	else if ($("#confirmPhoneNumberBtn").val() === "취소") { // 인증 취소라면
		// 핸드폰번호 입력칸 disabled 속성 제거
		$("#memberPhoneNumber").prop("disabled", false);
		// 핸드폰 인증번호 입력칸 disabled 속성 추가
		$("#phoneNumberConfirmNumber").prop("disabled", true);
		// 인증번호 확인 버튼 button 속성 변경
		$("#checkConfirmPhoneNumberBtn").prop("type", "hidden");

		// 타이머 초기화
		clearInterval(timer);
		// 타이머 숨기기
		$("#confirmTimer").hide();
		// 버튼명 변경
		$("#confirmPhoneNumberBtn").val("인증");

	}
};

// 인증번호 확인 기능
const checkConfirmPhoneNumber = () => {
	console.log("인증번호 확인 실행");
	const inputCode = $("#phoneNumberConfirmNumber").val().trim();

	if (!inputCode) {
		Swal.fire({
			icon: "warning",
			title: "인증번호를 입력해주세요."
		});
		return;
	}
	//문자코드 확인 
	$.ajax({
		type: "POST",
		url: checkSmsCodeUrl,
		data: { authCode: inputCode },
		dataType: "json",
		success: function(response) {
			console.log("서버 응답:", response);
			if (response.valid === true) {
				clearInterval(timer);
				printSweetAlert("success", "인증이 완료되었습니다.");
				$("#memberPhoneNumber").prop("disabled", false);
				$("#phoneNumberConfirmNumber").val("").prop("disabled", true);
				$("#checkConfirmPhoneNumberBtn").prop("type", "hidden");
				$("#confirmPhoneNumberBtn").val("인증 완료");
				$("#confirmTimer").hide();
			} else {
				printSweetAlert("error", "잘못된 인증번호입니다.");
				$("#phoneNumberConfirmNumber").val("");
				$("#confirmPhoneNumberBtn").val("취소");
				confirmPhoneNumber();
			}
		},
		error: function(status, error) {
			console.error("AJAX 오류 발생:", status, error);
			printSweetAlert("error", "서버 오류", "인증 확인 중 문제가 발생했습니다.");
		}
	});
};
// 타이머 시작 기능
const startPhoneNumberConfirmTimer = () => {
	clearInterval(timer);

	let timeLeft = 300; // 5분 (300초)로 설정

	timer = setInterval(() => {
		// 남은 시간 초단위로 표시
		let minutes = Math.floor(timeLeft / 60); // 분 계산
		let seconds = timeLeft % 60; // 초 계산

		// "4:56" 형식으로 표시
		$("#confirmTimer").text(`${minutes}:${seconds < 10 ? '0' : ''}${seconds}`);

		// 시간이 다 되면 타이머 종료
		if (timeLeft <= 0) {
			console.log("핸드폰 인증 타이머 종료");
			clearInterval(timer);
			// 인증 취소 처리
			$("#confirmPhoneNumberBtn").val("취소");
			confirmPhoneNumber();
			// 스위트 알럿창 출력
			printSweetAlert("warning", "인증 시간이 지났습니다.");
		}

		timeLeft--;
	}, 1000); // 1초마다 업데이트
}

// 본인확인 기능
const checkLoginedMember = () => {
	return new Promise((resolve) => {
		// 비밀번호 입력받기
		Swal.fire({
			title: "본인인증을 위해 비밀번호를 입력해주세요.",
			input: "password", // 입력 타입: 비밀번호
			inputAttributes: {
				autocapitalize: "off" // 자동 대문자 변환 방지
			},
			showCancelButton: true, // 취소 버튼 표시
			showLoaderOnConfirm: true, // 확인 시 로딩 아이콘 표시
			preConfirm: (loginedMemberPassword) => {
				// 비밀번호 미입력 시
				if (!loginedMemberPassword.trim()) {
					Swal.showValidationMessage("비밀번호를 입력해주세요.");
					return false;
				}

				// fetch 요청
				return fetch(checkLoginedMemberUrl, {
					method: "POST",
					headers: {
						"Content-Type": "application/x-www-form-urlencoded"
					},
					body: "loginedMemberPassword=" + encodeURIComponent(loginedMemberPassword)
				})
					.then(response => response.text())
					.then(response => {
						// 인증 성공
						if (response === "true") {
							console.log("인증 성공");
							return true;
						} else {
							// 인증 실패
							throw new Error("인증 실패");
						}
					})
					.catch(() => {
						Swal.showValidationMessage("비밀번호가 일치하지 않습니다.");
						return false;
					});
			},
			allowOutsideClick: () => !Swal.isLoading() // 로딩 중 외부 클릭 방지
		}).then((result) => {
			if (result.isConfirmed && result.value === true) {
				resolve(true);
			} else {
				Swal.fire({
					icon: "info",
					title: "본인 인증이 취소되었습니다."
				}).then(() => {
					resolve(false);
				});
			}
		}).catch((error) => {
			console.error("AJAX 요청 에러 발생", error);
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		});
	});
};

// 폼 데이터와 원본 데이터 비교 함수
const isFormModified = () => {
	let isModified = false;

	// 각 form 필드를 가져와서 비교
	$('#memberInfoForm input').each(function() {
		const originalValue = $(this).data('original'); // data-original에서 원본 값 가져오기
		const currentValue = $(this).val(); // 현재 form 입력값

		if (currentValue !== originalValue) { // 값이 하나라도 다르면
			isModified = true; // 수정된 것으로 판단
		}
	});

	return isModified;
};

// 페이지네이션 페이지 이동 기능
const changePage = (type, element) => {
	console.log("내정보 페이지네이션 클릭 타입 : [" + type + "]");
	console.log("내정보 페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("내정보 페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === 'Previous') { // "<" 버튼 클릭 시
		myInfoState.pageNumber[type]--;
	} else if (element.attr('id') === 'Next') { // ">" 버튼 클릭 시
		myInfoState.pageNumber[type]++;
	} else { // 페이지 번호 클릭 시
		myInfoState.pageNumber[type] = element.data('page');
	}

	// 정보 불러오기 함수 호출
	loadDatas(type);
}

// 정보 불러오기 기능
const loadDatas = (type) => {
	console.log("불러올 정보 타입 : [" + type + "]");

	if (type === "myBoard") { // 본인 작성글이라면
		$.ajax({ // 비동기
			url: loadMoreMyBoardUrl, // 보낼 주소
			type: 'POST', // 방식
			data: { // 보낼 값
				myBoardPageNumber: myInfoState.pageNumber[type],
				myBoardContentCount: myInfoState.contentCount[type]
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기
				// 본인 작성 글 생성 함수 호출
				insert(type, response);
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
	}
	else if (type === "likedBoard") { // 좋아요 글이라면
		$.ajax({ // 비동기
			url: loadMoreLikedBoardUrl, // 보낼 주소
			type: 'POST', // 방식
			data: { // 보낼 값
				likedBoardPageNumber: myInfoState.pageNumber[type],
				likedBoardContentCount: myInfoState.contentCount[type]
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기

				// 좋아요 글 생성 함수 호출
				insert(type, response);
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
			}
		});
	}
	else if (type === "purchaseList") { // 주문 내역이라면
		$.ajax({ // 비동기
			url: loadMorePurchaseUrl, // 보낼 주소
			type: 'POST', // 방식
			data: { // 보낼 값
				purchasePageNumber: myInfoState.pageNumber[type],
				purchaseContentCount: myInfoState.contentCount[type]
			},
			dataType: 'json', // 받을 타입
			success: (response) => { // 성공적이라면
				console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기

				// 주문 내역 생성 함수 호출
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

// 반복하며 내역 생성 기능
const insert = (type, response) => {
	console.log("받은 생성 타입 : [" + type + "]");
	console.log("받은 생성 정보 : [" + response + "]");

	let wrapper = "";
	if (type === "myBoard") {
		wrapper = $("#boardTable tbody");
	}
	else {
		wrapper = $("#" + type + "Wrapper");
	}

	// 초기화
	wrapper.empty();

	if (response.length === 0) { // 응답이 비어있다면
		console.log("내역 존재하지 않음");

		if (type === "myBoard") {
			// 본인 작성 글 생성
			wrapper.append(`
				<tr>
					<td colspan="5">작성하신 글이 없습니다.</td>
				</tr>
			`);
		}
		else {
			// 비우고 생성
			wrapper.append(`
				<li>
					<a href="javascript:void(0);">
						내역이 없습니다.
					</a>
				</li>
			`);
		}

		// 페이지네이션 생성 함수 호출
		makePageNation(type, 1);
		return;
	}

	if (type === "myBoard") { // 본인 작성글이라면
		// 반복하며 본인 작성 글 생성
		response.forEach(myBoardData => {
			wrapper.append(`
				<tr onclick = "location.href='boardDetail.do?boardComboNumber=`+ myBoardData.boardComboNumber + `'">
			        <td>`+ myBoardData.boardComboNumber + `</td>
			        <td>`+ myBoardData.boardComboTitle + `</td>
			        <td>`+ myBoardData.boardComboViewCount + `</td>
			        <td>`+ myBoardData.boardComboLikedCount + `</td>
			        <td>`+ myBoardData.boardComboRegisterDate + `</td>
				</tr>
			`);
		});
	}
	else if (type === "purchaseList") { // 주문 내역이라면
		// 반복하며 주문 내역 생성
		response.forEach(purchaseListData => {
			wrapper.append(`
				<li>
					<a href="purchaseDetail.do?purchaseNumber=${purchaseListData.purchaseNumber}">
					${purchaseListData.purchaseTerminalId}
					<span class="last">${purchaseListData.purchaseTotalPrice}</span>
					</a>
				</li>
			`);
		});
	}
	else if (type === "likedBoard") { // 좋아요 글이라면
		// 반복하며 좋아요 글 생성
		response.forEach(likedBoardData => {
			wrapper.append(`
				<li>
				<a href="boardDetail.do?boardComboNumber=${likedBoardData.boardComboNumber}">${likedBoardData.boardComboNumber}
					<span class="last">${likedBoardData.memberName}</span>
				</a>
				</li>
			`);
		});
	}

	// 페이지네이션 생성 함수 호출
	makePageNation(type, response[0].totalCountNumber);
}

// 페이지네이션 생성 기능
function makePageNation(type, totalNumber) {
	console.log("페이지네이션 생성 타입 : [" + type + "]");
	console.log("페이지네이션 생성할 총 데이터 수 : [" + totalNumber + "]");

	// 총 페이지 수
	const totalPageNumber = Math.ceil(totalNumber / myInfoState.contentCount[type]);
	console.log("총 페이지 수 : [" + totalPageNumber + "]");

	// 현재 그룹
	let group = Math.ceil(myInfoState.pageNumber[type] / myInfoState.pageGroupSize);
	// 그룹의 처음 페이지 수
	let startPage = (group - 1) * myInfoState.pageGroupSize + 1;
	// 그룹의 마지막 페이지 수
	let endPage = Math.min(group * myInfoState.pageGroupSize, totalPageNumber);

	// 1 혹은 마지막 페이지면 클래스 추가
	let prevClass = myInfoState.pageNumber[type] <= 1 ? 'disabled-link' : '';
	let nextClass = myInfoState.pageNumber[type] >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + myInfoState.pageNumber[type] + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	console.log("현재 페이지 수 : [" + myInfoState.pageNumber[type] + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 작성할 내용 초기화
	let content = "";

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link `+ type + ` ` + prevClass + `" href="javascript: void(0);" aria-label="Previous" id="Previous">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;

	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = myInfoState.pageNumber[type] == i ? 'active' : '';
		content += `
	    <li class="page-item `+ activeClass + `">
	        <a class="page-link `+ type + `" href="javascript:void(0);" data-page=` + i + ` > ` + i + `</a>
	    </li>
	    `;
	}

	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link purchasePage `+ nextClass + `" href="javascript:void(0);" aria-label="Next" id="Next">
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;

	// 페이지네이션 비우고 새로 생성
	$("#" + myInfoState.pageNation[type]).empty().append(content);
}

// 회원 탈퇴 기능
const withdraw = () => {
	console.log("회원 탈퇴 실행");

	// SweetAlert로 탈퇴 여부 확인
	Swal.fire({
		icon: "warning", // 경고 아이콘
		title: "정말로 계정을 삭제하시겠습니까?",
		text: "삭제된 계정 정보는 복구하실 수 없습니다.",
		showCancelButton: true, // 취소 버튼 표시
		showLoaderOnConfirm: true,
		confirmButtonColor: "#d33", // 붉은색 강조
	}).then((result) => {
		console.log("탈퇴 확인 여부 : [" + result.isConfirmed + "]");
		if (!result.isConfirmed) {
			console.log("회원탈퇴 취소");
			return;
		}
		// 본인 인증 확인
		checkLoginedMember().then((isAuthenticated) => {
			if (!isAuthenticated) { // 본인 인증 실패 시
				return;
			}

			// AJAX 요청: 회원 탈퇴
			$.ajax({
				type: "POST", // 방식
				url: withdrawUrl, // 찾아갈 주소
				data: {}, // 보낼 값
				dataType: "text", // 받을 타입
				success: (response) => { // 성공적이라면
					if (response === "true") { // 탈퇴 성공 시
						console.log("탈퇴 성공");
						sessionStorage.removeItem("loginedMemberNumber"); // 세션 제거
						// 스위트 알럿창 출력
						printSweetAlert("", "다시 뵙기를 고대하겠습니다.", "감사합니다.", "logout.did");
					} else { // 탈퇴 실패 시
						console.log("탈퇴 실패");
						// 스위트 알럿창 출력
						printSweetAlert("error", "회원 탈퇴에 실패하였습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
					}
				},
				error: (status, error) => { // 에러 처리
					console.error("AJAX 요청 에러 발생", status, error);
					// 스위트 알럿창 출력
					printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
				}
			});
		});
	});
};

// 카카오 주소 검색 API
const kakaoPostAPIcode = () => {
	console.log("주소 API 실행");
	// 카카오 주소 검색 팝업을 생성하고 실행 
	new daum.Postcode({
		// 사용자가 주소를 선택했을 때 실행되는 콜백 함수 
		oncomplete: (data) => {
			// data = 선택한 주소 정보
			console.log(data);

			let addr = ''; // 주소 변수(도로명, 지번)
			let extraAddr = ''; // 참고항목 변수(건물병, 법정동 등 )

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져오기
			// 사용자가 도로명 주소를 선택했을 경우
			if (data.userSelectedType === 'R') {
				addr = data.roadAddress;
			} else {
				// 사용자가 지번 주소를 선택했을 경우
				addr = data.jibunAddress;
			}

			// Geocoder 객체 생성
			let geocoder = new kakao.maps.services.Geocoder();

			geocoder.addressSearch(addr, function(result, status) {
				if (status === kakao.maps.services.Status.OK) {
					let lat = result[0].y; // 위도
					let lng = result[0].x; // 경도
					console.log("📍 위도:", lat, "경도:", lng);
				} else {
					console.error("주소 → 좌표 변환 실패");
				}
			});

			// 참고항목 조합(도로명 주소일 때만)
			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			//Kakao가 도로명 주소를 선택하면 userSelectedType에 'R'을 넣기로 약속
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
			}
			// 사용자가 주소를 선택했을 때, 그 주소를 input에 넣는 코드
			// 우편번호와 주소 정보를 해당 필드에 넣는다. 
			$("#memberAddressMain").val(addr);
			// 상세주소 입력란 readonly 속성 제거
			$("#memberAddressDetail").prop("readonly", false);
			// 커서를 상세주소 입력란으로 자동 이동
			$("#memberAddressDetail").focus();
		}
	}).open(); //팝업 창 실행(사용자 주소 검색창)
}

// 초기 기능 실행 기능
const runPostLoginSetup = () => {
	// 정보 불러오기 함수 호출
	loadDatas("myBoard");
	loadDatas("likedBoard");
	loadDatas("purchaseList");
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 본인 작성글 페이지네이션 이벤트 등록
	$(document).on("click", ".myBoard", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("myBoard", $(this)); // 페이지 이동
	});

	// 좋아요 글 페이지네이션 이벤트 등록
	$(document).on("click", ".likedBoard", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("likedBoard", $(this)); // 페이지 이동
	});

	// 주문 내역 페이지네이션 이벤트 등록
	$(document).on("click", ".purchaseList", function(event) { // 페이지네이션 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		changePage("purchaseList", $(this)); // 페이지 이동
	});

	// 폼 제출 시
	$('#memberInfoForm').on('submit', function(event) {
		event.preventDefault(); // 기본 제출 동작 방지

		if ($("#updateMyInfoBtn").text() === "정보수정") { // 정보수정 버튼 클릭 시

			// 간편 로그인 계정이라면
			if (20000 <= loginedMemberNumber) {
				console.log("간편 로그인 계정 확인");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "간편 로그인 계정입니다.");
				return;
			}

			// 본인 인증 확인
			checkLoginedMember().then((isAuthenticated) => {
				if (!isAuthenticated) { // 본인 인증 실패 시
					return;
				}

				// 버튼 내용 변경
				$("#updateMyInfoBtn").text("수정 완료");
				console.log("회원정보 수정 가능 상태로 변경");

				// 정보수정 입력 form 잠금 해제 함수 호출
				unlockMemberInfoForm();
			});
			return;
		}

		// 표준 메서드 checkValidity()를 사용하여 폼의 입력값이 유효한지 확인
		if (!this.checkValidity()) {
			event.stopPropagation(); // 이벤트가 부모 요소로 전파되는 것을 방지
		}
		else {
			// 핸드폰번호 인증 검사
			if ($("#confirmPhoneNumberBtn").val() !== "인증 완료") {
				// 스위트 알럿창 출력
				printSweetAlert("warning", "휴대폰번호 인증을 진행해주세요.");
				return;
			}

			// 입력받는 값들 공백 제거 후 값을 다시 설정
			$("#memberEmailId").val($("#memberEmailId").val().replace(/\s+/g, ""));
			$("#memberEmailDomain").val($("#memberEmailDomain").val().replace(/\s+/g, ""));
			$("#memberName").val($("#memberName").val().replace(/\s+/g, ""));
			$("#memberPhoneNumber").val($("#memberPhoneNumber").val().replace(/\s+/g, ""));

			// 수정된 정보 저장
			$.ajax({
				type: "POST", // 방식
				url: updateMemberInfoUrl, // 찾아갈 주소
				data: $(this).serialize(), // 보낼 값
				dataType: "text", // 받을 타입
				success: (response) => { // 성공적이라면
					if (response === "true") { // 정보수정 성공 시
						console.log("정보 수정 성공");
						sessionStorage.removeItem("loginedMemberNumber");
						// 스위트 알럿창 출력
						printSweetAlert("success", "회원님의 정보가 수정되었습니다.", "다시 로그인해주세요.", "logout.did");
					}
					else { // 정보수정 실패 시
						console.log("정보 수정 실패");
						// 스위트 알럿창 출력
						printSweetAlert("error", "정보 수정에 실패했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
					}
				},
				error: (xhr, status, error) => { // 에러 처리
					console.error("AJAX 요청 에러 발생", xhr.status, status, error);
					// 스위트 알럿창 출력
					printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
				}
			});
		}
		// 폼에 'was-validated' 클래스를 추가하여 유효성 검사 상태를 시각적으로 표시
		$(this).addClass("was-validated");
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

	// 초기 이벤트 리스너 등록 함수 호출
	initEventListeners();
	// 초기 기능 실행 함수 호출
	runPostLoginSetup();
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});