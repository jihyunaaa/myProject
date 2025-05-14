package zic.honeyComboFactory.biz.boardComboVO;

import java.sql.Date;

public class BoardComboVO { // BoardComboDTO
	// 테이블 컬럼
	private long boardComboNumber; // 글 번호(PK)
	private String boardComboTitle; // 글 제목
	private String boardComboContent; // 글 내용
	private Date boardComboRegisterDate; // 글 작성일
	private long boardComboViewCount; // 글 조회수
	private long memberNumber; // 회원 번호(FK)
	// 추가 멤버변수
	private long totalCountNumber; // 총 데이터 수
	private String condition; // 조건
	private String searchKeyword; // 검색
	private String memberName; // 회원 이름
	private boolean memberIsWithdraw; // 회원 탈퇴 여부
	private long boardComboLikedCount; // 좋아요 수
	private boolean memberIsAdmin; // 관리자 여부
	private int boardComboIndex; // 시작 인덱스
	private int boardComboContentCount; // 한 페이지에 보여줄 데이터 수

	// getter, setter
	public long getBoardComboNumber() {
		return boardComboNumber;
	}

	public void setBoardComboNumber(long boardComboNumber) {
		this.boardComboNumber = boardComboNumber;
	}

	public String getBoardComboTitle() {
		return boardComboTitle;
	}

	public void setBoardComboTitle(String boardComboTitle) {
		this.boardComboTitle = boardComboTitle;
	}

	public String getBoardComboContent() {
		return boardComboContent;
	}

	public void setBoardComboContent(String boardComboContent) {
		this.boardComboContent = boardComboContent;
	}

	public Date getBoardComboRegisterDate() {
		return boardComboRegisterDate;
	}

	public void setBoardComboRegisterDate(Date boardComboRegisterDate) {
		this.boardComboRegisterDate = boardComboRegisterDate;
	}

	public long getBoardComboViewCount() {
		return boardComboViewCount;
	}

	public void setBoardComboViewCount(long boardComboViewCount) {
		this.boardComboViewCount = boardComboViewCount;
	}

	public long getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}

	public long getTotalCountNumber() {
		return totalCountNumber;
	}

	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public boolean isMemberIsWithdraw() {
		return memberIsWithdraw;
	}

	public void setMemberIsWithdraw(boolean memberIsWithdraw) {
		this.memberIsWithdraw = memberIsWithdraw;
	}

	public long getBoardComboLikedCount() {
		return boardComboLikedCount;
	}

	public void setBoardComboLikedCount(long boardComboLikedCount) {
		this.boardComboLikedCount = boardComboLikedCount;
	}

	public boolean isMemberIsAdmin() {
		return memberIsAdmin;
	}

	public void setMemberIsAdmin(boolean memberIsAdmin) {
		this.memberIsAdmin = memberIsAdmin;
	}

	public int getBoardComboIndex() {
		return boardComboIndex;
	}

	public void setBoardComboIndex(int boardComboIndex) {
		this.boardComboIndex = boardComboIndex;
	}

	public int getBoardComboContentCount() {
		return boardComboContentCount;
	}

	public void setBoardComboContentCount(int boardComboContentCount) {
		this.boardComboContentCount = boardComboContentCount;
	}

	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "BoardComboVO [boardComboNumber=" + boardComboNumber + ", boardComboTitle=" + boardComboTitle
				+ ", boardComboContent=" + boardComboContent + ", boardComboRegisterDate=" + boardComboRegisterDate
				+ ", boardComboViewCount=" + boardComboViewCount + ", memberNumber=" + memberNumber
				+ ", totalCountNumber=" + totalCountNumber + ", condition=" + condition + ", searchKeyword="
				+ searchKeyword + ", memberName=" + memberName + ", memberIsWithdraw=" + memberIsWithdraw
				+ ", boardComboLikedCount=" + boardComboLikedCount + ", memberIsAdmin=" + memberIsAdmin
				+ ", boardComboIndex=" + boardComboIndex + ", boardComboContentCount=" + boardComboContentCount + "]";
	}
}