package zic.honeyComboFactory.view.boardComboLiked;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedService;
import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;

@Controller
public class BoardComboLikedController {
	@Autowired
	private BoardComboService boardComboService;
	@Autowired
	private BoardComboLikedService boardComboLikedService;

	// 꿀조합 게시글 좋아요 여부 반환 기능
	@PostMapping("/boardComboLiked/isLiked")
	@ResponseBody
	public ResponseEntity<Boolean> likedComboBoard(BoardComboLikedVO boardComboLikedVO, HttpSession session,
			@RequestParam("boardComboNumber") long number) {

		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		boardComboLikedVO.setMemberNumber(memberNumber);

		boolean isLiked = boardComboLikedService.getOne(boardComboLikedVO) != null;

		return ResponseEntity.ok(isLiked);
	}

	// 꿀조합 게시글 좋아요 등록/삭제 기능
	@PostMapping("/boardComboLiked/like")
	public ResponseEntity<Void> memberLikedComboBoard(@RequestBody Map<String, Object> payload,
			BoardComboLikedVO boardComboLikedVO, HttpSession session) {
		System.out.println("Controller-꿀조합 게시글 좋아요 등록/삭제 실행");

		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());
		long boardComboNumber = Long.parseLong(payload.get("boardComboNumber").toString());
		String likedCondition = payload.get("likedCondition").toString();

		boardComboLikedVO.setMemberNumber(memberNumber);
		boardComboLikedVO.setBoardComboNumber(boardComboNumber);

		boolean flag = false;
		// 좋아요 등록이라면
		if ("INSERTLIKED".equals(likedCondition)) {
			System.out.println("좋아요 등록 실행");
			flag = boardComboLikedService.insert(boardComboLikedVO);
		}
		// 좋아요 취소라면
		else if ("DELETELIKED".equals(likedCondition)) {
			System.out.println("좋아요 취소 실행");
			flag = boardComboLikedService.delete(boardComboLikedVO);
		}

		// 결과에 따라 로그 찍기
		if (flag) {
			System.out.println("게시글 좋아요 처리 완료: [" + likedCondition + "] , 회원번호: [" + memberNumber + "]");
		} else {
			System.err.println("게시글 좋아요 처리 실패: [" + likedCondition + "] , 회원번호: [" + memberNumber + "]");
			// 예외 발생
			throw new RuntimeException("게시글 좋아요 처리 실패");
		}

		// 빈 응답 반환 (sendBeacon은 비워도 됨)
		return ResponseEntity.ok().build();
	}

	// 꿀조합 게시글 좋아요 등록/삭제 기능 <= 쿠키 적용 전
//	@PostMapping("/boardComboLiked/like")
//	@ResponseBody
//	public ResponseEntity<String> memberLikedComboBoard(BoardComboVO boardComboVO, BoardComboLikedVO boardComboLikedVO,
//			HttpSession session, @RequestParam("boardComboNumber") long number,
//			@RequestParam("likedCondition") String condition) {
//		
//		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());
//		
//		boardComboLikedVO.setMemberNumber(memberNumber);
//		
//		boardComboVO.setCondition("SELECTONECOMBOBOARD");
//		boardComboVO.setBoardComboNumber(number);
//		
//		BoardComboVO checkMemberLiked = boardComboService.getOne(boardComboVO);
//		int boardComboLikedCount = (int) checkMemberLiked.getBoardComboLikedCount();
//		
//		boolean flag = false;
//		
//		if ("INSERTLIKED".equals(condition)) {
//			flag = boardComboLikedService.insert(boardComboLikedVO);
//			boardComboLikedCount++;
//		} else if ("DELETELIKED".equals(condition)) {
//			flag = boardComboLikedService.delete(boardComboLikedVO);
//			boardComboLikedCount--;
//		}
//		
//		if (flag) {
//			return ResponseEntity.ok(String.valueOf(boardComboLikedCount));
//		} else {
//			return ResponseEntity.ok("false");
//		}
//	}
}
