<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- 회원정보(MemberUser 객체) -->
<c:set var="mvo" value="${SPRING_SECURITY_CONTEXT.authentication.principal}"/>
<!-- 회원 권한정보 -->
<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities}"/>
<script type="text/javascript">
    let csrfHeaderName = '${_csrf.headerName}';
    let csrfTokenValue = '${_csrf.token}';
    
	function logout() {
		$.ajax({
			url        : '${contextPath}/logout',
			//시큐리티에서는 로그아웃을 요청할때 post 방식으로 보내야 함
			type       : 'post',
			//스프링 시큐리티는 항상 토큰을 넘겨줘야함
			beforeSend : (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfTokenValue),
			success    : () => location.href = '${contextPath}/',
			error      : ()=> alert('error')
		})
	}
</script>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${contextPath}">SpringMVC10</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="${contextPath}">Home</a></li>
        <li><a href="boardMain.do">게시판</a></li>
        <li><a href="#">Page 2</a></li>
      </ul>
        <!-- 스프링 시큐리티에서 인증을 확인할때 쓰이는 태그, isAnonymos()는 인증이 안 되었다는 뜻 -->
        <security:authorize access="isAnonymous()">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="${contextPath}/memLoginForm.do"><span class="glyphicon glyphicon-log-in"></span>로그인</a></li>
            <li><a href="${contextPath}/memJoinForm.do"><span class="glyphicon glyphicon-user"></span>회원가입</a></li>
          </ul>
        </security:authorize>
        <!-- 스프링 시큐리티에서 인증을 확인할때 쓰이는 태그, isAuthenticated()는 인증이 되었다는 뜻  -->
        <security:authorize access="isAuthenticated()">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="${contextPath}/memUpdateForm.do"><span class="glyphicon glyphicon-wrench"></span>회원정보수정</a></li>
            <li><a href="${contextPath}/memImageForm.do"><span class="glyphicon glyphicon-picture"></span>사진등록</a></li>
            <!-- 스프링 시큐리티에서는 post방식으로 /logout이라고 적어주면 자동으로 logout()메서드와 매칭됨  -->
            <li><a href="javascript:logout()"><span class="glyphicon glyphicon-log-out"></span>로그아웃</a></li>
              <c:if test="${empty mvo.member.memProfile}">
                <li>
                  <img src="${contextPath}/resources/img/person.png" class="img-circle" style="width: 50px; height: 50px;">
                  ${mvo.member.memName}님
                  (
                    <!-- 스프링에서 권한정보를 가져올때 사용하는 태그 -->
                    <security:authorize access="hasRole('ROLE_USER')">U</security:authorize>
                    <security:authorize access="hasRole('ROLE_MANAGER')">M</security:authorize>
                    <security:authorize access="hasRole('ROLE_ADMIN')">A</security:authorize>
                  )
                </li>
              </c:if>
              <c:if test="${!empty mvo.member.memProfile}">
                <li>
                  <img src="${contextPath}/resources/upload/${mvo.member.memProfile}" class="img-circle" style="width: 50px; height: 50px;">
                  ${mvo.member.memName}님
                  (
                    <!-- 스프링에서 권한정보를 가져올때 사용하는 태그 -->
                    <security:authorize access="hasRole('ROLE_USER')">U</security:authorize>
                    <security:authorize access="hasRole('ROLE_MANAGER')">M</security:authorize>
                    <security:authorize access="hasRole('ROLE_ADMIN')">A</security:authorize>
                  )
                </li>
              </c:if>
          </ul>
        </security:authorize>
    </div>
  </div>
</nav>