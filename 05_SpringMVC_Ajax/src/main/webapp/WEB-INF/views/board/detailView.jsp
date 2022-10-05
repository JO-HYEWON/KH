<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세 정보</title>
<script src="/resources/js/jquery-3.6.1.min.js"></script>
</head>
<body>
	<h1 align="center">${board.boardNo }번 게시글 상세 보기</h1>
	<br><br>
	<table align="center" width="500" border="1">
		<tr>
			<td>제목</td>
			<td>${board.boardTitle }</td>
		</tr>
		<tr>
			<td>작성자</td>
			<td>${board.boardWriter }</td>
		</tr>
		<tr>
			<td>작성날짜</td>
			<td>${board.bCreateDate }</td>
		</tr>
		<tr>
			<td>조회수</td>
			<td>${board.boardCount }</td>
		</tr>
		<tr height="300">
			<td>내용</td>
			<td>${board.boardContents }
			<%-- <img alt="본문이미지" src="/resources/buploadFiles/${board.boardFileRename }"> --%>
			</td>
		</tr>
		<tr>
			<td>첨부파일</td>
			<td>
				<img alt="본문이미지" src="/resources/buploadFiles/${board.boardFileRename }" 
				width="300" height="300">
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<a href="/board/modifyView.kh?boardNo=${board.boardNo }&page=${page}">수정 페이지로 이동</a>
				<a href="#" onclick="boardRemove(${page});">삭제하기</a>
			</td>
		</tr>
	</table>
	<!-- 	댓글 등록 -->
	<table align="center" width="500" border="1">
		<tr>
			<td>
				<textarea rows="3" cols="55" name="replyContents" id="replyContents"></textarea>
			</td>
			<td>
				<button id="rSubmit">등록하기</button>
			</td>
		</tr>
	</table>
<!-- 	<form action="/board/addReply.kh" method="post"> -->
<%-- 		<input type="hidden" name="page" value="${page }"> --%>
<%-- 		<input type="hidden" name="refBoardNo" value="${board.boardNo }"> --%>
<!-- 		<table align="center" width="500" border="1"> -->
<!-- 			<tr> -->
<!-- 				<td> -->
<!-- 					<textarea rows="3" cols="55" name="replyContents"></textarea> -->
<!-- 				</td> -->
<!-- 				<td> -->
<!-- 					<input type="submit" value="등록하기"> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<!-- 	</form> -->
	<!-- 	댓글 목록 -->
	<table align="center" width="500" border="1" id="rtb">
		<thead>
			<tr>
<!-- 				댓글 갯수 -->
				<td colspan="4"><b id="rCount"></b></td> 
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	<script>
		getReplyList();
		function getReplyList() {
			var boardNo = "${board.boardNo }";
			$.ajax({
				url : "/board/replyList.kh",
				data : {"boardNo" : boardNo},
				type : "get",
				success : function(rList) {
					var $tableBody = $("#rtb tbody");
					$tableBody.html("");	// 내용 초기화 후 append()
					$("#rCount").text("댓글 (" + rList.length + ")");
					if(rList != null) {
						console.log(rList);
						for(var i in rList) {
							var $tr = $("<tr>");
							var $rWriter = $("<td width='100'>").text(rList[i].replyWriter);
							var $rContent = $("<td>").text(rList[i].replyContents);
							var $rCreateDate = $("<td width='100'>").text(rList[i].rCreateDate);
							var $btnArea = $("<td width='80'>")
												.append("<a href='#'>수정</a>")
												.append("<a href='#'>삭제</a>");
							$tr.append($rWriter); // <tr><td width='100'>khuser01</td></tr>
							$tr.append($rContent); // <tr><td width='100'>khuser01</td><td>댓글 내용</td></tr>
							$tr.append($rCreateDate);
							$tr.append($btnArea);
							$tableBody.append($tr);
							// <tbody><tr><td></td><td></td>...</tr></tbody>
						}
					}
				},
				error : function() {
					//console.log("서버 요청 실패!");
					alert("ajax 통신 실패! 관리자에게 문의하세요!!");
				}
			});
		}
	
		$("#rSubmit").on("click", function() {
			var refBoardNo = "${board.boardNo }";
			var replyContents = $("#replyContents").val();
			$.ajax({
				url : "/board/replyAdd.kh",
				data : {
					"refBoardNo" 	: refBoardNo, 
					"replyContents" : replyContents 
				},
				type : "post",
				success : function(result) {
					if(result == "success") {
						alert("댓글 등록 성공!");
						// DOM조작, 함수호출 등.. 가능
						$("#replyContents").val(""); // 작성 후 내용 초기화
						getReplyList();	// 댓글 리스트 출력
					}else {
						alert("댓글 등록 실패!");
					}
				},
				error : function() {
					alert("서버 요청 실패!");
				}
			});
		});
	
		function boardRemove(value) {
			event.preventDefault(); // 하이퍼링크 이동 방지
			if(confirm("게시물을 삭제하시겠습니까?")) {
				location.href="/board/remove.kh?page="+value;
			}
		}
		function removeReply(replyNo) {
			event.preventDefault();
			if(confirm("정말 삭제하시겠습니까?")) {
				var $delForm = $("<form>");
				$delForm.attr("action", "/board/removeReply.kh");
				$delForm.attr("method", "post");
				$delForm.append("<input type='hidden' name='replyNo' value='"+replyNo+"'>");
				$delForm.appendTo("body");
				$delForm.submit();
			}
		}
		function modifyView(obj, replyContents, replyNo) {
			event.preventDefault();
			var $tr = $("<tr>");
			$tr.append("<td colspan='3'><input type='text' size='50' value='"+replyContents+"'></td>");
			$tr.append("<td><button onclick='modifyReply(this, "+replyNo+");'>수정</button></td>");
			//console.log($tr[0]);
			//console.log(obj); // obj는 this를 통해 이벤트가 발생한 태그
			$(obj).parent().parent().after($tr);
		}
		function modifyReply(obj, rNo) {
			var inputTag = $(obj).parent().prev().children();
			console.log(inputTag.val());
			var replyContents = inputTag.val(); //= $("#modifyInput").val();
			//console.log(replyContents);
			//console.log(rNo);
			// <form action="" method=""></form>
			var $form = $("<form>");
			$form.attr("action", "/board/modifyReply.kh");
			$form.attr("method", "post");
			$form.append("<input type='hidden' value='"+replyContents+"' name='replyContents'>");
			$form.append("<input type='hidden' value='"+rNo+"' name='replyNo'>");
			console.log($form[0]);
			$form.appendTo("body");
			$form.submit();
		}
	</script>
</body>
</html>