package com.cos.blog.service;

import java.util.List;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.BoardDao;
import com.cos.blog.domain.board.dto.DetailReqDto;
import com.cos.blog.domain.board.dto.UpdateReqDto;
import com.cos.blog.domain.board.dto.saveReqDto;
import com.cos.blog.domain.reply.dto.ReplyReqDto;

// 게시판에 대한 많은 서비스들을 컨트롤러로 부터 요청받아 DB에게 분기 시켜주는 역할

public class BoardService {
	
	private BoardDao boardDao = new BoardDao();
	
	
//	// 객체를 왜 생성자에 만든이유??
//	public BoardService() {
//		boardDao = new BoardDao();
//	}
//	
	
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
	
	public int 글개수() {
		return boardDao.count();
	}
	
	public int 글개수(String keyword) {
		return boardDao.count(keyword);
	}
	
	// 글 상세보기 서비스를 호출했다면
	// 웹 화면에 필요한 상세보기 정보를 한 번에 다 리턴해줘야하는 것이 올바르다
	// 서비스를 나눠서 저기서 반 다른 곳에서 반 이렇게 상세보기 정보를 나눠서 들고오는 것은
	// 서비스의 개념에 맞지 않다. 하지만 지금은 그렇게 구현 하겠
	public DetailReqDto 글상세보기(int id) {
		// 조회수를 올리는 로직 구현
		
		int result = boardDao.updateReadCount(id);
		if(result == 1) {
			return boardDao.findById(id);			
		} else 
			return null;
	}
	
	public int 게시글삭제(int boardId) {
		return boardDao.deleteById(boardId);
	}
	
	public int 글수정(UpdateReqDto dto) {
		return boardDao.update(dto);
	}
	
	public List<Board> 글검색(String keyword, int page){
		return boardDao.findByKeyword(keyword, page);
	}
	
	
	
}
