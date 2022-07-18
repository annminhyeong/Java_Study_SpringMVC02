package kr.board.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//빈(환경설정) 등록을 위한 어노테이션
@Configuration

//WEB MVC 어노테이션을 쓸 수 있게 선언 
@EnableWebMvc

//@Controller, @Service, @Repository 어노테이션을 스캔해 bean객체에 등록해줌
@ComponentScan(basePackages = {"kr.board.controller"})

//servlet-context.xml
public class ServletConfig implements WebMvcConfigurer{
	
	//jsp에 직접 접근 못하므로 jsp에 접근가능하도록 경로설정
	//Prefix(경로)와 Suffix(확장자) 설정을 함 
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");
		registry.viewResolver(bean);
	}
	
	//이미지, 자바스크립트, CSS 그리고 HTML 파일과 같은 정적인 리소스의 경로 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
}
