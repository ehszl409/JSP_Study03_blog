package com.cos.blog.domain.user.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LoginReqDto {
	private String username;
	private String password;
	
}
