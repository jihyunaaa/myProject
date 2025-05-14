<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>회원가입 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<!-- 사용자 정의 css -->
<link href="assets/css/customization/join.css" type="text/css"
	rel="stylesheet">
<!-- 스위트 알럿 css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
<style>
#logoImg {
	display: block;
	margin: 0 auto;
}
</style>
</head>
<body>
	<div class="container">
		<div class="input-form-backgroud row">
			<div class="input-form col-md-12 mx-auto">
				<a href="main.do"><img id="logoImg" alt="로고"
					src="assets/img/logo/joinLogo.jpg"></a> <a href="main.do">&lt;
					Home 이동</a>
				<h1 class="mb-3">회원가입</h1>
				<form id="validation-form" novalidate action="javascript:void(0);">
					<div class="mb-3">
						<label for="memberId">아이디</label> <input type="text"
							name="memberId" class="form-control" id="memberId"
							placeholder="아이디를 입력해주세요" onblur="checkJoinMemberId(event)"
							pattern="^[a-z][a-z0-9]{2,15}$" maxlength="15" required>
						<div class="invalid-feedback" id="memberIdFeedback">영어와 숫자만
							세글자 이상 입력</div>
					</div>

					<div class="mb-3">
						<label for="memberPassword">비밀번호</label> <input type="password"
							class="form-control" id="memberPassword" name="memberPassword"
							placeholder="특수문자가 하나 이상 포함된 여섯 글자 이상을 입력하세요"
							pattern="^(?!([\d])\1{5,})(?=.*[\W_]).{6,}$" maxlength="15"
							required onkeyup="checkConfirmMemberPassword()">
						<div class="invalid-feedback">특수문자가 하나 이상 포함된 전부 동일하지 않은 여섯
							글자 이상을 입력 (-제외)</div>
					</div>

					<div class="mb-3">
						<label for="confirmMemberPassword">비밀번호 확인</label> <input
							type="password" class="form-control" id="confirmMemberPassword"
							name="confirmMemberPassword" placeholder="이전 입력하신 비밀번호를 입력하세요"
							onkeyup="checkConfirmMemberPassword()" required>
						<div id="confirmMemberPasswordFeedback" class="invalid-feedback">비밀번호
							불일치</div>
					</div>

					<div class="row g-3 mb-3">
						<div class="col-sm-4">
							<label for="joinAddress">이메일</label> <input type="text"
								id="memberEmailId" name="memberEmailId" class="form-control"
								placeholder="이메일" aria-label="City" pattern="^[a-zA-Z0-9]+$"
								maxlength="15" required>
							<div class="invalid-feedback">영어와 숫자만 입력</div>
						</div>
						<div class="col-sm">
							<label class="visually-hidden" for="autoSizingInputGroup">&nbsp;</label>
							<div class="input-group">
								<div class="input-group-text">@</div>
								<select class="form-control" name="memberEmailDomain"
									id="selectEmailAddress" onchange="onChangeEmailAddress(value)">
									<option value="setInputEmailAddress">직접입력</option>
									<option>google.com</option>
									<option>naver.com</option>
									<option>daum.net</option>
									<option>hanmail.net</option>
								</select>
							</div>
						</div>
						<div class="col-sm">
							<label for="inputState" class="form-label">&nbsp;</label> <input
								id="inputEmailAddress" type="text" class="form-control"
								placeholder="직접입력" aria-label="Zip"
								pattern="^[a-zA-Z0-9]+\.+[a-zA-Z]{3,}$" maxlength="15" required>
						</div>
					</div>

					<div class="row g-3 mb-3">
						<div class="col-sm-6">
							<label for="memberName">이름</label> <input type="text"
								name="memberName" class="form-control" id="memberName"
								placeholder="이름을 입력해주세요" maxlength="15" required>
						</div>
						<div class="col-sm">
							<label for="memberBirth">생년월일</label> <input type="date"
								name="memberBirth" class="form-control" id="memberBirth"
								required>
						</div>
					</div>

					<div class="row g-3 mb-3">
						<div class="col-sm-6">
							<label>핸드폰 번호 &nbsp;<input
								id="memberPhoneNumberConfirmBtn" type="button"
								class="btn btn-warning" value="인증"
								onclick="memberPhoneNumberConfirm()">
							</label> <input type="tel" name="memberPhoneNumber" class="form-control"
								id="memberPhoneNumber" placeholder="핸드폰 번호를 입력해주세요(-제외)"
								pattern="^(?!(\d)\1{10})\d{11}$"
								title="올바른 11자리 휴대폰 번호를 입력해주세요. (같은 숫자 반복은 불가)" maxlength="11"
								required>
							<div class="invalid-feedback" id="memberPhoneNumberFeedback">인증
								필수</div>
						</div>
						<div class="col-sm" id="phoneNumberConfirmWrapper"></div>
					</div>

					<div class="mb-3">
						<label>주소 &nbsp;<input type="button"
							onclick="kakaoPostAPIcode()" class="btn btn-warning" value="검색"></label>
						<input type="text" name="memberAddressMain" class="form-control"
							id="memberAddressMain" placeholder="도로명 주소" readonly> <input
							type="text" name="memberAddressDetail" class="form-control"
							id="memberAddressDetail"
							pattern="^(?=.*[가-힣])(?=.*[0-9])[가-힣a-zA-Z0-9\s\-.,#]{2,50}$"
							title="상세 주소는 한글과 숫자를 포함한 2~50자의 값만 입력 가능합니다."
							placeholder="상세 주소" readonly>
					</div>

					<hr class="mb-4">
					<div class="custom-control custom-checkbox">
						<input type="checkbox" class="custom-control-input" id="aggrement"
							required> <label class="custom-control-label"
							for="aggrement">개인정보 수집 및 이용에 동의합니다.</label>
					</div>
					<div class="mb-4"></div>
					<button class="btn btn-warning btn-lg btn-block" type="submit">가입
						완료</button>
				</form>
			</div>
		</div>
	</div>

	<!-- jQuery js -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	
	<!-- 주소 검색 js -->
	<script
		src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey={api키}&libraries=services"></script>
	
	<!-- 스위트 알럿 js -->
	<script
		src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>
	
	<!-- 서버에서 로그인 정보 확인 js -->
	<script src="assets/js/customization/loginStatus.js"></script>
		
	<!-- 회원가입 js -->
	<script src="assets/js/customization/join.js"></script>
</body>
</html>