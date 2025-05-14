package zic.honeyComboFactory.biz.review.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.reviewVO.ReviewVO;

public class OracleReviewDAOJdbcTemplate { // 리뷰 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Connection conn;
	private PreparedStatement pstmt;

	// [1] 리뷰 작성 (C)
	// REVIEW_NUMBER는 가장 큰 번호 + 1해서 매 데이터 생성마다 PK 하나씩 증가
	final String INSERTREVIEW = "INSERT INTO REVIEW (REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, PRODUCT_COMBO_NUMBER)"
			+ " VALUES (SEQ_REVIEW.NEXTVAL, ?, SYSDATE, ?, ?, ?)";

	// [2] 모든 리뷰 목록 조회 (R - ALL)
	// 리뷰 전체 목록 최신순(글 작성일)으로 조회.
	// MEMBER 테이블 JOIN으로 회원 정보 함꼐 불러와서 출력
	final String SELECTALLREVIEWLIST = "SELECT REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, MEMBER_NAME,"
			+ " MEMBER_IS_WITHDRAW, PRODUCT_COMBO_NUMBER, TOTAL_REVIEW_COUNT FROM (SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE,"
			+ " REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT, REVIEW.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW,"
			+ " REVIEW.PRODUCT_COMBO_NUMBER, ROW_NUMBER() OVER (ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC) AS RN, COUNT(REVIEW.REVIEW_NUMBER)"
			+ " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER) WHERE RN BETWEEN ? AND ?";

	// [3] 리뷰 상세 조회 (R - ONE)
	// MEMBER_NAME, MEMBER_COMBO_NUMBER 2가지 조건을 통해 1개의 리뷰 상세 조회
	final String SELECTONEREVIEW = "SELECT REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NAME, MEMBER_NUMBER,"
			+ " PRODUCT_COMBO_NUMBER, MEMBER_IS_WITHDRAW FROM (SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE,"
			+ " REVIEW.REVIEW_CONTENT, MEMBER.MEMBER_NAME, REVIEW.MEMBER_NUMBER, REVIEW.PRODUCT_COMBO_NUMBER, MEMBER.MEMBER_IS_WITHDRAW FROM REVIEW"
			+ " JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER) WHERE MEMBER_NUMBER = ? AND PRODUCT_COMBO_NUMBER = ?";

	// [4] 리뷰 수정 (U)
	// 리뷰 내용, 별점만 수정 가능
	final String UPDATEREVIEW = "UPDATE REVIEW SET REVIEW_SCORE = ?, REVIEW_CONTENT = ? WHERE REVIEW_NUMBER = ?";

	// [5] 리뷰 삭제 (D)
	// 리뷰 번호를 조건으로 삭제
	final String DELETEREVIEW = "DELETE FROM REVIEW WHERE REVIEW_NUMBER = ?";

	// [6] 한 상품에 대한 리뷰 목록 출력 (R - ALL)
	// 상품 번호를 조건으로 조회
	final String SELECTALLREVIEWONEPRODUCT = "SELECT REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, MEMBER_NAME,"
			+ " MEMBER_IS_WITHDRAW, PRODUCT_COMBO_NUMBER, TOTAL_REVIEW_COUNT FROM (SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE,"
			+ " REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT, MEMBER.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW,"
			+ " REVIEW.PRODUCT_COMBO_NUMBER, ROW_NUMBER() OVER (ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC) RN, COUNT(REVIEW.REVIEW_NUMBER)"
			+ " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE REVIEW.PRODUCT_COMBO_NUMBER = ?)"
			+ " WHERE RN BETWEEN ? AND ?";

