package zic.honeyComboFactory.biz.boardComboLikedVO;

import java.util.List;

public interface BoardComboLikedService { // 꿀조합 게시판 좋아요 인터페이스
	List<BoardComboLikedVO> getAll(BoardComboLikedVO boardComboLikedVO); // SelectAll()
	BoardComboLikedVO getOne(BoardComboLikedVO boardComboLikedVO); // SelectOne()
	
	boolean insert(BoardComboLikedVO boardComboLikedVO);
	boolean update(BoardComboLikedVO boardComboLikedVO);
	boolean delete(BoardComboLikedVO boardComboLikedVO);
}
