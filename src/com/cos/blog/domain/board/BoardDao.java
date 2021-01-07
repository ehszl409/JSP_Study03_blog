package com.cos.blog.domain.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cos.blog.config.DB;
import com.cos.blog.domain.board.dto.saveReqDto;
import com.cos.blog.domain.user.dto.JoinReqDto;

public class BoardDao {
	
	public int save(saveReqDto dto) { // 회원가입
		String sql = "INSERT INTO board(userId, title, content, createDate) VALUES(?,?,?,now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getUserId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
		
	}
	
	
	
	// DAO의 역할 DB에 접근에 쿼리를 실행해서 데이터를 리턴해줘야 한다
	public List<Board> findAll(int page) {
		// 쿼리 준비
		String sql = "SELECT id, userId, title, content, readCount, createDate FROM board ORDER BY id DESC LIMIT ?,4";
		// DB 연결
		Connection conn = DB.getConnection();
		// 쿼리를 파싱해준다.
		PreparedStatement pstmt = null;
		// 게시글들을 담을 리스트 객체 생성.
		List<Board> boards = new ArrayList<>();
		
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, page*4);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board boardList = new Board();
				boardList.setId(rs.getInt("id"));
				boardList.setUserId(rs.getInt("userId"));
				boardList.setTitle(rs.getString("title"));
				boardList.setContent(rs.getString("content"));
				boardList.setReadCount(rs.getInt("readCount"));
				boardList.setCreateDate(rs.getTimestamp("createDate"));
				boards.add(boardList);
			}
			return boards;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
}
