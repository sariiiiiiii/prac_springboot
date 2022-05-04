package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.board.domain.AttachDTO;
import com.board.domain.BoardDTO;
import com.board.mapper.AttachMapper;
import com.board.mapper.BoardMapper;
import com.board.paging.Criteria;
import com.board.paging.PaginationInfo;
import com.board.util.FileUtils;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private AttachMapper attachMapper;
	
	@Autowired
	private FileUtils fileUtils;

	@Override
	public boolean registerBoard(BoardDTO params) {
		
		int queryResult = 0;

		if (params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else { 
			queryResult = boardMapper.updateBoard(params);
			
			if("Y".equals(params.getDeleteYn())) { // 게시글이 수정되는 시점에서 기존의 있던 글은 deleteYn = "Y"처리 된다
				attachMapper.deleteAttach(params.getIdx());
				
				// fileIdxs에 포함된 idx를 가지는 파일의 삭제 여부를 'N'으로 업데이트
				if(CollectionUtils.isEmpty(params.getFileIdx()) == false) {
					attachMapper.undeleteAttach(params.getFileIdx());
					// a, b, c 파일이 deleteYn = "Y"로 업데이트 되고 삭제가 되지 않은 기존의 파일은 undelete 해준다
				}
			}
		}
		
		return (queryResult > 0);
	}
	
	@Override
	public boolean registerBoard(BoardDTO params, MultipartFile[] files) {
		
		int queryResult = 1;
		
		if(registerBoard(params) == false) {
			return false;
		}
		
		List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx());
		if(CollectionUtils.isEmpty(fileList) == false) {
			queryResult = attachMapper.insertAttach(fileList);
			if(queryResult < 1) {
				queryResult = 0;
			}
		}
		
		return (queryResult > 0);
	}

	@Override
	public BoardDTO getBoardDetail(Long idx) {
		return boardMapper.selectBoardDetail(idx);
	}

	@Override
	public boolean deleteBoard(Long idx) {
		int queryResult = 0;

		BoardDTO board = boardMapper.selectBoardDetail(idx);

		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);
		
		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}

	
	// 첨부파일 리스트 조회
	@Override
	public List<AttachDTO> getAttachFileList(Long boardIdx) {
		
		int fileTotalCount = attachMapper.selectAttachTotalCount(boardIdx);
		if(fileTotalCount < 1) {
			return Collections.emptyList(); // 게시글의 첨부된 파일이 없으면 빈 객체 리턴
		}
		
		return attachMapper.selectAttachList(boardIdx);
	}



}