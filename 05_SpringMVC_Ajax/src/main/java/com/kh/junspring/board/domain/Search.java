package com.kh.junspring.board.domain;

public class Search {
	private String searchCondition;
	private String searchValue;
	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	@Override
	public String toString() {
		return "Search [조건=" + searchCondition + ", 값=" + searchValue + "]";
	}
	
	
}
