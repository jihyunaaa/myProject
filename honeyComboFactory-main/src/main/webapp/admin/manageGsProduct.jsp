<%@ page contentType="text/html; charset=UTF-8" errorPage="error.do"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>GS25 상품 관리 - 꿀조합팩토리</title>
</head>
<body>
    <div id="wrapper">
        <!-- 네비게이션 include -->
        <%@ include file="nav.jsp" %>
        
  <div id="page-wrapper">
    <div class="header">
      <h1 class="page-header"> GS 상품 관리 페이지</h1>
      <ol class="breadcrumb">
        <li><a href="#">ADMIN</a></li>
        <li><a href="#">PRODUCT</a></li>
        <li class="active">GS PRODUCT</li>
      </ol>
    </div>

    <!-- GS 상품 테이블 -->
    <div id="page-inner">
    <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="card-content">
            <h4 class="center">GS 상품 목록</h4>
            <div class="table-responsive">
              <table class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th>상품번호</th>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>할인율</th>
                    <th>할인가격</th>
                    <th>재고</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="product" items="${gsProductList}">
                    <tr>
                      <td>${product.productSingleNumber}</td>
                      <td>${product.productSingleName}</td>
                      <td>${product.productSinglePrice}원</td>
                      <td>${product.productSingleDiscount}%</td>
                      <td>${product.productSingleDiscountedPrice}원</td>
                      <td>${product.productSingleStock}개</td>
                    </tr>
                  </c:forEach>

                  <c:if test="${empty gsProductList}">
                    <tr>
                      <td colspan="6" style="text-align: center;">등록된 상품이 없습니다.</td>
                    </tr>
                  </c:if>
                </tbody>
              </table>
            </div> <!-- table-responsive -->
          </div> <!-- card-content -->
        </div> <!-- card -->
      </div> <!-- col-md-12 -->
    </div> <!-- row -->
  </div> <!-- page-wrapper -->
</div> <!-- wrapper -->
	
</body>
</html>