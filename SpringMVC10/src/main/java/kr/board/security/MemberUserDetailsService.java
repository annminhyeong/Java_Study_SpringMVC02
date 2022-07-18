package kr.board.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.board.entity.Member;
import kr.board.entity.MemberUser;
import kr.board.mapper.MemberMapper;

//2. UserDetailsService
public class MemberUserDetailsService implements UserDetailsService{
	
	@Autowired
	private MemberMapper membermapper;
	
	@Override
	//username : 사용자 로그인 아이디
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//로그인 처리하기
		System.out.println("로그인 요청이 오면 실행");
		Member mvo = membermapper.memLogin(username);
		
		//member(로그인값)를 UserDetail의 자손 User클래스를 구현해서 User로 리턴해야 로그인이 가능함
		if(mvo != null) {
			return new MemberUser(mvo);
		}else {
			System.out.println("user with username " + username + "does not exist");
			throw new UsernameNotFoundException("user with username " + username + "does not exist");
		}
	}

}
