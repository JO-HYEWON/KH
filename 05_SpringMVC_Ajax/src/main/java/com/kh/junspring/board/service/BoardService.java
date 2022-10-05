package com.kh.junspring.board.service;

import java.util.List;

import com.kh.junspring.board.domain.Board;
import com.kh.junspring.board.domain.Reply;

public interface BoardService {
	// registerBoard
	// BOARD_TBL <- INSERT INTO BOARD_TBL VALUES(~~~~
	public int registerBoard(Board board);
	// modifyBoard
	// BOARD_TBL <- UPDATE BOARD_TBL SET BOARD_TITLE = '33', BOARD_CONTENTS = '33' WHERE BOARD_NO = 3
	public int modifyBoard(Board board);
	// removeOneByNo
	// BOARD_TBL <- DELETE FROM BOARD_TBL WHERE BOARD_NO = 22
	public int removeOneByNo(int boardNo);
	// printAllBoard
	// BOARD_TBL <- SELECT * FROM BOARD_TBL WHERE B_STATUS = 'Y'
	public List<Board> printAllBoard(int currentPage, int limit);
	// getTotalCount 
	// BOARD_TBL <- SELECT COUNT(*) FROM BOARD_TBL WHERE B_STATUS = 'Y'
	public int getTotalCount(String searchCondition, String searchValue);
	// printOneByNo
	// BOARD_TBL <- SELECT * FROM BOARD_TBL WHERE BOARD_NO = 11
	public Board printOneByNo(Integer boardNo);
	public List<Board> printAllByValue(
			String searchCondition
			, String searchValue
			, int currentPage
			, int boardLimit);
	// 댓글관리
	// REPLY_TBL <- INSERT INTO REPLY_TBL VALUES(#{replyNo}, #{refBoardNo}, #{replyContents}, #{replyWriter}, 
	// #{rCreateDate}, #{rUpdateDate}, #{r}Status)
	public int registerReply(Reply reply);
	// 댓글 수정
	public int modifyReply(Reply reply);
	// 댓글 삭제
	public int deleteReply(Integer replyNo);
	// 댓글 목록 조회
	// REPLY_TBL <- SELECT * FROM REPLY_TBL WHERE R_STATUS = 'Y' 
	// AND REF_BOARD_NO = 233
	public List<Reply> printAllReply(int refBoardNo);
}
