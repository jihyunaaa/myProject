package zic.honeyComboFactory.biz.member.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import zic.honeyComboFactory.biz.memberVO.MemberVO;

public class OracleMemberDAOJdbcTemplate { // 회원 기능 - Oracle DB

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 1. 전체 회원 목록 조회
	final String SELECTALL = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL,"
			+ " MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW, TOTAL_COUNT_NUMBER FROM (SELECT MEMBER_NUMBER,"
			+ " MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN,"
			+ " MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW, (SELECT COUNT(MEMBER_NUMBER) FROM MEMBER) AS TOTAL_COUNT_NUMBER,"
			+ " ROW_NUMBER() OVER (ORDER BY MEMBER_NUMBER DESC) RN FROM MEMBER) WHERE RN BETWEEN ? AND ?";

	// 2. 로그인
	final String SELECTONELOGIN = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_PASSWORD, MEMBER_IS_ADMIN FROM MEMBER WHERE MEMBER_ID = ?";

	// 3. ID로 회원조회 (관리자 기능)
	final String SELECTONEMEMBER = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN,"
			+ " MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW"
			+ " FROM MEMBER WHERE MEMBER_ID = ?";

	// 4. 내 정보 상세 보기
	final String SELECTONEMYINFORMATION = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER,"
			+ " MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN FROM MEMBER WHERE MEMBER_NUMBER = ?";

	// 5. 회원가입
	final String INSERTJOIN = "INSERT INTO MEMBER (MEMBER_NUMBER, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER,"
			+ " MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW)"
			+ " VALUES (SEQ_MEMBER.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 0, 0)";

	// 6. 비밀번호 변경
	final String UPDATEPASSWORD = "UPDATE MEMBER SET MEMBER_PASSWORD = ? WHERE MEMBER_NUMBER = ? OR MEMBER_ID = ?";

	// 7. 아이디 찾기 ONE
	final String SELECTONEFINDID = "SELECT MEMBER_ID, MEMBER_NUMBER, MEMBER_IS_WITHDRAW FROM MEMBER "
			+ "WHERE (MEMBER_BIRTH = ? AND MEMBER_PHONE_NUMBER = ?) "
			+ "OR (MEMBER_BIRTH = ? AND MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?)";

	// 8. 비밀번호 찾기 ONE
	final String SELECTONEFINDPASSWORD = "SELECT MEMBER_NUMBER, MEMBER_PASSWORD, MEMBER_IS_WITHDRAW FROM MEMBER "
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
	final String SELECTONEMYPAGE = "SELECT MEMBER_NUMBER, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_IS_ADMIN, MEMBER_ID FROM MEMBER "
			+ "WHERE MEMBER_NUMBER = ?";

	// 14. 간편로그인 회원가입
	final String INSERTSOCIAL = "INSERT INTO MEMBER (MEMBER_NUMBER, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER,"
			+ " MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 0, 0)";

	// getAll → R
	public List<MemberVO> getAll(MemberVO memberVO) {
		Object[] args = { memberVO.getMemberIndex(), memberVO.getMemberContentCount() };
		return jdbcTemplate.query(SELECTALL, args, new MemberGetAllRowMapper());

	}

