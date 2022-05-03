package com.board.paging;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Criteria {
	
	/* 현재 페이지 번호 */
	private int currentPageNo;
	
	/* 페이지당 출력할 데이터 개수 */
	private int recordsPerPage;
	
	/* 화면 하단에 출력할 페이지 사이즈 10으로 지정하면 1 ~ 10까지의 페이지사이즈가 보여지게 됨 */
	private int pageSize;
	
	/* 검색 키워드 동적 SQL을 처리하면서 사용 */
	private String searchKeyword;
	
	/* 검색 유형 searchKeyword와 함게 사용 LIKE검색(와일ㄷ카드)을 사용할 수 있다 */
	private String searchType;

	/* 생성자 */
	public Criteria() {
		this.currentPageNo = 1; /* 기본값으로 현재 페이지 번호를 1 */
		this.recordsPerPage = 10; /* 페이지당 출력할 데이터의 갯수 */
		this.pageSize = 10; /* 화면 하단에 출력할 페이지 사이즈 */
	}
	
	
	// Criteria 클래스의 멤버변수들을 queryString 형태로 바꿔주는 반환
	// Spring에서 제공하는 UriComponents 클래스를 사용하면 URI를 더 효율적으로 처리할 수 있다
	public String makeQueryString(int pageNo) {

		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.queryParam("currentPageNo", pageNo)
				.queryParam("recordsPerPage", recordsPerPage)
				.queryParam("pageSize", pageSize)
				.queryParam("searchType", searchType)
				.queryParam("searchKeyword", searchKeyword)
				.build()
				.encode();

		return uriComponents.toUriString();
		// 해당 메소드의 리턴값 : ?currentPage=1&recordsPerPage=10&pageSize=10&searchType&searchKeyword=
	}
	
	
	
	
	/* MySQL에서 LIMIT구문 앞부분에 사용되는 메소드 */
//	public int getStartPage() {
//		return (currentPageNo - 1) * recordsPerPage;
//	}
	
}
