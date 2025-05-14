package zic.honeyComboFactory.biz.boardCombo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

// @Repository("oracleBoardComboDAO")
public class OracleBoardComboDAO { // 꿀조합 게시판 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	/*
	 * [1] 꿀조합 게시판 글 작성 (C) PK값인 BOARD_COMBO_NUMBER는 가장 큰 번호 + 1 해서 매 데이터 생성마다 PK
	 * 하나씩 증가
	 */
	final String INSERTCOMBOBOARD = "INSERT INTO BOARD_COMBO (BOARD_COMBO_NUMBER, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, MEMBER_NUMBER)"
			+ " VALUES (SEQ_BOARD_COMBO.NEXTVAL, ?, ?, ?)";


	// [2] 꿀조합 게시판 글 목록 조회(최신순) (R - ALL)
	final String SELECTALLCOMBOBOARDDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " ORDER BY BOARD_COMBO_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	// [3] 꿀조합 게시판 글 목록 조회 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " ORDER BY BOARD_COMBO_NUMBER ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	// [4] 꿀조합 게시판 글 목록 조회 R ALL (인기순)
	final String SELECTALLCOMBOBOARDPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [5] 꿀조합글 게시판 검색 R ALL (최신순)
	final String SELECTALLCOMBOBOARDSEARCHDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [6] 꿀조합글 게시판 검색 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDSEARCHASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_REGISTER_DATE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [7] 꿀조합글 게시판 검색 R ALL (인기순)
	final String SELECTALLCOMBOBOARDSEARCHPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE BOARD_COMBO_TITLE LIKE '%' || ? || '%' ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [8] 한 사용자가 작성한 꿀조합 게시판 글 개수 포함 출력 R ALL
	final String SELECTALLMEMBERWRITE = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE MEMBER_NUMBER = ? ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [9] 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 X, MEMBER_IS_ADMIN = 1인 글만 출력
	final String SELECTALLADMINCONTENT = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE MEMBER_IS_ADMIN = 1 ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	
	// [10] 회원 글 목록 출력 (최신순)
	final String SELECTALLMEMBERCONTENTDESC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [11] 회원 글 목록 출력 (오래된순)
	final String SELECTALLMEMBERCONTENTASC = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER FROM VIEW_BOARD_COMBO"
			+ " WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_REGISTER_DATE ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [12] 회원 글 목록 출력 (인기순)
	final String SELECTALLMEMBERCONTENTPOPULAR = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE,"
			+ " BOARD_COMBO_CONTENT, BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT, TOTAL_COUNT_NUMBER"
			+ " FROM VIEW_BOARD_COMBO WHERE MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_LIKED_COUNT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	// [13] 꿀조합 게시판 상세글 출력 R ONE
	final String SELECTONECOMBOBOARD = "SELECT BOARD_COMBO_NUMBER, MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT,"
			+ " BOARD_COMBO_REGISTER_DATE, BOARD_COMBO_VIEW_COUNT, BOARD_COMBO_LIKED_COUNT FROM VIEW_BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?";

	// [14] 꿀조합 게시판 글 수정 U
	final String UPDATECOMBOBOARD = "UPDATE BOARD_COMBO SET BOARD_COMBO_TITLE = ?, BOARD_COMBO_CONTENT = ? WHERE BOARD_COMBO_NUMBER = ?";

	// [15] 꿀조합 게시판 글 삭제 D
	final String DELETECOMBOBOARD = "DELETE FROM BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?";

