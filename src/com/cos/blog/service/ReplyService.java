package com.cos.blog.service;

import java.util.List;

import com.cos.blog.domain.reply.Reply;
import com.cos.blog.domain.reply.ReplyDao;
import com.cos.blog.domain.reply.dto.ReplyReqDto;

public class ReplyService {
	private ReplyDao replyDao;
	
	public ReplyService() {
		replyDao = new ReplyDao();
	}
	
	public int 댓글쓰기(ReplyReqDto dto) {
		return replyDao.reply(dto);
	}
	
	public Reply 댓글찾기(int id) {
		return replyDao.findById(id);
	}
	
	public List<Reply> 댓글목록보기(int boardId){
		return replyDao.findAll(boardId);
	}
	
	public int 댓글삭제(int id) {
		return replyDao.deleteById(id);
	}
}
