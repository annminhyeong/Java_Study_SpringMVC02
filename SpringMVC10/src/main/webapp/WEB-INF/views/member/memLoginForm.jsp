<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- 회원정보(MemberUser 객체) -->
<c:set var="mvo" value="${SPRING_SECURITY_CONTEXT.authentication.principal}"/>
<!-- 회원 권한정보 -->
<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities}"/>
<!DOCTYPE html>
<html lang="en">
<head>
<title>SpringMVC10</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
    	//
    	if(${param.error != null}){
    		$('#MessageType').attr('class','modal-content panel-danger')
    		$('.modal-title').text('실패 메세지')
    		$('.modal-body').text('아이디와 비밀번호를 다시 입력해주세요')
    		$('#myMessage').modal('show')	
    	}
    	
		if(${!empty msgType}){
			$('#MessageType').attr('class','modal-content panel-success')
			$('#myMessage').modal('show')	
		}
    });
</script>
</head>
<body>
  <jsp:include page="../common/header.jsp"></jsp:include>
  <div class="container">
    <h2>SpringMVC10</h2>
    <div class="panel panel-default">
      <div class="panel-heading">로그인</div>
      <div class="panel-body">
        <form name="frm" action="${contextPath}/memLogin.do" method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <table class="table table-bordered text-center">
            <tr>
              <td style="width: 110px; vertical-align: middle;">아이디</td>
              <!-- 스프링 시큐리티에서는 name이름은 username이여야함 -->
              <td><input id="memID" name="username" type="text" maxlength="20" placeholder="아이디를 입력하세요" class="form-control"></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">비밀번호</td>
              <!-- 스프링 시큐리테에서는 name이름은 password이여야함 -->
              <td><input id="memPassword" name="password" type="password" maxlength="20" placeholder="비밀번호를 입력하세요" class="form-control"></td>
            </tr>
            <tr>
              <td colspan="2" class="text-right"><input type="submit" value="로그인" class="btn btn-primary btn-sm"></td>
            </tr>
          </table>
        </form>
      </div>
      <!-- 로그인 실패 모달창 -->
      <div id="myMessage" class="modal fade" role="dialog">
        <div class="modal-dialog">
          <!-- Modal content-->
          <div id="MessageType" class="modal-content">
            <div class="modal-header panel-heading">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">${msgType}</h4>
            </div>
            <div class="modal-body">
              <p>${msg}</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>
        </div>
      </div>
      <div class="panel-footer">스프링 연습</div>
    </div>
  </div>
</body>
</html>