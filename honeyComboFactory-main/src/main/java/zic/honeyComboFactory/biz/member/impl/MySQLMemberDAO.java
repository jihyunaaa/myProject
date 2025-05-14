package zic.honeyComboFactory.biz.member.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.util.MySQLJDBCUtil;

@Repository("mySQLMemberDAO")
public class MySQLMemberDAO { // 회원 기능 - MySQL DB
	private Connection conn;
	private PreparedStatement pstmt;

	// 1. 전체 회원 목록 조회
	final String SELECTALL = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, "
			+ "MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, "
			+ "MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW, (SELECT COUNT(MEMBER_NUMBER) FROM MEMBER) AS TOTAL_COUNT_NUMBER FROM MEMBER "
			+ "ORDER BY MEMBER_NUMBER DESC LIMIT ?, ?";
	// 2. 로그인
	final String SELECTONELOGIN = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_IS_ADMIN FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_PASSWORD = ?";

	// 3. ID로 회원조회 (관리자 기능)
	final String SELECTONEMEMBER = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, "
			+ "MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW FROM MEMBER "
			+ "WHERE MEMBER_ID = ?";

	// 4. 내 정보 상세 보기
	final String SELECTONEMYINFORMATION = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, "
			+ "MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN FROM MEMBER WHERE MEMBER_NUMBER = ?";

	// 5. 회원가입
	final String INSERTJOIN = "INSERT INTO MEMBER (MEMBER_NUMBER, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_BIRTH, "
			+ "MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, "
			+ "MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW) "
			+ "SELECT IFNULL(MAX(MEMBER_NUMBER), 0) + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), 0, 0 FROM MEMBER";

	// 6. 비밀번호 변경
	final String UPDATEPASSWORD = "UPDATE MEMBER SET MEMBER_PASSWORD = ? WHERE MEMBER_NUMBER = ? OR MEMBER_ID = ?";

	// 7. 아이디 찾기 ONE
	final String SELECTONEFINDID = "SELECT MEMBER_ID FROM MEMBER "
			+ "WHERE (MEMBER_BIRTH = ? AND MEMBER_PHONE_NUMBER = ?) "
			+ "OR (MEMBER_BIRTH = ? AND MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?)";

	// 8. 비밀번호 찾기 ONE
	final String SELECTONEFINDPASSWORD = "SELECT MEMBER_NUMBER FROM MEMBER "
			+ "WHERE (MEMBER_BIRTH = ? AND MEMBER_ID = ? AND MEMBER_PHONE_NUMBER = ?) "
			+ "OR (MEMBER_BIRTH = ? AND MEMBER_ID = ? AND MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?)";

	// 9. 내 정보 수정 U
	final String UPDATEMYINFORMATION = "UPDATE MEMBER SET MEMBER_PHONE_NUMBER = ?, MEMBER_ADDRESS_MAIN = ?, "
			+ "MEMBER_ADDRESS_DETAIL = ?, MEMBER_EMAIL_ID = ?, MEMBER_EMAIL_DOMAIN = ? WHERE MEMBER_NUMBER = ?";

	// 10. 회원 탈퇴 U
	final String UPDATECANCEL = "UPDATE MEMBER SET MEMBER_IS_WITHDRAW = 1 WHERE MEMBER_NUMBER = ?";

	// 11. 이메일 중복검사 ONE
	final String SELECTONEEMAIL = "SELECT MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, MEMBER_ID FROM MEMBER "
			+ "WHERE MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?";

	// 12. 핸드폰중복검사 ONE
	final String SELECTONEPHONE = "SELECT MEMBER_NUMBER FROM MEMBER WHERE MEMBER_PHONE_NUMBER = ?";

	// 13. 내정보 본인확인 ONE
	final String SELECTONEMYPAGE = "SELECT MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, MEMBER_ID " + "FROM MEMBER "
			+ "WHERE MEMBER_NUMBER = ? AND MEMBER_PASSWORD = ?";

