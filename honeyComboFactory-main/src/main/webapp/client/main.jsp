<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>꿀조합팩토리</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">

<!-- 사용자 정의 css -->
<link href="assets/css/customization/board.css" type="text/css"
	rel="stylesheet">

<style type="text/css">
.video-area {
	background-image: url(assets/img/advertisement/mainVideoImg.png)
		!important;
}
</style>

</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<!--? slider Area Start -->
		<div class="slider-area">
			<div class="slider-active">
				<!-- 1번 Slider Start -->
				<div
					class="single-slider slider-height d-flex align-items-center slide-bg">
					<div class="container">
						<div class="row justify-content-between align-items-center">
							<div class="col-xl-8 col-lg-8 col-md-8 col-sm-8">
								<div class="hero__caption">
									<h1 data-animation="fadeInLeft" data-delay=".4s"
										data-duration="2000ms">MD 강.력.추.천<br>꿀조합!</h1>
									<p data-animation="fadeInLeft" data-delay=".7s"
										data-duration="2000ms">${MDRecommendProductData.productComboInformation}</p>
									<!-- Hero-btn -->
									<div class="hero__btn" data-animation="fadeInLeft"
										data-delay=".8s" data-duration="2000ms">
										<a
											href="productDetail.do?productComboNumber=${MDRecommendProductData.productComboNumber}"
											class="btn hero-btn">구매하러 가기</a>
									</div>
								</div>
							</div>
							<div
								class="col-xl-3 col-lg-3 col-md-4 col-sm-4 d-none d-sm-block">
								<div class="hero__img" data-animation="bounceIn"
									data-delay=".4s">
									<img src="${MDRecommendProductData.productComboImage}"
										alt="MD추천 꿀조합 상품" class="heartbeat-fluid">
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 1번 Slider End -->
				<!-- 2번 Slider Start -->
				<div
					class="single-slider slider-height d-flex align-items-center slide-bg">
					<div class="container">
						<div class="row justify-content-between align-items-center">
							<div class="col-xl-8 col-lg-8 col-md-8 col-sm-8">
								<div class="hero__caption">
									<h1 data-animation="fadeInLeft" data-delay=".4s"
										data-duration="2000ms">신규회원 이벤트!</h1>
									<p data-animation="fadeInLeft" data-delay=".7s"
										data-duration="2000ms">
										지금 회원가입 시 <b>5000원 할인 쿠폰</b> 바로 지급!<br>어서 가입하세요!
									</p>
									<!-- Hero-btn -->
									<div class="hero__btn" data-animation="fadeInLeft"
										data-delay=".8s" data-duration="2000ms">
										<a href="login.do" class="btn hero-btn">가입하러 가기</a>
									</div>
								</div>
							</div>
							<div
								class="col-xl-3 col-lg-3 col-md-4 col-sm-4 d-none d-sm-block">
								<div class="hero__img" data-animation="bounceIn"
									data-delay=".4s">
									<img src="assets/img/hero/joinCupon.png" alt="할인쿠폰 이미지"
										class="heartbeat">
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 2번 Slider End -->
			</div>
		</div>
		<!-- slider Area End-->
		<!--? Video Area Start -->
		<div class="video-area">
			<div class="container-fluid">
				<div class="row align-items-center">
					<div class="col-lg-12">
						<div class="video-wrap">
							<div class="play-btn ">
								<a class="popup-video"
									href="https://www.youtube.com/watch?v=AxE2EWc6Cuo"><i
									class="fas fa-play"></i></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Video Area End -->
		<br> <br> <br>
		<!-- 편의점 통합 꿀조합 TOP3 Start -->
		<div class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>편의점 통합 꿀조합 TOP3</h2>
							<p>
								각 편의점 상품들을 조합하여 먹는 꿀조합!<br> <b>빨리 먹는 사람이 맛.잘.알!</b>
							</p>
						</div>
					</div>
				</div>
				<div class="row">
					<c:forEach var="allStoreProductCombo"
						items="${allStoreProductComboTop}">
						<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
							<div class="single-popular-items mb-50 text-center">
								<div class="popular-img">
									<img src="${allStoreProductCombo.productComboImage}"
										alt="편의점 통합 꿀조합 Top3 상품 이미지">
									<div class="img-cap"
										onclick="insertCart(${allStoreProductCombo.productComboNumber},1,true, ${allStoreProductCombo.productComboStock})">
										<span>장바구니 담기</span>
									</div>
								</div>
								<div class="popular-caption">
									<h3>
										<a
											href="productDetail.do?productComboNumber=${allStoreProductCombo.productComboNumber}">${allStoreProductCombo.productComboName}</a>
									</h3>
									<span><fmt:formatNumber
											value="${allStoreProductCombo.productComboPrice}"
											type="number" groupingUsed="true" />원</span>
									<c:if test="${0 < allStoreProductCombo.productComboDiscount}">
										<span style="color: red;">-${allStoreProductCombo.productComboDiscount}%
										</span>
										<span style="color: blue;"><fmt:formatNumber
												value="${allStoreProductCombo.productComboDiscountedPrice}"
												type="number" groupingUsed="true" />원</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<!-- 더보기 Button -->
				<div class="row justify-content-center">
					<div class="room-btn">
						<a href="comboProduct.do" class="btn view-btn1">꿀조합 더 보러가기</a>
					</div>
				</div>
			</div>
		</div>
		<!-- 편의점 통합 꿀조합 TOP3 End -->
		<br>
		<hr>
		<br> <br>
		<!-- CU 편의점 꿀조합 TOP3 Start -->
		<div class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>CU 편의점 꿀조합 TOP3</h2>
							<p>
								CU 편의점 상품들을 조합하여 먹는 꿀조합!<br> <b>빨리 먹는 사람이 맛.잘.알!</b>
							</p>
						</div>
					</div>
				</div>
				<div class="row">
					<c:forEach var="CUStoreProductCombo"
						items="${CUStoreProductComboTop}">
						<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
							<div class="single-popular-items mb-50 text-center">
								<div class="popular-img">
									<img src="${CUStoreProductCombo.productComboImage}"
										alt="CU 편의점 꿀조합 Top3 상품 이미지">
									<div class="img-cap"
										onclick="insertCart(${CUStoreProductCombo.productComboNumber},1,true, ${CUStoreProductCombo.productComboStock})">
										<span>장바구니 담기</span>
									</div>
								</div>
								<div class="popular-caption">
									<h3>
										<a
											href="productDetail.do?productComboNumber=${CUStoreProductCombo.productComboNumber}">${CUStoreProductCombo.productComboName}</a>
									</h3>
									<span><fmt:formatNumber
											value="${CUStoreProductCombo.productComboPrice}"
											type="number" groupingUsed="true" />원</span>
									<c:if test="${0 < CUStoreProductCombo.productComboDiscount}">
										<span style="color: red;">-${CUStoreProductCombo.productComboDiscount}%</span>
										<span style="color: blue;"><fmt:formatNumber
												value="${CUStoreProductCombo.productComboDiscountedPrice}"
												type="number" groupingUsed="true" />원</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<!-- 더보기 Button -->
				<div class="row justify-content-center">
					<div class="room-btn">
						<a href="comboProduct.do" class="btn view-btn1">꿀조합 더 보러가기</a>
					</div>
				</div>
			</div>
		</div>
		<!-- CU 편의점 꿀조합 TOP3 End -->
		<br>
		<hr>
		<br> <br>
		<!-- GS25 편의점 꿀조합 TOP3 Start -->
		<div class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>GS25 편의점 꿀조합 TOP3</h2>
							<p>
								GS25 편의점 상품들을 조합하여 먹는 꿀조합!<br> <b>빨리 먹는 사람이 맛.잘.알!</b>
							</p>
						</div>
					</div>
				</div>
				<div class="row">
					<c:forEach var="GSStoreProductCombo"
						items="${GSStoreProductComboTop}">
						<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
							<div class="single-popular-items mb-50 text-center">
								<div class="popular-img">
									<img src="${GSStoreProductCombo.productComboImage}"
										alt="GS25 편의점 꿀조합 Top3 상품 이미지">
									<div class="img-cap"
										onclick="insertCart(${GSStoreProductCombo.productComboNumber},1,true, ${GSStoreProductCombo.productComboStock})">
										<span>장바구니 담기</span>
									</div>
								</div>
								<div class="popular-caption">
									<h3>
										<a
											href="productDetail.do?productComboNumber=${GSStoreProductCombo.productComboNumber}">${GSStoreProductCombo.productComboName}</a>
									</h3>
									<span><fmt:formatNumber
											value="${GSStoreProductCombo.productComboPrice}"
											type="number" groupingUsed="true" />원</span>
									<c:if test="${0 < GSStoreProductCombo.productComboDiscount}">
										<span style="color: red;">-${GSStoreProductCombo.productComboDiscount}%</span>
										<span style="color: blue;"><fmt:formatNumber
												value="${GSStoreProductCombo.productComboDiscountedPrice}"
												type="number" groupingUsed="true" />원</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<!-- 더보기 Button -->
				<div class="row justify-content-center">
					<div class="room-btn">
						<a href="comboProduct.do" class="btn view-btn1">꿀조합 더 보러가기</a>
					</div>
				</div>
			</div>
		</div>
		<!-- GS25 편의점 꿀조합 TOP3 End -->
		<br>
		<hr>
		<br> <br>
		<!-- 꿀조합 게시판 인기 글 TOP3 Start -->
		<div class="popular-items ">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>꿀조합 게시판 인기 글 TOP3</h2>
							<p>
								<b>고객분들의 추천 꿀조합!</b>
							</p>
						</div>
					</div>
				</div>
				<!-- 게시글 Start -->
				<table id="boardTable">
					<thead>
						<tr>
							<th>번호</th>
							<th>작성자</th>
							<th>제목</th>
							<th>조회수</th>
							<th>좋아요</th>
							<th>작성일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="boardComboPopular" items="${boardComboPopularTop}">
							<tr
								onclick="location.href='boardDetail.do?boardComboNumber=${boardComboPopular.boardComboNumber}'">
								<td>${boardComboPopular.boardComboNumber}</td>
								<td>${boardComboPopular.memberName}</td>
								<td>${boardComboPopular.boardComboTitle}</td>
								<td>${boardComboPopular.boardComboViewCount}</td>
								<td>${boardComboPopular.boardComboLikedCount}</td>
								<td>${boardComboPopular.boardComboRegisterDate}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 게시글 End -->
				<br> <br>
				<!-- 더보기 Button -->
				<div class="row justify-content-center">
					<div class="room-btn">
						<a href="comboBoard.do" class="btn view-btn1">더 보러가기</a>
					</div>
				</div>
			</div>
		</div>
		<!-- 꿀조합 게시판 인기 글 TOP3 End -->
	</main>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<!-- 푸터 영역 -->
	<%@ include file="footer.jsp"%>

	<!-- main Jquery -->
	<script src="assets/js/main.js"></script>

	<!-- 광고 SweetAlert -->
	<!-- CDN 추가 -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

	<!-- 메인 광고 팝업 js -->
	<script src="/assets/js/customization/mainAdPopup.js"></script>
	
	<!-- 사용자 정의 js -->
	<script src="assets/js/customization/mainPage.js"></script>

</body>
</html>