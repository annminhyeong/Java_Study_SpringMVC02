package kr.board.entity;

import lombok.Data;

@Data
public class AuthVO {
	private int no;       //번호
	private String memID; //아이디
	private String auth;  //권한
}
