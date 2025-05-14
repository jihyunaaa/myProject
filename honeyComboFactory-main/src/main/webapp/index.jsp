<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.ArrayList, java.util.Map"%>
<%

ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session.getAttribute("shoppingCart");
if (shoppingCart == null) {
    shoppingCart = new ArrayList<>();
}

response.sendRedirect("main.do");

%>