	// R = selectAll()
	public List<BoardComboVO> getAll(BoardComboVO boardComboVO) {
		List<BoardComboVO> datas = new ArrayList<BoardComboVO>();
		try {
			conn = OracleJDBCUtil.connect();
			// 꿀조합 게시판 글 목록 조회(최신순)
			if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDDESC")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDDESC);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 꿀조합 게시판 글 목록 조회
			else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDASC")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDASC);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 꿀조합 게시판 글 목록 조회
			else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDPOPULAR);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 꿀조합글 게시판 검색
			else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHDESC")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHDESC);
				pstmt.setString(1, boardComboVO.getSearchKeyword());
				pstmt.setInt(2, boardComboVO.getBoardComboIndex());
				pstmt.setInt(3, boardComboVO.getBoardComboContentCount());
			}
			// 꿀조합글 게시판 검색
			else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHASC")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHASC);
				pstmt.setString(1, boardComboVO.getSearchKeyword());
				pstmt.setInt(2, boardComboVO.getBoardComboIndex());
				pstmt.setInt(3, boardComboVO.getBoardComboContentCount());
			}
			// 꿀조합글 게시판 검색
			else if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHPOPULAR);
				pstmt.setString(1, boardComboVO.getSearchKeyword());
				pstmt.setInt(2, boardComboVO.getBoardComboIndex());
				pstmt.setInt(3, boardComboVO.getBoardComboContentCount());
			}
			// 한 사용자가 작성한 꿀조합 게시판 글 개수 포함 출력
			else if (boardComboVO.getCondition().equals("SELECTALLMEMBERWRITE")) {
				pstmt = conn.prepareStatement(SELECTALLMEMBERWRITE);
				pstmt.setLong(1, boardComboVO.getMemberNumber());
				pstmt.setInt(2, boardComboVO.getBoardComboIndex());
				pstmt.setInt(3, boardComboVO.getBoardComboContentCount());
			}
			// 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 X, MEMBER_IS_ADMIN = 1인 글만 출력
			else if (boardComboVO.getCondition().equals("SELECTALLADMINCONTENT")) {
				pstmt = conn.prepareStatement(SELECTALLADMINCONTENT);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 회원 글 목록 출력 (최신순)
			else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTDESC")) {
				pstmt = conn.prepareStatement(SELECTALLMEMBERCONTENTDESC);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 회원 글 목록 출력 (오래된순)
			else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTASC")) {
				pstmt = conn.prepareStatement(SELECTALLMEMBERCONTENTASC);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			// 회원 글 목록 출력 (인기순)
			else if (boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTPOPULAR")) {
				pstmt = conn.prepareStatement(SELECTALLMEMBERCONTENTPOPULAR);
				pstmt.setInt(1, boardComboVO.getBoardComboIndex());
				pstmt.setInt(2, boardComboVO.getBoardComboContentCount());
			}
			else {
				System.out.println("[ERROR] 알 수 없는 condition입니다: " + boardComboVO.getCondition());
				return datas; // 빈 리스트 반환
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardComboVO data = new BoardComboVO();
				if (boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDDESC")
						|| boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDASC")
						|| boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDPOPULAR")
						|| boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHDESC")
						|| boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHASC")
						|| boardComboVO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHPOPULAR")
						|| boardComboVO.getCondition().equals("SELECTALLMEMBERWRITE")
						|| boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTDESC")
						|| boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTASC")
						|| boardComboVO.getCondition().equals("SELECTALLMEMBERCONTENTPOPULAR")) {

					data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
					data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
					data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				}
				// 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 X, MEMBER_IS_ADMIN = 1인 글만 출력
				else if (boardComboVO.getCondition().equals("SELECTALLADMINCONTENT")) {
					data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
					data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
					data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
					data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				}
				datas.add(data);
				System.out.println("글번호: " + data.getBoardComboNumber() + ", 좋아요 수: " + data.getBoardComboLikedCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}

	// R = selectOne()
	public BoardComboVO getOne(BoardComboVO boardComboVO) {
		BoardComboVO data = null;
		try {
			conn = OracleJDBCUtil.connect();
			// 꿀조합 게시판 상세글 출력
			pstmt = conn.prepareStatement(SELECTONECOMBOBOARD);
			pstmt.setLong(1, boardComboVO.getBoardComboNumber());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				data = new BoardComboVO();
				data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
				data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
				data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
				data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
				data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}

	// C
	public boolean insert(BoardComboVO boardComboVO) { 
		try {
			conn = OracleJDBCUtil.connect();
			// 꿀조합 게시판 글 작성
			pstmt = conn.prepareStatement(INSERTCOMBOBOARD);  
			pstmt.setString(1, boardComboVO.getBoardComboTitle());  
			pstmt.setString(2, boardComboVO.getBoardComboContent());    
			pstmt.setLong(3, boardComboVO.getMemberNumber()); 
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}

	// U
	public boolean update(BoardComboVO boardComboVO) { 
		try {
			conn=OracleJDBCUtil.connect();
			// 꿀조합 게시판 글 수정
			pstmt=conn.prepareStatement(UPDATECOMBOBOARD);
			pstmt.setString(1, boardComboVO.getBoardComboTitle());
			pstmt.setString(2, boardComboVO.getBoardComboContent());
			pstmt.setLong(3, boardComboVO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}

	// D
	public boolean delete(BoardComboVO boardComboVO) { 
		try {
			conn=OracleJDBCUtil.connect();
			// 꿀조합 게시판 글 삭제
			pstmt=conn.prepareStatement(DELETECOMBOBOARD);
			pstmt.setLong(1, boardComboVO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
}