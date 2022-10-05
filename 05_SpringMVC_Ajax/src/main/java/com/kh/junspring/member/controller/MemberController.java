package com.kh.junspring.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.junspring.member.domain.Member;
import com.kh.junspring.member.service.MemberService;
import com.kh.junspring.member.service.logic.MemberServiceImpl;

@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
//	private void doGet() {
//		// TODO Auto-generated method stub
//		request.getRequestDispatcher("/WEB-INF/views/member/join.jsp").forward(request, response);
//	}
	
	// 회원가입할때 회원가입 페이지 보여줌
	@RequestMapping(value="/member/joinView.kh", method=RequestMethod.GET)
	public String memberJoinView(Model model) {
		return "member/join";
		// /WEB-INF/views/member/join.jsp
	}
	//  회원정보를 DB에 저장하는 URL
	@RequestMapping(value="/member/register.kh", method=RequestMethod.POST)
	public ModelAndView memberJoin(
			// ModelAttribute를 쓸 때 jsp의 input태그의 name값은 VO의 멤버변수 명과 같아야 함
			// 다른게 있다면 Member객체에서 그 값은 안들어감.
			@ModelAttribute Member member
			, @RequestParam("post") String post
			, @RequestParam("address1") String address1
			, @RequestParam("address2") String address2
//			, Model model
			, ModelAndView mv) {
		// request.setCharacterEncoding("utf-8");
		try {
//			Member member 
//			= new Member(memberId, memberPwd, memberName, memberEmail, memberPhone
//					, post + "," + memberAddress);
			member.setMemberAddr(post + "," + address1 + "," + address2);
			int result = mService.registerMember(member);
			if(result > 0) {
				// response.sendRedirect("/member/list.kh");
				//return "redirect:/home.kh";
				mv.setViewName("redirect:/home.kh");
			}else {
				//model.addAttribute("msg", "회원가입실패");
				//return "common/errorPage";
				mv.addObject("msg", "회원가입을 실패했습니다.");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			//model.addAttribute("msg", e.getMessage());
			//return "common/errorPage";
			mv.addObject("msg", e.toString()).setViewName("common/errorPage");
		}
		return mv;
	}
	// 로그인 기능
	@RequestMapping(value="/member/login.kh", method=RequestMethod.POST)
	public ModelAndView memberLogin(
			@ModelAttribute Member member
//			@RequestParam("member-id") String memberId
//			,@RequestParam("member-pwd") String memberPwd
//			, Model model
			, ModelAndView mv
			, HttpServletRequest request) {
		try {
			//Member member = new Member(memberId, memberPwd);
			Member loginUser = mService.loginMember(member);
			if(loginUser != null) {
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", loginUser);
				// 세션에 저장정보는 민감정보 제외
//				return "redirect:/home.kh";
				mv.setViewName("redirect:/home.kh");
			}else {
//				model.addAttribute("msg", "회원정보없음");
//				return "common/errorPage";
				mv.addObject("msg", "회원정보를 찾을 수 없습니다.");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
//			model.addAttribute("msg", e.getMessage());
//			return "common/errorPage";
			// request.setAttribute("msg", "실패!");
			// request.getRequestDispatcher("/WEB-INF/views/common/errorPage.jsp")
			//.forward(request, response);
			mv.addObject("msg", e.toString());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	// 로그아웃 기능
	@RequestMapping(value="/member/logout.kh", method=RequestMethod.GET)
	public ModelAndView memberLogout(
			HttpServletRequest request
//			, Model model
			, ModelAndView mv) {
		HttpSession session = request.getSession();
		if(session != null) {
			session.invalidate();
//			return "redirect:/home.kh";
			mv.setViewName("redirect:/home.kh");
		}else {
//			model.addAttribute("msg", "로그아웃 실패");
//			return "common/errorPage";
			mv.addObject("msg", "로그아웃 실패");
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	// 회원 상세정보 조회
	@RequestMapping(value="/member/myPage.kh", method=RequestMethod.GET)
	public ModelAndView showMyPage(HttpServletRequest request
//			, Model model
			, ModelAndView mv) {
		try {
			/////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			Member member = (Member)session.getAttribute("loginUser");
			String memberId = member.getMemberId();
			Member mOne = mService.printOneById(memberId);
			//////////////////////////////////////////////////////
			String mAddr = mOne.getMemberAddr(); // 18230,서울시 중구
			String [] addrInfos = mAddr.split(",");
//			model.addAttribute("member", mOne);
//			model.addAttribute("addrInfos", addrInfos);
//			return "member/myPage";
			mv.addObject("member", mOne).addObject("addrInfos", addrInfos);
			mv.setViewName("member/myPage");
		} catch (Exception e) {
//			model.addAttribute("msg", e.getMessage());
//			return "common/errorPage";
			mv.addObject("msg", e.getMessage()).setViewName("common/errorPage");
		}
		return mv;
	}
	// 회원 정보 수정
	@RequestMapping(value="/member/modify.kh", method=RequestMethod.POST)
	public ModelAndView modifyMember(
			@ModelAttribute Member member
			, @RequestParam("post") String post
			, @RequestParam("address") String memberAddr
//			, Model model
			, ModelAndView mv) {
		try {
			member.setMemberAddr(post + "," + memberAddr);
			int result = mService.modifyMember(member);
			if(result > 0) {
				// redirect
				// return "redirect:/home.kh";
//				return "redirect:/home.kh";
				mv.setViewName("redirect:/home.kh");
			}else {
//				model.addAttribute("msg", "회원 정보가 수정되지 않았습니다.");
//				return "common/errorPage";
				mv.addObject("msg", "회원 정보 수정 실패!");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
//			model.addAttribute("msg", e.getMessage());
//			return "common/errorPage";
			mv.addObject("msg", e.getMessage()).setViewName("common/errorPage");
		}
		return mv;
	}
	// 회원정보삭제 -> 회원탈퇴 기능
	@RequestMapping(value="/member/remove.kh", method=RequestMethod.GET)
	public ModelAndView removeMember(HttpSession session
//			, Model model
			, ModelAndView mv) {
		try {
			Member member = (Member)session.getAttribute("loginUser");
			String memberId = member.getMemberId();
			int result = mService.removeMember(memberId);
//			return "redirect:/member/logout.kh";
			mv.setViewName("redirect:/member/logout.kh");
		} catch (Exception e) {
//			model.addAttribute("msg", e.toString());
//			return "common/errorPage";
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
//	private void doPost(HttpServletRequest request, HttpServletResponse response) {
//		// TODO Auto-generated method stub
//		String memberId = request.getParameter("memberId");
//		Member member = new Member();
//		int result = new MemberServiceImple().registerMember(member);
//	}
}
