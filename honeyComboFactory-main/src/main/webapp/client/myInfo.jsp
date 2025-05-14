<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>내정보 - 꿀조합팩토리</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link href="assets/css/customization/board.css" type="text/css"
	rel="stylesheet">
</head>

<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<br>
	<br>
	<br>
	<main>
		<!--================Checkout Area =================-->
		<section class="checkout_area">
			<div class="container">
				<div class="cupon_area">
					<div class="check_title">
						<h2>작성글</h2>
					</div>
					<br>
					<!-- 게시글 Start -->
					<div class="tab-content" id="nav-tabContent">
						<table id="boardTable">
							<thead>
								<tr>
									<th>번호</th>
									<th>제목</th>
									<th>조회수</th>
									<th>좋아요</th>
									<th>작성일</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 게시글 End -->
					<!-- 페이지네이션 Start -->
					<nav class="blog-pagination justify-content-center d-flex">
						<ul class="pagination" id="myBoardPageNation">
						</ul>
					</nav>
					<!-- 페이지네이션 End -->
				</div>
				<br>
				<div class="billing_details">
					<div class="row">
						<div class="col-lg-8">
							<h3>
								등록 정보 <input id="changePasswordBtn" type="button"
									onclick="changeMemberPassword()"
									class="small genric-btn warning radius" value="비밀번호 변경">
							</h3>
							<form id="memberInfoForm" class="row contact_form">
								<div class="col-md-12 form-group">
									<label for="first">아이디</label> <input type="password"
										class="form-control" id="memberId" name="memberId"
										value="${myInfoData.memberId}" disabled />
								</div>
								<div class="col-md-12 form-group">
									<label for="first">이름</label> <input type="password"
										class="form-control" id="memberName" name="memberName"
										placeholder="15자 이내로 이름을 입력하세요"
										value="${myInfoData.memberName}" maxlength="15"
										data-original="${myInfoData.memberName}" disabled required />
								</div>
								<div class="col-md-6 form-group">
									<label for="first">핸드폰 번호<input
										id="confirmPhoneNumberBtn" type="hidden"
										onclick="confirmPhoneNumber()"
										class="small genric-btn warning radius" value="인증 완료"></label>
									<input type="password" class="form-control"
										id="memberPhoneNumber" name="memberPhoneNumber"
										placeholder="핸드폰 번호를 입력해주세요(-제외)" pattern="[0-9]{11}"
										maxlength="11" title="숫자 11자리 입력 필수(-제외)"
										value="${myInfoData.memberPhoneNumber}"
										onblur="checkMemberPhoneNumber(event)"
										data-original="${myInfoData.memberPhoneNumber}" disabled
										required />
								</div>
								<div class="col-md-6 form-group">
									<label for="first">인증 번호<input
										id="checkConfirmPhoneNumberBtn" type="hidden"
										onclick="checkConfirmPhoneNumber()"
										class="small genric-btn warning radius" value="확인">&nbsp;<span
										id="confirmTimer" style="color: red; display: none;">5:00</span></label>
									<input type="text" class="form-control"
										id="phoneNumberConfirmNumber" disabled required />
								</div>
								<div class="col-md-6 form-group">
									<label for="first">주소 <input id="memberAddressBtn"
										onclick="kakaoPostAPIcode()" type="hidden"
										class="small genric-btn warning radius" value="검색"></label> <input
										type="password" class="form-control"
										value="${myInfoData.memberAddressMain}"
										data-original="${myInfoData.memberAddressMain}"
										id="memberAddressMain" name="memberAddressMain" disabled
										required />
								</div>
								<div class="col-md-6 form-group">
									<label for="first">&nbsp;</label> <input type="password"
										class="form-control" value="${myInfoData.memberAddressDetail}"
										data-original="${myInfoData.memberAddressDetail}"
										pattern="^(?=.*[가-힣])(?=.*[0-9])[가-힣a-zA-Z0-9\s\-.,#]{2,50}$"
										title="상세 주소는 한글과 숫자를 포함한 2~50자의 값만 입력 가능합니다."
										id="memberAddressDetail" name="memberAddressDetail" disabled
										required />
								</div>
								<div class="col-md-12 form-group">
									<label for="first">생년월일</label> <input type="password"
										class="form-control" id="memberBirth" name="memberBirth"
										value="${myInfoData.memberBirth}" disabled />
								</div>
								<div class="col-md-5 form-group">
									<label for="first">이메일</label> <input type="password"
										class="form-control" id="memberEmailId"
										value="${myInfoData.memberEmailId}"
										placeholder="영어와 숫자만 입력하세요"
										data-original="${myInfoData.memberEmailId}" title="영어와 숫자만 입력"
										pattern="^[a-zA-Z0-9]+$" name="memberEmailId" disabled
										required />
								</div>
								<div class="col-md-1 form-group">
									<label>&nbsp;</label> <input type="text" value="@"
										class="form-control" disabled />
								</div>
								<div class="col-md-6 form-group">
									<label for="first">&nbsp;</label> <input type="password"
										class="form-control" value="${myInfoData.memberEmailDomain}"
										data-original="${myInfoData.memberEmailDomain}"
										placeholder="이메일 주소를 입력해주세요(점(.) 포함)"
										pattern="^[a-zA-Z0-9]+\.+[a-zA-Z]{3,}$" maxlength="15"
										id="memberEmailDomain" name="memberEmailDomain" disabled
										required />
								</div>
								<button id="updateMyInfoBtn" type="submit"
									class="btn_3 genric-btn warning radius">정보수정</button>
								&nbsp;&nbsp;
								<button class="btn_3 genric-btn danger radius" type="button"
									onclick="withdraw()">회원탈퇴</button>
							</form>
						</div>
						<div class="col-lg-4">
							<br>
							<div class="order_box">
								<h2>주문 내역</h2>
								<ul class="list list_2">
									<li><a href="javascript:void(0);">결제번호 <span>비용</span>
									</a></li>
								</ul>
								<ul class="list" id="purchaseListWrapper">
								</ul>
								<br>
								<!-- 페이지네이션 Start -->
								<nav class="justify-content-center d-flex">
									<ul class="pagination" id="purchaseListPageNation">
									</ul>
								</nav>
								<!-- 페이지네이션 End -->
							</div>
							<div class="order_box">
								<h2>좋아요 글 내역</h2>
								<ul class="list list_2">
									<li><a href="javascript:void(0);">번호 <span>작성자</span>
									</a></li>
								</ul>
								<ul class="list" id="likedBoardWrapper">
								</ul>
								<br>
								<!-- 페이지네이션 Start -->
								<nav class="justify-content-center d-flex">
									<ul class="pagination" id="likedBoardPageNation">
									</ul>
								</nav>
								<!-- 페이지네이션 End -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
		<!--================End Checkout Area =================-->
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
	<script type="text/javascript" src="assets/js/customization/myInfo.js"></script>
	<!-- 주소 검색 js -->
	<script
		src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey={api키}&libraries=services"></script>
</body>
</html>