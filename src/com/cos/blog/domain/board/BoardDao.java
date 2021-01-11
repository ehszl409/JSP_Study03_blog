package com.cos.blog.domain.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cos.blog.config.DB;
import com.cos.blog.domain.board.dto.DetailReqDto;
import com.cos.blog.domain.board.dto.UpdateReqDto;
import com.cos.blog.domain.board.dto.saveReqDto;



public class BoardDao {
	

	
	
	
	public int update(UpdateReqDto dto) { // 조회수 증가
		String sql = "UPDATE board SET title = ?, content = ? WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getId());
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
		
	}
	
	
//<<<<<<< Updated upstream
	public int deleteById(int boardId) { // 게시글 삭제
		String sql = "DELETE FROM board WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
		
	}
	public int updateReadCount(int id) { // 조회수 증가
		String sql = "UPDATE board SET readCount = readCount+1 WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
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
	public int save(saveReqDto dto) { // 게시물 작성
//=======
//	public int save(saveReqDto dto) { // 글쓰기
//>>>>>>> Stashed changes
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
	public List<Board> findAll(int page) { // 게시물 목록
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
	
	
		public int count() { // 총 게시글 찾기
			// 쿼리 준비
			String sql = "SELECT count(*) FROM board";
			Connection conn = DB.getConnection();
			// 쿼리를 파싱해준다.
			PreparedStatement pstmt = null;
			
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();		
				if(rs.next()) {
					return rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DB.close(conn, pstmt, rs);
			}
			return -1;
		}
		
		public DetailReqDto findById(int id) { // 게시글 상세보기
			StringBuffer sb = new StringBuffer();
			sb.append("select b.id, b.title, b.content, b.readCount, b.userId, u.username from ");
			sb.append("board b inner join user u ");
			sb.append("on b.userId = u.Id ");
			sb.append("where b.Id = ?");
			
			Connection conn = DB.getConnection();
			PreparedStatement pstmt = null;	
			ResultSet rs = null;
			try {
				String sql = sb.toString();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				rs = pstmt.executeQuery();		
				if(rs.next()) {
					DetailReqDto dto = new DetailReqDto();
						dto.setId(rs.getInt("b.id"));
						dto.setTitle(rs.getString("b.title"));
						dto.setContent(rs.getString("b.content"));
						dto.setReadCount(rs.getInt("b.readCount"));
						dto.setUserId(rs.getInt("b.userId"));
						dto.setUsername(rs.getString("u.username"));
					return dto;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DB.close(conn, pstmt, rs);
			}
			return null;
		}
		
		
		
	
}