	// getOne → R
	public MemberVO getOne(MemberVO memberVO) {

	    // 로그인
	    if (memberVO.getCondition().equals("SELECTONELOGIN")) {
	        System.out.println("로그인");
	        Object[] args = { memberVO.getMemberId() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONELOGIN, args, new MemberGetOneLoginRowMapper());
	       
	        return getSingleResult(list);
	    }

	    // 내정보 본인확인
	    else if (memberVO.getCondition().equals("SELECTONEMYPAGE")) {
	        System.out.println("내 정보 본인확인");
	        Object[] args = { memberVO.getMemberNumber() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEMYPAGE, args, new MemberGetOneCheckUserRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // ID로 회원 조회
	    else if (memberVO.getCondition().equals("SELECTONEMEMBER")) {
	        System.out.println("ID로 회원 조회");
	        Object[] args = { memberVO.getMemberId() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEMEMBER, args, new MemberGetOneCheckMemberRowMapper());
	       
	        return getSingleResult(list);
	    }

	    // 내 정보 상세 보기
	    else if (memberVO.getCondition().equals("SELECTONEMYINFORMATION")) {
	        System.out.println("내 정보 상세 보기");
	        Object[] args = { memberVO.getMemberNumber() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEMYINFORMATION, args, new MemberGetOneMyInfoRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // 아이디 찾기
	    else if (memberVO.getCondition().equals("SELECTONEFINDID")) {
	        System.out.println("아이디 찾기");
	        Object[] args = {
	            memberVO.getMemberBirth(),
	            memberVO.getMemberPhoneNumber(),
	            memberVO.getMemberBirth(),
	            memberVO.getMemberEmailId(),
	            memberVO.getMemberEmailDomain()
	        };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEFINDID, args, new MemberGetOneFindIdRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // 비밀번호 찾기
	    else if (memberVO.getCondition().equals("SELECTONEFINDPASSWORD")) {
	        System.out.println("비밀번호 찾기");
	        Object[] args = {
	            memberVO.getMemberBirth(),
	            memberVO.getMemberId(),
	            memberVO.getMemberPhoneNumber(),
	            memberVO.getMemberBirth(),
	            memberVO.getMemberId(),
	            memberVO.getMemberEmailId(),
	            memberVO.getMemberEmailDomain()
	        };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEFINDPASSWORD, args, new MemberGetOneFindPasswordRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // 이메일 중복검사
	    else if (memberVO.getCondition().equals("SELECTONEEMAIL")) {
	        System.out.println("이메일 중복검사");
	        Object[] args = { memberVO.getMemberEmailId(), memberVO.getMemberEmailDomain() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEEMAIL, args, new MemberGetOneCheckRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // 핸드폰 중복검사
	    else if (memberVO.getCondition().equals("SELECTONEPHONE")) {
	        System.out.println("핸드폰 중복검사");
	        Object[] args = { memberVO.getMemberPhoneNumber() };
	        List<MemberVO> list = jdbcTemplate.query(SELECTONEPHONE, args, new MemberGetOneMemberNumberRowMapper());
	        
	        return getSingleResult(list);
	    }

	    // 조건이 일치하지 않으면 null
	    else {
	        return null;
	    }
	}

	// insert → C
	public boolean insert(MemberVO memberVO) {

		// 일반 회원 가입
		if (memberVO.getCondition().equals("INSERTJOIN")) {
			Object[] args = { memberVO.getMemberId(), memberVO.getMemberPassword(), memberVO.getMemberName(),
					memberVO.getMemberBirth(), memberVO.getMemberPhoneNumber(),
					(memberVO.getMemberAddressMain() != null && !memberVO.getMemberAddressMain().isEmpty())
							? memberVO.getMemberAddressMain()
							: null,
					(memberVO.getMemberAddressDetail() != null && !memberVO.getMemberAddressDetail().isEmpty())
							? memberVO.getMemberAddressDetail()
							: null,
					memberVO.getMemberEmailId(), memberVO.getMemberEmailDomain() };

			System.out.println("회원가입");
			int result = jdbcTemplate.update(INSERTJOIN, args);

			if (result <= 0) {
				return false;
			}
		}
		
		// 간편 로그인
		else if(memberVO.getCondition().equals("INSERTSOCIAL")) {
			if(memberVO.getLoginType()==0 ) {
				String sql = "SELECT NVL(MAX(MEMBER_NUMBER), 19999) + 1 FROM MEMBER WHERE MEMBER_NUMBER >= 20000 AND MEMBER_NUMBER < 30000";
				memberVO.setMemberNumber(jdbcTemplate.queryForObject(sql, Long.class));
			}
			else if (memberVO.getLoginType() == 1) { // 네이버
				String sql = "SELECT NVL(MAX(MEMBER_NUMBER), 29999) + 1	FROM MEMBER WHERE MEMBER_NUMBER >= 30000 AND MEMBER_NUMBER < 40000";
				memberVO.setMemberNumber(jdbcTemplate.queryForObject(sql, Long.class));
			}
			Object[] args = { memberVO.getMemberNumber(), memberVO.getMemberId(), memberVO.getMemberPassword(), memberVO.getMemberName(),
					memberVO.getMemberBirth(), memberVO.getMemberPhoneNumber(), memberVO.getMemberAddressMain(), memberVO.getMemberAddressDetail(),
					memberVO.getMemberEmailId(), memberVO.getMemberEmailDomain() };
			
			int result = jdbcTemplate.update(INSERTSOCIAL,args);
			
			if(result <= 0) {
				return false;
			}
		}
		return true;

	}

	// update → U
	public boolean update(MemberVO memberVO) {

		// 비밀번호 변경
		if (memberVO.getCondition().equals("UPDATEPASSWORD")) {
			Object[] args = { memberVO.getMemberPassword(), memberVO.getMemberNumber(), memberVO.getMemberId() };
			System.out.println("비밀번호 변경");
			int result = jdbcTemplate.update(UPDATEPASSWORD, args);

			if (result <= 0) {
				return false;
			}
		}
		// 회원정보 수정
		else if (memberVO.getCondition().equals("UPDATEMYINFORMATION")) {
			Object[] args = { memberVO.getMemberPhoneNumber(), memberVO.getMemberAddressMain(),
					memberVO.getMemberAddressDetail(), memberVO.getMemberEmailId(), memberVO.getMemberEmailDomain(),
					memberVO.getMemberNumber() };
			System.out.println("회원정보 수정");
			int result = jdbcTemplate.update(UPDATEMYINFORMATION, args);
			if (result <= 0) {
				return false;
			}
		}
		// 회원 탈퇴
		else if (memberVO.getCondition().equals("UPDATECANCEL")) {
			Object[] args = { memberVO.getMemberNumber() };
			System.out.println("회원탈퇴");
			int result = jdbcTemplate.update(UPDATECANCEL, args);
			if (result <= 0) {
				return false;
			}
		}
		return true;
	}

	// delete → D
	public boolean delete(MemberVO memberVO) {
		return false;
	}
	
	// 반환 메서드
	private MemberVO getSingleResult(List<MemberVO> list) {
	    if (list.isEmpty()) {
	        return null;
	    }
	    return list.get(0);
	}
}

// getAll에 필요한 데이터 반환
class MemberGetAllRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
		data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

		System.out.println("getAll 데이터 [" + data + "]");
		return data;
	}

}

// 내정보 본인확인, 이메일 중복검사에 필요한 데이터 반환
class MemberGetOneCheckRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();

		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		data.setMemberId(rs.getString("MEMBER_ID"));

		System.out.println("getAll 데이터 [" + data + "]");
		return data;
	}
}

// 로그인
class MemberGetOneLoginRowMapper implements RowMapper<MemberVO>{

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();
		
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberId(rs.getString("MEMBER_ID"));
		data.setMemberPassword(rs.getString("MEMBER_PASSWORD"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		
		System.out.println("로그인 데이터 ["+data+"]");
		return data;
	}	
}


// 핸드폰 중복검사에 필요한 데이터 반환
class MemberGetOneMemberNumberRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();

		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));

		System.out.println("getOne 데이터 [" + data + "]");
		return data;
	}
}

// 비밀번호 찾기
class MemberGetOneFindPasswordRowMapper implements RowMapper<MemberVO>{

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();
		
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberPassword(rs.getString("MEMBER_PASSWORD"));
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
		
