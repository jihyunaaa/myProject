<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CU 상품 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- Font Awesome CDN -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/storeProduct.css">

<style type="text/css">
.slider-area .single-slider {
	background-image: url('assets/img/hero/CUheader.jpg');
	background-color: purple;
}
</style>
</head>
<body data-brand="CU">
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<!-- Hero Area Start-->
		<div class="slider-area ">
			<div class="single-slider slider-height2 d-flex align-items-center">
			</div>
		</div>
		<!-- Hero Area End-->
		<br> <br> <br>
		<section class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>핫이슈</h2>
						</div>
					</div>
				</div>

				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="hotIssueWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="hotPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">

					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>+1 증정 상품</h2>
						</div>
					</div>
				</div>
				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="plusWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="plusPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>지금 나에게 필요한건?</h2>
							<p>CU의 다양한 상품들을 소개합니다!</p>
							<div class="search-box">
								<input id="searchKeyword" type="text"
									placeholder="연세우유 크림빵을 검색해보세요!">
									<i class="fas fa-search" onclick="searchCUProductName()" style="cursor: pointer;"></i>
							</div>
						</div>
					</div>
				</div>
				<div class="row product-btn justify-content-between mb-40">
					<div class="properties__button">
						<!--Nav Button  -->
						<nav>
							<div class="nav nav-tabs" id="nav-tab" role="tablist">
								<a class="nav-item nav-link active" id="nav-home-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-home" aria-selected="true"
									onclick="setCUProductOrderCondition('ORDERPOPULAR')">인기순</a> <a
									class="nav-item nav-link" id="nav-profile-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-profile" aria-selected="false"
									onclick="setCUProductOrderCondition('ORDERHIGHPRICES')">가격
									높은순</a> <a class="nav-item nav-link" id="nav-contact-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-contact" aria-selected="false"
									onclick="setCUProductOrderCondition('ORDERLOWPRICES')">가격
									낮은순</a>
							</div>
						</nav>
						<!--End Nav Button  -->
					</div>
					<!-- Grid and List view -->
					<div class="grid-list-view"></div>
					<!-- Select items -->
					<div class="select-this">
						<div class="category-container">
							<button class="category dark"
								onclick="setCUProductCategory('ALLPRODUCT')">전체</button>
							<button class="category purple"
								onclick="setCUProductCategory('DAILYSUPPLIESPRODUCT')">생활용품</button>
							<button class="category green"
								onclick="setCUProductCategory('FOODPRODUCT')">식품</button>
							<button class="category blue"
								onclick="setCUProductCategory('BEVERAGEPRODUCT')">음료</button>
						</div>
					</div>
				</div>
				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="CUWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="CUPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>우리 주변 편의점</h2>
							<br>
							<div class="search-box">
								<input id="searchStoreKeyword" type="text"
									placeholder="편의점 이름을 검색해보세요!">
									<i class="fas fa-search" onclick="searchStore()" style="cursor: pointer;"></i>
							</div>
						</div>
						<div id="mapWrapper" style="width: 100%; height: 500px;"></div>
						<!-- 위치 찾기 버튼 추가 -->
						<button id="locationButton" onclick="getLocation()">현재
							위치로 이동</button>
						<p id="location"></p>
					</div>
				</div>
			</div>
			</div>
		</section>
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
	<script src="assets/js/customization/CUProduct.js"></script>

	<!-- Map API CDN -->
	<!-- KakaoMapAPI_Appkey 부분에 자신의 Appkey 입력 -->	
	<script type="text/javascript"
		src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=KakaoMapAPI_AppKey&libraries=services,clusterer,drawing"></script>
	<!-- 지도 js -->
	<script src="assets/js/customization/map.js"></script>

</body>
</html>