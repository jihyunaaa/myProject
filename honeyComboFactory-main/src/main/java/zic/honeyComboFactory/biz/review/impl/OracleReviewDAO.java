package zic.honeyComboFactory.biz.review.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import zic.honeyComboFactory.biz.reviewVO.ReviewVO;
import zic.honeyComboFactory.common.util.OracleJDBCUtil;

public class OracleReviewDAO { // 리뷰 기능 - Oracle DB
	private Connection conn;
	private PreparedStatement pstmt;

	// [1] 리뷰 작성 (C)
	// REVIEW_NUMBER는 가장 큰 번호 + 1해서 매 데이터 생성마다 PK 하나씩 증가
	final String INSERTREVIEW = "INSERT INTO REVIEW (REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, PRODUCT_COMBO_NUMBER)"
			+ " VALUES (SEQ_REVIEW.NEXTVAL, ?, SYSDATE, ?, ?, ?)";


	// [2] 모든 리뷰 목록 조회 (R - ALL)
	// 리뷰 전체 목록 최신순(글 작성일)으로 조회.
	// MEMBER 테이블 JOIN으로 회원 정보 함꼐 불러와서 출력
	final String SELECTALLREVIEWLIST = "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
			+ " REVIEW.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, COUNT(REVIEW.REVIEW_NUMBER)"
			+ " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC"
			+ " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";



	// [3] 리뷰 상세 조회 (R - ONE)
	// MEMBER_NAME, MEMBER_COMBO_NUMBER 2가지 조건을 통해 1개의 리뷰 상세 조회
	final String SELECTONEREVIEW = "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
			+ " MEMBER.MEMBER_NAME, REVIEW.MEMBER_NUMBER, REVIEW.PRODUCT_COMBO_NUMBER, MEMBER.MEMBER_IS_WITHDRAW FROM REVIEW JOIN MEMBER"
			+ " ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE MEMBER.MEMBER_NUMBER = ? AND REVIEW.PRODUCT_COMBO_NUMBER = ?";



	// [4] 리뷰 수정 (U)
	// 리뷰 내용, 별점만 수정 가능
	final String UPDATEREVIEW = "UPDATE REVIEW SET REVIEW_SCORE = ?, REVIEW_CONTENT = ? WHERE REVIEW_NUMBER = ?";


	// [5] 리뷰 삭제 (D)
	// 리뷰 번호를 조건으로 삭제
	final String DELETEREVIEW = "DELETE FROM REVIEW WHERE REVIEW_NUMBER = ?";


	// [6] 한 상품에 대한 리뷰 목록 출력 (R - ALL)
	// 상품 번호를 조건으로 조회
	final String SELECTALLREVIEWONEPRODUCT = "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT,"
			+ " MEMBER.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, COUNT(REVIEW.REVIEW_NUMBER)"
			+ " OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE REVIEW.PRODUCT_COMBO_NUMBER = ?"
			+ " ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


