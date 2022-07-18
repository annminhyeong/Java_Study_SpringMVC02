package kr.board.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

//스프링 시큐리티 설정파일
//내부적으로 DelegatingFilterPoxy를 스프링에 등록하여 스프링 시큐리티를 내부적으로 동작시킨다.
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer{
	
}
