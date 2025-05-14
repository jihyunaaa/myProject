package zic.honeyComboFactory.view.review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import zic.honeyComboFactory.biz.reviewVO.ReviewService;
import zic.honeyComboFactory.biz.reviewVO.ReviewVO;

@Controller
public class ReviewController { // 리뷰 컨트롤러
	@Autowired
	private ReviewService reviewService;

	// 리뷰 불러오기 기능
	@PostMapping("/review/load")
	@ResponseBody
	public Map<String, Object> loadMoreReview(@RequestParam long productNumber, @RequestParam int reviewPageNumber, ReviewVO reviewVO) {
		System.out.println("리뷰 정보 불러오기 컨트롤러 진입");
		
		System.out.println("리뷰 정보 불러올 상품 번호 [" + productNumber + "]");
		System.out.println("리뷰 정보 더보기 횟수 [" + reviewPageNumber + "]");
		System.out.println("리뷰 정보 보여줄 데이터 수 [" + reviewVO.getReviewContentCount() + "]");

		int contentCount = reviewVO.getReviewContentCount();
		// 시작 인덱스 계산
		int startIndex = ((reviewPageNumber - 1) * contentCount)+1;
		// 마지막 인덱스 계산
		int endRow = reviewPageNumber * contentCount;
		
		System.out.println("리뷰 정보 불러올 시작 인덱스 번호 [" + startIndex + "]");
		System.out.println("리뷰 정보 불러올 마지막 인덱스 번호 [" + endRow + "]");

		// 리뷰 조회를 위한 DTO 설정
		reviewVO.setCondition("SELECTALLREVIEWONEPRODUCT");
		reviewVO.setReviewContentCount(endRow);
		if (30000 <= productNumber) { // 꿀조합 상품이라면
			System.out.println("꿀조합 상품 리뷰 불러오기");
			reviewVO.setProductComboNumber(productNumber);
		} else { // 개별 상품이라면
			System.out.println("개별상품 리뷰 불러오기");
			reviewVO.setProductSingleNumber(productNumber);
		}
		reviewVO.setReviewIndex(startIndex);

		// 리뷰 목록 가져오기
		List<ReviewVO> reviewList = this.reviewService.getAll(reviewVO);
		System.out.println("불러온 리뷰 목록 [" + reviewList + "]");

		List<Map<String, Object>> reviewArray = new ArrayList<>();
		// 불러온 리뷰 목록 담기
		for (ReviewVO review : reviewList) {
			Map<String, Object> reviewData = new HashMap<>();
			reviewData.put("reviewNumber", review.getReviewNumber());
			reviewData.put("reviewScore", review.getReviewScore());
			reviewData.put("reviewRegisterDate", review.getReviewRegisterDate().toString());
			reviewData.put("reviewContent", review.getReviewContent());
			reviewData.put("memberNumber", review.getMemberNumber());
			reviewData.put("memberName", review.getMemberName());
			reviewData.put("memberIsWithdraw", review.isMemberIsWithdraw());
			reviewData.put("totalCountNumber", review.getTotalReviewCount());
			reviewArray.add(reviewData);
		}
		System.out.println("반환할 불러온 리뷰 목록 : ["+reviewArray+"]");

		// 반환 응답 세팅
		Map<String, Object> response = new HashMap<>();
		response.put("reviewDatas", reviewArray);

		return response;
	}

	// 리뷰 등록 기능
	@PostMapping("/review/insert")
	@ResponseBody
	public Map<String, Object> insertReview(@RequestParam long productNumber, ReviewVO reviewVO, HttpSession session) {
		System.out.println("리뷰 작성 컨트롤러 진입");

		Map<String, Object> reviewData = new HashMap<>();
		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());

		System.out.println("리뷰 저장할 상품 번호 : [" + productNumber + "]");
		System.out.println("리뷰 저장할 회원 번호 : [" + memberNumber + "]");
		System.out.println("리뷰 저장할 별점 수 : [" + reviewVO.getReviewScore() + "]");
		System.out.println("리뷰 저장할 내용 : [" + reviewVO.getReviewContent() + "]");

		// 중복 검사용 파서 세팅
		reviewVO.setCondition("SELECTONEREVIEW");
		reviewVO.setMemberNumber(memberNumber);
		if (30000 <= productNumber) { // 꿀조합 상품이라면
			System.out.println("꿀조합 상품 리뷰 작성");
			reviewVO.setProductComboNumber(productNumber);
		} else { // 개별 상품이라면
			System.out.println("개별상품 리뷰 작성");
			reviewVO.setProductSingleNumber(productNumber);
		}

		// 중복 리뷰 작성 검사
		ReviewVO isReview = this.reviewService.getOne(reviewVO);
		System.out.println("받은 중복 리뷰 작성 여부 : [" + (isReview != null) + "]");

		// 중복 리뷰 작성이라면
		if (isReview != null) {
			System.out.println("리뷰 중복 작성 '0' 반환");
			reviewData.put("reviewData", 0);
			return reviewData;
		}

		// 리뷰 저장 성공 여부
		boolean flag = this.reviewService.insert(reviewVO);
		System.out.println("리뷰 저장 성공 여부 [" + flag + "]");

		if (!flag) { // 리뷰 저장 실패라면
			System.out.println("리뷰 저장 실패 '1' 반환");
			reviewData.put("reviewData", 1);
			return reviewData;
		}

		// 리뷰 저장 정보 받아오기
		reviewVO = this.reviewService.getOne(reviewVO);
		System.out.println("받은 리뷰 저장 정보 : [" + reviewVO + "]");

		reviewData.put("reviewNumber", reviewVO.getReviewNumber());
		reviewData.put("reviewScore", reviewVO.getReviewScore());
		reviewData.put("reviewRegisterDate", reviewVO.getReviewRegisterDate().toString());
		reviewData.put("reviewContent", reviewVO.getReviewContent());
		reviewData.put("memberName", reviewVO.getMemberName());
		System.out.println("반환할 리뷰 작성 데이터 : [" + reviewData + "]");

		return reviewData;
	}

	// 리뷰 수정 기능
	@PostMapping("/review/update")
	@ResponseBody
	public boolean updateReview(ReviewVO reviewVO) {
		System.out.println("리뷰 수정 컨트롤러 진입");
		System.out.println("수정할 리뷰 번호 : [" + reviewVO.getReviewNumber() + "]");
		System.out.println("수정할 리뷰 별점 수 : [" + reviewVO.getReviewScore() + "]");
		System.out.println("수정할 리뷰 내용 : [" + reviewVO.getReviewContent() + "]");

		// 리뷰 수정 성공 여부
		boolean flag = this.reviewService.update(reviewVO);
		System.out.println("리뷰 수정 성공 여부 : [" + flag + "]");

		return flag;
	}

	// 리뷰 삭제 기능
	@PostMapping("/review/delete")
	@ResponseBody
	public boolean deleteReview(ReviewVO reviewVO) {
		System.out.println("리뷰 삭제 컨트롤러 진입");
		System.out.println("받은 삭제할 리뷰 번호 : [" + reviewVO.getReviewNumber() + "]");

		// 리뷰 삭제 성공 여부
		boolean flag = this.reviewService.delete(reviewVO);
		System.out.println("리뷰 삭제 성공 여부 : [" + flag + "]");

		return flag;
	}
}