	// R = selectAll()
	public List<ReviewVO> getAll(ReviewVO reviewVO) {
		List<ReviewVO> datas = new ArrayList<>();
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[M 로그] SELECTALL() 호출, condition: " + reviewVO.getCondition());
			// 전체 리뷰 목록
			if (reviewVO.getCondition().equals("SELECTALLREVIEWLIST")) {
				pstmt = conn.prepareStatement(SELECTALLREVIEWLIST);
				pstmt.setInt(1, reviewVO.getReviewIndex());
				pstmt.setInt(2, reviewVO.getReviewContentCount());
				System.out.println("[M 로그] SELECTALLREVIEWLIST 실행: index=[" + reviewVO.getReviewIndex() + "], count=["
						+ reviewVO.getReviewContentCount() + "]");
			}
			// 한 상품에 대한 리뷰 목록
			else if (reviewVO.getCondition().equals("SELECTALLREVIEWONEPRODUCT")) {
				pstmt = conn.prepareStatement(SELECTALLREVIEWONEPRODUCT);
				pstmt.setLong(1, reviewVO.getProductComboNumber());
				pstmt.setInt(2, reviewVO.getReviewIndex());
				pstmt.setInt(3, reviewVO.getReviewContentCount());
				System.out.println("[M 로그] SELECTALLREVIEWONEPRODUCT 실행: comboNumber=["
						+ reviewVO.getProductComboNumber() + "], index=[" + reviewVO.getReviewIndex() + "], count=["
						+ reviewVO.getReviewContentCount() + "]");
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReviewVO data = new ReviewVO();
				// 전체 리뷰 목록
				if (reviewVO.getCondition().equals("SELECTALLREVIEWLIST")) {
					data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
					data.setReviewScore(rs.getInt("REVIEW_SCORE"));
					data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
					data.setReviewContent(rs.getString("REVIEW_CONTENT"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
					datas.add(data);
				}
				// 한 상품에 대한 리뷰 목록
				else if (reviewVO.getCondition().equals("SELECTALLREVIEWONEPRODUCT")) {
					data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
					data.setReviewScore(rs.getInt("REVIEW_SCORE"));
					data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
					data.setReviewContent(rs.getString("REVIEW_CONTENT"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
					data.setTotalReviewCount(rs.getLong("TOTAL_REVIEW_COUNT"));
					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
					datas.add(data);
				}
			}
			System.out.println("[M 로그] 조회된 리뷰 개수: " + datas.size());
		} catch (Exception e) {
			System.out.println("[M 로그] SELECTALL에서 발생한 에러 [" + reviewVO.getCondition() + "]");
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}

	// R = selectOne()
	public ReviewVO getOne(ReviewVO reviewVO) {
		ReviewVO data = null;
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[M 로그] SELECTONE 호출, condition: " + reviewVO.getCondition());
			// 리뷰 상세 조회
			if (reviewVO.getCondition().equals("SELECTONEREVIEW")) {
				pstmt = conn.prepareStatement(SELECTONEREVIEW);
				pstmt.setLong(1, reviewVO.getMemberNumber());
				pstmt.setLong(2, reviewVO.getProductComboNumber());
				System.out.println("[M 로그] SELECTONEREVIEW 조건 실행 : memberNumber=[" + reviewVO.getMemberNumber()
						+ "], productComboNumber=[" + reviewVO.getProductComboNumber() + "]");
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				data = new ReviewVO();
				data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
				data.setReviewScore(rs.getInt("REVIEW_SCORE"));
				data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
				data.setReviewContent(rs.getString("REVIEW_CONTENT"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
				data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
			}
		} catch (Exception e) {
			System.out.println("[M 로그] SELECTONE 에러 발생 (" + reviewVO.getCondition() + ")");
			e.printStackTrace();
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}

	// C
	public boolean insert(ReviewVO reviewVO) {
		try {
			System.out.println("[M 로그] INSERT 호출");
			conn = OracleJDBCUtil.connect();
			// 리뷰 작성
			pstmt = conn.prepareStatement(INSERTREVIEW);
			pstmt.setInt(1, reviewVO.getReviewScore());
			pstmt.setString(2, reviewVO.getReviewContent());
			pstmt.setLong(3, reviewVO.getMemberNumber());
			pstmt.setLong(4, reviewVO.getProductComboNumber());
			System.out.println("[M 로그] INSERTREVIEW 실행 : reviewScore =" + reviewVO.getReviewScore() + "], memberName=["
					+ reviewVO.getMemberName() + "], memberNumber=[" + reviewVO.getMemberNumber()
					+ "], productComboNumber=[" + reviewVO.getProductComboNumber() + "]");
			int rs = pstmt.executeUpdate();
			System.out.println("[M 로그] INSERT 결과: [" + rs + "]");
			if (rs <= 0) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("[M 로그] INSERT 에러 발생 (" + reviewVO.getCondition() + ")");
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}

	// U
	public boolean update(ReviewVO reviewVO) {
		try {
			conn = OracleJDBCUtil.connect();
			System.out.println("[M 로그] UPDATE 호출");
			// 리뷰 수정
			pstmt = conn.prepareStatement(UPDATEREVIEW);
			pstmt.setLong(1, reviewVO.getReviewScore());
			pstmt.setString(2, reviewVO.getReviewContent());
			pstmt.setLong(3, reviewVO.getReviewNumber());
			System.out.println(
					"[M 로그] UPDATEREVIEW 실행 : reviewScore =[" + reviewVO.getReviewScore() + "], reviewContent =["
							+ reviewVO.getReviewContent() + "], reviewNumber =[" + reviewVO.getReviewNumber() + "]");
			int rs = pstmt.executeUpdate();
			System.out.println("[M 로그] UPDATE 결과:[" + rs + "]");
			return rs > 0;
		} catch (Exception e) {
			System.out.println("[M 로그] UPDATE 에러 발생 (" + reviewVO.getCondition() + ")");
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
	}

	// D
	public boolean delete(ReviewVO reviewVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = OracleJDBCUtil.connect();
			// 리뷰 삭제
			pstmt = conn.prepareStatement(DELETEREVIEW);
			pstmt.setLong(1, reviewVO.getReviewNumber());
			System.out.println("[M 로그] DELETEREVIEW 실행 : reviewNumber =[" + reviewVO.getReviewNumber() + "]");
			int rs = pstmt.executeUpdate();
			System.out.println("[M 로그] DELETE 결과:[" + rs + "]");
			return rs > 0;
		} catch (Exception e) {
			System.out.println("[M 로그] DELETE 에러 발생 (" + reviewVO.getCondition() + ")");
			e.printStackTrace();
			return false;
		} finally {
			OracleJDBCUtil.disconnect(conn, pstmt);
		}
	}
}