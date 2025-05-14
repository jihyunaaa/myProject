package zic.honeyComboFactory.view.page;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboService;
import zic.honeyComboFactory.biz.boardComboVO.BoardComboVO;
import zic.honeyComboFactory.biz.memberVO.MemberService;
import zic.honeyComboFactory.biz.memberVO.MemberVO;
import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentService;
import zic.honeyComboFactory.biz.productComboComponentVO.ProductComboComponentVO;
import zic.honeyComboFactory.biz.productComboVO.ProductComboService;
import zic.honeyComboFactory.biz.productComboVO.ProductComboVO;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleService;
import zic.honeyComboFactory.biz.productSingleVO.ProductSingleVO;
import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailService;
import zic.honeyComboFactory.biz.purchaseDetailVO.PurchaseDetailVO;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseService;
import zic.honeyComboFactory.biz.purchaseVO.PurchaseVO;
import zic.honeyComboFactory.common.util.QrUtil;

@Controller
public class MemberPageController { // 회원 페이지 이동 컨트롤러

	@Autowired
	private MemberService memberService;
	@Autowired
	private BoardComboService boardComboService;
	@Autowired
	private ProductComboService productComboService;
	@Autowired
	private ProductSingleService productSingleService;
	@Autowired
	private ProductComboComponentService productComboComponentService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private PurchaseDetailService purchaseDetailService;

	// 메인 페이지 이동
	@GetMapping("/main.do")
	public String mainPage(ProductSingleVO productSingleVO, ProductComboVO productComboVO, BoardComboVO boardComboVO,
			Model model) {
		// 꿀조합 Top3 출력
		productComboVO.setCondition("SELECTALLPOPULAR");
		productComboVO.setProductComboIndex(1);
		productComboVO.setProductComboContentCount(3);

		List<ProductComboVO> comboList = this.productComboService.getAll(productComboVO);
		// 품절 시 이미지 경로 변경
		returnApplyProductComboSoldoutImgPathList(comboList);

		model.addAttribute("allStoreProductComboTop", comboList);

		System.out.println(comboList);
		System.out.println("메인 꿀조합 top3 로그");

		// CU 꿀조합 Top3 출력
		productComboVO.setCondition("SELECTALLPRICEDESC");
		productComboVO.setProductComboIndex(1);
		productComboVO.setProductComboContentCount(3);
		List<ProductComboVO> cuList = this.productComboService.getAll(productComboVO);
		// 품절 시 이미지 경로 변경
		returnApplyProductComboSoldoutImgPathList(cuList);

		model.addAttribute("CUStoreProductComboTop", cuList);

		System.out.println(cuList);
		System.out.println("메인 cu 꿀조합 top3 로그");

		// GS 꿀조합 Top3 출력
		productComboVO.setCondition("SELECTALLPRICEDESC");
		productComboVO.setProductComboIndex(1);
		productComboVO.setProductComboContentCount(3);
		List<ProductComboVO> gsList = this.productComboService.getAll(productComboVO);
		// 품절 시 이미지 경로 변경
		returnApplyProductComboSoldoutImgPathList(gsList);

		model.addAttribute("GSStoreProductComboTop", gsList);

		System.out.println(gsList);
		System.out.println("메인 gs25 꿀조합 top3 로그");

		// 꿀조합 게시판 글 출력
		boardComboVO.setCondition("SELECTALLMEMBERCONTENTPOPULAR");
		boardComboVO.setBoardComboIndex(1);
		boardComboVO.setBoardComboContentCount(3);
		List<BoardComboVO> boardComboList = this.boardComboService.getAll(boardComboVO);
		model.addAttribute("boardComboPopularTop", boardComboList);

		// MDPICK 상품 출력 (1개, 광고용)
		productComboVO.setCondition("SELECTONEADVERTISEMENT");
		productComboVO.setProductComboADNumber(34001);
		ProductComboVO mdPick = this.productComboService.getOne(productComboVO);
		mdPick.setProductComboImage("assets/img/comboProduct/34001AD.png");
		model.addAttribute("MDRecommendProductData", mdPick);

		return "client/main";
	}

