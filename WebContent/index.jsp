<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	RequestDispatcher dis = 
	request.getRequestDispatcher("board?cmd=list");
	dis.forward(request, response);
	// 톰캣이 생성한 req, resp를 재사용한다.
	// 다시 접근하는 것이 아니라 내부적으로 움직인다는 뜻이다.
	// 그래서 필터를 거치지 않고 URI 접근이 가능하다.
%>