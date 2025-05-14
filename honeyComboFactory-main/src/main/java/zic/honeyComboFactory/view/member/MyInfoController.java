package zic.honeyComboFactory.view.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedService;
import zic.honeyComboFactory.biz.boardComboLikedVO.BoardComboLikedVO;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseService;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;

@Controller
public class MyInfoController { // 내정보 처리
	@Autowired
	private MemberService memberService;
	@Autowired
	private BoardComboService boardComboService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private BoardComboLikedService boardComboLikedService;

	// 내정보 수정 기능
	@PostMapping("/member/myInfo/update")
	@ResponseBody
	public String updateMyInfo(MemberVO membervo, HttpSession session) {
		System.out.println("내정보 수정 컨트롤러 진입");
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		membervo.setCondition("UPDATEMYINFORMATION");
		membervo.setMemberNumber(memberNumber);

		boolean flag = this.memberService.update(membervo);

		if (flag) {
			System.out.println("회원정보 업데이트 성공");
			return "true";
		} else {
			System.out.println("회원정보 업데이트 실패");
			return "false";
		}
	}

	// 본인 작성글 불러오기 기능
	@PostMapping("/member/myInfo/myBoard")
	@ResponseBody
	public List<Map<String, Object>> loadMyBoard(@RequestParam(required = false) Integer myBoardPageNumber, @RequestParam(required = false) Integer myBoardContentCount, BoardComboVO boardComboVO, HttpServletRequest request,
			HttpSession session) {
		System.out.println("내 게시글 목록 서블릿 도착");
		// 본인 작성 글 페이지네이션
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		System.out.println("꿀조합 게시판 회원번호 [" + memberNumber + "]");

		// 페이지 값 받기
		if (myBoardPageNumber == null) {
			 myBoardPageNumber = 1;
		}
		System.out.println("페이지 번호 로그 [" + myBoardPageNumber + "]");

		// 보여줄 내역 수
		if (myBoardContentCount == null) {
			myBoardContentCount = 3;
		}
		System.out.println("작성글 보여줄 내역 수 [" + myBoardContentCount + "]");

		int boardStartIndex = ((myBoardPageNumber - 1) * myBoardContentCount)+1;
		int endRow = myBoardPageNumber * myBoardContentCount;
		System.out.println("작성글 시작 인덱스 번호 [" + boardStartIndex + "]");
		System.out.println("작성글 마지막 인덱스 번호 [" + endRow + "]");

		boardComboVO.setCondition("SELECTALLMEMBERWRITE");
		boardComboVO.setMemberNumber(memberNumber);
		boardComboVO.setBoardComboIndex(boardStartIndex);
		boardComboVO.setBoardComboContentCount(endRow);

		List<BoardComboVO> boardComboList = this.boardComboService.getAll(boardComboVO);
		if (boardComboList == null) {
			boardComboList = new ArrayList<>();
		}
		System.out.println("본인 작성글 로그 [" + boardComboList + "]");

		List<Map<String, Object>> boardArray = new ArrayList<>();
		for (BoardComboVO board : boardComboList) {
			Map<String, Object> boardObject = new HashMap<>();
			// JSONObject는 Map이므로 타입을 명시적으로 설정
			boardObject.put("boardComboNumber", board.getBoardComboNumber());
			boardObject.put("boardComboTitle", board.getBoardComboTitle());
			boardObject.put("boardComboViewCount", board.getBoardComboViewCount());
			boardObject.put("boardComboLikedCount", board.getBoardComboLikedCount());
			boardObject.put("boardComboRegisterDate", board.getBoardComboRegisterDate().toString());
			boardObject.put("totalCountNumber", board.getTotalCountNumber());
			boardArray.add(boardObject);
		}
		// 응답 설정 및 전송
		return boardArray;
	}

