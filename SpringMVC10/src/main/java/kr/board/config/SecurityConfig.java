package kr.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import kr.board.security.MemberUserDetailsService;

//빈(환경설정) 등록을 위한 어노테이션
@Configuration

//web과 시큐리티를 연결하는 역할
@EnableWebSecurity

//스프링 시큐리티 환경설정 파일
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//스프링 시큐리티에 memberUserDetailsService 객체생성
	@Bean
	public UserDetailsService memberUserDetailsService() {
		return new MemberUserDetailsService();
	}
	
	//스프링 보안 인증 메니저에게 memberUserDetailsService를 전달하기 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//memberUserDetailsService 에서 넘어온 passowrd는 암호화되어 있지 않기 때문에 암호화 한 뒤 인증 메니저가 처리
		//userDetailsService : userDetailsService를 등록하는 함수
		auth.userDetailsService(memberUserDetailsService()).passwordEncoder(passwordEncoder());
		System.out.println("인증메니저 시작");
	}
	
	//보안요청에 대한 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//한글 인코딩 설정
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
		
		//클라이언트 요청에 대한 권한 설정(HttpSecurity)
		http
			.authorizeRequests() //클라이언트가 권한요청을 했을때 
			.antMatchers("/").permitAll() //url에 따라서 권한 매칭 (permitAll은 모든사용자)
			.and() //and
			//formLogin : 요청이 왔을때 스프링 기본 로그인으로 이동, loginPage : 요청이 왔을때 사용자 커스텀 로그인 페이지로 이동    
			.formLogin().loginPage("/memLoginForm.do")
			//loginProcessingUrl : 로그인 요청이 왔을 때 어떤 url로 로그인 처리를 할지 설정
			.loginProcessingUrl("/memLogin.do").permitAll()
			.and() //and
			//로그아웃 요청이 왔을때 세션 끊고 로그아웃이 성공하면 로그아웃 성공 url로 이동
			.logout().invalidateHttpSession(true).logoutSuccessUrl("/")
			.and() //and
			//오류발생시 오류페이지로 이동
			.exceptionHandling().accessDeniedPage("/access-denied");
			
		
	}
	
	//비밀번호 인코딩 객체등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
