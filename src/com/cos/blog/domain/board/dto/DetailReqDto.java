package com.cos.blog.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailReqDto {
	private int id;
	private String title;
	private String content;
	private int readCount;
	private String username;
	private int userId;
	
	// 루시 필터 적용해보기
	public String getTitle() {
		return title.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
