package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.board.entity.Board;
import kr.board.mapper.BoardMapper;

@Controller
public class BoardController {
	
	@Autowired
	private BoardMapper boardmapper;
	
	@RequestMapping("/")
	public String main() {
		return "main";
	}
	//JSON 형식으로 변환해서 리턴할려면 @ResponseBody 붙이기 
	//(Jackson-Databind API를 pom.xml에 추가하기)
	@RequestMapping("/boardList.do")
	public @ResponseBody List<Board> boardList(){
		List<Board> list = boardmapper.getLists();
		
		
		return list;
	}
}
