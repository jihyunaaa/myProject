package zic.honeyComboFactory.biz.boardComboLiked.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;
import zic.honeyComboFactory.common.util.MySQLJDBCUtil;

@Repository("mySQLBoardComboLikedDAO")
public class MySQLBoardComboLikedDAO { // 꿀조합 게시판 좋아요 기능 - MySQL DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 1. 좋아요 추가
	final String INSERTBOARDCOMBOLIKED = "INSERT INTO BOARD_COMBO_LIKED (BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER) "
			+ "SELECT IFNULL(MAX(BOARD_COMBO_LIKED_NUMBER), 0) + 1, ?, ? FROM BOARD_COMBO_LIKED";

	// 2. 좋아요 삭제
	final String DELETEBOARDCOMBOLIKED = "DELETE FROM BOARD_COMBO_LIKED WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";

	// 3. 좋아요 누른 총 개수, 좋아요 누른 게시물 목록(글 번호, 글 제목, 글 작성자) 출력 - 최신순
	final String SELETEALL = "SELECT BOARD_COMBO_LIKED.BOARD_COMBO_LIKED_NUMBER, BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER, "
			+ "BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NAME, BOARD_COMBO_LIKED.MEMBER_NUMBER, "
			+ "COUNT(BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO_LIKED JOIN BOARD_COMBO "
			+ "ON BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER "
			+ "JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE BOARD_COMBO_LIKED.MEMBER_NUMBER = ? ORDER BY BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER DESC LIMIT ?, ?";

	// 4. 글 번호와 회원 번호가 둘 다 있다면 정보를 가져옴
	final String SELECTONE = "SELECT BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER FROM BOARD_COMBO_LIKED "
			+ "WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";

	// R = selectAll()
	public List<BoardComboLikedVO> getAll(BoardComboLikedVO boardComboLikedVO) {
		ArrayList<BoardComboLikedVO> datas = new ArrayList<BoardComboLikedVO>();
		try {
			conn = MySQLJDBCUtil.connect();
			// 좋아요 누른 총 개수, 좋아요 누른 게시물 목록(글 번호, 글 제목, 글 작성자) 출력 - 최신순
			pstmt = conn.prepareStatement(SELETEALL);
			pstmt.setLong(1, boardComboLikedVO.getMemberNumber());
			pstmt.setInt(2, boardComboLikedVO.getBoardComboLikedIndex());
			pstmt.setInt(3, boardComboLikedVO.getBoardComboLikedContentCount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardComboLikedVO data = new BoardComboLikedVO();
				data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
				data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
				data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				datas.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}

	// R = selectOne()
	public BoardComboLikedVO getOne(BoardComboLikedVO boardComboLikedVO) {
		BoardComboLikedVO data = null;
		try {
			conn = MySQLJDBCUtil.connect();
			// 글 번호와 회원 번호가 둘 다 있다면 정보를 가져옴
			pstmt = conn.prepareStatement(SELECTONE);
			pstmt.setLong(1, boardComboLikedVO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedVO.getBoardComboNumber());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				data = new BoardComboLikedVO();
				data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}

	// C
	public boolean insert(BoardComboLikedVO boardComboLikedVO){
		try {
			conn=MySQLJDBCUtil.connect();
			// 좋아요 추가
			pstmt=conn.prepareStatement(INSERTBOARDCOMBOLIKED);
			pstmt.setLong(1, boardComboLikedVO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedVO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
	
	// U
	public boolean update(BoardComboLikedVO boardComboLikedVO){
		return false;
	}

	// D
	public boolean delete(BoardComboLikedVO boardComboLikedVO){
		try {
			conn=MySQLJDBCUtil.connect();
			// 좋아요 삭제
			pstmt=conn.prepareStatement(DELETEBOARDCOMBOLIKED);
			pstmt.setLong(1, boardComboLikedVO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedVO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
}
