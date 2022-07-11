<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>SpringMVC05</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="resources/css/boardList.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
 
<div class="container">
  <h2>SpringMVC05</h2>
  <div class="panel panel-default">
    <div class="panel-heading">BOARD</div>
    <div class="panel-body">
		<table class="table table-bordered table-hover">
			<tr>
				<td>번호</td>
				<td>제목</td>
				<td>작성자</td>
				<td>작성일</td>
				<td>조회수</td>
			</tr>
			<c:forEach var="vo" items="${list}">
				<tr>
					<td>${vo.idx}</td>
					<td>
						<a href="boardContent.do?idx=${vo.idx}">${vo.title}</a>
					</td>
					<td>${vo.writer}</td>
					<td>${fn:split(vo.indate," ")[0]}</td>
					<td>${vo.count}</td>
				</tr>
			</c:forEach>
		</table>
		<button class="btn btn-primary btn-small insert">글쓰기</button>
	</div>
    <div class="panel-footer">스프링 연습</div>
  </div>
</div>
<script src="resources/js/boardList.js"></script>
</body>