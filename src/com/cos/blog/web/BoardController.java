package com.cos.blog.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.BoardDao;
import com.cos.blog.domain.board.dto.saveReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.utill.Script;



@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
   
    public BoardController() {
        super();
      
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		HttpSession session = request.getSession();
		BoardService boardService = new BoardService();
		
		
		// 로그인을 하고 글쓰기를 했는지 판단하는 로직
		if (cmd.equals("saveForm")) {
			User principal = (User) session.getAttribute("principal");
			if(principal != null) {
				RequestDispatcher dis = 
				request.getRequestDispatcher("board/saveForm.jsp");
				dis.forward(request, response);
				
			} else {
				RequestDispatcher dis = 
				request.getRequestDispatcher("user/loginForm.jsp");
				dis.forward(request, response);
				
			} 
		} else if(cmd.equals("save")) {
			int userId = Integer.parseInt(request.getParameter("userId"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			System.out.println("title: "+title);
			System.out.println("content: "+content);
			
			// 글을 DB에 저장
			// 1. 글저장DTO를 만들어서 담는다.
			saveReqDto dto = new saveReqDto().builder()
					.userId(userId)
					.title(title)
					.content(content)
					.build();
			System.out.println("dto:"+dto);
			
			// 2. 서비스로 부터 DB에 글쓰기가 잘되었는지 리턴만 받아서 분기하면 된다.
			int result = boardService.글쓰기(dto);
			if (result == 1) { // 글쓰기 성공
				response.sendRedirect("index.jsp");
			} else {
				Script.back(response, "글쓰기가 실패했습니다.");
			}
			
		} else if(cmd.equals("list")) { // 게시글들 보여주기
			// 서비스한테 DB에 가서 게시물 목록을 가져오라고 요청
			// 그리고 가져오면 양이 많기 때문에 리스트에 저장한다.
			int page = Integer.parseInt(request.getParameter("page")); 
			List<Board> boards = boardService.목록보기(page);
			
			int boardMax = boardService.총게시글();
			int limitPage = boardMax/4;
			
			boolean isEnd = false;
			boolean isStart = false;
			if(page == limitPage) {
				isEnd = true;
				request.setAttribute("isEnd", isEnd);
			} else if(page == 0) {
				isStart = true;
				request.setAttribute("isStart", isStart);
			} else {
				isStart = false;
				isEnd = false;
				request.setAttribute("isStart", isStart);
				request.setAttribute("isEnd", isEnd);
			} 
			
			System.out.println("boards.size(): " + boards.size());
			System.out.println("boardMax: " + boardMax);
			System.out.println("limitPage: " + limitPage);
			System.out.println(boards);
			request.setAttribute("boardList", boards);
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
		}
	}

}
