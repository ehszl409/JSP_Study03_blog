package com.cos.blog.domain.board.dto;

import lombok.Data;

@Data
public class CommonRespDto<T> {
	private int statusCode;
	private T data;
}
