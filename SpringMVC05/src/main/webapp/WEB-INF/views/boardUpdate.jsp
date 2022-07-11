<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>SpringMVC05</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
 
<div class="container">
  <h2>SpringMVC05</h2>
  <div class="panel panel-default">
    <div class="panel-heading">Board</div>
    <div class="panel-body">
    	<form action="../boardUpdate.do" method="post">
    		<input type="hidden" name="idx" value="${vo.idx}">
    		<table class="table table-bordered">
				<tr>
					<td>제목</td>
					<td>
						<input type="text" name="title" value="${vo.title}" class="form-control">
					</td>
				</tr>
				<tr>
					<td>내용</td>
					<td>
						<textarea rows="7" name="content" class="form-control">${vo.content}</textarea>
					</td>
				</tr>
				<tr>
					<td>작성자</td>
					<td>
						<input type="text" value="${vo.writer}" readonly="readonly" class="form-control">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<button type="submit" class="btn btn-primary btn-sm">수정</button>
						<button type="reset" class="btn btn-warning btn-sm">취소</button>
						<button type="button" class="btn btn-info btn-sm list">목록</button>
					</td>
				</tr>
			</table>
    	</form>

	</div>
    <div class="panel-footer">스프링 연습</div>
  </div>
</div>
<script src="${path}/resources/js/boardUpdate.js"></script>
</body>