<%@ page import="java.net.URLEncoder" %> 
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
    String scope = "name,email,birthday,birthyear,mobile";
    String clientId = "0wNhrwG_vJZEL3r9AZs9";
    String redirectURI = URLEncoder.encode("http://localhost:8088/honeyComboFactory/client/callback.jsp", "UTF-8");
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString();
    String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
         + "&client_id=" + clientId 
         + "&redirect_uri=" + redirectURI
         + "&state=" + state 
         + "&scope=" + scope;
    session.setAttribute("state", state);
    pageContext.setAttribute("naverLoginUrl", naverLoginUrl);
%>