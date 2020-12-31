package com.cos.blog.domain.user.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class JoinReqDto {
	private String password;
	private String email;
	private String address;
	
}
