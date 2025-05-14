package zic.honeyComboFactory.biz.memberVO;

import java.util.List;

public interface MemberService { // 회원 인터페이스

	List<MemberVO> getAll(MemberVO vo); // SelectAll()

	MemberVO getOne(MemberVO vo); // SelectOne()

	boolean insert(MemberVO vo);

	boolean update(MemberVO vo);

	boolean delete(MemberVO vo);
}
