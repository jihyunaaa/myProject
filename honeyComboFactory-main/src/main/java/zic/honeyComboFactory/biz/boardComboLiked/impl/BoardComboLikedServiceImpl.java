package zic.honeyComboFactory.biz.boardComboLiked.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedService;
import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;

@Service("boardComboLikedService")
public class BoardComboLikedServiceImpl implements BoardComboLikedService { // 꿀조합 게시판 좋아요 서비스
	@Autowired // BoardComboLikedDAO 객체가 메모리에 new 되어있어야 가능
	private OracleBoardComboLikedDAOMybatis boardComboLikedDAO; // Oracle DB
	// private MySQLBoardComboLikedDAO boardComboLikedDAO; // MySql DB

	@Override
	public List<BoardComboLikedVO> getAll(BoardComboLikedVO boardComboLikedVO) {
		return this.boardComboLikedDAO.getAll(boardComboLikedVO);
	}
	
	@Override
	public BoardComboLikedVO getOne(BoardComboLikedVO boardComboLikedVO) {
		return this.boardComboLikedDAO.getOne(boardComboLikedVO);
	}
	
	@Override
	public boolean insert(BoardComboLikedVO boardComboLikedVO) {
		return this.boardComboLikedDAO.insert(boardComboLikedVO);
	}

	@Override
	public boolean update(BoardComboLikedVO boardComboLikedVO) {
		return this.boardComboLikedDAO.update(boardComboLikedVO);
	}

	@Override
	public boolean delete(BoardComboLikedVO boardComboLikedVO) {
		return this.boardComboLikedDAO.delete(boardComboLikedVO);
	}
}
