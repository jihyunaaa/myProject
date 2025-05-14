package zic.honeyComboFactory.view.boardCombo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;

@Controller
public class BoardComboController {
	@Autowired
	private BoardComboService boardComboService;

	// 꿀조합 게시글 제목 검색 기능
	@GetMapping("/boardCombo/search")
	public ResponseEntity<Map<String, Object>> searchComboBoard(BoardComboVO boardComboVO,
			@RequestParam("boardOrderCondition") String condition, @RequestParam("boardPageNumber") int page,
			@RequestParam("boardContentCount") int count) {

		// 정렬 조건 설정
		if ("ORDERUPTODATE".equals(condition)) {
			boardComboVO.setCondition("SELECTALLCOMBOBOARDSEARCHDESC");
		} else if ("ORDEROLD".equals(condition)) {
			boardComboVO.setCondition("SELECTALLCOMBOBOARDSEARCHASC");
		} else if ("ORDERPOPULAR".equals(condition)) {
			boardComboVO.setCondition("SELECTALLCOMBOBOARDSEARCHPOPULAR");
		}

		// 시작 인덱스 계산
		int startIndex = ((page - 1) * count) + 1;
		// 마지막 인덱스 계산
		int endRow = page * count;
		// 시작 인덱스
		boardComboVO.setBoardComboIndex(startIndex);
		// 페이지당 보여줄 데이터 수
		boardComboVO.setBoardComboContentCount(endRow);

		// 데이터 추출
		List<BoardComboVO> results = boardComboService.getAll(boardComboVO);

		Map<String, Object> boardComboData = new HashMap<>();
		boardComboData.put("boardComboDatas", results);

		return ResponseEntity.ok(boardComboData);
	}

	// 꿀조합 게시글 정렬 기능
	@GetMapping("/boardCombo/order")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> comboBoardOrder(BoardComboVO boardComboVO,
			@RequestParam("boardOrderCondition") String condition, @RequestParam("boardPageNumber") int page,
			@RequestParam("boardContentCount") int count) {

		// 정렬 조건 설정
		if ("ORDERUPTODATE".equals(condition)) { // 최신순
			boardComboVO.setCondition("SELECTALLMEMBERCONTENTDESC");
		} else if ("ORDEROLD".equals(condition)) { // 오래된순
			boardComboVO.setCondition("SELECTALLMEMBERCONTENTASC");
		} else if ("ORDERPOPULAR".equals(condition)) { // 인기순
			boardComboVO.setCondition("SELECTALLMEMBERCONTENTPOPULAR");
		}

		// 시작 인덱스 계산
		int startIndex = ((page - 1) * count) + 1;
		// 마지막 인덱스 계산
		int endRow = page * count;
		// 시작 인덱스
		boardComboVO.setBoardComboIndex(startIndex);
		// 페이지당 보여줄 데이터 수
		boardComboVO.setBoardComboContentCount(endRow);

		System.out.println("DB보낼 시작 인덱스 : [" + boardComboVO.getBoardComboIndex() + "]");
		System.out.println("DB보낼 페이지당 보여줄 데이터 수 : [" + boardComboVO.getBoardComboContentCount() + "]");
		System.out.println("DB보낼 정렬 조건 : [" + boardComboVO.getCondition() + "]");
		List<BoardComboVO> resultList = boardComboService.getAll(boardComboVO);

		Map<String, Object> boardComboData = new HashMap<>();
		boardComboData.put("boardComboDatas", resultList);

		return ResponseEntity.ok(boardComboData);

	}

	// 꿀조합 게시글 등록 기능
	@PostMapping("/boardCombo/insert")
	@ResponseBody
	public ResponseEntity<Boolean> insertBoard(BoardComboVO boardComboVO, HttpSession session,
			@RequestParam("updateBoardTitle") String title, @RequestParam("updateBoardContent") String content) {

		// 회원 번호 받아오기
		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());

		// 회원 번호
		boardComboVO.setMemberNumber(memberNumber);
		// 제목
		boardComboVO.setBoardComboTitle(title);
		// 내용
		boardComboVO.setBoardComboContent(content);

		boolean result = boardComboService.insert(boardComboVO);
		return ResponseEntity.ok(result);
	}

	// 꿀조합 게시글 수정 기능
	@PostMapping("/boardCombo/update")
	@ResponseBody
	public ResponseEntity<Boolean> updateBoard(BoardComboVO boardComboVO, @RequestParam("boardComboNumber") long number,
			@RequestParam("boardComboTitle") String title, @RequestParam("boardComboContent") String content) {

		// 게시글 번호
		boardComboVO.setBoardComboNumber(number);
		// 게시글 제목
		boardComboVO.setBoardComboTitle(title);
		// 게시글 내용
		boardComboVO.setBoardComboContent(content);

		boolean result = boardComboService.update(boardComboVO);

		return ResponseEntity.ok(result);
	}

	// 꿀조합 게시글 삭제 기능
	@PostMapping("/boardCombo/delete")
	@ResponseBody
	public ResponseEntity<Boolean> deleteBoard(BoardComboVO boardComboVO,
			@RequestParam("boardComboNumber") long number) {

		// 게시글 번호
		boardComboVO.setBoardComboNumber(number);

		boolean result = boardComboService.delete(boardComboVO);

		return ResponseEntity.ok(result);
	}
}