	// 주문 내역 불러오기 기능
	@PostMapping("/member/myInfo/purchase")
	@ResponseBody
	public List<Map<String, Object>> loadMyPurchase(@RequestParam(required = false) Integer purchasePageNumber, @RequestParam(required = false) Integer purchaseContentCount, PurchaseVO purchaseVO, HttpSession session) {
		System.out.println("내 구매내역 목록 서블릿 도착");

		// 주문 내역
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		System.out.println("주문상세 회원번호 [" + memberNumber + "]");

		// 페이지 값 받기
		if (purchasePageNumber == null) {
			purchasePageNumber = 1;
		}
		System.out.println("페이지 번호 로그 [" + purchasePageNumber + "]");

		// 보여줄 내역 수
		if (purchaseContentCount == null) {
			purchaseContentCount = 3;
		}
		System.out.println("내역 수 로그 [" + purchaseContentCount + "]");

		// 시작 인덱스
		int purchaseStartIndex = ((purchasePageNumber - 1) * purchaseContentCount)+1;
		int endRow = purchasePageNumber * purchaseContentCount;
		System.out.println("주문내역 시작 인덱스 번호 [" + purchaseStartIndex + "]");
		System.out.println("주문내역 마지막 인덱스 번호 [" + endRow + "]");

		// 주문 페이지네이션에 필요한 값 전달
		purchaseVO.setCondition("SELECTALLONEPERSON");
		purchaseVO.setMemberNumber(memberNumber);
		purchaseVO.setPurchaseIndex(purchaseStartIndex);
		purchaseVO.setPurchaseContentCount(endRow);
		List<PurchaseVO> purchaseList = this.purchaseService.getAll(purchaseVO);
		if (purchaseList == null) {
			purchaseList = new ArrayList<>();
		}
		System.out.println("주문내역 로그 [" + purchaseList + "]");

		List<Map<String, Object>> purchaseArray = new ArrayList<>();
		for (PurchaseVO purchase : purchaseList) {
			Map<String, Object> purchaseObject = new HashMap<>();
			// JSONObject는 Map이므로 타입을 명시적으로 설정
			purchaseObject.put("purchaseNumber", String.valueOf(purchase.getPurchaseNumber()));
			purchaseObject.put("purchaseTotalPrice", purchase.getPurchaseTotalPrice());
			purchaseObject.put("totalCountNumber", purchase.getTotalCountNumber());
			purchaseObject.put("purchaseTerminalId", purchase.getPurchaseTerminalId());
			purchaseArray.add(purchaseObject);
		}
		// 응답 설정 및 전송
		return purchaseArray;
	}

	// 좋아요 누른 글 불러오기 기능
	@PostMapping("/member/myInfo/likedBoard")
	@ResponseBody
	public List<Map<String, Object>> loadLikedBoard(@RequestParam(required = false) Integer likedBoardPageNumber, @RequestParam(required = false) Integer likedBoardContentCount, BoardComboLikedVO boardComboLikedVO, HttpSession session) {
		System.out.println("내 좋아요 목록 서블릿 도착");

		// 좋아요 글 내역
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		// 페이지 값 받기
		if (likedBoardPageNumber == null) {
			likedBoardPageNumber = 1;
		}
		System.out.println("페이지 번호 로그 [" + likedBoardPageNumber + "]");

		// 보여줄 내역 수
		if (likedBoardContentCount == null) {
			likedBoardContentCount = 3;
		}
		System.out.println("좋아요 보여줄 내역 수 [" + likedBoardContentCount + "]");

		int likedBoardStartIndex = ((likedBoardPageNumber - 1) * likedBoardContentCount)+1;
		int endRow = likedBoardPageNumber * likedBoardContentCount;
		System.out.println("작성글 시작 인덱스 번호 [" + likedBoardStartIndex + "]");
		System.out.println("작성글 마지막 인덱스 번호 [" + endRow + "]");

		boardComboLikedVO.setMemberNumber(memberNumber);
		boardComboLikedVO.setBoardComboLikedIndex(likedBoardStartIndex);
		boardComboLikedVO.setBoardComboLikedContentCount(endRow);

		List<BoardComboLikedVO> boardComboLikedList = this.boardComboLikedService.getAll(boardComboLikedVO);
		if (boardComboLikedList == null) {
			boardComboLikedList = new ArrayList<>();
		}
		System.out.println("좋아요한 글 로그 [" + boardComboLikedList + "]");

		List<Map<String, Object>> boardComboLikedArray = new ArrayList<>();
		for (BoardComboLikedVO boardComboLiked : boardComboLikedList) {
			Map<String, Object> boardComboLikedObject = new HashMap<>();
			// JSONObject는 Map이므로 타입을 명시적으로 설정
			boardComboLikedObject.put("boardComboNumber", boardComboLiked.getBoardComboNumber());
			boardComboLikedObject.put("memberName", boardComboLiked.getMemberName());
			boardComboLikedObject.put("totalCountNumber", boardComboLiked.getTotalCountNumber());

			boardComboLikedArray.add(boardComboLikedObject);
		}
		// 응답 설정 및 전송
		return boardComboLikedArray;
	}

	// 회원탈퇴 기능
	@PostMapping("/member/withdraw")
	@ResponseBody
	public String withdraw(MemberVO memberVO, HttpSession session) {
		System.out.println("회원탈퇴 컨트롤러 진입");
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		System.out.println("회원탈퇴할 회원 번호 [" + memberNumber + "]");

		memberVO.setCondition("UPDATECANCEL");
		memberVO.setMemberNumber(memberNumber);
		boolean flag = this.memberService.update(memberVO);

		if (flag) {
			System.out.println("회원탈퇴 성공");
			session.invalidate();
		} else {
			System.out.println("회원탈퇴 실패");
		}
		// 텍스트 응답 설정
		return Boolean.toString(flag); // "true" 또는 "false" 반환
	}
}
