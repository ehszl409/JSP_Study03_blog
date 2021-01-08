package com.cos.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.user.User;
import com.cos.blog.domain.user.dto.JoinReqDto;
import com.cos.blog.domain.user.dto.LoginReqDto;
import com.cos.blog.service.UserService;
import com.cos.blog.utill.Script;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		UserService userService = new UserService();

		// http://localhost:8000/blog/user?cmd=loginForm
		if (cmd.equals("loginForm")) {
			// 서비스 호출
			RequestDispatcher dis = 
			request.getRequestDispatcher("user/loginForm.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("login")) { // 로그인 요청이 들어왔을때
			// 1. 요청한 데이터 값을 파싱한다.
			// x-www-form-urlencorded 형식의 username과 password를 파싱해서 저장한다.
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			// 2. 서비스에게 로그인을 위해 DB랑 요청 받은 데이터를 비교 해달라고 부탁한다.
			// 로그인을 위해 만들어진 DTO의 객체를 생성하고 값을 넣어준다.
			// 왜냐하면 컨트롤러에서 요청 값을 받아서 넣어줘야지 서비스에게 전달해줄 수 있다.
			// 여기서 DTO를 사용하는 이유는 로그인에는 유저네임과 패스워드 데이터만 전송해주면
			// 되기 때문에 2개의 필드값만 가지고 있는 로그인 전용 DTO를 만들어 넘겨준다.
			// 이렇게 하지 않고 user 모델에 담아서 주면 요청 받지 못한 필드 값들은 모두
			// null이 들어가서 의도치 않은 실수나 오류가 생기게 된다.
			LoginReqDto dto = new LoginReqDto();
			dto.setUsername(username);
			dto.setPassword(password);
			
			// 서비스 메소드 중 로그인()메소드를 호출하고 그 안에 dto값을 담아서 보내준다.
			// 이 후 리턴 받는 값은 유저타입의 userEntity속에 담아 주도록 한다.
			User userEntity = userService.로그인(dto);
			
			// 3. 서비스로 부터 리턴 받은 값으로 세션값을 저장한다.
			// userEntity에 값이 담겼다라는 것은 가입된 정보가 있어서 DB로부터 가져왔다는 뜻이기에
			// userEntity가 null이 아닌지만 판단 해주면 된다.
			if(userEntity != null) {
				// 세션 공간을 가져오고 'principal'이라는 key값에 userEntity와 함께 담아준다.
				HttpSession session = request.getSession();
				session.setAttribute("principal", userEntity);// 인증주체
				// 메인 페이지로 이동 시킨다.
				response.sendRedirect("index.jsp");
			}else {
				Script.back(response, "로그인실패");
			}
			
			
		} else if (cmd.equals("joinForm")) {
			RequestDispatcher dis = 
			request.getRequestDispatcher("user/joinForm.jsp");
			dis.forward(request, response);
			
		} else if (cmd.equals("join")) {
			// 서비스 호출
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			
			JoinReqDto dto = new JoinReqDto();
			dto.setUsername(username);
			dto.setPassword(password);
			dto.setEmail(email);
			dto.setAddress(address);
			System.out.println("회원가입 : " + dto);
			
			int result = userService.회원가입(dto);
			if (result == 1) {
				response.sendRedirect("index.jsp");
			} else {
				// Script.back();
				Script.back(response, "회원가입 실패");
			}
		} else if (cmd.equals("usernameCheck")) {
			BufferedReader br = request.getReader();
			String username = br.readLine();
			System.out.println(username);
			int result = userService.유저네임중복체크(username);
			PrintWriter out = response.getWriter();
			if (result == 1) {
				out.print("ok");
			} else {
				out.print("fail");
			}
			out.flush();
		} else if(cmd.equals("logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("index.jsp");
		}

	}
}
