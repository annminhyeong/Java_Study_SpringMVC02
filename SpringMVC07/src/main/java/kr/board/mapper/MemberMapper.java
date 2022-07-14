package kr.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.Member;

//@Mapper : mybatis API가 가지고 있음
@Mapper
public interface MemberMapper {
	
	//아이디 중복체크
	public Member registerCheck(String memID);
	
	//회원가입 (성공1, 실패0)
	public int register(Member m);
	
	//로그인
	public Member memLogin(Member mvo);
	
	//회원정부 수정
	public int memUpdate(Member mvo);
}
