<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>헤더 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">

<!-- CSS here -->
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<link rel="stylesheet" href="assets/css/owl.carousel.min.css">
<link rel="stylesheet" href="assets/css/flaticon.css">
<link rel="stylesheet" href="assets/css/slicknav.css">
<link rel="stylesheet" href="assets/css/animate.min.css">
<link rel="stylesheet" href="assets/css/magnific-popup.css">
<link rel="stylesheet" href="assets/css/fontawesome-all.min.css">
<link rel="stylesheet" href="assets/css/themify-icons.css">
<link rel="stylesheet" href="assets/css/slick.css">
<link rel="stylesheet" href="assets/css/nice-select.css">
<link rel="stylesheet" href="assets/css/style.css">
<!-- 스위트 알럿 css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
<style>
header {
	background-color: rgb(255, 255, 165);
}
</style>
</head>
<body>
	<!--? Preloader Start -->
	<div id="preloader-active">
		<div
			class="preloader d-flex align-items-center justify-content-center">
			<div class="preloader-inner position-relative">
				<div class="preloader-circle"></div>
				<div class="preloader-img pere-text">
					<img src="assets/img/loading/bee.png" alt="꿀벌 이미지">
				</div>
			</div>
		</div>
	</div>
	<!-- Preloader Start -->
	<header>
		<!-- Header Start -->
		<div class="header-area">
			<div class="main-header header-sticky">
				<div class="container-fluid">
					<div class="menu-wrapper">
						<!-- Logo -->
						<div class="logo">
							<a href="main.do"><img src="assets/img/logo/headerLogo.png"
								alt="로고 이미지"></a>
						</div>
						<!-- Main-menu -->
						<div class="main-menu d-none d-lg-block">
							<nav>
								<ul id="navigation">
									<li><a href="main.do">Home</a></li>
									<li><a href="#">편의점</a>
										<ul class="submenu">
											<li><a href="CUProduct.do">CU</a></li>
											<li><a href="GSProduct.do">GS25</a></li>
										</ul></li>
									<li><a href="comboBoard.do">꿀조합 게시판</a></li>
									<li class="hot"><a href="comboProduct.do">꿀조합</a></li>
									<c:if test="${empty loginedMemberNumber}">
										<li><a href="login.do">로그인</a></li>
									</c:if>
									<c:if test="${not empty loginedMemberNumber}">
										<li><a href="myInfo.do">내정보</a></li>
										<li><a href="cart.do">장바구니</a></li>
										<li><a href="javascript:void(0);" onclick="logout()">로그아웃</a></li>
									</c:if>
								</ul>
							</nav>
						</div>
						<!-- Header Right 
						<div class="header-right">
							<ul>
								<c:if test="${empty loginedMemberNumber}">
									<li>비회원</li>
								</c:if>
								<c:if test="${not empty loginedMemberNumber}">
									<li>${loginedMemberName}</li>
								</c:if>
								<li><a href="javascript:void(0);" onclick="location.href='cart.do'"><span
										class="flaticon-shopping-cart"></span></a></li>
							</ul>
						</div>
						-->
					</div>
					<!-- Mobile Menu -->
					<div class="col-12">
						<div class="mobile_menu d-block d-lg-none"></div>
					</div>
				</div>
			</div>
		</div>
		<!-- Header End -->
	</header>

	<!-- JS here -->

	<script src="assets/js/vendor/modernizr-3.5.0.min.js"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="assets/js/vendor/jquery-1.12.4.min.js"></script>
	<script src="assets/js/popper.min.js"></script>
	<script src="assets/js/bootstrap.min.js"></script>
	<!-- Jquery Mobile Menu -->
	<script src="assets/js/jquery.slicknav.min.js"></script>

	<!-- Jquery Slick , Owl-Carousel Plugins -->
	<script src="assets/js/owl.carousel.min.js"></script>
	<script src="assets/js/slick.min.js"></script>

	<!-- One Page, Animated-HeadLin -->
	<script src="assets/js/wow.min.js"></script>
	<script src="assets/js/animated.headline.js"></script>
	<script src="assets/js/jquery.magnific-popup.js"></script>

	<!-- Scrollup, nice-select, sticky -->
	<script src="assets/js/jquery.scrollUp.min.js"></script>
	<script src="assets/js/jquery.nice-select.min.js"></script>
	<script src="assets/js/jquery.sticky.js"></script>

	<!-- contact js -->
	<script src="assets/js/contact.js"></script>
	<script src="assets/js/jquery.form.js"></script>
	<script src="assets/js/jquery.validate.min.js"></script>
	<script src="assets/js/mail-script.js"></script>
	<script src="assets/js/jquery.ajaxchimp.min.js"></script>

	<!-- Jquery Plugins, main Jquery -->
	<script src="assets/js/plugins.js"></script>
	<script src="assets/js/main.js"></script>
	
	<!-- 스위트 알럿 CDN js -->
	<script
		src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>
		
	<!-- 스위트 알럿 출력 js -->
	<script src="assets/js/customization/sweetalert.js"></script>
	
	<!-- 서버에서 로그인 정보 확인 js -->
	<script src="assets/js/customization/loginStatus.js"></script>
	
	<!-- 헤더 js -->
	<script src="assets/js/customization/header.js"></script>
</body>
</html>