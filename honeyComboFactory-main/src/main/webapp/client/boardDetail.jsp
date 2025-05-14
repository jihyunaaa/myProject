<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세정보 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/boardDetail.css">
<style type="text/css">
/* .table-hover 클래스를 덮어쓰기 */
table.table.table-hover {
	border: 2px solid black ! important;
	border-collapse: collapse !important;
	width: 100% !important;
}

/* 테이블 헤더와 데이터 셀에 테두리 색상 및 패딩 적용 */
table.table.table-hover th, table.table.table-hover td {
	border: 1px solid black !important;
	padding: 8px !important;
}
</style>
</head>
<body data-board-number="${boardComboDetailData.boardComboNumber}"
	data-board-liked-count="${boardComboDetailData.boardComboLikedCount}">
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<!-- Hero Area Start-->
		<!-- 
		<div class="slider-area ">
			<div class="single-slider slider-height2 d-flex align-items-center"
				style="background-image: url(' assets/ img/ logo/ mainLogo/ jps">
			</div>
		</div>
		 -->
		<!-- Hero Area End-->
		<br> <br> <br>
		<!-- 메인 Start -->
		<section class="popular-items">
			<div class="container">
				<!-- 게시글 Start -->
				<table id="boardDetailTable">
					<tr>
						<th>글 번호</th>
						<td>${boardComboDetailData.boardComboNumber}</td>
						<th>작성일</th>
						<td>${boardComboDetailData.boardComboRegisterDate}</td>
						<th>조회수</th>
						<td>${boardComboDetailData.boardComboViewCount}</td>
					</tr>
					<tr>
						<th>작성자</th>
						<c:if test="${boardComboDetailData.memberIsWithdraw}">
							<td>탈퇴한 회원</td>
						</c:if>
						<c:if test="${not boardComboDetailData.memberIsWithdraw}">
							<td>${boardComboDetailData.memberName}</td>
						</c:if>
						<th>제목</th>
						<td colspan="3" id="titleArea">${boardComboDetailData.boardComboTitle}</td>
					</tr>
					<tr>
						<td colspan="6">
							<div id="boardDetailContent">${boardComboDetailData.boardComboContent}</div>
						</td>
					</tr>
				</table>
				<!-- 게시글 End -->
				<br>
				<!-- 게시글 수정/삭제 버튼 Start -->
				<div class="row product-btn justify-content-between mb-40">
					&nbsp;
					<!-- Select items -->
					<div class="select-this">
						<div>
							<div class="select-itms">
								<c:if
									test="${loginedMemberNumber == boardComboDetailData.memberNumber}">
									<a href="javascript:void(0);" id="updateBtn" class="genric-btn warning radius" data-mode="edit">수정</a>
								</c:if>
								<c:if
									test="${loginedMemberNumber == boardComboDetailData.memberNumber or clientInfo.memberIsAdmin}">
									<a href="javascript:void(0);" class="genric-btn warning radius"
										onclick="deleteComboBoard(${boardComboDetailData.boardComboNumber})">삭제</a>
								</c:if>
							</div>
						</div>
					</div>
				</div>
				<!-- 게시글 수정/삭제 버튼 End -->
				<c:if test="${not boardComboDetailData.memberIsAdmin}">
					<!-- 게시글 좋아요 버튼 Start -->
					<div class="row justify-content-center">
						<div class="room-btn" id="likedBtnWrapper">
							<a href="javascript:void(0);" id="likeBtn"
								class="genric-btn primary-border e-large danger-border"><i
								class="fa fa-heart" aria-hidden="true"></i> <span>좋아요
									${boardComboDetailData.boardComboLikedCount}</span></a>
						</div>
					</div>
					<!-- 게시글 좋아요 버튼 End -->
				</c:if>
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

	<!-- ckeditor5 -->
		<script
			src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
	<!-- 사용자 정의 js -->
	<script type="text/javascript"
		src="assets/js/customization/boardDetail.js"></script>
</body>
</html>