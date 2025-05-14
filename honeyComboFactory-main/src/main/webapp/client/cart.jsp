<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>장바구니 - 꿀조합팩토리</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link href="assets/css/customization/cart.css" type="text/css"
	rel="stylesheet">
</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<br>
	<br>
	<br>
	<main>
		<!--================Cart Area =================-->
		<section class="cart_area">
			<div class="container">
				<div class="cart_inner">
					<div class="table-responsive">
						<table class="table">
							<thead>
								<tr>
									<th scope="col">장바구니 상품</th>
									<th scope="col">가격</th>
									<th scope="col">수량</th>
									<th scope="col">총 가격</th>
									<th scope="col"><input type="checkbox"
										id="cartAllCheckBox" onchange="selectAllProduct(this.checked)"></th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${empty cartProductDatas}">
									<tr>
										<td>장바구니에 담긴 상품이 없습니다.</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</c:if>
								<c:if test="${not empty cartProductDatas}">
									<c:forEach var="cartProductData" items="${cartProductDatas}">
										<tr id="cartRow-${cartProductData.cartProductNumber}">
											<td>
												<div class="media">
													<div class="d-flex">
														<img src="${cartProductData.cartProductImage}"
															alt="장바구니 상품 이미지" />
													</div>
													<div class="media-body">
														<p>${cartProductData.cartProductName}</p>
														<c:if test="${0 < cartProductData.cartProductDiscount}">
															<span style="color: red;">-${cartProductData.cartProductDiscount}%
																할인</span>
														</c:if>
													</div>
												</div>
											</td>
											<td>
												<h5
													id="cartProductPrice-${cartProductData.cartProductNumber}">
													<fmt:formatNumber
														value="${cartProductData.cartProductPrice}" type="number"
														groupingUsed="true" />
													원
												</h5> <c:if test="${0 < cartProductData.cartProductDiscount}">
													<h5>
														<i class="fas fa-arrow-down" style="color: red;"></i>
													</h5>
													<h5 id="cartProductDiscountedPrice-${cartProductData.cartProductNumber}">
														<fmt:formatNumber
															value="${cartProductData.cartProductDiscountedPrice}"
															type="number" groupingUsed="true" />
														원
													</h5>
												</c:if>
											</td>
											<td>
												<div class="product_count">
													<c:if test="${0 < cartProductData.cartProductStock}">
														<span class="input-number-decrement"
															onclick="changeCartProductCount(${cartProductData.cartProductNumber}, 'downCartProductCount')">
															<i class="ti-minus"></i>
														</span>
														<input class="input-number" type="text"
															id="count-${cartProductData.cartProductNumber}"
															value="${cartProductData.cartProductCount}" min="1"
															readonly>
														<span class="input-number-increment"
															onclick="changeCartProductCount(${cartProductData.cartProductNumber}, 'upCartProductCount')">
															<i class="ti-plus"></i>
														</span>
													</c:if>
													<c:if test="${cartProductData.cartProductStock <= 0}">
														<input class="input-number" type="text"
															id="count-${cartProductData.cartProductNumber}"
															value="${cartProductData.cartProductCount}" min="1"
															disabled>
													</c:if>
												</div>
											</td>
											<td>
												<h5>
													<span
														id="cartProductSumPrice-${cartProductData.cartProductNumber}">
														<c:if test="${0 < cartProductData.cartProductDiscount}">
															<fmt:formatNumber
																value="${cartProductData.cartProductDiscountedPrice * cartProductData.cartProductCount}"
																type="number" groupingUsed="true" />
														</c:if> <c:if test="${cartProductData.cartProductDiscount <= 0}">
															<fmt:formatNumber
																value="${cartProductData.cartProductPrice * cartProductData.cartProductCount}"
																type="number" groupingUsed="true" />
														</c:if>
													</span>원
												</h5>
											</td>
											<td><c:if test="${0 < cartProductData.cartProductStock}">
													<input type="checkbox" class="productCheckBox"
														id="productCheckBox-${cartProductData.cartProductNumber}"
														onclick="clickOneCheckBox(this.checked)"
														value="${cartProductData.cartProductNumber}">
												</c:if> <c:if test="${cartProductData.cartProductStock <= 0}">
													<input type="checkbox" class="productCheckBox"
														id="productCheckBox-${cartProductData.cartProductNumber}"
														onclick="clickOneCheckBox(this.checked)"
														value="${cartProductData.cartProductNumber}" disabled>
												</c:if></td>
										</tr>
									</c:forEach>
								</c:if>
								<tr>
									<td></td>
									<td></td>
									<td>
										<h5>총 구매 가격</h5>
									</td>
									<td>
										<h5>
											<span id="totalAmount">0</span>원
										</h5>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<div class="checkout_btn_inner float-right">
							<a class="btn_1" href="javascript:void(0);"
								onclick="deleteCartProduct()">선택 상품 삭제</a> <a
								class="btn_1 checkout_btn_1" href="javascript:void(0);"
								onclick="purchaseCartProduct()">결제하기</a>
						</div>
					</div>
				</div>
			</div>
		</section>
		<!--================End Cart Area =================-->
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
	<script type="text/javascript" src="assets/js/customization/cart.js"></script>
</body>
</html>