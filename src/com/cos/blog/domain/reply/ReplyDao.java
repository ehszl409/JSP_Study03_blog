package com.cos.blog.domain.reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cos.blog.config.DB;
import com.cos.blog.domain.board.dto.DetailReqDto;
import com.cos.blog.domain.reply.dto.ReplyReqDto;

public class ReplyDao {
	
	
	public int deleteById(int id) {
		String sql = "DELETE FROM reply WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int result = pstmt.executeUpdate();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	public List<Reply> findAll(int boardId) { // 게시글 상세보기
		String sql = "SELECT id, userId, boardId, content, createDate FROM reply WHERE boardId = ? ORDER BY id DESC";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();	
			List<Reply> replys = new ArrayList<>();
			while(rs.next()) {
				Reply reply = new Reply();
					reply.setId(rs.getInt("id"));
					reply.setUserId(rs.getInt("userId"));
					reply.setBoardId(rs.getInt("boardId"));
					reply.setContent(rs.getString("content"));
					reply.setCreateDate(rs.getTimestamp("createDate"));
					replys.add(reply);
			}
			return replys;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	
	public Reply findById(int id) { // 게시글 상세보기
		String sql = "SELECT id, userId, boardId, content FROM reply WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();		
			if(rs.next()) {
				Reply reply = new Reply();
					reply.setId(rs.getInt("id"));
					reply.setUserId(rs.getInt("userId"));
					reply.setBoardId(rs.getInt("boardId"));
					reply.setContent(rs.getString("content"));
				return reply;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public int reply(ReplyReqDto dto) { 
		String sql = "INSERT INTO reply(userId, boardId, content, createDate) VALUES(?,?,?,now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int generateKey;
				
		try {
			// INSERT된 행의 고유 번호를 매기는 것.
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, dto.getUserId());
			pstmt.setInt(2, dto.getBoardId());
			pstmt.setString(3, dto.getContent());
			int result = pstmt.executeUpdate();
			
			// 만들어진 ID키를 Resultset에 담아 줄 수 있다.
			rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				// generateKey는 제일 앞 컬럼으로 생성 된다.
				generateKey = rs.getInt(1);
				System.out.println("생성된 ID키:" + generateKey);
				if(result == 1) {
					return generateKey;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
		
	}
}
