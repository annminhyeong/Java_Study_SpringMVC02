package kr.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

//빈(환경설정) 등록을 위한 어노테이션
@Configuration

//web과 시큐리티를 연결하는 역할
@EnableWebSecurity

//스프링 시큐리티 환경설정 파일
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//보안요청에 대한 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//한글 인코딩 설정
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
	}
}
