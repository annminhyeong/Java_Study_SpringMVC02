<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>SpringMVC07</title>
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
	function registerCheck() {
		let memID = $('#memID').val()
		$.ajax({
			url     : '${contextPath}/memRegisterCheck.do',
			type    : 'get',
			data    : {'memID' : memID},
			success : (result)=>{
				if(result === 1){
					$('#checkMessage').html("아이디 사용가능")
					$('#checkType').attr('class','modal-content panel-success')
					
				}else{
					$('#checkMessage').html("아이디 사용불가")
					$('#checkType').attr('class','modal-content panel-warning')
				}
				$('#myModal').modal('show')
			},
			error   : ()=> alert('error')
		})
	}
	
	function passwordCheck() {
		let memPassword1 = $('#memPassword1').val()
		let memPassword2 = $('#memPassword2').val()
		if(memPassword1 !== memPassword2){
			$('#passMessage').attr('class','text-danger')
			$('#passMessage').html('비밀번호가 일치하지 않습니다.')
		}else{
			$('#passMessage').attr('class','text-success')
			$('#passMessage').html('비밀번호가 일치합니다.')
			$('#memPassword').val(memPassword1)
		}
	}
	
	function goInsert() {
		let memAge = $('#memAge').val()
		if(memAge === null || memAge ==='' || memAge === 0){
			alert('나이를 입력하세요');
			return false;
		}
		document.frm.submit();
	}
</script>
</head>
<body>
  <jsp:include page="../common/header.jsp"></jsp:include>
  <div class="container">
    <h2>SpringMVC07</h2>
    <div class="panel panel-default">
      <div class="panel-heading">회원가입</div>
      <div class="panel-body">
        <form name="frm" action="${contextPath}/memRegister.do" method="post">
          <input id="memPassword" name="memPassword" type="hidden" value="">
          <table class="table table-bordered text-center">
            <tr>
              <td style="width: 110px; vertical-align: middle;">아이디</td>
              <td><input id="memID" name="memID" type="text" maxlength="20" placeholder="아이디를 입력하세요" class="form-control"></td>
              <td style="width: 110px;"><button type="button" onclick="registerCheck()" class="btn btn-primary btn-sm">중복확인</button></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">비밀번호</td>
              <td colspan="2"><input id="memPassword1" name="memPassword1" type="password" maxlength="20" placeholder="비밀번호를 입력하세요" class="form-control" onkeyup="passwordCheck()"></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">비밀번호 확인</td>
              <td colspan="2"><input id="memPassword2" name="memPassword2" type="password" maxlength="20" placeholder="비밀번호를 확인하세요" class="form-control" onkeyup="passwordCheck()"> <span id="passMessage"></span></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">이름</td>
              <td colspan="2"><input id="memName" name="memName" type="text" maxlength="20" placeholder="이름을 입력하세요" class="form-control"></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">나이</td>
              <td colspan="2"><input id="memAge" name="memAge" type="number" maxlength="3" placeholder="나이을 입력하세요" class="form-control"></td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">성별</td>
              <td colspan="2">
                <div class="form-group text-center">
                  <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-primary active"><input id="memGender" name="memGender" type="radio" autocomplete="off" value="남자" checked>남자 </label> <label class="btn btn-primary"><input id="memGender" name="memGender" type="radio" autocomplete="off" value="여자">여자 </label>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td style="width: 110px; vertical-align: middle;">이메일</td>
              <td colspan="2"><input id="memEmail" name="memEmail" type="text" maxlength="20" placeholder="이메일을 입력하세요" class="form-control"></td>
            </tr>
            <tr>
              <td colspan="3" class="text-right"><input type="button" value="등록" class="btn btn-primary btn-sm" onclick="goInsert()"></td>
            </tr>
          </table>
        </form>
      </div>
      <!-- 모달창 -->
      <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
          <!-- Modal content-->
          <div id="checkType" class="modal-content">
            <div class="modal-header panel-heading">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">메세지 확인</h4>
            </div>
            <div class="modal-body">
              <p id="checkMessage"></p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 회원가입 실패 모달창 -->
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