/**
 *  계정찾기 js
 */
const findMemberIdUrl = "/member/findId"; // 아이디 찾기 url
const findMemberPasswordUrl = "/member/findPassword" // 비밀번호 찾기 url
const updateMemberPasswordUrl = "/member/updatePassword"; // 비밀번호 변경 url
const verifyEmailCodeUrl = "/member/verifyEmailCode"; // 이메일 발신 url
const checkEmailCodeUrl = "/member/checkEmailCode"; // 이메일 발신번호 확인 url
const sendSmsCodeUrl = "/member/sendSmsCode";//sms 발신 url
const checkSmsCodeUrl = "/member/checkSmsCode"; //sms 발신번호 확인 url

let timer;
let sendingConfirmNumber = 0;

// 인증 방식 선택 시 input 표시 제어 기능
const updateAuthMethodVisibility = () => {
	const idAuth = $("input[name='idAuthMethod']:checked").val();
	const pwAuth = $("input[name='passwordAuthMethod']:checked").val();

	if (idAuth === "phoneNum") {
		$('#memberPhoneNumberById').closest('.form-group').removeClass('hidden');
		$('#memberEmailById').closest('.form-group').addClass('hidden');
	} else {
		$('#memberPhoneNumberById').closest('.form-group').addClass('hidden');
		$('#memberEmailById').closest('.form-group').removeClass('hidden');
	}

	if (pwAuth === "phoneNum") {
		$('#memberPhoneNumberByPassword').closest('.form-group').removeClass('hidden');
		$('#memberEmailByPassword').closest('.form-group').addClass('hidden');
	} else {
		$('#memberPhoneNumberByPassword').closest('.form-group').addClass('hidden');
		$('#memberEmailByPassword').closest('.form-group').removeClass('hidden');
	}
};

