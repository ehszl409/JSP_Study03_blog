package com.cos.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.dto.CommonRespDto;
import com.cos.blog.domain.board.dto.DetailReqDto;
import com.cos.blog.domain.board.dto.UpdateReqDto;
import com.cos.blog.domain.board.dto.saveReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.utill.Script;
import com.google.gson.Gson;



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
			
			int boardCount = boardService.글개수();
			
			int lastPage = (boardCount-1)/4; // 4/4 => 0 첫페이지page = 0
			double currentPosition = (double)page/lastPage*100;
		
			System.out.println("boardCount: " + boardCount);
			System.out.println("lastPage: " + lastPage);
			request.setAttribute("lastPage", lastPage);
			request.setAttribute("currentPosition", currentPosition);
			
			System.out.println(boards);
			request.setAttribute("boardList", boards);
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("detail")) { // 상세보기 구현
			System.out.println("detail접속.");
			int id = Integer.parseInt(request.getParameter("id"));
			DetailReqDto dto = boardService.글상세보기(id);
			
			if(dto == null) {
				Script.back(response, "글 상세보기를 불러오는 것이 실패 했습니다.");
			} else {
			request.setAttribute("dto", dto);
			System.out.println("dto:" + dto);
			RequestDispatcher dis = 
			request.getRequestDispatcher("board/detail.jsp");
			dis.forward(request, response);
			}
		} else if (cmd.equals("delete")) {
			// ajax요청시 URL에 있는 id값을 가져 온다.
			int id = Integer.parseInt(request.getParameter("id"));
			
			int result = boardService.게시글삭제(id);
			System.out.println("result: " + result);
			CommonRespDto<String> commonRespDto = new CommonRespDto<>();
			commonRespDto.setStatusCode(result);
			commonRespDto.setData("성공");
			
			// 4. 자바 오브젝트를 Json으로 바꿔 응답해주기
			Gson gson = new Gson();
			String respData = gson.toJson(commonRespDto);
			System.out.println("commonRespDto: " + commonRespDto);
			System.out.println("respData: " + respData);
			PrintWriter out = response.getWriter();
			out.print(respData);
			out.flush();
		} else if(cmd.equals("updateForm")) { // 수정 버튼을 누르면 동작하는 요청되는 URI
			// 상세보기 데이터 필요
			// board.id를 받아온다
			int id = Integer.parseInt(request.getParameter("id"));
			// 글상세보기를 호출하는 이유는 
			// 글상세보기에서 title, content를 얻어오기 위해서 입니다.
			DetailReqDto dto = boardService.글상세보기(id);
			// 얻어 온걸 준비해주고
			request.setAttribute("dto", dto);
			// updateForm으로 던저 줍니다.
			// updateForm은 글 수정을 위한 페이지 입니다.
			RequestDispatcher dis = 
			request.getRequestDispatcher("board/updateForm.jsp");
			dis.forward(request, response);
		} else if(cmd.equals("update")) {
			int id = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			UpdateReqDto dto = new UpdateReqDto().builder()
					.id(id)
					.title(title)
					.content(content)
					.build();
			
			int result = boardService.글수정(dto);
			if (result == 1) {
				// 왜 샌드리다렉을 사용헸을까??
				
//				response.sendRedirect("/blog/board?cmd=detail&id="+id);
				RequestDispatcher dis = 
				request.getRequestDispatcher("/board?cmd=detail&id="+id);
				dis.forward(request, response);
			} else {
				Script.back(response, "글 수정에 실패 했습니다.");
			}
			
			
		}
	}

}
