<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>상품 상세정보 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- rating CSS -->
<link rel="stylesheet" type="text/css"
	href="assets/css/customization/star-rating-svg.css">
<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/productDetail.css">
</head>
<body data-product-number="${productData.productNumber}">
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<!-- Hero Area Start-->
		<div class="slider-area ">
			<div class="single-slider slider-height2 d-flex align-items-center">
				<div class="container">
					<div class="row">
						<div class="col-xl-12">
							<div class="hero-cap text-center">
								<h2>${productData.productCategory}</h2>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Hero Area End-->
		<!--================Single Product Area =================-->
		<div class="product_image_area">
			<div class="container">
				<div class="row justify-content-center">
					<div class="col-lg-12">
						<div class="product_img_slide owl-carousel">
							<div class="single_product_img">
								<img src="${productData.productImg}" alt="상품 이미지"
									class="img-fluid">
							</div>
						</div>
					</div>
					<div class="col-lg-8">
						<div class="single_product_text text-center">
							<h3>${productData.productName}</h3>
							<p>${productData.productInformation}</p>
							<div class="card_area">
								<div class="product_count_area">
									<c:if test="${productData.productDiscount > 0}">
										<p style="color: red;">-${productData.productDiscount}%
											할인상품</p>
									</c:if>
									<div class="product_count d-inline-block">
										<span class="product_count_item inumber-decrement"> <i
											class="ti-minus"></i></span> <input id="cartProductCount"
											class="product_count_item input-number" type="text" value="1"
											min="1" max="${productData.productStock}" readonly> <span
											class="product_count_item number-increment"> <i
											class="ti-plus"></i></span>
									</div>
									<p>
										상품 가격:
										<fmt:formatNumber value="${productData.productPrice}"
											type="number" groupingUsed="true" />
										원
										<c:if test="${0 < productData.productDiscount}">
											<br>
											<i class="fas fa-arrow-down" style="color: red;"></i>
											<br>
											할인 가격: <fmt:formatNumber
												value="${productData.productDiscountedPrice}" type="number"
												groupingUsed="true" />원
										</c:if>
									</p>
								</div>
								<div class="add_to_cart">
									<a href="javascript:void(0);"
										class="insertCartBtn btn_3 genric-btn warning radius"
										data-number="${productData.productNumber}"
										data-combo="${productData.isComboProduct}"
										data-stock="${productData.productStock}">장바구니 담기</a>
								</div>
							</div>
						</div>
					</div>
					<!-- 제품 상세 정보 영역 Start -->
					<div class="comment-form">
						<aside class="single_sidebar_widget popular_post_widget">
							<c:if test="${not empty productComboComponentDatas}">
								<h4 class="widget_title">구성품</h4>
								<div class="media post_item">
									<ul class="instagram_row flex-wrap">
										<c:forEach var="productComboComponentData"
											items="${productComboComponentDatas}">
											<li><a
												href="productDetail.do?
												productSingleNumber=${productComboComponentData.productSingleNumber}">
													<img class="img-fluid"
													src="${productComboComponentData.productSingleImage}"
													alt="상품 구성품 이미지">
											</a></li>
											<br>
										</c:forEach>
									</ul>
								</div>
							</c:if>
						</aside>
					</div>
				</div>
				<!-- 제품 상세 정보 영역 End -->
				<!-- 댓글 영역 Start -->
				<div class="container">
					<div class="comments-area">
						<!-- 댓글 등록 영역 Start -->
						<h4>댓글 등록</h4>
						<div class="form-contact comment_form" id="commentForm">
							<div class="row">
								<div class="col-12">
									<div class="form-group">
										<p id="my-rating"></p>
										<input type="text" class="form-control w-100" name="comment"
											id="comment" cols="30" rows="9" placeholder="로그인 후 이용가능합니다."
											disabled>
									</div>
								</div>
							</div>
							<div class="form-group">
								<button type="submit" class="genric-btn warning radius"
									onclick="insertReview()">등록</button>
							</div>
						</div>
						<!-- 댓글 등록 영역 End -->
						<br> <br>
						<h2>
							<span id="totalReviewCount"></span> 의견
						</h2>
						<div id="noReview">
							<p>작성된 리뷰가 없습니다.</p>
						</div>
						<div id="reviewWrapper"></div>
						<br>
						<div id="moreLoadReviewBtn">
							<button class="w-100 genric-btn warning radius"
								id="loadMoreReviewBtn"
								onclick="loadMoreReview(${productData.productNumber})">
								더보기</button>
						</div>
					</div>
					<!-- 댓글 영역 End -->
				</div>
			</div>
		</div>
		<!--================End Single Product Area =================-->
		<br>
		<hr>
		<br> <br>
		<!-- 추천 상품 Start -->
		<div class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>추천 상품</h2>
						</div>
					</div>
				</div>
				<div class="row">
					<c:forEach var="recommendProductData"
						items="${recommendProductDatas}">
						<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
							<div class="single-popular-items mb-50 text-center">
								<div class="popular-img">
									<img src="${recommendProductData.productSingleImage}"
										alt="추천 상품 이미지">
									<div class="insertCartBtn img-cap"
										data-number="${recommendProductData.productSingleNumber}"
										data-combo="false"
										data-stock="${recommendProductData.productSingleStock}">
										<span>장바구니 담기</span>
									</div>
								</div>
								<div class="popular-caption">
									<h3>
										<a
											href="productDetail.do?productSingleNumber=${recommendProductData.productSingleNumber}">${recommendProductData.productSingleName}</a>
									</h3>
									<span><fmt:formatNumber
											value="${recommendProductData.productSinglePrice}"
											type="number" groupingUsed="true" />원</span>
									<c:if test="${0 < recommendProductData.productSingleDiscount}">
										<span style="color: red;">-${recommendProductData.productSingleDiscount}%
										</span>
										<span style="color: blue;"><fmt:formatNumber
												value="${recommendProductData.productSingleDiscountedPrice}"
												type="number" groupingUsed="true" />원</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<!-- GS25 편의점 꿀조합 TOP3 End -->
	</main>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<!-- 푸터 영역 -->
	<%@ include file="footer.jsp"%>

	<!-- Jquery Plugins, main Jquery -->
	<script src="assets/js/plugins.js"></script>
	<script src="assets/js/main.js"></script>
	<!-- rating js -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
	<script
		src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
	<script src="assets/js/customization/jquery.star-rating-svg.js"></script>
	<!-- 사용자 정의 js -->
	<script type="text/javascript"
		src="assets/js/customization/productDetail.js"></script>

</body>
</html>