	// getAll → R
	public List<ReviewVO> getAll(ReviewVO reviewVO) {
		// 전체 리뷰 목록
		if (reviewVO.getCondition().equals("SELECTALLREVIEWLIST")) {
			System.out.println("전체 리뷰 목록");
			Object[] args = { reviewVO.getReviewIndex(), reviewVO.getReviewContentCount() };
			return jdbcTemplate.query(SELECTALLREVIEWLIST, args, new ReviewGetAllRowMapper());
		}
		// 상품에 대한 리뷰 목록 전체 출력
		else if (reviewVO.getCondition().equals("SELECTALLREVIEWONEPRODUCT")) {
			System.out.println("상품에 대한 리뷰 목록 전체 출력");
			Object[] args = { reviewVO.getProductComboNumber(), reviewVO.getReviewIndex(),
					reviewVO.getReviewContentCount() };
			return jdbcTemplate.query(SELECTALLREVIEWONEPRODUCT, args, new ReviewOneProductGetAllRowMapper());
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ReviewVO getOne(ReviewVO reviewVO) {
		// 리뷰 상세 조회
		if (reviewVO.getCondition().equals("SELECTONEREVIEW")) {
			System.out.println("리뷰 상세 조회");
			Object[] args = { reviewVO.getMemberNumber(), reviewVO.getProductComboNumber() };
			
			List<ReviewVO> list = jdbcTemplate.query(SELECTONEREVIEW, args, new ReviewGetOneRowMapper());
			
			return getSingleResult(list);
		}
		// 조건에 만족하지 못하면 null 반환
		else {
			return null;
		}
	}

	// insert → C
	public boolean insert(ReviewVO reviewVO) {
		System.out.println("리뷰 등록");
		Object[] args = { reviewVO.getReviewScore(), reviewVO.getReviewContent(), reviewVO.getMemberNumber(),
				reviewVO.getProductComboNumber() };
		// 리뷰 등록
		int result = jdbcTemplate.update(INSERTREVIEW, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(ReviewVO reviewVO) {
		System.out.println("리뷰 수정");
		Object[] args = { reviewVO.getReviewScore(), reviewVO.getReviewContent(), reviewVO.getReviewNumber() };
		// 리뷰 수정
		int result = jdbcTemplate.update(UPDATEREVIEW, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(ReviewVO reviewVO) {
		System.out.println("리뷰 삭제");
		Object[] args = { reviewVO.getReviewNumber() };
		// 리뷰 삭제
		int result = jdbcTemplate.update(DELETEREVIEW, args);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// 반환 메서드
	private ReviewVO getSingleResult(List<ReviewVO> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}

// 리뷰 목록 전체 출력
class ReviewGetAllRowMapper implements RowMapper<ReviewVO> {

	@Override
	public ReviewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReviewVO data = new ReviewVO();

		data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
		data.setReviewScore(rs.getInt("REVIEW_SCORE"));
		data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
		data.setReviewContent(rs.getString("REVIEW_CONTENT"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
		data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
		data.setTotalReviewCount(rs.getLong("TOTAL_REVIEW_COUNT"));

		System.out.println("getAll 리뷰 목록 데이터 [" + data + "]");
		return data;
	}

}

// 상품에 대한 리뷰 목록 전체 출력
class ReviewOneProductGetAllRowMapper implements RowMapper<ReviewVO> {

	@Override
	public ReviewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReviewVO data = new ReviewVO();

		data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
		data.setReviewScore(rs.getInt("REVIEW_SCORE"));
		data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
		data.setReviewContent(rs.getString("REVIEW_CONTENT"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
		data.setTotalReviewCount(rs.getLong("TOTAL_REVIEW_COUNT"));
		data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));

		System.out.println("getAll 상품에 대한 리뷰 목록 데이터 [" + data + "]");
		return data;
	}
}

// 리뷰 상세 조회
class ReviewGetOneRowMapper implements RowMapper<ReviewVO> {

	@Override
	public ReviewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReviewVO data = new ReviewVO();

		data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
		data.setReviewScore(rs.getInt("REVIEW_SCORE"));
		data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
		data.setReviewContent(rs.getString("REVIEW_CONTENT"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));

		System.out.println("getOne 리뷰 상세 조회 [" + data + "]");
		return data;
	}

}

//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * // [1] 리뷰 작성 (C) // REVIEW_NUMBER는 가장 큰 번호 + 1해서 매 데이터 생성마다 PK 하나씩 증가 final
 * String INSERTREVIEW =
 * "INSERT INTO REVIEW (REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, PRODUCT_COMBO_NUMBER)"
 * + " VALUES (SEQ_REVIEW.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
 * 
 * // [2] 모든 리뷰 목록 조회 (R - ALL) // 리뷰 전체 목록 최신순(글 작성일)으로 조회. // MEMBER 테이블
 * JOIN으로 회원 정보 함꼐 불러와서 출력 final String SELECTALLREVIEWLIST =
 * "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
 * +
 * " REVIEW.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, COUNT(REVIEW.REVIEW_NUMBER)"
 * +
 * " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC"
 * + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
 * 
 * // [3] 리뷰 상세 조회 (R - ONE) // MEMBER_NAME, MEMBER_COMBO_NUMBER 2가지 조건을 통해 1개의
 * 리뷰 상세 조회 final String SELECTONEREVIEW =
 * "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
 * +
 * " MEMBER.MEMBER_NAME, REVIEW.MEMBER_NUMBER, REVIEW.PRODUCT_COMBO_NUMBER, MEMBER.MEMBER_IS_WITHDRAW FROM REVIEW JOIN MEMBER"
 * +
 * " ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE MEMBER.MEMBER_NUMBER = ? AND REVIEW.PRODUCT_COMBO_NUMBER = ?"
 * ;
 * 
 * // [4] 리뷰 수정 (U) // 리뷰 내용, 별점만 수정 가능 final String UPDATEREVIEW =
 * "UPDATE REVIEW SET REVIEW_SCORE = ?, REVIEW_CONTENT = ? WHERE REVIEW_NUMBER = ?"
 * ;
 * 
 * // [5] 리뷰 삭제 (D) // 리뷰 번호를 조건으로 삭제 final String DELETEREVIEW =
 * "DELETE FROM REVIEW WHERE REVIEW_NUMBER = ?";
 * 
 * // [6] 한 상품에 대한 리뷰 목록 출력 (R - ALL) // 상품 번호를 조건으로 조회 final String
 * SELECTALLREVIEWONEPRODUCT =
 * "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
 * +
 * " MEMBER.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, COUNT(REVIEW.REVIEW_NUMBER)"
 * +
 * " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE REVIEW.PRODUCT_COMBO_NUMBER = ?"
 * +
 * " ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 */
