<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>헤더 - 꿀조합팩토리</title>
<!-- Bootstrap Styles-->
<link href="assets/admin/css/bootstrap.css" rel="stylesheet" />
<!-- FontAwesome Styles-->
<link href="assets/admin/css/font-awesome.css" rel="stylesheet" />
<!-- Morris Chart Styles-->
<link href="assets/admin/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Custom Styles-->
<link href="assets/admin/css/custom-styles.css" rel="stylesheet" />
<!-- Google Fonts-->
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
    rel='stylesheet' type='text/css' />
<link rel="stylesheet" href="assets/admin/js/Lightweight-Chart/cssCharts.css">
<!-- 스위트 알럿 css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
</head>
<body>
<!-- 네비게이션바 -->
		<nav class="navbar navbar-default top-navbar" role="navigation">
			<div class="navbar-header">
				<!-- 화면 상단의 메뉴 버튼 요소 -->
				<button type="button" class="navbar-toggle waves-effect waves-dark"
					data-toggle="collapse" data-target=".sidebar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

				<a class="navbar-brand waves-effect waves-dark"
					href="/admin/member"> <i class="large material-icons"></i>
					<strong>HoneyComboFactory</strong>
				</a>

			</div>
			<!-- 사이드바 드롭다운 -->
			<ul class="nav navbar-top-links navbar-right">
				<li><a class="dropdown-button waves-effect waves-dark" href="#"
					data-activates="dropdown1"><b>${loginedMemberName}
							<!--  로그인한 관리자 이름 출력 -->
					</b>관리자</a></li>
			</ul>
		</nav>
		
		<nav class="navbar-default navbar-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav" id="main-menu">
					<!-- 1. 회원관리 -->
					<li><a class="active-menu waves-effect waves-dark"
						href="manageMember.do">회원 관리</a></li>
					<!-- 2. 상품관리 (드롭다운 포함) -->
					<li class="dropdown"><a href="/admin/product/combo"
						class="dropdown-toggle">상품관리</a> <!-- 꿀조합 상품을 기본으로 함 -->
						<ul class="dropdown-menu">
							<li><a href="manageCUProduct.do">CU</a></li>
							<li><a href="manageGSProduct.do">GS25</a></li>
							<li><a href="manageComboProduct.do">꿀조합</a></li>
						</ul></li>
					<!-- 3. 게시판 관리 -->
					<li><a href="manageBoardCombo.do"
						class="waves-effect waves-dark">게시판 관리</a>
					<!-- css 적용 위해 클래스 추가 -->
					<!-- 4. 로그아웃 -->
					<li><a href="javascript:void(0);" onclick="logout()" class="waves-effect waves-dark"></i>로그아웃</a></li>
				</ul>
			</div>
		</nav>
		
	<!-- JS Scripts-->
	<!-- jQuery Js -->
	<script
		src="assets/admin/js/jquery-1.10.2.js"></script>
	<!-- Bootstrap Js -->
	<script
		src="assets/admin/js/bootstrap.min.js"></script>
	<!-- Metis Menu Js -->
	<script
		src="assets/admin/js/jquery.metisMenu.js"></script>
	<!-- Custom Js -->
    <script src="assets/admin/js/custom-scripts.js"></script>
	
	<!-- 스위트 알럿 js -->
	<script
		src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>
	
	<!-- 스위트 알럿 출력 js -->
	<script src="assets/js/customization/sweetalert.js"></script>
	
	<!-- 서버에서 로그인 정보 확인 js -->
	<script src="assets/admin/js/customization/loginStatus.js"></script>
	
	<!-- 관리자 헤더 js -->
	<script type="text/javascript"
		src="assets/admin/js/customization/nav.js"></script>
</body>
</html>