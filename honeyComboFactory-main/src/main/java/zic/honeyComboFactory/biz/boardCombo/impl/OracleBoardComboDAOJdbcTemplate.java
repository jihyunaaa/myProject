package zic.honeyComboFactory.biz.boardCombo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;

public class OracleBoardComboDAOJdbcTemplate { // 꿀조합 게시판 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;
	/*
	 * [1] 꿀조합 게시판 글 작성 (C) PK값인 BOARD_COMBO_NUMBER는 가장 큰 번호 + 1 해서 매 데이터 생성마다 PK
	 * 하나씩 증가
	 */
	final String INSERTCOMBOBOARD = "INSERT INTO BOARD_COMBO (BOARD_COMBO_NUMBER, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, MEMBER_NUMBER)"
			+ " VALUES (SEQ_BOARD_COMBO.NEXTVAL, ?, ?, ?)";

	// [2] 꿀조합 게시판 글 목록 조회(최신순) (R - ALL)
	final String SELECTALLCOMBOBOARDDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER,"
			+ " MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_NUMBER DESC) RN FROM VIEW_BOARD_COMBO) WHERE RN BETWEEN ? AND ?";

	// [3] 꿀조합 게시판 글 목록 조회 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME,"
			+ " MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER()"
			+ " AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_NUMBER ASC) RN FROM VIEW_BOARD_COMBO) WHERE RN BETWEEN ? AND ?";

	// [4] 꿀조합 게시판 글 목록 조회 R ALL (인기순)
	final String SELECTALLCOMBOBOARDPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE,"
			+ " BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER()"
			+ " OVER (ORDER BY BOARD_COMBO_LIKED_COUNT DESC) RN FROM VIEW_BOARD_COMBO) WHERE RN BETWEEN ? AND ?";

	// [5] 꿀조합글 게시판 검색 R ALL (최신순)
	final String SELECTALLCOMBOBOARDSEARCHDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE,"
			+ " BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER()"
			+ " OVER (ORDER BY BOARD_COMBO_REGISTER_DATE DESC) RN FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' AND MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [6] 꿀조합글 게시판 검색 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDSEARCHASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE,"
			+ " BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_REGISTER_DATE ASC)"
			+ " RN FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' AND MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [7] 꿀조합글 게시판 검색 R ALL (인기순)
	final String SELECTALLCOMBOBOARDSEARCHPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE,"
			+ " BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_LIKED_COUNT DESC)"
			+ " RN FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' AND MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [8] 한 사용자가 작성한 꿀조합 게시판 글 개수 포함 출력 R ALL
	final String SELECTALLMEMBERWRITE = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT,"
			+ " BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE,"
			+ " BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_REGISTER_DATE DESC) RN FROM VIEW_BOARD_COMBO WHERE MEMBER_NUMBER = ?)"
			+ " WHERE RN BETWEEN ? AND ?";

	// [9] 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 X, MEMBER_IS_ADMIN = 1인 글만 출력
	final String SELECTALLADMINCONTENT = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT,"
			+ " TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT,"
			+ " COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_REGISTER_DATE DESC) RN FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 1)";

