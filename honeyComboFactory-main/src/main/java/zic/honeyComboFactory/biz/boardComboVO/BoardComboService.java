package zic.honeyComboFactory.biz.boardComboVO;

import java.util.List;

public interface BoardComboService { // 꿀조합 게시판 인터페이스
	List<BoardComboVO> getAll(BoardComboVO boardComboVO); // SelectAll()
	BoardComboVO getOne(BoardComboVO boardComboVO); // SelectOne()
	
	boolean insert(BoardComboVO boardComboVO);
	boolean update(BoardComboVO boardComboVO);
	boolean delete(BoardComboVO boardComboVO);
}
