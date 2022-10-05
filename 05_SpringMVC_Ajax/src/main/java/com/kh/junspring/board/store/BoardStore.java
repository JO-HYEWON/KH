package com.kh.junspring.board.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.junspring.board.domain.Board;
import com.kh.junspring.board.domain.Reply;

public interface BoardStore {
	// insertBoard
	public int insertBoard(SqlSession session, Board board);
	public int updateBoard(SqlSession session,Board board);
	public int deleteOneByNo(SqlSession session, int boardNo);
	public int updateBoardCount(SqlSession session, int boardNo);
	// selectAllBoard
	public List<Board> selectAllBoard(SqlSession session, int currentPage, int limit);
	public int selectTotalCount(SqlSession session, String searchCondition, String searchValue);
	public Board selectOneByNo(SqlSession session, Integer boardNo);
	public List<Board> selectAllByValue(SqlSession session, String searchCondition, String searchValue,int currentPage, int boardLimit);
	// 댓글관리
	// 댓글 등록
	public int insertReply(SqlSession session, Reply reply);
	// 댓글 수정
	public int modifyReply(SqlSession session, Reply reply);
	// 댓글 삭제
	public int deleteReply(SqlSession session, Integer replyNo);
	// 댓글 전체조회
	public List<Reply> selectAllReply(SqlSession session,int refBoardNo);
}
