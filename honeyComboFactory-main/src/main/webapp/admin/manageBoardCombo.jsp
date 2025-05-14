<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>꿀조합 게시판 관리 - 꿀조합팩토리</title>
</head>
<body>

 <div id="wrapper">
        <!-- 네비게이션 include -->
        <%@ include file="nav.jsp" %>
		
    <div id="wrapper">
        <div id="page-wrapper">
            <div class="header">
                <h1 class="page-header">꿀조합 게시판 관리</h1>
                <ol class="breadcrumb">
                    <li><a href="#">ADMIN</a></li>
                    <li><a href="#">BOARD</a></li>
                    <li class="active">COMBO</li>
                </ol>
            </div>
            <div id="page-inner">
                <div class="row">
                    <div class="col-md-12">
                        <!-- 테이블 시작 -->
                        <div class="panel panel-default">
                            <div class="panel-heading">꿀조합 게시판 목록</div>
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered table-hover"
                                        id="dataTables-example">
                                        <thead>
                                            <tr>
                                                <th>번호</th>
                                                <th>작성자</th>
                                                <th>제목</th>
                                                <th>조회수</th>
                                                <th>좋아요</th>
                                                <th>작성일</th>
                                                <th>관리</th>
                                            </tr>
                                        </thead>       
                                        
                                        <!-- 관리자일 때 관리자로 간주하여 이름에 관리자 출력, 좋아요 수는 출력하지 않음 -->
                                        <tbody>
                                            <c:forEach var="boardComboData" items="${boardComboDatas}">
                                                <tr>
                                                    <td>${boardComboData.boardComboNumber}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${memberIsAdmin}">
                                                                관리자
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${boardComboData.memberName}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${boardComboData.boardComboTitle}</td>
                                                    <td>${boardComboData.boardComboViewCount}</td>
                                                      <td>
                                                        <c:choose>
                                                           <c:when test="${memberIsAdmin}">
                                                                -
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${boardComboData.boardComboLikedCount}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${boardComboData.boardComboRegisterDate}</td>
                                                    <td>
                                                        <button type="button" class="btn btn-info btn-xs">수정</button>
                                                        <button type="button" class="btn btn-danger btn-xs">삭제</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!-- 테이블 끝 -->
                    </div>
                </div>
                <footer>
                   <!-- 푸터 추가 -->
                </footer>
            </div>
            <!-- /. PAGE INNER  -->
        </div>
        <!-- /. PAGE WRAPPER  -->
    </div>
    <!-- /. WRAPPER  -->
    
    <!-- JS Scripts-->
    <!-- DATA TABLE SCRIPTS -->
    <script src="assets/admin/js/dataTables/jquery.dataTables.js"></script>
    <script src="assets/admin/js/dataTables/dataTables.bootstrap.js"></script>
	
</body>
</html>