package com.cos.blog.domain.user.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UpdateReqDto {
	private String username;
	private String password;
	private String email;
	private String address;
	
}
