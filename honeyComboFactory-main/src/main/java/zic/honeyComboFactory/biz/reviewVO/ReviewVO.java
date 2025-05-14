package zic.honeyComboFactory.biz.reviewVO;

import java.sql.Date;

public class ReviewVO { // ReviewDTO
	// 테이블 컬럼
	private long reviewNumber;
	private int reviewScore;
	private Date reviewRegisterDate;
	private String reviewContent;
	private long memberNumber;
	private long productSingleNumber;
	private long productComboNumber;
	// 추가 멤버변수
	private String memberName;
	private long totalReviewCount; // 한 상품에 달린 리뷰 총 개수
	private boolean memberIsWithdraw;
	private String condition;
	private int reviewIndex;
	private int reviewContentCount;

	// getter, setter
	public long getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(long reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public int getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(int reviewScore) {
		this.reviewScore = reviewScore;
	}

	public Date getReviewRegisterDate() {
		return reviewRegisterDate;
	}

	public void setReviewRegisterDate(Date reviewRegisterDate) {
		this.reviewRegisterDate = reviewRegisterDate;
	}

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public long getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}

	public long getProductSingleNumber() {
		return productSingleNumber;
	}

	public void setProductSingleNumber(long productSingleNumber) {
		this.productSingleNumber = productSingleNumber;
	}

	public long getProductComboNumber() {
		return productComboNumber;
	}

	public void setProductComboNumber(long productComboNumber) {
		this.productComboNumber = productComboNumber;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public long getTotalReviewCount() {
		return totalReviewCount;
	}

	public void setTotalReviewCount(long totalReviewCount) {
		this.totalReviewCount = totalReviewCount;
	}

	public boolean isMemberIsWithdraw() {
		return memberIsWithdraw;
	}

	public void setMemberIsWithdraw(boolean memberIsWithdraw) {
		this.memberIsWithdraw = memberIsWithdraw;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getReviewIndex() {
		return reviewIndex;
	}

	public void setReviewIndex(int reviewIndex) {
		this.reviewIndex = reviewIndex;
	}

	public int getReviewContentCount() {
		return reviewContentCount;
	}

	public void setReviewContentCount(int reviewContentCount) {
		this.reviewContentCount = reviewContentCount;
	}

	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "ReviewVO [reviewNumber=" + reviewNumber + ", reviewScore=" + reviewScore + ", reviewRegisterDate="
				+ reviewRegisterDate + ", reviewContent=" + reviewContent + ", memberNumber=" + memberNumber
				+ ", productSingleNumber=" + productSingleNumber + ", productComboNumber=" + productComboNumber
				+ ", memberName=" + memberName + ", totalReviewCount=" + totalReviewCount + ", memberIsWithdraw="
				+ memberIsWithdraw + ", condition=" + condition + ", reviewIndex=" + reviewIndex
				+ ", reviewContentCount=" + reviewContentCount + "]";
	}
}