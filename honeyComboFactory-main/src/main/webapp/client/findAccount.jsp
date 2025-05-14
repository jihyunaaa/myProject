<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>계정 찾기 - 꿀조합팩토리</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">

<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/findAccount.css">
</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<br>
	<br>
	<main>
		<!--================login_part Area =================-->
		<section class="login_part">
			<div class="container">
				<div class="row align-items-center justify-content-center">
					<div class="col-lg-6 col-md-5">
						<div class="tab-menu">
							<div id="tab-id" class="active" onclick="showTab('id')">아이디
								찾기</div>
							<div id="tab-password" onclick="showTab('password')">비밀번호
								찾기</div>
						</div>
						<!-- 아이디 찾기 Start -->
						<form id="id-form">
							<div class="form-group">
								<label> <input type="radio" name="idAuthMethod"
									value="phoneNum" checked> 회원정보에 등록한 휴대전화로 인증
								</label> <label> <input type="radio" name="idAuthMethod"
									value="email"> 본인확인 이메일로 인증
								</label>
							</div>
							<div class="form-group">
								<input type="date" id="memberBirthById" required>
							</div>
							<div class="form-group">
								<input type="text" placeholder="휴대전화번호 입력 (-제외)"
									id="memberPhoneNumberById" maxlength="11" pattern="[0-9]{11}"
									required> <span id="sendingconfirmById"
									style="color: red;" class="hidden">인증번호가 발송된 상태</span>&nbsp;
								<button id="confirmPhoneNumberBtnById"
									onclick="sendConfirmNumber('id',this)" type="button">인증번호
									전송</button>
							</div>
							<div class="form-group hidden">
								<input type="text" placeholder="이메일 입력" id="memberEmailById"
									pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
									required>

								<button id="confirmEmailBtnById"
									onclick="sendConfirmNumber('id',this)" type="button">인증번호
									전송</button>
							</div>
							<div class="form-group" id="confirmWrapperById">
								<input type="text" placeholder="인증번호 입력" id="confirmById"
									readonly> <span id="timerById" style="color: red;"
									class="hidden">5:00</span>&nbsp;
								<button onclick="checkConfirmNumber('id')" type="button">확인</button>
							</div>
							<button class="submit-btn">아이디 찾기</button>
						</form>
						<!-- 아이디 찾기 End -->

						<!-- 비밀번호 찾기 Start -->
						<form id="password-form" class="hidden">
							<div class="form-group">
								<label> <input type="radio" name="passwordAuthMethod"
									value="phoneNum" checked>회원정보에 등록한 휴대전화로 인증
								</label> <label> <input type="radio" name="passwordAuthMethod"
									value="email">본인확인 이메일로 인증
								</label>
							</div>
							<div class="form-group">
								<input type="date" id="memberBirthByPassword" required>
							</div>
							<div class="form-group">
								<input type="text" placeholder="아이디 입력" id="memberIdByPassword"
									maxlength="15" required>
							</div>
							<div class="form-group">
								<input type="text" placeholder="휴대전화번호 입력 (-제외)"
									id="memberPhoneNumberByPassword" maxlength="11"
									pattern="[0-9]{11}" required> <span
									id="sendingconfirmByPassword" style="color: red;"
									class="hidden">인증번호가 발송된 상태</span>&nbsp;
								<button id="confirmPhoneNumberBtnByPassword"
									onclick="sendConfirmNumber('password',this)" type="button">인증번호
									전송</button>
							</div>
							<div class="form-group hidden">
								<input type="text" placeholder="이메일 입력"
									id="memberEmailByPassword"
									pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
									required>

								<button id="confirmEmailBtnByPassword"
									onclick="sendConfirmNumber('password',this)" type="button">인증번호
									전송</button>
							</div>
							<div class="form-group" id="confirmWrapperByPassword">
								<input type="text" placeholder="인증번호 입력" id="confirmByPassword"
									readonly> <span id="timerByPassword"
									style="color: red;" class="hidden">5:00</span>&nbsp;
								<button onclick="checkConfirmNumber('password')" type="button">확인</button>
							</div>
							<button class="submit-btn">비밀번호 찾기</button>
						</form>
						<!-- 비밀번호 찾기 End -->
					</div>
				</div>
			</div>
		</section>
		<!--================login_part end =================-->
	</main>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<!-- 푸터 영역 -->
	<%@ include file="footer.jsp"%>

	<!-- 사용자 정의 js -->
	<script src="assets/js/customization/findAccount.js"></script>
</body>
</html>