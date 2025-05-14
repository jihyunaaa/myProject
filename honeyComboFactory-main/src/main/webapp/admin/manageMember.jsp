<%@ page contentType="text/html; charset=UTF-8" errorPage="error.do"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원 관리 - 꿀조합팩토리</title>
</head>
<body>
	    <div id="wrapper">
        <!-- 네비게이션 include -->
        <%@ include file="nav.jsp" %>
        
		<!-- 본문 영역 -->
		<div id="page-wrapper">
			<div class="header">
				<h1 class="page-header">회원 관리 페이지</h1>
				<ol class="breadcrumb">
					<li><a href="#">ADMIN</a></li>
					<li><a href="#">MEMEBR</a></li>
					<li class="active">MEMBER</li>
				</ol>
			</div>

			<!-- 회원 목록 테이블 생성 -->
			<div id="page-inner">
			<div class="row">
				<div class="col-md-12">
					<div class="card">
						<div class="card-content">
							<h4 class="center">회원 목록</h4>
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th>회원번호</th>
										<th>아이디</th>
										<th>이름</th>
										<th>이메일</th>
										<th>전화번호</th>
										<th>가입일</th>
										<th>휴면여부</th>
										<th>관리</th>
									</tr>
								</thead>
								<!-- 반복문으로 회원 목록 출력 (한 페이지에 30개까지만 출력) -->
								<tbody>
									<c:forEach var="member" items="${memberDatas}" begin="0"
										end="29">
										<tr>
											<td>${member.memberNumber}</td>
											<td>${member.memberId}</td>
											<td>${member.memberName}</td>
											<td>${member.memberEmailId}@${member.memberEmailDomain}</td>
											<td>${member.memberPhoneNumber}</td>
											<td>${member.memberRegisterDate}</td>
											<td><c:choose>
													<c:when test="${member.memberIsWithdraw}">
														<span style="color: red;">휴면</span>
													</c:when>
													<c:otherwise>
														<span style="color: green;">활동중</span>
													</c:otherwise>
												</c:choose></td>
												<td> <!-- 회원 정보 삭제/수정 버튼 -->
												<button onclick="editMember(${member.memberNumber})" class="btn btn-primary btn-sm">수정</button>
												<button onclick="deleteMember(${member.memberNumber})" class="btn btn-danger btn-sm">삭제</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>

	<!-- JS Scripts -->
	<!-- Morris.js 먼저 -->
	<script src="assets/admin/js/morris/raphael-2.1.0.min.js"></script>
	<script src="assets/admin/js/morris/morris.js"></script>

	<!-- 그 다음에 easypiechart 관련 -->
	<script src="assets/admin/js/easypiechart.js"></script>
	<script src="assets/admin/js/easypiechart-data.js"></script>

	<!-- 기타 순서 관계없는 것들 -->
	<script src="assets/admin/materialize/js/materialize.min.js"></script>
	<script src="assets/admin/js/Lightweight-Chart/jquery.chart.js"></script>
	
</body>
</html>