<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>SpringMVC06</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script type="text/javascript">
  	$(document).ready(function(){
		loadList();
	});
  	
  	function loadList() {
		//서버와 통신 (Ajax)
		$.ajax({
			url : "boardList.do",
			type : "get",
			dataType : "json",
			success : makeView,
			error : ()=>{alert("error")}
		});
	}
  	
  	function makeView(data) {
  		let listHTML = "<table class='table table-bordered'>"
  			listHTML += "<tr>";
  			listHTML += "<td>번호</td>";
  			listHTML += "<td>제목</td>";
  			listHTML += "<td>작성자</td>";
  			listHTML += "<td>작성일</td>";
  			listHTML += "<td>조회수</td>";
  			listHTML += "</tr>";
  			
  		$.each(data, (index, obj)=>{
  			listHTML += "<tr>";
  			listHTML += "<td>" + obj.idx    +"</td>";
  			listHTML += "<td>" + obj.title  +"</td>";
  			listHTML += "<td>" + obj.writer +"</td>";
  			listHTML += "<td>" + obj.indate +"</td>";
  			listHTML += "<td>" + obj.count  +"</td>";
  			listHTML += "</tr>";
  		});
  		listHTML += "</table>"
  		$('#view').html(listHTML);
  		
	}
  </script>
</head>
<body>
 
<div class="container">
  <h2>SpringMVC06</h2>
  <div class="panel panel-default">
    <div class="panel-heading">Board</div>
    <div class="panel-body" id="view">Panel Content</div>
    <div class="panel-footer">스프링 연습</div>
  </div>
</div>
</body>