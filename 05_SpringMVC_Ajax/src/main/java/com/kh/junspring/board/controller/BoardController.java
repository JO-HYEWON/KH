package com.kh.junspring.board.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.junspring.board.domain.Board;
import com.kh.junspring.board.domain.Reply;
import com.kh.junspring.board.service.BoardService;
import com.kh.junspring.member.domain.Member;

// @Component
@Controller
public class BoardController {
	@Autowired
	private BoardService bService;
	
	/**
	 * 게시글 등록화면
	 * @return mv
	 */
	@RequestMapping(value="/board/writeView.kh", method=RequestMethod.GET)
	public String showBoardWrite() {
		return "board/boardWriteForm";
	}
	// 게시글 등록
	/**
	 * 게시글 등록, 첨부파일 등록
	 * @param mv
	 * @param board
	 * @return mv
	 */
	@RequestMapping(value="/board/register.kh", method=RequestMethod.POST)
	public ModelAndView registBoard(
			ModelAndView mv
			, @ModelAttribute Board board
			, @RequestParam(value="uploadFile", required=false) MultipartFile uploadFile
			,HttpServletRequest request) {
		
		try {
			String boardFilename = uploadFile.getOriginalFilename();
			if(!boardFilename.equals("")) {
				///////////////////////////////////////////////////////////////////////////
				String root = request.getSession().getServletContext().getRealPath("resources");
				String savePath = root + "\\buploadFiles";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String boardFileRename 
				= sdf.format(new Date(System.currentTimeMillis()))+"."
						+boardFilename.substring(boardFilename.lastIndexOf(".")+1);
				// 1.png, img.png
				File file = new File(savePath);
				if(!file.exists()) {
					file.mkdir();
				}
				/////////////////////////////////////////////////////////////////////////////
				uploadFile.transferTo(new File(savePath+"\\"+boardFileRename)); 
				// 파일을 buploadFiles 경로에 저장하는 메소드
				String boardFilepath = savePath+"\\"+boardFileRename;
				board.setBoardFilename(boardFilename);
				board.setBoardFileRename(boardFileRename);
				board.setBoardFilepath(boardFilepath);
			}
			int result = bService.registerBoard(board);
			mv.setViewName("redirect:/board/list.kh");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	/**
	 * 게시글 수정 화면
	 * @param mv
	 * @param boardNo
	 * @return
	 */
	@RequestMapping(value="/board/modifyView.kh", method=RequestMethod.GET)
	public ModelAndView boardModifyView(
			ModelAndView mv
			,@RequestParam("boardNo") Integer boardNo
			,@RequestParam("page") int page) {
		try {
			Board board = bService.printOneByNo(boardNo);
			mv.addObject("board", board);
			mv.addObject("page", page);
			mv.setViewName("board/modifyForm");
		} catch (Exception e) {
			mv.addObject("msg", e.toString());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	/**
	 * 게시글 수정
	 * @param board
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/board/modify.kh", method=RequestMethod.POST)
	public ModelAndView boardModify(
			@ModelAttribute Board board
			, ModelAndView mv
			,@RequestParam(value="reloadFile", required=false) MultipartFile reloadFile
			,@RequestParam("page") Integer page
			,HttpServletRequest request) {
		try {
			String boardFilename = reloadFile.getOriginalFilename();
			if(reloadFile != null && !boardFilename.equals("")) {
				// 수정, 1. 대체(replace) / 2. 삭제 후 저장
				// 파일삭제
				String root = request.getSession().getServletContext().getRealPath("resources");
				String savedPath = root + "\\buploadFiles";
				//Board bOne = bService.printOneByNo(board.getBoardNo());
				File file = new File(savedPath + "\\" + board.getBoardFileRename());
				if(file.exists()) {
					file.delete();
				}
				// 파일 다시 저장
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String boardFileRename = sdf.format(new Date(System.currentTimeMillis()))
						+ "." + boardFilename.substring(boardFilename.lastIndexOf(".")+1);
				String boardFilepath = savedPath + "\\" + boardFileRename;
				reloadFile.transferTo(new File(boardFilepath));
				board.setBoardFilename(boardFilename);
				board.setBoardFileRename(boardFileRename);
				board.setBoardFilepath(boardFilepath);
			}
			int result = bService.modifyBoard(board);
			mv.setViewName("redirect:/board/list.kh?page="+page);
		} catch (Exception e) {
			mv.addObject("msg", e.toString()).setViewName("common/errorPage");
		}
		return mv;
	}
	/**
	 * 게시글 삭제 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/board/remove.kh", method=RequestMethod.GET)
	public String boardRemove(
			HttpSession session
			, Model model
			, @RequestParam("page") Integer page) {
		try {
			int boardNo = (int)session.getAttribute("boardNo");
			int result = bService.removeOneByNo(boardNo);
			if(result > 0) {
				session.removeAttribute("boardNo");
			}
			return "redirect:/board/list.kh?page="+page;
		} catch (Exception e) {
			model.addAttribute("msg", e.toString());
			return "common/errorPage";
		}
	}
	/**
	 * 게시글 전체 조회
	 * @param mv
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/board/list.kh", method=RequestMethod.GET)
	public ModelAndView boardListView(
			ModelAndView mv
			,@RequestParam(value="page", required=false) Integer page) {
		/////////////////////////////////////////////////////////////////////////
		int currentPage = (page != null) ? page : 1;
		int totalCount = bService.getTotalCount("","");
		int boardLimit = 10;
		int naviLimit = 5;
		int maxPage;
		int startNavi;
		int endNavi;
		// 23/5 = 4.8 + 0.9 = 5(.7)
		maxPage = (int)((double)totalCount/boardLimit + 0.9);
		startNavi = ((int)((double)currentPage/naviLimit+0.9)-1)*naviLimit+1;
		endNavi = startNavi + naviLimit - 1;
		if(maxPage < endNavi) {
			endNavi = maxPage;
		}
		//////////////////////////////////////////////////////////////////////////
		// /board/list.kh?page=${currentPage }
		List<Board> bList = bService.printAllBoard(currentPage, boardLimit);
		if(!bList.isEmpty()) {
			mv.addObject("urlVal", "list");
			mv.addObject("maxPage", maxPage);
			mv.addObject("currentPage", currentPage);
			mv.addObject("startNavi", startNavi);
			mv.addObject("endNavi", endNavi);
			mv.addObject("bList", bList);
		}
		mv.setViewName("board/listView");
		return mv;
	}
	/**
	 * 게시글 상세 조회
	 * @param mv
	 * @param boardNo
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/board/detail.kh", method=RequestMethod.GET)
	public ModelAndView boardDetailView(
			ModelAndView mv
			, @RequestParam("boardNo") Integer boardNo
			, @RequestParam("page") Integer page
			, HttpSession session) {
		try {
			Board board = bService.printOneByNo(boardNo);
			List<Reply> rList = bService.printAllReply(boardNo);
			session.setAttribute("boardNo", board.getBoardNo());
			// 세션에 boardNo 저장 -> 삭제하기 위해서
			mv.addObject("rList", rList);
			mv.addObject("board", board);
			mv.addObject("page", page);
			mv.setViewName("board/detailView");
		} catch (Exception e) {
			mv.addObject("msg", e.toString());
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	/**
	 * 게시글 조건 검색 
	 * @param mv
	 * @param searchCondition
	 * @param searchValue
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/board/search.kh", method=RequestMethod.GET)
	public ModelAndView boardSearchList(
			ModelAndView mv
			, @RequestParam("searchCondition") String searchCondition
			, @RequestParam("searchValue") String searchValue
			, @RequestParam(value="page", required=false) Integer page) {
		try {
			int currentPage = (page != null) ? page : 1;
			int totalCount = bService.getTotalCount(searchCondition, searchValue);
			int boardLimit = 10;
			int naviLimit = 5;
			int maxPage;
			int startNavi;
			int endNavi;
			maxPage = (int)((double)totalCount/boardLimit + 0.9);
			startNavi = ((int)((double)currentPage/naviLimit+0.9)-1)*naviLimit+1;
			endNavi = startNavi + naviLimit - 1;
			if(maxPage < endNavi) {
				endNavi = maxPage;
			}
			List<Board> bList = bService.printAllByValue(
					searchCondition, searchValue, currentPage, boardLimit);
			if(!bList.isEmpty()) {
				mv.addObject("bList", bList);
			}else {
				mv.addObject("bList", null);
			}
			mv.addObject("urlVal", "search");
			mv.addObject("searchCondition", searchCondition);
			mv.addObject("searchValue", searchValue);
			mv.addObject("maxPage", maxPage);
			mv.addObject("currentPage", currentPage);
			mv.addObject("startNavi", startNavi);
			mv.addObject("endNavi", endNavi);
			mv.setViewName("board/listView");
		} catch (Exception e) {
			mv.addObject("msg", e.toString()).setViewName("common/errorPage");
		}
		return mv;
	}
	
	// 댓글 관리
	/**
	 * 댓글 등록
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/board/addReply.kh", method=RequestMethod.POST)
	public ModelAndView addBoardReply(
			ModelAndView mv
			, @ModelAttribute Reply reply
			, @RequestParam("page") int page
			, HttpSession session) {
		Member member = (Member)session.getAttribute("loginUser");
		String replyWriter = member.getMemberId();
		int boardNo = reply.getRefBoardNo();
		reply.setReplyWriter(replyWriter);
		int result = bService.registerReply(reply);
		if(result > 0) {
			mv.setViewName(
			"redirect:/board/detail.kh?boardNo="+boardNo+"&page="+page);
		}
		return mv;
	}
	@RequestMapping(value="/board/modifyReply.kh", method=RequestMethod.POST)
	public String modifyBoardReply(
			//@RequestParam("replyNo") Integer replyNo
			//, @RequestParam("replyContents") String replyContents
			@ModelAttribute Reply reply) {
		int result = bService.modifyReply(reply);
		//return "redirect:/board/detail.kh?boardNo=";
		return "redirect:/board/list.kh";
	}
	
	@RequestMapping(value="/board/removeReply.kh", method=RequestMethod.POST)
	public String removeBoardReply(
			@RequestParam("replyNo") Integer replyNo) {
		int result = bService.deleteReply(replyNo);
		return "redirect:/board/list.kh";
	}
	
	// 댓글관리 - Ajax 버전!!
	@ResponseBody
	@RequestMapping(value="/board/replyAdd.kh", method=RequestMethod.POST)
	public String boardReplyAdd(
			@ModelAttribute Reply reply) {
		reply.setReplyWriter("admin");
		int result = bService.registerReply(reply);
		if(result > 0) {
			return "success";
		}else {
			return "fail";
		}
	}
	@ResponseBody
	@RequestMapping(value="/board/replyList.kh", produces="application/json;charset=utf-8", method=RequestMethod.GET)
	public String boardReplyList(
			@RequestParam("boardNo") int boardNo) {
		int bNo = (boardNo == 0) ? 1 : boardNo;
		List<Reply> rList = bService.printAllReply(bNo);
		if(!rList.isEmpty()) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			return gson.toJson(rList);
		}
		return null;
	}
}












