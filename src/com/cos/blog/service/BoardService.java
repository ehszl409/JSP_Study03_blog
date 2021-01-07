package com.cos.blog.service;

import java.util.List;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.BoardDao;
import com.cos.blog.domain.board.dto.saveReqDto;

// 게시판에 대한 많은 서비스들을 컨트롤러로 부터 요청받아 DB에게 분기 시켜주는 역할

public class BoardService {
	
	private BoardDao boardDao;
	
	// 객체를 왜 생성자에 만든이유??
	public BoardService() {
		boardDao = new BoardDao();
	}
	
	// 글쓰기가 잘 되었는지 확인만 해주면 되서 int를 리턴해준다.
	public int 글쓰기(saveReqDto dto) {
		return boardDao.save(dto);
	}
	
	// 서비스는 컨트롤러로 부터 게시글 목록 보는 함수를 요청 받았고,
	// DAO한테 fidnAll()이라는 함수를 요청해서 DB에 있는 게시글 데이터를
	// 리턴하길 요청한다. 역시 데이터 양이 많아 리스트로 리턴 해야한다고 요청한다.
	public List<Board> 목록보기(int page){
		return boardDao.findAll(page);
	}
	
}