		System.out.println("비밀번호 찾기 getOne 데이터 ["+data+ "]");
		return data;
	}
	
}

// 내 정보 본인확인
class MemberGetOneCheckUserRowMapper implements RowMapper<MemberVO>{

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();
		
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberPassword(rs.getString("MEMBER_PASSWORD"));
		data.setMemberName(rs.getString("MEMBER_NAME"));
		data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
		data.setMemberId(rs.getString("MEMBER_ID"));
		
		System.out.println("내 정보 본인확인 getOne 데이터 ["+data+"]");
		return data;
	}
	
}


// ID로 회원조회
class MemberGetOneCheckMemberRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();

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

		System.out.println("getOne 회원조회 데이터 [" + data + "]");
		return data;
	}
}

// 내 정보 상세 보기
class MemberGetOneMyInfoRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {

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

		System.out.println("내 정보 상세 보기 [" + data + "]");
		return data;
	}
}

// ID 찾기
class MemberGetOneFindIdRowMapper implements RowMapper<MemberVO> {

	@Override
	public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberVO data = new MemberVO();

		data.setMemberId(rs.getString("MEMBER_ID"));
		data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
		data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));

		System.out.println("ID 찾기 데이터 [" + data + "]");
		return data;
	}
}




//23ai 버전 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY을 사용하였으나 11g에서는 사용 불가

/*
 * //1. 전체 회원 목록 조회 final String SELECTALL =
 * "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN,"
 * +
 * " MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW,"
 * +
 * " (SELECT COUNT(MEMBER_NUMBER) FROM MEMBER) AS TOTAL_COUNT_NUMBER FROM MEMBER ORDER BY MEMBER_NUMBER DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
 * ;
 */