	// [10] 회원 글 목록 출력 (최신순)
	final String SELECTALLMEMBERCONTENTDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_REGISTER_DATE DESC) RN FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [11] 회원 글 목록 출력 (오래된순)
	final String SELECTALLMEMBERCONTENTASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_REGISTER_DATE ASC) RN FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [12] 회원 글 목록 출력 (인기순)
	final String SELECTALLMEMBERCONTENTPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " TOTAL_COUNT_NUMBER FROM (SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT,"
			+ " COUNT(BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, ROW_NUMBER() OVER (ORDER BY BOARD_COMBO_LIKED_COUNT DESC) RN FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 0) WHERE RN BETWEEN ? AND ?";

	// [13] 꿀조합 게시판 상세글 출력 R ONE
	final String SELECTONECOMBOBOARD = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT"
			+ " FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?";

	// [14] 꿀조합 게시판 글 수정 U
	final String UPDATECOMBOBOARD = "UPDATE BOARD_COMBO SET BOARD_COMBO_TITLE = ?, BOARD_COMBO_CONTENT = ? WHERE BOARD_COMBO_NUMBER = ?";

	// [15] 꿀조합 게시판 글 삭제 D
	final String DELETECOMBOBOARD = "DELETE FROM BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?";

	// getAll → R
	public List<BoardComboVO> getAll(BoardComboVO boardComboVO) {
		// 꿀조합 게시판 글 목록 조회(최신순)
		if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDDESC")) {
			System.out.println("꿀조합 게시판 글 목록 조회(최신순) 로그");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDDESC, args, new BoardComboGetAllRowMapper());
		}
		// 꿀조합 게시판 글 목록 조회(오래된순)
		else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDASC")) {
			System.out.println("꿀조합 게시판 글 목록 조회(오래된순)");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDASC, args, new BoardComboGetAllRowMapper());
		}
		// 꿀조합 게시판 글 목록 조회(인기순)
		else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDPOPULAR")) {
			System.out.println("꿀조합 게시판 글 목록 조회(인기순)");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDPOPULAR, args, new BoardComboGetAllRowMapper());
		}
		// 꿀조합 게시판 검색(최신순)
		else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHDESC")) {
			System.out.println("꿀조합 게시판 검색(최신순)");
			Object[] args = { boardComboVO.getSearchKeyword(), boardComboVO.getBoardComboIndex(),
					boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDSEARCHDESC, args, new BoardComboGetAllRowMapper());
		}
		// 꿀조합 게시판 검색(오래된순)
		else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHASC")) {
			System.out.println("꿀조합 게시판 검색(오래된순)");
			Object[] args = { boardComboVO.getSearchKeyword(), boardComboVO.getBoardComboIndex(),
					boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDSEARCHASC, args, new BoardComboGetAllRowMapper());
		}
		// 꿀조합 게시판 검색(인기순)
		else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHPOPULAR")) {
			System.out.println("꿀조합 게시판 검색(인기순)");
			Object[] args = { boardComboVO.getSearchKeyword(), boardComboVO.getBoardComboIndex(),
					boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLCOMBOBOARDSEARCHPOPULAR, args, new BoardComboGetAllRowMapper());
		}
		// 회원 1명이 작성한 꿀조합 게시판 검색
		else if (boardComboVO.getCondition().equals("SELECTALLMEMBERWRITE")) {
			System.out.println("회원 1명이 작성한 꿀조합 게시판 검색");
			Object[] args = { boardComboVO.getMemberNumber(), boardComboVO.getBoardComboIndex(),
					boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLMEMBERWRITE, args, new BoardComboGetAllRowMapper());
		}
		// 관리자가 작성한 꿀조합 게시판 출력
		else if (boardComboVO.getCondition().equals("SELECTALLADMINCONTENT")) {
			System.out.println("관리자가 작성한 꿀조합 게시판 출력");
			return jdbcTemplate.query(SELECTALLADMINCONTENT, new BoardComboGetAllAdminRowMapper());
		}
		// 회원이 작성한 꿀조합 게시판 출력(최신순)
		else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTDESC")) {
			System.out.println("회원이 작성한 꿀조합 게시판 출력(최신순)");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLMEMBERCONTENTDESC, args, new BoardComboGetAllRowMapper());
		}
		// 회원이 작성한 꿀조합 게시판 출력(오래된순)
		else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTASC")) {
			System.out.println("회원이 작성한 꿀조합 게시판 출력(오래된순)");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLMEMBERCONTENTASC, args, new BoardComboGetAllRowMapper());
		}
		// 회원이 작성한 꿀조합 게시판 출력(인기순)
		else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTPOPULAR")) {
			System.out.println("회원이 작성한 꿀조합 게시판 출력(인기순)");
			Object[] args = { boardComboVO.getBoardComboIndex(), boardComboVO.getBoardComboContentCount() };
			return jdbcTemplate.query(SELECTALLMEMBERCONTENTPOPULAR, args, new BoardComboGetAllRowMapper());
		}
		// 조건에 만족하지 못하면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public BoardComboVO getOne(BoardComboVO boardComboVO) {
		System.out.println("게시글 상세 조회");
		Object[] args = { boardComboVO.getBoardComboNumber() };
		List<BoardComboVO> list = jdbcTemplate.query(SELECTONECOMBOBOARD, args, new BoardComboGetOneRowMapper());
		
		return getSingleResult(list);
	}

	// insert → C
	public boolean insert(BoardComboVO boardComboVO) {
		System.out.println("게시글 등록");
		Object[] args = { boardComboVO.getBoardComboTitle(), boardComboVO.getBoardComboContent(),
				boardComboVO.getMemberNumber() };
		// 게시글 등록
		int result = jdbcTemplate.update(INSERTCOMBOBOARD, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(BoardComboVO boardComboVO) {
		System.out.println("게시글 수정");
		Object[] args = { boardComboVO.getBoardComboTitle(), boardComboVO.getBoardComboContent(),
				boardComboVO.getBoardComboNumber() };
		// 게시글 수정
		int result = jdbcTemplate.update(UPDATECOMBOBOARD, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(BoardComboVO boardComboVO) {
		System.out.println("게시글 삭제");
		Object[] args = { boardComboVO.getBoardComboNumber() };
		// 게시글 삭제
		int result = jdbcTemplate.update(DELETECOMBOBOARD, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 반환 메서드
	private BoardComboVO getSingleResult(List<BoardComboVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}

// getAll에 필요한 데이터 반환(관리자 글 출력 제외)
class BoardComboGetAllRowMapper implements RowMapper<BoardComboVO> {

	@Override
	public BoardComboVO mapRow(ResultSet rs, int rowNum) throws SQLException {

		BoardComboVO data = new BoardComboVO();

		data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
		data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
		data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
		data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
		data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 관리자 제외 글 데이터 [" + data + "]");
		return data;
	}
}

// getAll에 필요한 데이터 반환(관리자 글 출력)
class BoardComboGetAllAdminRowMapper implements RowMapper<BoardComboVO> {

	@Override
	public BoardComboVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComboVO data = new BoardComboVO();

		data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
		data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
		data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
		data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 관리자 글 데이터 [" + data + "]");
		return data;
	}
}

// getOne에 필요한 데이터 반환
class BoardComboGetOneRowMapper implements RowMapper<BoardComboVO> {

	@Override
	public BoardComboVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComboVO data = new BoardComboVO();

		data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
		data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
		data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
		data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
		data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));

		System.out.println("getOne 데이터 [" + data + "]");
		return data;
	}

}

// 23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * 
 * // [2] 꿀조합 게시판 글 목록 조회(최신순) (R - ALL) final String SELECTALLCOMBOBOARDDESC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * + " ORDER BY BOARD_COMBO_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // [3] 꿀조합 게시판 글 목록 조회 R ALL (오래된순) final String SELECTALLCOMBOBOARDASC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * + " ORDER BY BOARD_COMBO_NUMBER ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // [4] 꿀조합 게시판 글 목록 조회 R ALL (인기순) final String SELECTALLCOMBOBOARDPOPULAR =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [5] 꿀조합글 게시판 검색 R ALL (최신순) final String SELECTALLCOMBOBOARDSEARCHDESC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
 * +
 * " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [6] 꿀조합글 게시판 검색 R ALL (오래된순) final String SELECTALLCOMBOBOARDSEARCHASC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_REGISTER_DATE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [7] 꿀조합글 게시판 검색 R ALL (인기순) final String SELECTALLCOMBOBOARDSEARCHPOPULAR
 * =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
 * +
 * " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [8] 한 사용자가 작성한 꿀조합 게시판 글 개수 포함 출력 R ALL final String SELECTALLMEMBERWRITE
 * =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE MEMBER_NUMBER = ? ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [9] 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 X, MEMBER_IS_ADMIN = 1인 글만 출력 final
 * String SELECTALLADMINCONTENT =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE MEMBER_IS_ADMIN = 1 ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [10] 회원 글 목록 출력 (최신순) final String SELECTALLMEMBERCONTENTDESC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
 * +
 * " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [11] 회원 글 목록 출력 (오래된순) final String SELECTALLMEMBERCONTENTASC =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
 * +
 * " WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_REGISTER_DATE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [12] 회원 글 목록 출력 (인기순) final String SELECTALLMEMBERCONTENTPOPULAR =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
 * +
 * " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER"
 * +
 * " FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 * 
 * // [13] 꿀조합 게시판 상세글 출력 R ONE final String SELECTONECOMBOBOARD =
 * "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
 * +
 * " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?"
 * ;
 * 
 */