	// 내정보 페이지 이동
	@GetMapping("/myInfo.do")
	public String myInfoPage(MemberVO memberVO, HttpSession session, Model model) { // 내정보 페이지 이동
		// 로그인 없이 페이지 접근 시
		if (!isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 없이 내정보 페이지 접근");
			// 로그인 페이지 이동
			return "redirect:/client/login";
		}

		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		System.out.println("마이페이지 이동할 회원 번호 [" + memberNumber + "]");

		// 회원번호 저장 후 일치하는 값 찾아서 request로 보내기
		memberVO.setMemberNumber(memberNumber);
		memberVO.setCondition("SELECTONEMYINFORMATION");
		memberVO = this.memberService.getOne(memberVO);
		System.out.println("회원 정보 로그 [" + memberVO + "]");

		model.addAttribute("myInfoData", memberVO);
		return "client/myInfo";
	}

	// 꿀조합 게시글 등록 페이지 이동
	@GetMapping("/updateBoard.do")
	public String updateBoardPage(HttpSession session) {
		// 로그인 없이 페이지 접근 시
		if (!isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 없이 꿀조합 게시글 등록 페이지 접근");
			// 로그인 페이지 이동
			return "redirect:/client/login";
		}

		return "client/updateBoard";
	}

	// 주문상세 페이지 이동
	@GetMapping("/purchaseDetail.do")
	public String purchaseDetailPage(PurchaseDetailVO purchaseDetailVO, PurchaseVO purchaseVO, Model model,
			HttpSession session) {
		// 로그인 없이 페이지 접근 시
		if (!isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 없이 주문상세 페이지 접근");
			// 로그인 페이지 이동
			return "redirect:/client/login";
		}

		// 주문 정보 출력
		System.out.println("주문 정보 가져올 purchaseVO : [" + purchaseVO + "]");
		purchaseVO = this.purchaseService.getOne(purchaseVO);

		model.addAttribute("purchaseData", purchaseVO);
		System.out.println("주문 정보 : [" + purchaseVO + "]");

		// 주문 상세정보 출력
		System.out.println("주문상세 정보 가져올 purchaseDetailVO : [" + purchaseDetailVO + "]");
		List<PurchaseDetailVO> purchaseProductDetailList = this.purchaseDetailService.getAll(purchaseDetailVO);
		// 주문정보 담을 리스트 생성
		List<Map<String, Object>> purchaseProductDetailDatas = new ArrayList<>();
		// 리스트 반복하여 map 객체에 담기
		for (PurchaseDetailVO detail : purchaseProductDetailList) {
			Map<String, Object> map = new HashMap<>();

			map.put("purchaseProductCount", detail.getPurchaseProductCount()); // 상품 개수
			// 개별 상품이라면
			if (detail.getProductComboNumber() == 0L) {
				map.put("purchaseProductName", detail.getProductSingleName()); // 이름
				map.put("purchaseProductPrice", detail.getProductSinglePrice()); // 가격
				map.put("purchaseProductDiscount", detail.getProductSingleDiscount()); // 할인율
				map.put("purchaseProductDiscountedPrice", detail.getProductSingleDiscountedPrice()); // 할인 가격
			} else { // 꿀조합 상품이라면
				map.put("purchaseProductName", detail.getProductComboName()); // 이름
				map.put("purchaseProductPrice", detail.getProductComboPrice()); // 가격
				map.put("purchaseProductDiscount", detail.getProductComboDiscount()); // 할인율
				map.put("purchaseProductDiscountedPrice", detail.getProductComboDiscountedPrice()); // 할인 가격
			}

			// 생성한 map을 결과 리스트에 추가
			purchaseProductDetailDatas.add(map);
		}

		// model에 담기
		model.addAttribute("purchaseProductDetailDatas", purchaseProductDetailDatas);
		System.out.println("페이지 보낼 주문내역 상세정보 : [" + purchaseProductDetailDatas + "]");

		// QR 코드에 담을 문자열 생성 (주문번호 기반)
		String qrText = "ORDER-" + purchaseVO.getPurchaseNumber();
		// QR 코드 Base64 이미지 생성(크기: 470x200)
		String qrBase64 = QrUtil.generateQRCodeBase64(qrText, 470, 200, Color.BLACK, new Color(247, 247, 247));

		// model에 담기
		model.addAttribute("qrBase64", qrBase64);
		System.out.println("페이지 보낼 QR코드 Base64 길이 : [" + qrBase64.length() + "]");

		return "client/purchaseDetail";
	}

	// 꿀조합 게시판 페이지 이동
	@GetMapping("/comboBoard.do")
	public String comboBoardPage(BoardComboVO boardComboVO, Model model) {
		boardComboVO.setCondition("SELECTALLADMINCONTENT");
		List<BoardComboVO> adminList = boardComboService.getAll(boardComboVO);
		model.addAttribute("boardComboAdminDatas", adminList);
		System.out.println("꿀조합 페이지 출력할 관리자 글 목록 : [" + adminList + "]");

		return "client/comboBoard";
	}

