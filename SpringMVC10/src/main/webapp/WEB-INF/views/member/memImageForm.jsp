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
    	if(${!empty msgType}){
    		$('#MessageType').attr('class','modal-content panel-danger')
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
      <div class="panel-heading">사진등록</div>
      <div class="panel-body">
        <!-- 파일인 경우는 get방식처럼 보내야함 -->
        <form name="frm" action="${contextPath}/memImageUpdate.do?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="multipart/form-data">
          <input type="hidden" id="memID" name="memID" value="${mvo.member.memID}">
          <table class="table table-bordered text-center">
            <tr>
              <td style="width: 110px; vertical-align: middle;">아이디</td>
              <td>${mvo.member.memID}</td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">사진업로드</td>
              <td>
                <span class="btn btn-default">
                  이미지를 업로드하세요.<input type="file" name="memProfile" >
                </span>
              </td>
            </tr>
            <tr>
              <td colspan="2" class="text-right"><input type="submit" value="등록" class="btn btn-primary btn-sm"></td>
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