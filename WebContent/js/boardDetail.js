function deleteById(boardId) {
	$.ajax({
		/* 질문) 왜 post를 delete로 바꾸면 실행되지 않는 이유 */
		type: "post",
		url: "/blog/board?cmd=delete&id=" + boardId,
		dataType: "json"
	})
		.done(function(result) {
			if (result.StatusCode == 1) {
				console.log(result);
				alert("게시물 삭제 성공");
				location.href = "index.jsp";
			} else {
				console.log(result);
				alert("게시물 삭제 실패");
			}
		});

}

function addReply(resultData){
	
	var replyitem = `<li id="reply-${resultData.id}" class="media">`;
	replyitem += `<div class="media-body">`;
	replyitem += `<strong class="text-primary">${resultData.userId}</strong>`;
	replyitem += `<p>${resultData.content}</p>`;
	replyitem += `</div>`;
	replyitem += `<div class="m-2">`;
	replyitem += `<i onclick="deleteReply(${resultData.id})" class="material-icons">delete</i>`;
	replyitem += `</div>`;
	replyitem += `</li>`;
	
	$("#reply__list").prepend(replyitem);
}

function deleteReply(id){	
	$.ajax({
		type : "post",
		url : "/blog/reply?cmd=delete&id="+id,
		dataType : "json"
	}).done(function(result) { //  { "statusCode" : 1 }
		if (result.statusCode == 1) {
			console.log(result);
			$("#reply-"+id).remove();
		} else {
			alert("댓글삭제 실패");
		}
	});
}



function replySave(userId, boardId) {
	var data = {
		userId: userId,
		boardId: boardId,
		content: $("#content").val()
	}
	console.log(data);

	$.ajax({
		type: "post",
		url: "/blog/reply?cmd=save",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	})
		.done(function(result) {
			if (result.statusCode == 1) {
				console.log(result);
				alert("댓글 달기 성공");
				addReply(result.data);
				$("#content").val("");
		
				// 댓글 시스템은 굳이 ajax요청을 하지 않아도 된다.
				// 새로고침 한 번이면 댓글 데이터 정도는 불러오는데 서버에 부하도 적고
				// 코드 짜는것도 심플해지기 떄문이다.
				//location.reload();
			} else {
				console.log(result);
				alert("댓글 달기 실패");
			}
		});
}