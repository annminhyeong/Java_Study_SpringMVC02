package kr.board.entity;

import lombok.Data;

//Lombok API : getter, setter, toString 자동추가
//@Data - Lombok API가 가지고 있음

@Data
public class Board {
	private int idx;        //번호
	private String memID;   //아이디
	private String title;   //제목
	private String content; //내용
	private String writer;  //작성자
	private String indate;  //작성일
	private int count;      //조회수
	
	public Board() {}
	Board(int idx, String memID, String title, String content, String writer, String indate, int count) {
		this.idx     = idx;
		this.memID   = memID;
		this.title   = title;
		this.content = content;
		this.writer  = writer;
		this.indate  = indate;
		this.count   = count;
	}
}
