<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>꿀조합 게시판 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link href="assets/css/customization/board.css" type="text/css"
	rel="stylesheet">
<style type="text/css">
.slider-area .single-slider {
	background-image: url('assets/img/hero/iatethis.png');
	background-size: cover;
}
</style>
</head>
<body>
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
		<!-- 메인 Start -->
		<section class="popular-items">
			<div class="container">
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>꿀조합 게시판</h2>
						</div>
					</div>
				</div>
				<!-- 게시글 정렬 셀렉바 Start -->
				<div class="row product-btn justify-content-between mb-40">
					<div class="properties__button">
						<!--Nav Button  -->
						<nav>
							<div class="nav nav-tabs" id="nav-tab" role="tablist">
								<a class="nav-item nav-link active" id="nav-home-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-home" aria-selected="true"
									onclick="orderComboBoard('ORDERUPTODATE')">최신순</a> <a
									class="nav-item nav-link" id="nav-profile-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-profile" aria-selected="false"
									onclick="orderComboBoard('ORDEROLD')">오래된순</a> <a
									class="nav-item nav-link" id="nav-contact-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-contact" aria-selected="false"
									onclick="orderComboBoard('ORDERPOPULAR')">인기순</a>
							</div>
						</nav>
						<!--End Nav Button  -->
					</div>
					<aside class="single_sidebar_widget search_widget">
						<div>
							<div class="form-group">
								<div class="input-group mb-3">
									<input type="text" class="form-control" id="searchKeyword"
										placeholder="제목 검색" maxlength="100">
									<div class="input-group-append">
										<button class="genric-btn warning" type="button"
											onclick="searchComboBoardTitle()">
											<i class="ti-search"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</aside>
				</div>
				<!-- 게시글 정렬 셀렉바 End -->
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
						<c:forEach var="boardComboAdminData"
							items="${boardComboAdminDatas}">
							<tr class="adminBoard"
								onclick="location.href='boardDetail.do?boardComboNumber=${boardComboAdminData.boardComboNumber}'">
								<td>${boardComboAdminData.boardComboNumber}</td>
								<td>관리자</td>
								<td>${boardComboAdminData.boardComboTitle}</td>
								<td>${boardComboAdminData.boardComboViewCount}</td>
								<td>-</td>
								<td>${boardComboAdminData.boardComboRegisterDate}</td>
							</tr>
						</c:forEach>
					<tbody>
					<tfoot></tfoot>
				</table>
				<!-- 게시글 End -->
				<br>
				<!-- 게시글 등록 버튼 Start -->
				<div class="row product-btn justify-content-between mb-40">
					&nbsp;
					<!-- Select items -->
					<div class="select-this">
						<form action="#">
							<div class="select-itms">
								<a href="updateBoard.do" class="genric-btn warning radius">등록</a>
							</div>
						</form>
					</div>
				</div>
				<!-- 게시글 등록 버튼 End -->
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="pageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
			</div>
		</section>
		<!-- 게시글 End -->
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
	<script type="text/javascript"
		src="assets/js/customization/comboBoard.js"></script>
</body>
</html>