// 본인 인증 완료 시 다른 버튼 멈추기 기능
const stopBtn = (isSendingConfirmNumber) => {
	$("#tab-id").css("pointer-events", isSendingConfirmNumber ? "none" : "auto");
	$("#tab-password").css("pointer-events", isSendingConfirmNumber ? "none" : "auto");
	$('input[type="radio"]').prop('disabled', isSendingConfirmNumber);
};
const checkConfirmNumber = (findType) => {
	console.log("인증번호 확인 종류 : [" + findType + "]");

	const inputConfirm = findType === "id" ? $("#confirmById") : $("#confirmByPassword");
	const inputCode = inputConfirm.val().trim();
	const authMethod = $("input[name='" + findType + "AuthMethod']:checked").val();

	const timerElement = findType === "id" ? $("#timerById") : $("#timerByPassword");
	const sendingconfirm = findType === "id" ? $("#sendingconfirmById") : $("#sendingconfirmByPassword");
	const confirmBtn = findType === "id"
		? (authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnById") : $("#confirmEmailBtnById"))
		: (authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnByPassword") : $("#confirmEmailBtnByPassword"));

	// 이메일 인증 (서버 비교 방식)
	if (authMethod === "email") {
		// 입력된 인증번호가 없다면
		if (!inputCode) {
			printSweetAlert("warning", "인증번호를 입력해주세요."); // 경고 안내 출력
			return;
		}
		// 서버 측에 인증번호 비교 요청
		$.ajax({
			type: "POST",
			url: checkEmailCodeUrl, // 인증번호 비교 컨트롤러
			data: { code: inputCode }, // 사용자가 입력한 인증번호
			dataType: "json", // JSON으로 받음

			// 요청 성공
			success: (res) => {
				if (res.valid) { // 인증 성공
					printSweetAlert("success", "성공적으로 인증됐습니다."); //메시지 출력
					clearInterval(timer); // 타이머 종료
					timerElement.addClass("hidden"); // 타이머 숨김
					sendingconfirm.addClass("hidden");
					inputConfirm.val("").prop("readonly", true); // 입력창 변경
					sendingConfirmNumber = 0; // 초기화
					$('button.submit-btn').prop('disabled', false); // 초기화 
					confirmBtn.text("재인증"); // 버튼 문구 변경
				} else { // 인증 실패
					printSweetAlert("warning", "옳지 않은 인증번호입니다."); //메시지 출력
				}
			},
			// 서버 오류 시
			error: () => {
				printSweetAlert("error", "서버 오류가 발생했습니다."); // 에러 메시지 출력
			}
		});

		return;
	}

	// 문자 인증 (서버 확인 방식)
	if (!inputCode) {
		printSweetAlert("warning", "인증번호를 입력해주세요.");
		return;
	}

	$.ajax({
		type: "POST",
		url: checkSmsCodeUrl,
		data: { authCode: inputCode },
		dataType: "json",
		success: (res) => {
			if (res.valid) {
				printSweetAlert("success", "성공적으로 인증됐습니다.");
				clearInterval(timer);
				timerElement.addClass("hidden");
				sendingconfirm.addClass("hidden");
				inputConfirm.val("").prop("readonly", true);
				sendingConfirmNumber = 0;
				$('button.submit-btn').prop('disabled', false);
				confirmBtn.text("재인증");
			} else {
				printSweetAlert("warning", "옳지 않은 인증번호입니다.");
			}
		},
		error: () => {
			printSweetAlert("error", "서버 오류가 발생했습니다.");
		}
	});
};
// 인증번호 전송 기능
const sendConfirmNumber = (findType, event) => {
	console.log("인증할 찾기 종류 : [" + findType + "]");
	let authMethod = $("input[name='" + findType + "AuthMethod']:checked").val();
	let authValue = authMethod === 'phoneNum'
		? $('#' + (findType === "id" ? 'memberPhoneNumberById' : 'memberPhoneNumberByPassword')).val().trim()
		: $('#' + (findType === "id" ? 'memberEmailById' : 'memberEmailByPassword')).val().trim();
	let inputConfirm = $("#" + (findType === "id" ? "confirmById" : "confirmByPassword"));

	if ($(event).text() === "재인증") {
		$(event).text("인증번호 전송");
		stopBtn(false);
		return;
	}

	// 인증번호가 발송된 상태인지 검사
	if (sendingConfirmNumber != 0) {
		$("#sendingconfirmBy" + (findType === "id" ? "Id" : "Password")).removeClass("hidden");
		return;
	}

	// 입력값 유효성 검사 함수 호출
	if (!checkAuthValue(authMethod, authValue)) {
		return;
	}

	if (authMethod === 'email') { // 이메일 인증이라면
		const emailParts = authValue.split('@');
		const emailId = emailParts[0];
		const emailDomain = emailParts[1];
		const birth = findType === "id" ? $('#memberBirthById').val().trim() : $('#memberBirthByPassword').val().trim();

		$.ajax({
			type: "POST",
			url: verifyEmailCodeUrl,
			data: {
				memberEmailId: emailId,
				memberEmailDomain: emailDomain,
				memberBirth: birth
			},
			dataType: "json",
			success: (res) => {
				// 스위트 알럿창 출력
				printSweetAlert("", res.message); // 인증번호가 이메일로 전송되었습니다
				if (res.code) {
					sendingConfirmNumber = res.code; // 서버에서 받은 인증번호 저장
					console.log("서버로부터 받은 인증번호 : " + sendingConfirmNumber);
				}
				stopBtn(true);
				inputConfirm.prop("readonly", false);
				startConfirmTimer(findType);
			},
			error: (xhr, status, error) => {
				console.error("이메일 인증 AJAX 에러 발생", xhr.status, status, error);
				// 스위트 알럿창 출력
				printSweetAlert("error", "이메일 인증번호 전송 중 오류가 발생했습니다.");
			}
		});
		return; // 아래 코드 실행하지 않도록 막기
	}
	else {//문자라면 (핸드폰번호인증)
		$.ajax({
			type: "POST",
			url: sendSmsCodeUrl,
			data: { phoneNumber: authValue },
			dataType: "json",
			success: (res) => {
				if (res.status === "success") {
					printSweetAlert("success", "인증번호가 발송되었습니다.");
					stopBtn(true);
					inputConfirm.prop("readonly", false);
					startConfirmTimer(findType);
				} else {
					printSweetAlert("error", "인증번호 전송 실패");
				}
			},
			error: (xhr) => {
				console.error("문자 인증 에러", xhr);
				printSweetAlert("error", "서버 오류");
			}
		});
	}
};
// 인증번호 타이머 기능
const startConfirmTimer = (findType) => {
	clearInterval(timer);
	let timeLeft = 300;
	timer = setInterval(() => {
		let minutes = Math.floor(timeLeft / 60);
		let seconds = timeLeft % 60;
		let timerElement = $("#" + (findType === "id" ? "timerById" : "timerByPassword"));
		let sendingconfirm = $("#" + (findType === "id" ? "sendingconfirmById" : "sendingconfirmByPassword"));
		let inputConfirm = $("#" + (findType === "id" ? "confirmById" : "confirmByPassword"));

		timerElement.text(`${minutes}:${seconds < 10 ? '0' : ''}${seconds}`).removeClass("hidden");

		if (timeLeft <= 0) {
			clearInterval(timer);
			// 스위트 알럿창 출력
			printSweetAlert("warning", "인증시간이 초과하여 종료합니다.");
			timerElement.addClass("hidden");
			sendingconfirm.addClass("hidden");
			inputConfirm.prop("readonly", true);
			sendingConfirmNumber = 0;
			stopBtn(false);
		}
		timeLeft--;
	}, 1000);
};
// 본인 인증용 입력값 유효성 검사 기능
const checkAuthValue = (authMethod, authValue) => {
	const phoneNumPattern = /^(?!(\d)\1{10})\d{11}$/;
	const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	if (!authValue) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "휴대폰번호 혹은 이메일 주소를 입력해주세요.");
		return false;
	}

	if (authMethod === "phoneNum" && !phoneNumPattern.test(authValue)) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "휴대폰번호를 모두 같지 않은 숫자로만 11자리 입력해주세요.");
		return false;
	}
	if (authMethod === "email" && !emailPattern.test(authValue)) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "옳은 이메일 주소 형태를 입력해주세요.");
		return false;
	}
	return true;
};

