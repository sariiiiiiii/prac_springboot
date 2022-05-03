package com.board.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.board.adapter.GsonLocalDateTimeAdapter;
import com.board.domain.CommentDTO;
import com.board.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	
	
	
//	@PostMapping("/comments")
//	@PatchMapping("/comments/{idx}")
	@RequestMapping(value = { "/comments", "/comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH }) // 설계 규칙 URI 구분
	public JsonObject registerComment(@PathVariable(value = "idx", required = false) Long idx, @RequestBody final CommentDTO params) {
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			boolean isRegistered = commentService.registerComment(params);
			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}
	
	
	
	@GetMapping("/comments/{boardIdx}")
	public JsonObject getCommentList(@PathVariable("boardIdx") Long boardIdx, @ModelAttribute("param") CommentDTO param) {
		
		JsonObject jsonObj = new JsonObject(); // Json객체 생성
		
		List<CommentDTO> commentList = commentService.getCommentList(param); // 댓글목록을 List에 저장
		
		//CollectionUitls.isEmpty는 Null값 체크를 동시에 size까지 체크를 해줌
		if(CollectionUtils.isEmpty(commentList) == false) {

				// Gson 클래스의 메소드를 이용하여 commentList에 담긴 댓글을 JsonArray 타입으로 변환하고
				// Json객체에 "commentList"라는 프로퍼티로 추가하여 리턴
				// ※리턴받은 Json을 보면 insertTime 또한 JSON으로 이루어져 있다 게시판구현에서는 Temporals 클래스의 format 메소드를 사용하여
				// LocalDateTime 타입의 날짜 형식을 지정하고는 했는데 REST 방식의 처리는 자바스크립트로 처리되어야 하고,
				// 자바스크립트에서는 타임리프 클래스의 메소드를 사용할 수 없기 때문에 어댑터 클래스를 생성하고, 날짜형태를 보다 쉽게 다루기 위해
				// Moment js 라이브러리를 추가해주도록 하자
				Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
				JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
				jsonObj.add("commentList", jsonArr);
				
		}
		return jsonObj;
	}
	
	@DeleteMapping("/comments/{idx}")
	public JsonObject deleteComment(@PathVariable("idx") Long idx) {
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			boolean isDeleted = commentService.deleteComment(idx);
			jsonObj.addProperty("result", isDeleted);
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에서 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		
		return jsonObj;
	}
	
}