	// 꿀조합 게시글 상세정보 페이지 이동
	@GetMapping("/boardDetail.do")
	public String boardDetailPage(BoardComboVO boardComboVO, Model model) {
		boardComboVO = this.boardComboService.getOne(boardComboVO);
		System.out.println("게시글 상세페이지 보낼 값 : [" + boardComboVO + "]");

		String path = "redirect:comboBoard.do";

		if (boardComboVO != null) {
			model.addAttribute("boardComboDetailData", boardComboVO);
			path = "client/boardDetail";
		}
		return path;
	}

	// 로그인 페이지 이동
	@GetMapping("/login.do")
	public String loginPage(HttpSession session) {
		// 로그인 상태로 페이지 접근 시
		if (isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 상태로 로그인 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		return "client/login";
	}

	// 회원가입 페이지 이동
	@GetMapping("/join.do")
	public String joinPage(HttpSession session) {
		// 로그인 상태로 페이지 접근 시
		if (isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 상태로 회원가입 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		return "client/join";
	}

	// 계정찾기 페이지로 이동
	@GetMapping("/findAccount.do")
	public String findAccountPage(HttpSession session) {
		// 로그인 상태로 페이지 접근 시
		if (isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 상태로 계정찾기 페이지 접근");
			// 메인 페이지 이동
			return "redirect:/client/main";
		}

		return "client/findAccount";
	}

	// 장바구니 페이지 이동
	@GetMapping("cart.do")
	public String cartPage(Model model, HttpSession session) {
		// 로그인 없이 페이지 접근 시
		if (!isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 없이 장바구니 페이지 접근");
			// 로그인 페이지 이동
			return "redirect:/client/login";
		}

		final String soldoutImgLink = "assets/img/product/soldout.png";

		System.out.println("장바구니 페이지 이동 컨트롤러 진입");
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");
		if (session.getAttribute("loginedMemberNumber") == null) {
			return "redirect:login.do";
		}

		if (shoppingCart == null) {
			return "client/cart";
		}

		ArrayList<Map<String, Object>> cartList = new ArrayList<>();
		for (Map<String, Object> cartItem : shoppingCart) {
			int productNumber = (int) cartItem.get("productNumber");
			int cartProductCount = (int) cartItem.get("cartProductCount");
			boolean isComboProduct = (boolean) cartItem.get("isComboProduct");

			Map<String, Object> productMap = new HashMap<>();

			if (isComboProduct) {
				ProductComboVO productComboVO = new ProductComboVO();
				productComboVO.setCondition("SELECTONE");
				productComboVO.setProductComboNumber(productNumber);
				productComboVO = this.productComboService.getOne(productComboVO);

				// 품절 시 이미지 경로 변경
				int stock = productComboVO.getProductComboStock();
				if (stock <= 0) { // 품절 상품이라면
					productComboVO.setProductComboImage(soldoutImgLink);
				}

				productMap.put("cartProductName", productComboVO.getProductComboName());
				productMap.put("cartProductImage", productComboVO.getProductComboImage());
				productMap.put("cartProductNumber", productComboVO.getProductComboNumber());
				productMap.put("cartProductStock", productComboVO.getProductComboStock());
				productMap.put("cartProductPrice", productComboVO.getProductComboPrice());
				productMap.put("cartProductDiscount", productComboVO.getProductComboDiscount());
				productMap.put("cartProductDiscountedPrice", productComboVO.getProductComboDiscountedPrice());
				productMap.put("cartProductCount", cartProductCount);
			} else {
				ProductSingleVO productSingleVO = new ProductSingleVO();
				productSingleVO.setCondition("SELECTONE");
				productSingleVO.setProductSingleNumber(productNumber);
				productSingleVO = this.productSingleService.getOne(productSingleVO);

				// 품절 시 이미지 경로 변경
				int stock = productSingleVO.getProductSingleStock();
				if (stock <= 0) { // 품절 상품이라면
					productSingleVO.setProductSingleImage(soldoutImgLink);
				}

				productMap.put("cartProductName", productSingleVO.getProductSingleName());
				productMap.put("cartProductImage", productSingleVO.getProductSingleImage());
				productMap.put("cartProductNumber", productSingleVO.getProductSingleNumber());
				productMap.put("cartProductStock", productSingleVO.getProductSingleStock());
				productMap.put("cartProductPrice", productSingleVO.getProductSinglePrice());
				productMap.put("cartProductDiscount", productSingleVO.getProductSingleDiscount());
				productMap.put("cartProductDiscountedPrice", productSingleVO.getProductSingleDiscountedPrice());
				productMap.put("cartProductCount", cartProductCount);
			}
			cartList.add(productMap);
		}
		System.out.println("장바구니 페이지 보낼 정보 : [" + cartList + "]");

		model.addAttribute("cartProductDatas", cartList);
		return "client/cart";
	}

	// 꿀조합 상품 판매 페이지 이동
	@GetMapping("/comboProduct.do")
	public String comboProductPage(ProductComboVO productComboVO, Model model) {
		// MD Pick
		int mdPerPage = 3;
		int mdStartIndex = 1;

		productComboVO.setCondition("SELECTALLMD");
		System.out.println("정렬 조건 (MD) [" + productComboVO.getCondition() + "]");

		productComboVO.setProductComboIndex(mdStartIndex);
		productComboVO.setProductComboContentCount(mdPerPage);
		List<ProductComboVO> mdPickList = this.productComboService.getAll(productComboVO);
		// 품절 시 이미지 경로 변경
		returnApplyProductComboSoldoutImgPathList(mdPickList);

		model.addAttribute("MDproductComboTop", mdPickList);

		// BEST ISSUES
		int hotPerPage = 3;
		int hotStartIndex = 1;

		productComboVO.setCondition("SELECTALLHOTISSUE");
		System.out.println("정렬 조건 (HOT) [" + productComboVO.getCondition() + "]");

		productComboVO.setProductComboIndex(hotStartIndex);
		productComboVO.setProductComboContentCount(hotPerPage);
		List<ProductComboVO> hotIssueList = this.productComboService.getAll(productComboVO);
		// 품절 시 이미지 경로 변경
		returnApplyProductComboSoldoutImgPathList(hotIssueList);

		model.addAttribute("hotProductComboTop", hotIssueList);

		return "client/comboProduct";
	}

	// GS25 상품 판매 페이지 이동
	@GetMapping("/GSProduct.do")
	public String GSProductPage() {
		return "client/GSProduct";
	}

	// CU 상품 판매 페이지 이동
	@GetMapping("/CUProduct.do")
	public String CUProductPage() {
		return "client/CUProduct";
	}

	// 상품 상세 페이지 이동
	@GetMapping("/productDetail.do")
	public String productDetailPage(ProductComboComponentVO productComboComponentVO, ProductSingleVO productSingleVO,
			ProductComboVO productComboVO, Model model, HttpSession session) {
		final String soldoutImgLink = "assets/img/product/soldout.png";

		long productComboNumber = productComboVO.getProductComboNumber();
		long productSingleNumber = productSingleVO.getProductSingleNumber();
		System.out.println("상품상세정보 꿀조합상품 번호 :  [" + productComboNumber + "]");
		System.out.println("상품상세정보 이동할 개별상품 번호 : [" + productSingleNumber + "]");

		// 조합상품 데이터 가져오기
		if (productComboNumber != 0) {
			productComboVO.setCondition("SELECTONE");
			productComboVO = this.productComboService.getOne(productComboVO);

			Map<String, Object> productComboData = new HashMap<>();

			// 품절상품 이미지 경로 변경
			int stock = productComboVO.getProductComboStock();
			if (stock <= 0) { // 품절이라면
				productComboVO.setProductComboImage(soldoutImgLink);
			}

			productComboData.put("productNumber", productComboVO.getProductComboNumber());
			productComboData.put("productName", productComboVO.getProductComboName());
			productComboData.put("productStock", productComboVO.getProductComboStock());
			productComboData.put("productImg", productComboVO.getProductComboImage());
			productComboData.put("productInformation", productComboVO.getProductComboInformation());
			productComboData.put("productCategory", productComboVO.getProductComboCategory());
			productComboData.put("productPrice", productComboVO.getProductComboPrice());
			productComboData.put("productDiscount", productComboVO.getProductComboDiscount());
			productComboData.put("productDiscountedPrice", productComboVO.getProductComboDiscountedPrice());
			productComboData.put("isComboProduct", true);

			// model에 직접 저장
			model.addAttribute("productData", productComboData);
			System.out.println("출력될 조합 상품: " + productComboNumber);
			System.out.println("JSP로 보낼 상품 정보 [" + productComboData + "]");

			// 조합상품 구성품 출력
			productComboComponentVO.setCondition("SELECTALLCOMPONENT");
			List<ProductComboComponentVO> productComboComponent = this.productComboComponentService
					.getAll(productComboComponentVO);

			model.addAttribute("productComboComponentDatas", productComboComponent);

			System.out.println("조합 상품 구성품 출력 로그");
			System.out.println(productComboComponent);

		}
		// 개별상품 데이터 가져오기
		else if (productSingleNumber != 0) {
			productSingleVO.setCondition("SELECTONE");
			productSingleVO = this.productSingleService.getOne(productSingleVO);
			System.out.println("개별 상품 반환: " + productSingleVO);

			Map<String, Object> productSingleData = new HashMap<>();

			// 품절상품 이미지 경로 변경
			int stock = productSingleVO.getProductSingleStock();
			if (stock <= 0) { // 품절이라면
				productSingleVO.setProductSingleImage(soldoutImgLink);
			}

			productSingleData.put("productNumber", productSingleVO.getProductSingleNumber());
			productSingleData.put("productName", productSingleVO.getProductSingleName());
			productSingleData.put("productImg", productSingleVO.getProductSingleImage());
			productSingleData.put("productStock", productSingleVO.getProductSingleStock());
			productSingleData.put("productInformation", productSingleVO.getProductSingleInformation());
			productSingleData.put("productCategory", productSingleVO.getProductSingleCategory());
			productSingleData.put("productPrice", productSingleVO.getProductSinglePrice());
			productSingleData.put("productDiscount", productSingleVO.getProductSingleDiscount());
			productSingleData.put("productDiscountedPrice", productSingleVO.getProductSingleDiscountedPrice());
			productSingleData.put("isComboProduct", false);

			// model에 직접 저장
			model.addAttribute("productData", productSingleData);
			System.out.println("출력될 개별 상품: " + productSingleNumber);
			System.out.println("JSP로 보낼 상품 정보 [" + productSingleData + "]");
		}

		// 리뷰 목록 하단 개별상품 판매 목록
		productSingleVO.setCondition("SELECTALLSTOCKDESC");
		productSingleVO.setStartIndex(1);
		productSingleVO.setLimitNumber(3);

		List<ProductSingleVO> productSingleList = this.productSingleService.getAll(productSingleVO);

		// 품절 시 이미지 경로 변경
		for (ProductSingleVO vo : productSingleList) {
			int stock = vo.getProductSingleStock();

			if (stock <= 0) { // 품절이라면
				vo.setProductSingleImage(soldoutImgLink);
			}
		}

		model.addAttribute("recommendProductDatas", productSingleList);

		System.out.println("추천 상품 출력 로그");
		System.out.println(productSingleList);

		// 상품 상세정보 페이지 이동 Action
		return "client/productDetail";
	}

	// 결제승인 페이지 이동
	@GetMapping("/kakaoPayApproval.do")
	public String kakaoPayApprovalPage(HttpSession session) {
		// 로그인 없이 페이지 접근 시
		if (!isSessionLoginedMemberNumber(session)) {
			System.out.println("로그인 없이 결제승인 페이지 접근");
			// 로그인 페이지 이동
			return "redirect:/client/login";
		}

		return "client/kakaoPayApproval";
	}

	// 에러 페이지 이동
	@GetMapping("/error.do")
	public String errorPage() {
		return "client/error";
	}
	
	// 실행 중 에러 발생 시 에러 페이지 이동
	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException ex, Model model) {
	    // 예외 로그 출력
	    ex.printStackTrace();

	    // 에러 페이지로 이동
	    return "error.do";
	}

	// 고도화 메서드
	// call by reference로 리스트 반환 안해도 됨
	// 꿀조합 상품
	// 품절 시 이미지 경로 변경 기능
	private void returnApplyProductComboSoldoutImgPathList(List<ProductComboVO> list) {
		final String soldoutImgLink = "assets/img/product/soldout.png";

		for (ProductComboVO vo : list) {
			int stock = vo.getProductComboStock();

			if (stock <= 0) { // 품절 상품이라면
				vo.setProductComboImage(soldoutImgLink);
			}
		}
	}

	// 성능 최적화 메서드
	// 세션에 저장된 회원번호 존재여부 반환 기능
	private boolean isSessionLoginedMemberNumber(HttpSession session) {
		return session.getAttribute("loginedMemberNumber") != null;
	}
}
