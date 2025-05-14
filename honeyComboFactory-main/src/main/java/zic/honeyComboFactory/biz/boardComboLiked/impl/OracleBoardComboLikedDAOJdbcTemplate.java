package zic.honeyComboFactory.biz.boardComboLiked.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;

public class OracleBoardComboLikedDAOJdbcTemplate { // 꿀조합 게시판 좋아요 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 1. 좋아요 추가
	final String INSERTBOARDCOMBOLIKED = "INSERT INTO BOARD_COMBO_LIKED (BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER)"
			+ " VALUES (SEQ_BOARD_COMBO_LIKED.NEXTVAL, ?, ?)";

	// 2. 좋아요 삭제
	final String DELETEBOARDCOMBOLIKED = "DELETE FROM BOARD_COMBO_LIKED WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";

	// 3. 좋아요 누른 총 개수, 좋아요 누른 게시물 목록(글 번호, 글 제목, 글 작성자) 출력 - 최신순
	final String SELECTALL = "SELECT BOARD_COMBO_LIKED_NUMBER, BOARD_COMBO_NUMBER, BOARD_COMBO_TITLE, MEMBER_NAME, MEMBER_NUMBER,"
			+ " TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_LIKED.BOARD_COMBO_LIKED_NUMBER, BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER,"
			+ " BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NAME, BOARD_COMBO_LIKED.MEMBER_NUMBER, COUNT(BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER) OVER()"
			+ " AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER DESC) RN FROM BOARD_COMBO_LIKED JOIN BOARD_COMBO"
			+ " ON BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER"
			+ " WHERE BOARD_COMBO_LIKED.MEMBER_NUMBER = ?) WHERE RN BETWEEN ? AND ?";

	// 4. 글 번호와 회원 번호가 둘 다 있다면 정보를 가져옴
	final String SELECTONE = "SELECT BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER FROM BOARD_COMBO_LIKED"
			+ " WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";

	// getAll → R
	public List<BoardComboLikedVO> getAll(BoardComboLikedVO boardComboLikedVO) {
		System.out.println("좋아요 누른 게시글 출력(최신순");
		Object[] args = { boardComboLikedVO.getMemberNumber(), boardComboLikedVO.getBoardComboLikedIndex(),
				boardComboLikedVO.getBoardComboLikedContentCount() };
		return jdbcTemplate.query(SELECTALL, args, new BoardComboLikedGetAllRowMapper());
	}

	// getOne → R
	public BoardComboLikedVO getOne(BoardComboLikedVO boardComboLikedVO) {
	    System.out.println("글 번호, 회원 번호 모두 있다면 정보 출력");
	    Object[] args = { boardComboLikedVO.getMemberNumber(), boardComboLikedVO.getBoardComboNumber() };
	    List<BoardComboLikedVO> list = jdbcTemplate.query(SELECTONE, args, new BoardComboLikedGetOneRowMapper());
	    
	    return getSingleResult(list);
	}

	// insert → C
	public boolean insert(BoardComboLikedVO boardComboLikedVO) {
		System.out.println("좋아요 추가");
		Object[] args = { boardComboLikedVO.getMemberNumber(), boardComboLikedVO.getBoardComboNumber() };
		int result = jdbcTemplate.update(INSERTBOARDCOMBOLIKED, args);
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
		Object[] args = { boardComboLikedVO.getMemberNumber(), boardComboLikedVO.getBoardComboNumber() };
		int result = jdbcTemplate.update(DELETEBOARDCOMBOLIKED, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 반환 메서드
	private BoardComboLikedVO getSingleResult(List<BoardComboLikedVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}

// getAll에 필요한 데이터 반환
class BoardComboLikedGetAllRowMapper implements RowMapper<BoardComboLikedVO> {

	@Override
	public BoardComboLikedVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComboLikedVO data = new BoardComboLikedVO();
		data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
		data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
		data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 데이터 [" + data + "]");
		return data;
	}
}

// getOne에 필요한 데이터 반환
class BoardComboLikedGetOneRowMapper implements RowMapper<BoardComboLikedVO> {

	@Override
	public BoardComboLikedVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComboLikedVO data = new BoardComboLikedVO();
		data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));

		System.out.println("getOne 데이터 [" + data + "]");
		return data;
	}

}

//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * //3. 좋아요 누른 총 개수, 좋아요 누른 게시물 목록(글 번호, 글 제목, 글 작성자) 출력 - 최신순 final String
 * SELECTALL =
 * "SELECT BOARD_COMBO_LIKED.BOARD_COMBO_LIKED_NUMBER, BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER,"
 * +
 * " BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NAME, BOARD_COMBO_LIKED.MEMBER_NUMBER, COUNT(BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER) OVER()"
 * +
 * " AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO_LIKED JOIN BOARD_COMBO ON BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER"
 * +
 * " JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE BOARD_COMBO_LIKED.MEMBER_NUMBER = ?"
 * +
 * " ORDER BY BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 */