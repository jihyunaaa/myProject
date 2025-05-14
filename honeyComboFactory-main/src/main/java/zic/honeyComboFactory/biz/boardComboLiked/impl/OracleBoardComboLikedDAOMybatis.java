package zic.honeyComboFactory.biz.boardComboLiked.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;

@Repository("oracleBoardComboLikedDAO")
public class OracleBoardComboLikedDAOMybatis { // 꿀조합 게시판 좋아요 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;

	
	// getAll → R
	public List<BoardComboLikedVO> getAll(BoardComboLikedVO boardComboLikedVO) {
		System.out.println("좋아요 누른 게시글 출력(최신순");
		return mybatis.selectList("BoardComboLikedDAO.SELECTALL", boardComboLikedVO);
	}

	// getOne → R
	public BoardComboLikedVO getOne(BoardComboLikedVO boardComboLikedVO) {
	    System.out.println("글 번호, 회원 번호 모두 있다면 정보 출력");
	    return mybatis.selectOne("BoardComboLikedDAO.SELECTONE", boardComboLikedVO);
	}

	// insert → C
	public boolean insert(BoardComboLikedVO boardComboLikedVO) {
		System.out.println("좋아요 추가");
		
		int result = mybatis.insert("BoardComboLikedDAO.INSERTBOARDCOMBOLIKED", boardComboLikedVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(BoardComboLikedVO boardComboLikedVO) {
		return false;
	}

	// delete → D
	public boolean delete(BoardComboLikedVO boardComboLikedVO) {
		System.out.println("좋아요 삭제");
		int result = mybatis.delete("BoardComboLikedDAO.DELETEBOARDCOMBOLIKED", boardComboLikedVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}


}