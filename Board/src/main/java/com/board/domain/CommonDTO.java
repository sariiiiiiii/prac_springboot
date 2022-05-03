package com.board.domain;

import java.time.LocalDateTime;

import com.board.paging.Criteria;
import com.board.paging.PaginationInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDTO extends Criteria{
	
	// Controller는 데이터를 View로 넘겨주는 역할만을 해야된다
	// 그러한 이유에서 서비스에서 페이징 정보를 계산할 수 있도록, Criteria 클래스를 상속받으면서 PaginationInfo 클래스를 멤버변수로 갖는 공통으로 사용할 클래스 정의
	
	// 공통으로 사용할 DTO클래스이기 때문에 모든 데이터베이스에 공통으로 들어갈 컬럼이 있다면 멤버변수로 생성해주자
	
	/* 페이징 정보 */
	private PaginationInfo paginationInfo;
	
	/* 삭제 여부 */
	private String deleteYn;
	
	/* 등록일 */
	private LocalDateTime insertTime;
	
	/* 수정일 */
	private LocalDateTime updateTime;
	
	/* 삭제일 */
	private LocalDateTime deleteTime;
	

}
