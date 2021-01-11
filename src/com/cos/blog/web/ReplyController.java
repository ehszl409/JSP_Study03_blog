package com.cos.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.dto.CommonRespDto;
import com.cos.blog.domain.reply.Reply;
import com.cos.blog.domain.reply.dto.ReplyReqDto;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.ReplyService;
import com.cos.blog.utill.Script;
import com.google.gson.Gson;


@WebServlet("/reply")
public class ReplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public ReplyController() {
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
		ReplyService replyService = new ReplyService();
		
		if(cmd.equals("save")) {
			System.out.println("ajax통신 확인.");
			BufferedReader br = request.getReader();
			String readData = br.readLine();
			System.out.println("readData: "+ readData);
			Gson gson = new Gson();
			ReplyReqDto dto = gson.fromJson(readData, ReplyReqDto.class);
			System.out.println("dto: "+ dto);
			
			int result = replyService.댓글쓰기(dto);
			// 댓글 id를 찾아야 한다.
			CommonRespDto<Reply> commonRespDto = new CommonRespDto<>();
			Reply reply = null;
			if(result != -1) {
				reply = replyService.댓글찾기(result);
				commonRespDto.setStatusCode(1);
				commonRespDto.setData(reply);
			} else {
				commonRespDto.setStatusCode(-1);
			}
			
			System.out.println("StatusCode: " + commonRespDto.getStatusCode());
			System.out.println("Data: " + commonRespDto.getData());
		
			String respData = gson.toJson(commonRespDto);
			Script.responseData(response, respData);
			
		} else if(cmd.equals("delete")) {
			int id = Integer.parseInt(request.getParameter("id"));
			int result = replyService.댓글삭제(id);

			CommonRespDto commonDto = new CommonRespDto<>();
			commonDto.setStatusCode(result);  //1, -1

			Gson gson = new Gson();
			String jsonData = gson.toJson(commonDto);
			// { "statusCode" : 1 }
			Script.responseData(response, jsonData);
		}
		
		
		
	}

}
