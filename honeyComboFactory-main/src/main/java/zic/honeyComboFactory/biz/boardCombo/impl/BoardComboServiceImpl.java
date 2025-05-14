package zic.honeyComboFactory.biz.boardCombo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;

@Service("boardComboService")
public class BoardComboServiceImpl implements BoardComboService { // 꿀조합 게시판 서비스
	@Autowired // BoardComboDAO 객체가 메모리에 new 되어있어야 가능
	private OracleBoardComboDAOMybatis boardComboDAO; // Oracle DB
	// private MySQLBoardComboDAO boardComboDAO; // MySql DB

	@Override
	public List<BoardComboVO> getAll(BoardComboVO boardComboVO) {
		return this.boardComboDAO.getAll(boardComboVO);
	}
	
	@Override
	public BoardComboVO getOne(BoardComboVO boardComboVO) {
		return this.boardComboDAO.getOne(boardComboVO);
	}
	
	@Override
	public boolean insert(BoardComboVO boardComboVO) {
		return this.boardComboDAO.insert(boardComboVO);
	}

	@Override
	public boolean update(BoardComboVO boardComboVO) {
		return this.boardComboDAO.update(boardComboVO);
	}

	@Override
	public boolean delete(BoardComboVO boardComboVO) {
		return this.boardComboDAO.delete(boardComboVO);
	}
}
