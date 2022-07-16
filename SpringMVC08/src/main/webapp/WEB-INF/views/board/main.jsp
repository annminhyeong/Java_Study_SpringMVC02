<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>SpringMVC08</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
	let csrfHeaderName = "${_csrf.headerName}";
	let csrfTokenValue = "${_csrf.token}";
	
  	$(document).ready(function(){
		loadList();
	});
  	
  	function loadList() {
		//서버와 통신 (Ajax)
		$.ajax({
			url : "board/all",
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
  			listHTML += "<td id='t"+obj.idx+"'><a href='javascript:goContent("+obj.idx+")'>" + obj.title  +"</a></td>";
  			listHTML += "<td>" + obj.writer +"</td>";
  			listHTML += "<td>" + obj.indate.split(' ')[0] +"</td>";
  			listHTML += "<td id='cnt"+obj.idx+"'>" + obj.count  +"</td>";
  			listHTML += "</tr>";
  			
  			listHTML += "<tr id='c"+obj.idx+"' style='display:none'>";
  			listHTML += "<td>내용</td>";
  			listHTML += "<td colspan='4'>";
  			listHTML += "<textarea rows='7' readonly class='form-control' id='ta"+obj.idx+"'></textarea>";
  			if("${mvo.memID}" === obj.memID){
    			listHTML += "<br>";
    			listHTML += "<span id='ub"+obj.idx+"'><button class='btn btn-success btn-sm' onclick='goUpdateForm("+obj.idx+")'>수정</button></span>&nbsp;";
    			listHTML += "<button class='btn btn-warning btn-sm' onclick='goDelete("+obj.idx+")'>삭제</button>";
  			}else{
  				listHTML += "<br>";
    			listHTML += "<span id='ub"+obj.idx+"'><button disabled class='btn btn-success btn-sm' onclick='goUpdateForm("+obj.idx+")'>수정</button></span>&nbsp;";
    			listHTML += "<button disabled class='btn btn-warning btn-sm' onclick='goDelete("+obj.idx+")'>삭제</button>";
  			}
  			listHTML += "</td>";
  			listHTML += "</tr>";
  		});
  		
  		//로그인을 해야 보이는 부분
  		if(${!empty mvo}){
    		listHTML += "<tr>";
    		listHTML += "<td colspan='5'>";
    		listHTML += "<button class='btn btn-primary btn-sm' onclick='goForm()'>글쓰기</button>"
    		listHTML += "</td>";
    		listHTML += "</tr>";
    		listHTML += "</table>"
  		}
  		$('#view').html(listHTML);
  		$('#view').css('display','block')
  		$('#wForm').css('display','none')
	}
  	
  	function goForm() {
		$('#view').css('display','none')
		$('#wForm').css('display','block')
	}
  	
  	function goList() {
		$('#view').css('display','block')
		$('#wForm').css('display','none')
		$('#fClear').click()
	}
  	
  	function goInsert() {
		let fData =  $('#frm').serialize()
		$.ajax({
			url        : 'board/new',
			type       : 'post',
			data       : fData,
			beforeSend : (xhr)=> xhr.setRequestHeader(csrfHeaderName, csrfTokenValue),
			success    : loadList,
			error      : ()=> alert('error')
		});
		$('#fClear').click();
	}
  	
  	function goContent(idx) {
  		if($('#c'+idx).css('display')==='none'){
			$('#c'+idx).css('display','table-row')
			$('#ta'+idx).attr('readonly',true)
			$.ajax({
				url      : 'board/' + idx,
				type     : 'get',
				dataType : 'json',
				success  : (data)=> $('#ta'+idx).val(data.content),
				error    : ()=> alert('error')
			})
  		}else{
  			$('#c'+idx).css('display','none')
  			$.ajax({
				url        : 'board/count/'+idx,
				type       : 'put',
				dataType   : "json",
				beforeSend : (xhr)=> xhr.setRequestHeader(csrfHeaderName, csrfTokenValue),
				success    : (data)=>{$('#cnt'+idx).text(data.count)},
				error      : ()=> alert('error')
			})
  		}
	}
  	
  	function goDelete(idx) {
		$.ajax({
			url        : 'board/'+idx,
			type       : 'delete',
			beforeSend : (xhr)=> xhr.setRequestHeader(csrfHeaderName, csrfTokenValue),
			success    : loadList,
			error      : ()=> alert('error')
		})
	}
  	
  	function goUpdateForm(idx) {
		$('#ta'+idx).attr('readonly',false)
		let title = $('#t'+idx).text();
		let newInput = "<input type='text' id='nt"+idx+"' value='"+title+"' class='form-control'>";
		$('#t'+idx).html(newInput)
		
		let newButton = "<button class='btn btn-primary btn-sm' onclick='goUpdate("+idx+")'>수정하기</button>";
		$('#ub'+idx).html(newButton)
	}
  	
  	function goUpdate(idx) {
		let title = $('#nt'+idx).val()
		let content = $('#ta'+idx).val()
		$.ajax({
			url         : 'board/update',
			type        : 'put',
			contentType : 'application/json;charset=utf-8', //json으로 넘기겠다는 뜻
			data        : JSON.stringify({'idx' : idx, 'title': title, 'content': content}), //spring을 json으로 변환
			beforeSend  : (xhr)=> xhr.setRequestHeader(csrfHeaderName, csrfTokenValue),
			success     : loadList,
			error       : ()=> alert('error')
		})
	}
  </script>
</head>
<body>
  <jsp:include page="../common/header.jsp" />
  <div class="container">
    <h2>SpringMVC08</h2>
    <div class="panel panel-default">
      <div class="panel-heading">Board</div>
      <div class="panel-body" id="view">Panel Content</div>
      <div class="panel-body" id="wForm" style="display: none;">
        <form id="frm">
          <input type="hidden" value="${mvo.memID}" name="memID" id="memID">
          <table class="table">
            <tr>
              <td>제목</td>
              <td><input type="text" class="form-control" name="title" id="title"></td>
            </tr>
            <tr>
              <td>내용</td>
              <td><textarea rows="7" class="form-control" name="content" id="content"></textarea></td>
            </tr>
            <tr>
              <td>작성자</td>
              <td><input type="text" value="${mvo.memName}" readonly class="form-control" name=writer id="writer"></td>
            </tr>
            <tr>
              <td colspan="2" align="center">
                <button type="button" class="btn btn-success btn-sm" onclick="goInsert()">등록</button>
                <button type="reset" class="btn btn-warning btn-sm" id="fClear">취소</button>
                <button type="button" class="btn btn-info btn-sm" onclick="goList()">뒤로</button>
              </td>
            </tr>
          </table>
        </form>
      </div>
      <div class="panel-footer">스프링 연습</div>
    </div>
  </div>
</body>