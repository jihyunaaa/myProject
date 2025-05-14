<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 등록 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/updateBoard.css">
<style type="text/css">
.ck-editor__editable {
	min-height: 460px; /* CKEditor 편집 영역 높이 설정 */
}
</style>
</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<br> <br> <br>
		<!-- 메인 Start -->
		<section class="popular-items">
			<div class="container">
				<!-- 게시글 Start -->
				<table id="updateBoardTable">
					<tr>
						<th>작성자</th>
						<td>${loginedMemberName}</td>
						<th>제목</th>
						<td colspan="3"><input id="updateBoardTitle" type="text"
							maxlength="100" required></td>
					</tr>
					<tr>
						<td colspan="6">
							<div id="updateBoardContent">
								<textarea id="CKEditor5"></textarea>
							</div>
						</td>
					</tr>
				</table>
				<!-- 게시글 End -->
				<br>
				<!-- 게시글 등록 버튼 Start -->
				<div class="row product-btn justify-content-between mb-40">
					&nbsp;
					<!-- Select items -->
					<div class="select-this">
						<div class="select-itms">
							<a id="updateBoardBtn" href="javascript:updateBoard();" class="genric-btn warning radius">등록</a>
						</div>
					</div>
				</div>
				<!-- 게시글 등록 버튼 End -->
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
	<script src="assets/js/customization/updateBoard.js"></script>
</body>
</html>