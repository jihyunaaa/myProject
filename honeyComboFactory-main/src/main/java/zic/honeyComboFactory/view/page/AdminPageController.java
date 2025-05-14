package zic.honeyComboFactory.view.page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.biz.productComboVO.ProductComboService;
import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;

@Controller
public class AdminPageController { // 관리자 페이지 이동 컨트롤러

	@Autowired
	private MemberService memberService;
	@Autowired
	private ProductSingleService productSingleService;
	@Autowired
	private ProductComboService productComboService;
	@Autowired
	private BoardComboService boardComboService;

	// 회원 관리 이동
	@GetMapping("/manageMember.do")
	public String manageMemberPage(MemberVO memberVO, Model model, HttpSession session) {
		System.out.println("관리자 회원관리 페이지 이동");
		// 관리자 외 페이지 접근 시
		if (!isAdmin(session)) {
			System.out.println("관리자 외 회원관리 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		memberVO.setCondition("");
		memberVO.setMemberIndex(1);
		memberVO.setMemberContentCount(31);
		List<MemberVO> memberDatas = this.memberService.getAll(memberVO);

		System.out.println("관리자 회원관리 페이지 보낼 회원 목록 : [" + memberDatas + "]");
		model.addAttribute("memberDatas", memberDatas);

		return "admin/manageMember";
	}

	// CU 상품 관리 이동
	@GetMapping("/manageCUProduct.do")
	public String manageCUProductPage(ProductSingleVO productSingleVO, Model model, HttpSession session) {
		System.out.println("관리자 CU 상품관리 페이지 이동");
		// 관리자 외 페이지 접근 시
		if (!isAdmin(session)) {
			System.out.println("관리자 외 CU 상품관리 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		productSingleVO.setCondition("SELECTALLPOPULAR");
		productSingleVO.setProductSingleStore("CU");
		productSingleVO.setStartIndex(1);
		productSingleVO.setLimitNumber(31);
		List<ProductSingleVO> cuProductList = this.productSingleService.getAll(productSingleVO);

		System.out.println("관리자 CU 상품관리 페이지 보낼 상품 목록 : [" + cuProductList + "]");
		model.addAttribute("cuProductList", cuProductList);

		return "admin/manageCuProduct";
	}

	// GS25 상품 관리 이동
	@GetMapping("/manageGSProduct.do")
	public String manageGSProductPage(ProductSingleVO productSingleVO, Model model, HttpSession session) {
		System.out.println("관리자 GS25 상품관리 페이지 이동");
		// 관리자 외 페이지 접근 시
		if (!isAdmin(session)) {
			System.out.println("관리자 외 GS25 상품관리 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		productSingleVO.setCondition("SELECTALLPOPULAR");
		productSingleVO.setProductSingleStore("GS25");
		productSingleVO.setStartIndex(1);
		productSingleVO.setLimitNumber(31);
		List<ProductSingleVO> gsProductList = this.productSingleService.getAll(productSingleVO);

		System.out.println("관리자 GS25 상품관리 페이지 보낼 상품 목록 : [" + gsProductList + "]");
		model.addAttribute("gsProductList", gsProductList);

		return "admin/manageGsProduct";
	}

	// 꿀조합 상품 관리 이동
	@GetMapping("/manageComboProduct.do")
	public String manageComboProductPage(ProductComboVO productComboVO, Model model, HttpSession session) {
		System.out.println("관리자 꿀조합 상품관리 페이지 이동");
		// 관리자 외 페이지 접근 시
		if (!isAdmin(session)) {
			System.out.println("관리자 외 꿀조합 상품관리 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		productComboVO.setCondition("SELECTALLPOPULAR");
		productComboVO.setProductComboIndex(1);
		productComboVO.setProductComboContentCount(31);
		List<ProductComboVO> comboProductList = this.productComboService.getAll(productComboVO);

		System.out.println("관리자 꿀조합 상품관리 페이지 보낼 상품 목록 : [" + comboProductList + "]");
		model.addAttribute("comboProductList", comboProductList);

		return "admin/manageComboProduct";
	}

	// 꿀조합 게시판 관리 이동
	@GetMapping("/manageBoardCombo.do")
	public String manageBoardComboPage(BoardComboVO boardComboVO, Model model, HttpSession session) {
		System.out.println("관리자 꿀조합 게시판 관리 페이지 이동");
		// 관리자 외 페이지 접근 시
		if (!isAdmin(session)) {
			System.out.println("관리자 외 꿀조합 게시판 관리 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		boardComboVO.setCondition("SELECTALLADMINCONTENT");
		boardComboVO.setBoardComboIndex(1);
		boardComboVO.setBoardComboContentCount(31);
		List<BoardComboVO> boardComboDatas = this.boardComboService.getAll(boardComboVO);

		System.out.println("관리자 꿀조합 상품관리 페이지 보낼 상품 목록 : [" + boardComboDatas + "]");
		model.addAttribute("boardComboDatas", boardComboDatas);

		return "admin/manageBoardCombo";
	}

	// 성능 최적화 메서드
	// 세션에 저장된 회원(관리자)정보 존재여부 반환 기능
	private boolean isAdmin(HttpSession session) {
		// 관리자 여부 변수화
		Boolean isAdmin = (Boolean) session.getAttribute("memberIsAdmin");

		return session.getAttribute("loginedMemberNumber") != null && Boolean.TRUE.equals(isAdmin);
	}
}
