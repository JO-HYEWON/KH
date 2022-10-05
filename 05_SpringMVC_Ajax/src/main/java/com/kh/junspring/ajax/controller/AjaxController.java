package com.kh.junspring.ajax.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kh.junspring.member.domain.Member;

@Controller
public class AjaxController {
	
	@RequestMapping(value="/ajax/exercise.kh", method=RequestMethod.GET)
	public String showAjaxExercise() {
		return "ajax/ajaxExercise";
	}
	@ResponseBody
	@RequestMapping(value="/ajax/ex1.kh", method=RequestMethod.GET)
	public void exerciseAjax1(@RequestParam("msg") String msg) {
		System.out.println("전송 받은 데이터 : " + msg);
	}
	@ResponseBody
	@RequestMapping(value="/ajax/ex2.kh", produces="text/plain;charset=utf-8", method=RequestMethod.GET)
	public String  exerciseAjax2() {
		//response.setCharacterEncoding("utf-8");
		return "서버에서 왔습니다.";
	}
	@ResponseBody
	@RequestMapping(value="/ajax/ex3.kh", method=RequestMethod.GET)
	public String exerciseAjax3(
			  @RequestParam("num1") Integer num1
			, @RequestParam("num2") Integer num2) {
		Integer result = num1 + num2;
		return String.valueOf(result);
	}
	@ResponseBody
	@RequestMapping(value="/ajax/ex4.kh", produces="application/json;charset=utf-8", method=RequestMethod.GET)
	public String exerciseAjax4(@RequestParam("memberId") String memberId) {
		ArrayList<Member> mList = new ArrayList<Member>();
		mList.add(new Member("khuser01", "pass01"));
		mList.add(new Member("khuser02", "pass02"));
		mList.add(new Member("khuser03", "pass03"));
		mList.add(new Member("khuser04", "pass04"));
		mList.add(new Member("khuser05", "pass05"));
		Member member = null;
		for(Member mOne : mList) {
			if(mOne.getMemberId().equals(memberId)) {
				//member = mList.get(index);
				member = mOne;
			}
		}
		JSONObject jsonObj = new JSONObject(); // json 객체 생성 -> { } 생성 완료
		// { "userNo" : 1, "userName" : "일용자", "userAddr" : "서울시 중구" }
		jsonObj.put("memberId", member.getMemberId());
		jsonObj.put("memberPwd", member.getMemberPwd());
//		jsonObj.put("userNo", 1);
//		jsonObj.put("userName", "일용자");
//		jsonObj.put("userAddr", "서울시 중구");
		return jsonObj.toString();
	}
	@ResponseBody
	@RequestMapping(
			value="/ajax/ex5.kh"
			,produces="application/json;charset=utf-8"
			, method=RequestMethod.GET)
	public String exerciseAjax5(@RequestParam("memberId") String memberId) {
		ArrayList<Member> mList = new ArrayList<Member>();
		mList.add(new Member("khuser01", "pass01"));
		mList.add(new Member("khuser02", "pass02"));
		mList.add(new Member("khuser03", "pass03"));
		mList.add(new Member("khuser04", "pass04"));
		mList.add(new Member("khuser05", "pass05"));
		boolean checkOne = false;
		JSONArray jsonArr = new JSONArray(); // ======>  [   ]  배열 생성
		for(Member mOne : mList) {
			JSONObject jsonObj = new JSONObject(); // ====>  {   } 객체 생성
			if(mOne.getMemberId().equals(memberId)) {
				jsonObj.put("memberId", mOne.getMemberId());
				jsonObj.put("memberPwd", mOne.getMemberPwd());    
				// =====>  { "memberId" : "khuser01", "memberPwd" : "pass01" }, ...
				jsonArr.add(jsonObj);
				// =======> [ { "memberId" : "khuser01", "memberPwd" : "pass01" }, ... ]
				checkOne = true;
				break;
			}
		}
		if(!checkOne) {
			for(Member mOne : mList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("memberId", mOne.getMemberId());
				jsonObj.put("memberPwd", mOne.getMemberPwd());
				jsonArr.add(jsonObj);
			}
		}
		
		return jsonArr.toString();
		//return jsonArr.toJSONString();
		//return mList.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/ajax/ex6.kh", produces="application/json;charset=utf-8", method=RequestMethod.GET)
	public String exerciseAjax6() {
		ArrayList<Member> mList = new ArrayList<Member>();
		mList.add(new Member("khuser01", "pass01"));
		mList.add(new Member("khuser02", "pass02"));
		mList.add(new Member("khuser03", "pass03"));
		mList.add(new Member("khuser04", "pass04"));
		mList.add(new Member("khuser05", "pass05"));
		
		Gson gson = new Gson();
		return gson.toJson(mList);
//		JSONArray jsonArr = new JSONArray();
//		for(Member mOne : mList) {
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("memberId", mOne.getMemberId());
//			jsonObj.put("memberPwd", mOne.getMemberPwd());
//			jsonArr.add(jsonObj);
//		}
//		return jsonArr.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
