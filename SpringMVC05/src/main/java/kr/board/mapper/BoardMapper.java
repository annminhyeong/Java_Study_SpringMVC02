package kr.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.Board;

//@Mapper : mybatis API가 가지고 있음
@Mapper
public interface BoardMapper {
	//전체리스트 조회
	public List<Board> getLists();
	
	//게시판 등록
	public void boardInsert(Board vo);
}
