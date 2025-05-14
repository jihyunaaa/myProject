<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>주문 상세정보 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- Font Awesome 라이브러리 추가 -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>

<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<br> <br>
		<!--================ confirmation part start =================-->
		<section class="confirmation_part">
			<div class="container">
				<div class="row">
					<div class="col-lg-6 col-lx-4">
						<div class="single_confirmation_details">
							<h4>주문 정보</h4>
							<ul>
								<li>
									<p>주문 번호</p> <span>: ${purchaseData.purchaseNumber}</span>
								</li>
								<li>
									<p>결제번호</p> <span>: ${purchaseData.purchaseTerminalId}</span>
								</li>
								<li>
									<p>결제 가격</p> <span>: ${purchaseData.purchaseTotalPrice}원</span>
								</li>
							</ul>
						</div>
					</div>
					<div class="col-lg-6 col-lx-4">
						<div class="single_confirmation_details">
							<h4>QR 코드</h4>
							<c:if test="${not empty qrBase64}">
								<img src="data:image/png;base64,${qrBase64}" alt="주문한 QR 코드 이미지" />
							</c:if>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="order_details_iner">
							<h3>주문 상세정보</h3>
							<table class="table table-borderless">
								<thead>
									<tr>
										<th scope="col" colspan="2">상품</th>
										<th scope="col">가격</th>
										<th scope="col">수량</th>
										<th scope="col">총 가격</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="purchaseProductDetailData"
										items="${purchaseProductDetailDatas}">
										<tr>
											<th colspan="2"><span>${purchaseProductDetailData.purchaseProductName}</span>
												<c:if
													test="${0 < purchaseProductDetailData.purchaseProductDiscount}">
													<span style="color: red;">-${purchaseProductDetailData.purchaseProductDiscount}%
														할인상품</span>
												</c:if></th>
											<th><span> <fmt:formatNumber
														value="${purchaseProductDetailData.purchaseProductPrice}"
														type="number" groupingUsed="true" />원 <c:if
														test="${0 < purchaseProductDetailData.purchaseProductDiscount}">
														<i class="fas fa-arrow-right" style="color: red;"></i>
														<fmt:formatNumber
															value="${purchaseProductDetailData.purchaseProductDiscountedPrice}"
															type="number" groupingUsed="true" />원
													</c:if>
											</span></th>
											<th>${purchaseProductDetailData.purchaseProductCount}</th>
											<th><span> <c:if
														test="${purchaseProductDetailData.purchaseProductDiscount <= 0}">
														<fmt:formatNumber
															value="${purchaseProductDetailData.purchaseProductCount * purchaseProductDetailData.purchaseProductPrice}"
															type="number" groupingUsed="true" />원
																	</c:if> <c:if
														test="${0 < purchaseProductDetailData.purchaseProductDiscount}">
														<fmt:formatNumber
															value="${purchaseProductDetailData.purchaseProductCount * purchaseProductDetailData.purchaseProductDiscountedPrice}"
															type="number" groupingUsed="true" />원
																	</c:if>
											</span></th>
										</tr>
									</c:forEach>
								</tbody>

								<tfoot>
									<tr>
										<th scope="col" colspan="3">총합</th>
										<th scope="col"></th>
										<th scope="col"><fmt:formatNumber
												value="${purchaseData.purchaseTotalPrice}" type="number"
												groupingUsed="true" />원</th>
									</tr>
								</tfoot>
							</table>
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
	<%@ include file="footer.jsp"%>
	
	<!--주문상세 js -->
	<script type="text/javascript"
		src="assets/js/customization/purchaseDetail.js"></script>
</body>
</html>
