package com.cos.blog.domain.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyReqDto {
	private int userId;
	private int boardId;
	private String content;
	
}
