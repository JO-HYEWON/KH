<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>My Spring Web Page</title>
<!-- 	<link href="../../resources/css/menubar-style.css" rel="stylesheet"> -->
	<link href="/resources/css/menubar-style.css" rel="stylesheet">
</head>
<body>
<!-- 	jstl쓰기 위한 2가지 1. jar(4개), 2. taglib(디렉티브) jsp 추가 -->
	<h1 align="center">Welcome Our WebSite!!</h1>
	<div class="login-area">
<%-- 		<c:if test="${empty sessionScope.loginUser }">	 --%>
		<c:if test="${sessionScope.loginUser eq null }">
			<form action="/member/login.kh" method="post">
				<table align="right">
					<tr>
						<td>아이디 : </td>
						<td><input type="text" name="memberId"></td>
						<td rowspan="2">
							<input type="submit" value="로그인">
						</td>
					</tr>
					<tr>
						<td>비밀번호 : </td>
						<td><input type="password" name="memberPwd"></td>
	<!-- 					<td></td> -->
					</tr>
					<tr>
						<td colspan="3"><a href="/member/joinView.kh">회원가입</a></td>
	<!-- 					<td></td> -->
	<!-- 					<td></td> -->
					</tr>
				</table>
			</form>
		</c:if>
		<c:if test="${not empty loginUser }">
			<table align="right">
				<tr>
					<td colspan="2">${sessionScope.loginUser.memberName }님 환영합니다!!</td>
<!-- 					<td></td> -->
				</tr>
				<tr>
					<td><a href="/member/myPage.kh">정보수정</a></td>
					<td><a href="/member/logout.kh">로그아웃</a></td>
				</tr>
			</table>
		</c:if>	
	</div>
	<div class="nav-area">
		<div class="menu" onclick="location.href='/home.kh';">Home</div>
		<div class="menu" onclick="showNoticeList();">공지사항</div>
		<div class="menu" onclick="location.href='/board/list.kh';">자유게시판</div>
		<div class="menu" onclick="">사진게시판</div>
	</div>
	<script>
		function showNoticeList() {
			
		}
	</script>
</body>
</html>
