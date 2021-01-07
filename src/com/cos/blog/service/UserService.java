package com.cos.blog.service;

import com.cos.blog.domain.user.User;
import com.cos.blog.domain.user.UserDao;
import com.cos.blog.domain.user.dto.JoinReqDto;
import com.cos.blog.domain.user.dto.LoginReqDto;
import com.cos.blog.domain.user.dto.UpdateReqDto;

public class UserService {
	private UserDao userDao;

	public UserService() {
		userDao = new UserDao();
	}

	// 회원가입 회원수정 로그인 로그아웃 아이디중복체크

	public int 회원가입(JoinReqDto dto) {
		int result = userDao.save(dto);
		return result;
	}

	// 컨트롤러한테 요청을 받으면 아이디가 존재하는지 비교해서 리턴해주는 메소드이다.
	// 서비스 역시 연산을 하는 곳이 아니라 요청에 대해 필요한 메소드를 분류 하여 동작하는 곳이다.
	// 그래서 DB에서 쿼리를 대입하고 연산하는 역할은 userDao를 따로 만들어서 관리 하도록 한다.
	// 여기서 서비스는 userDao.findByUsernameAndPassword()를 호출해서 연산에 대한 리턴 값만 받아서 
	// 컨트롤러 한테 전달한다. 이 때 컨트롤러로 부터 받은 dto를 dao한테 전달 한다.
	public User 로그인(LoginReqDto dto) {
		return userDao.findByUsernameAndPassword(dto);
	}

	public int 회원수정(UpdateReqDto dto) {

		return -1;
	}

	public int 유저네임중복체크(String username) {
		int result = userDao.findByUsername(username);
		return result;
	}

}