	// R = selectAll()
	public List<MemberVO> getAll(MemberVO memberVO) {
		List<MemberVO> datas = new ArrayList<>();
		ResultSet rs = null;
		try {
			conn = MySQLJDBCUtil.connect();
			
			// 전체 회원 목록 조회
			pstmt = conn.prepareStatement(SELECTALL);
			pstmt.setLong(1, memberVO.getMemberIndex());
			pstmt.setLong(2, memberVO.getMemberContentCount());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setMemberId(rs.getString("MEMBER_ID"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
				data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
				data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
				data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
				data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
				data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
				data.setMemberRegisterDate(rs.getDate("MEMBER_REGISTER_DATE"));
				data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
				data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_ADMIN"));
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
	public MemberVO getOne(MemberVO memberVO) {
		MemberVO data = null;
		ResultSet rs = null;

		try {
			conn = MySQLJDBCUtil.connect();
			String condition = memberVO.getCondition();
			// 로그인
			if (condition.equals("SELECTONELOGIN")) {
				pstmt = conn.prepareStatement(SELECTONELOGIN);
				pstmt.setString(1, memberVO.getMemberId());
				pstmt.setString(2, memberVO.getMemberPassword());
			}
			// ID로 회원조회
			else if (condition.equals("SELECTONEMEMBER")) {
				pstmt = conn.prepareStatement(SELECTONEMEMBER);
				pstmt.setString(1, memberVO.getMemberId());
			}
			// 내 정보 상세 보기
			else if (condition.equals("SELECTONEMYINFORMATION")) {
				pstmt = conn.prepareStatement(SELECTONEMYINFORMATION);
				pstmt.setLong(1, memberVO.getMemberNumber());
			}
			// 아이디 찾기
			else if (condition.equals("SELECTONEFINDID")) {
				pstmt = conn.prepareStatement(SELECTONEFINDID);
				pstmt.setDate(1, memberVO.getMemberBirth());
				pstmt.setString(2, memberVO.getMemberPhoneNumber());
				pstmt.setDate(3, memberVO.getMemberBirth());
				pstmt.setString(4, memberVO.getMemberEmailId());
				pstmt.setString(5, memberVO.getMemberEmailDomain());
			}
			// 비밀번호 찾기
			else if (condition.equals("SELECTONEFINDPASSWORD")) {
				pstmt = conn.prepareStatement(SELECTONEFINDPASSWORD);
				pstmt.setDate(1, memberVO.getMemberBirth());
				pstmt.setString(2, memberVO.getMemberId());
				pstmt.setString(3, memberVO.getMemberPhoneNumber());
				pstmt.setDate(4, memberVO.getMemberBirth());
				pstmt.setString(5, memberVO.getMemberId());
				pstmt.setString(6, memberVO.getMemberEmailId());
				pstmt.setString(7, memberVO.getMemberEmailDomain());
			}
			// 이메일 중복검사
			else if (condition.equals("SELECTONEEMAIL")) {
				pstmt = conn.prepareStatement(SELECTONEEMAIL);
				pstmt.setString(1, memberVO.getMemberEmailId());
				pstmt.setString(2, memberVO.getMemberEmailDomain());
			}
			// 핸드폰중복검사
			else if (condition.equals("SELECTONEPHONE")) {
				pstmt = conn.prepareStatement(SELECTONEPHONE);
				pstmt.setString(1, memberVO.getMemberPhoneNumber());
			}
			// 내정보 본인확인
			else if (condition.equals("SELECTONEMYPAGE")) {
				pstmt = conn.prepareStatement(SELECTONEMYPAGE);
				pstmt.setLong(1, memberVO.getMemberNumber());
				pstmt.setString(2, memberVO.getMemberPassword());
			} else {
				return null;
			}
			rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("쿼리 실행 조건: " + condition);
				data = new MemberVO();
				condition = memberVO.getCondition();

				// 로그인
				if (condition.equals("SELECTONELOGIN")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setMemberId(rs.getString("MEMBER_ID"));
				}
				// 내정보 본인확인
				else if (condition.equals("SELECTONEMYPAGE")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setMemberId(rs.getString("MEMBER_ID"));
				}
				// ID로 회원조회
				else if (condition.equals("SELECTONEMEMBER")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberId(rs.getString("MEMBER_ID"));
					data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
					data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
					data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
					data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
					data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
					data.setMemberRegisterDate(rs.getDate("MEMBER_REGISTER_DATE"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
				}
				// 내 정보 상세 보기
				else if (condition.equals("SELECTONEMYINFORMATION")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberId(rs.getString("MEMBER_ID"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
					data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
					data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
					data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
					data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
					data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
				}
				// 아이디 찾기
				else if (condition.equals("SELECTONEFINDID")) {
					data.setMemberId(rs.getString("MEMBER_ID"));
				}
				// 비밀번호 찾기
				else if (condition.equals("SELECTONEFINDPASSWORD")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				}
				// 이메일 중복검사
				else if (condition.equals("SELECTONEEMAIL")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setMemberId(rs.getString("MEMBER_ID"));

				}
				// 핸드폰 중복검사
				else if (condition.equals("SELECTONEPHONE")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}

	// C
	public boolean insert(MemberVO memberVO) {
		try {
			conn = MySQLJDBCUtil.connect();
			pstmt = conn.prepareStatement(INSERTJOIN);

			// 회원가입
			pstmt.setString(1, memberVO.getMemberId());
			pstmt.setString(2, memberVO.getMemberPassword());
			pstmt.setString(3, memberVO.getMemberName());
			pstmt.setDate(4, memberVO.getMemberBirth());
			pstmt.setString(5, memberVO.getMemberPhoneNumber());
			// pstmt.setString(6, memberDTO.getMemberAddressMain());
			// pstmt.setString(7, memberDTO.getMemberAddressDetail());
			// pstmt.setString(8, memberDTO.getMemberEmailId());
			// pstmt.setString(9, memberDTO.getMemberEmailDomain());

			if (memberVO.getMemberAddressMain() != null && !memberVO.getMemberAddressMain().isEmpty()) {
				pstmt.setString(6, memberVO.getMemberAddressMain());
			} else {
				pstmt.setNull(6, Types.VARCHAR);
			}

			if (memberVO.getMemberAddressDetail() != null && !memberVO.getMemberAddressDetail().isEmpty()) {
				pstmt.setString(7, memberVO.getMemberAddressDetail());
			} else {
				pstmt.setNull(7, Types.VARCHAR);
			}

			pstmt.setString(8, memberVO.getMemberEmailId());
			pstmt.setString(9, memberVO.getMemberEmailDomain());

			int result = pstmt.executeUpdate();
			return result > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
	}

	// U
	public boolean update(MemberVO memberVO) {
		try {
			conn = MySQLJDBCUtil.connect();
			String condition = memberVO.getCondition();

			// 비밀번호 변경
			if (condition.equals("UPDATEPASSWORD")) {
				pstmt = conn.prepareStatement(UPDATEPASSWORD);
				pstmt.setString(1, memberVO.getMemberPassword());
				pstmt.setLong(2, memberVO.getMemberNumber());
				pstmt.setString(3, memberVO.getMemberId());
			}
			// 회원정보 수정
			else if (condition.equals("UPDATEMYINFORMATION")) {
				pstmt = conn.prepareStatement(UPDATEMYINFORMATION);
				pstmt.setString(1, memberVO.getMemberPhoneNumber());
				pstmt.setString(2, memberVO.getMemberAddressMain());
				pstmt.setString(3, memberVO.getMemberAddressDetail());
				pstmt.setString(4, memberVO.getMemberEmailId());
				pstmt.setString(5, memberVO.getMemberEmailDomain());
				pstmt.setLong(6, memberVO.getMemberNumber());
			}
			// 회원탈퇴
			else if (condition.equals("UPDATECANCEL")) {
				pstmt = conn.prepareStatement(UPDATECANCEL);
				pstmt.setLong(1, memberVO.getMemberNumber());
			}
			int result = pstmt.executeUpdate();
			return result > 0;
		} catch (Exception e) {
			System.out.println("쿼리 실행 중 오류: " + e.getMessage());
			return false;
		} finally {
			MySQLJDBCUtil.disconnect(conn, pstmt);
		}
	}

	// D
	boolean delete(MemberVO memberVO) {
		return false;
	}
}
