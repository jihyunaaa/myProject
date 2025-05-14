package zic.honeyComboFactory.biz.member.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.memberVO.MemberVO;

@Repository("oracleMemberDAO")
public class OracleMemberDAOMybatis { // 회원 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;
	// getAll → R
	public List<MemberVO> getAll(MemberVO memberVO) {
		System.out.println("전체 회원 조회");
		return mybatis.selectList("MemberDAO.SELECTALL", memberVO);
	}

	// getOne → R
	public MemberVO getOne(MemberVO memberVO) {
	    // 로그인
	    if (memberVO.getCondition().equals("SELECTONELOGIN")) {
	        System.out.println("로그인");
	        return mybatis.selectOne("MemberDAO.SELECTONELOGIN", memberVO);
	    }

	    // 내정보 본인확인
	    else if (memberVO.getCondition().equals("SELECTONEMYPAGE")) {
	        System.out.println("내 정보 본인확인");
	        return mybatis.selectOne("MemberDAO.SELECTONEMYPAGE", memberVO);
	    }

	    // ID로 회원 조회
	    else if (memberVO.getCondition().equals("SELECTONEMEMBER")) {
	        System.out.println("ID로 회원 조회");
	        return mybatis.selectOne("MemberDAO.SELECTONEMEMBER", memberVO);
	    }

	    // 내 정보 상세 보기
	    else if (memberVO.getCondition().equals("SELECTONEMYINFORMATION")) {
	        System.out.println("내 정보 상세 보기");
	        return mybatis.selectOne("MemberDAO.SELECTONEMYINFORMATION", memberVO);
	    }

	    // 아이디 찾기
	    else if (memberVO.getCondition().equals("SELECTONEFINDID")) {
	        System.out.println("아이디 찾기");
	        return mybatis.selectOne("MemberDAO.SELECTONEFINDID", memberVO);
	    }

	    // 비밀번호 찾기
	    else if (memberVO.getCondition().equals("SELECTONEFINDPASSWORD")) {
	        System.out.println("비밀번호 찾기");
	        return mybatis.selectOne("MemberDAO.SELECTONEFINDPASSWORD", memberVO);
	    }

	    // 이메일 중복검사
	    else if (memberVO.getCondition().equals("SELECTONEEMAIL")) {
	        System.out.println("이메일 중복검사");
	        return mybatis.selectOne("MemberDAO.SELECTONEEMAIL", memberVO);
	    }

	    // 핸드폰 중복검사
	    else if (memberVO.getCondition().equals("SELECTONEPHONE")) {
	        System.out.println("핸드폰 중복검사");
	        return mybatis.selectOne("MemberDAO.SELECTONEPHONE", memberVO);
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
			System.out.println("일반 회원 가입");
			int result = mybatis.insert("MemberDAO.INSERTJOIN", memberVO);

			if (result <= 0) {
				return false;
			}
		}
		// 간편 로그인
		else if(memberVO.getCondition().equals("INSERTSOCIAL")) {
			System.out.println("간편로그인");
			System.out.println("로그인 타입 ["+memberVO.getLoginType()+"]");
			Long newMemberNumber = mybatis.selectOne("MemberDAO.GETSOCIALMEMBERNUMBER", memberVO.getLoginType());
			memberVO.setMemberNumber(newMemberNumber);
			
			int result = mybatis.insert("MemberDAO.INSERTSOCIAL",memberVO);
			
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
			System.out.println("비밀번호 변경");
			int result = mybatis.update("MemberDAO.UPDATEPASSWORD", memberVO);

			if (result <= 0) {
				return false;
			}
		}
		// 회원정보 수정
		else if (memberVO.getCondition().equals("UPDATEMYINFORMATION")) {
			System.out.println("회원정보 수정");
			int result = mybatis.update("MemberDAO.UPDATEMYINFORMATION", memberVO);
			if (result <= 0) {
				return false;
			}
		}
		// 회원 탈퇴
		else if (memberVO.getCondition().equals("UPDATECANCEL")) {
			System.out.println("회원탈퇴");
			int result = mybatis.update("MemberDAO.UPDATECANCEL", memberVO);
			
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
}