// 비밀번호 찾기 기능
const findMemberPassword = () => {
	const memberId = $("#memberIdByPassword").val().trim();
	const memberBirth = $("#memberBirthByPassword").val().trim();
	const authMethod = $("input[name='passwordAuthMethod']:checked").val();
	const authValue = authMethod === 'phoneNum'
		? $('#memberPhoneNumberByPassword').val().trim()
		: $('#memberEmailByPassword').val().trim();

	console.log("memberId : [" + memberId + "]");
	console.log("memberBirth : [" + memberBirth + "]");
	console.log("authValue : [" + authValue + "]");

	// 모든 정보 미입력 시
	if (!memberBirth || !authValue || !memberId) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "모든 정보를 입력해주세요.");
		return;
	}

	$.ajax({
		type: "POST",
		url: findMemberPasswordUrl,
		data: { memberId, memberBirth, authMethod, authValue },
		dataType: "text",
		success: (response) => {
			// 존재하는 계정일 경우
			if (response == 0) {
				console.log("비밀번호 찾기 성공");
				changeMemberPassword();
			}
			// 존재하지 않는 계정일 경우
			else if (response == 1) {
				console.log("존재하지 않는 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "존재하지 않는 계정입니다.");
			}
			// 탈퇴한 계정일 경우
			else if (response == 2) {
				console.log("탈퇴한 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "탈퇴한 계정입니다.");
			}
			// 간편 로그인 계정일 경우
			else if (response == 3) {
				console.log("간편 로그인 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "간편 로그인 계정입니다.");
			}
		},
		error: (xhr, status, error) => {
			console.error("AJAX 에러 발생", xhr.status, status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
};

// 아이디 찾기 기능
const findMemberId = () => {
	const memberBirth = $('#memberBirthById').val().trim();
	const authMethod = $("input[name='idAuthMethod']:checked").val();
	const authValue = authMethod === 'phoneNum'
		? $('#memberPhoneNumberById').val().trim()
		: $('#memberEmailById').val().trim();

	if (!memberBirth || !authValue) {
		// 스위트 알럿창 출력
		printSweetAlert("warning", "모든 정보를 입력해주세요.");
		return;
	}

	$.ajax({
		type: "POST",
		url: findMemberIdUrl,
		data: { memberBirth, authMethod, authValue },
		dataType: "text",
		success: (response) => {
			// 존재하는 계정일 경우
			if (response.status == 0) {
				console.log("아이디 찾기 성공");
				// 스위트 알럿창 출력
				printSweetAlert("", "귀하의 아이디 : " + response.memberId, "", "main.do");
			}
			// 존재하지 않는 계정일 경우
			else if (response == 1) {
				console.log("존재하지 않는 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "존재하지 않는 계정입니다.");
			}
			// 탈퇴한 계정일 경우
			else if (response == 2) {
				console.log("탈퇴한 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "탈퇴한 계정입니다.");
			}
			// 간편 로그인 계정일 경우
			else if (response == 3) {
				console.log("간편 로그인 계정");
				// 스위트 알럿창 출력
				printSweetAlert("warning", "간편 로그인 계정입니다.");
			}
		},
		error: (status, error) => {
			console.error("AJAX 에러 발생", status, error);
			// 스위트 알럿창 출력
			printSweetAlert("error", "서버에 문제가 발생했습니다.", "지속될 시 관리자에게 문의하세요.", "error.do");
		}
	});
};

// 아이디/비밀번호 선택에 따라 폼 변경 기능
const showTab = (tab) => {
	if (tab === 'id') {
		$('#id-form').removeClass('hidden');
		$('#password-form').addClass('hidden');
		$('#tab-id').addClass('active');
		$('#tab-password').removeClass('active');
	} else {
		$('#id-form').addClass('hidden');
		$('#password-form').removeClass('hidden');
		$('#tab-id').removeClass('active');
		$('#tab-password').addClass('active');
	}
};

// 생년월일 오늘 날짜까지 한계지정 기능
const limitInputBirth = () => {
	const today = new Date();
	const todayDate = today.toISOString().split('T')[0];
	$("#memberBirthById").attr("max", todayDate);
	$("#memberBirthByPassword").attr("max", todayDate);
};

// 비밀번호 재설정 기능
const changeMemberPassword = () => {
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
			printSweetAlert("info", "비밀번호 재설정이 취소되었습니다.", "", "findAccount.do");
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
				changeMemberPassword(); // 재귀 호출
				return;
			}
			console.log("비밀번호 변경 비동기 실행");

			const memberId = $("#memberIdByPassword").val().trim();
			// AJAX 요청
			$.ajax({
				type: "POST",
				url: updateMemberPasswordUrl,
				data: {
					memberId: memberId,
					newPassword: result.value.trim()
				},
				dataType: "text",
				success: (response) => {
					if (response === "true") {
						printSweetAlert("info", "비밀번호가 변경되었습니다.", "로그인 페이지로 이동합니다.", "login.do");
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
};

// 초기 기능 실행 기능
const runPostLoginSetup = () => {
	limitInputBirth(); // 생년월일 오늘 날짜까지 한계지정 함수 호출
	updateAuthMethodVisibility(); // 인증 방식 선택 시 input 표시 제어 함수 호출
}

// 초기 이벤트 리스너 등록 기능
const initEventListeners = () => {
	// 인증 방식 선택 클릭 이벤트 위임
	$('input[name="idAuthMethod"]').on('change', updateAuthMethodVisibility);
	$('input[name="passwordAuthMethod"]').on('change', updateAuthMethodVisibility);

	// 비밀번호 찾기 버튼 클릭 이벤트 위임
	$('#password-form .submit-btn').on('click', function(event) {
		event.preventDefault();
		if (!this.checkValidity()) {
			event.stopPropagation();
		} else {
			// 본인 인증번호 발송 중일 시
			if (sendingConfirmNumber !== 0) {
				// 스위트 알럿창 출력
				printSweetAlert("warning", "인증번호가 발송중입니다.");
				return;
			}

			const authMethod = $("input[name='passwordAuthMethod']:checked").val();
			let confirmBtn = authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnByPassword") : $("#confirmEmailBtnByPassword");
			// 본인 인증 미완료 시
			if (confirmBtn.text() !== "재인증") {
				// 스위트 알럿창 출력
				printSweetAlert("warning", "본인 인증을 진행해주세요!");
				return;
			}
			findMemberPassword(); // 비밀번호 찾기 함수 호출
		}
	});

	// 아이디 찾기 버튼 클릭 이벤트 위임
	$('#id-form .submit-btn').on('click', function(event) {
		event.preventDefault();
		if (!this.checkValidity()) {
			event.stopPropagation();
		} else {
			// 본인 인증번호 발송 중일 시
			if (sendingConfirmNumber !== 0) {
				// 스위트 알럿창 출력
				printSweetAlert("warning", "인증번호가 발송중입니다.");
				return;
			}

			const authMethod = $("input[name='idAuthMethod']:checked").val();
			let confirmBtn = authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnById") : $("#confirmEmailBtnById");
			if (confirmBtn.text() !== "재인증") { // 본인 인증 미완료 시
				// 스위트 알럿창 출력
				printSweetAlert("warning", "본인 인증을 진행해주세요!");
				return;
			}
			findMemberId(); // 아이디 찾기 함수 호출
		}
	});
}

// 비동기 초기화 기능
async function initPage() {
	// 로그인 상태에 맞는 회원번호 반환 함수 호출
	loginedMemberNumber = await getLoginedMemberNumber();
	console.log("로그인한 회원번호 : [" + loginedMemberNumber + "]");

	if (loginedMemberNumber) { // 로그인 상태라면
		// 스위트 알럿창 출력
		printSweetAlert("error", "올바르지 않은 접근입니다.", "메인페이지로 이동합니다.", "main.do");
	}

	// 초기 이벤트 리스너 등록 함수 호출
	initEventListeners();
	// 초기 기능 실행 함수 호출
	runPostLoginSetup();
}

$(document).ready(() => {
	initPage(); // 비동기 초기화 함수 호출
});