<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">

	<form action="/blog/user?cmd=join" method="post" onsubmit="return valid()">

		<div class="d-flex justify-content-end">
			<button type="button" class="btn btn-info" onClick="usernameCheck()">중복체크</button>
		</div>
		<input type="text" name="username" id="username" class="form-control"
				placeholder="Enter Username" required /></br>

		<div class="form-group">
			<input type="password" name="password" class="form-control"
				placeholder="Enter Password" required />
		</div>

		<div class="form-group">
			<input type="email" name="email" class="form-control"
				placeholder="Enter Email" required />
		</div>
		
		<div class="d-flex justify-content-end">
			<button type="button" class="btn btn-info" onClick="goPopup();">주소검색</button>
		</div>
		<div class="form-group">
			<input type="text" name="address" id="address" class="form-control"
				placeholder="Enter Address" required readonly />
		</div>
		
		<button type="submit" class="btn btn-primary">회원가입완료</button>
	</form>
</div>

<script>
	// 회원가입 버튼을 아이디 중복체크를 하지 않고 동작하지 않도록 하기 위한 변수
	var isChecking = false;
	// valid()는 회원가입 버튼이 동작하기전 무조건 실행된다.
	function valid() {
		// false 상태라면 아이디 중복체크를 해라고 알림 창을 띄우고 
		// 계속해서 false 상태를 유지해 회원가입을 못하도록 한다.
		if (isChecking == false) {
			alert("아이디 중복체크를 해주세요");
		}
		return isChecking;
	}

	// 아이디 중복 체크를 하는 함수
	// 중복 체크버튼을 누르면 무조건 실행된다.
	function usernameCheck() {
		// DB에서 확인해서 정상이면 isChecking = true
		// username의 값을 JQuery로 가져와서 변수에 담는다.
		var username = $("#username").val();
		// ajax통신을 하기위한 정보를 명시한다
		$.ajax({
			type : "POST", // 요청하는 방식
			url : "/blog/user?cmd=usernameCheck", // 요청하는 주소
			data : username, // 요청하는 데이터
			contentType : "text/plain; charset=utf-8", // 요청하는 데이터 타입
			dataType : "text" // 응답 받을 데이터의 타입을 적으면 자바스크립트 오브젝트로 파싱해줌.
		}).done(function(data) { // ajax통신이 정상적으로 완료되고 응답 받은 데이터값이 done함수 속 으로에 들어가서 호출된다. 
			if (data === 'ok') { // 유저네임이 중복된다. 
				isChecking = false;
				alert('유저네임이 중복되었습니다.')
			} else {
				isChecking = true;
				alert("해당 유저네임을 사용할 수 있습니다.")
			}
		});
	}

	function goPopup() {
		// 주소검색을 수행할 팝업 페이지를 호출합니다.
		// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
		var pop = window.open("/blog/user/jusoPopup.jsp", "pop",
				"width=570,height=420, scrollbars=yes, resizable=yes");
	}
	function jusoCallBack(roadFullAddr) {
		var addressEl = document.querySelector("#address");
		addressEl.value = roadFullAddr;
	}
</script>

</body>
</html>