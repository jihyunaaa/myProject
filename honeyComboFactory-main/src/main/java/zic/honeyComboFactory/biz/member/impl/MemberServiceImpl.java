package zic.honeyComboFactory.biz.member.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.common.util.PasswordEncoderUtil;

@Service("memberService")
public class MemberServiceImpl implements MemberService { // 회원 서비스
	@Autowired // MemberDAO 객체가 메모리에 new 되어있어야 가능
	private OracleMemberDAOMybatis memberDAO; // Oracle DB
	// private MySQLMemberDAO memberDAO; // MySql DB
	@Autowired // 비밀번호 암호화 객체
	private PasswordEncoderUtil passwordEncoderUtil;

	@Override
	public List<MemberVO> getAll(MemberVO memberVO) {
		return this.memberDAO.getAll(memberVO);
	}

	@Override
	public MemberVO getOne(MemberVO memberVO) {
		return this.memberDAO.getOne(memberVO);
	}

	@Override
	public boolean insert(MemberVO memberVO) {
		// 비밀번호 암호화
		String encodedPassword = this.passwordEncoderUtil.encryptPassword(memberVO.getMemberPassword());
		memberVO.setMemberPassword(encodedPassword);

		return this.memberDAO.insert(memberVO);
	}

	@Override
	public boolean update(MemberVO memberVO) {
		// 비밀번호가 들어있을 경우
		if(memberVO.getMemberPassword() != null) {
			// 비밀번호 암호화
			String encodedPassword = this.passwordEncoderUtil.encryptPassword(memberVO.getMemberPassword());
			memberVO.setMemberPassword(encodedPassword);
		}

		return this.memberDAO.update(memberVO);
	}

	@Override
	public boolean delete(MemberVO memberVO) {
		return this.memberDAO.delete(memberVO);
	}
}
