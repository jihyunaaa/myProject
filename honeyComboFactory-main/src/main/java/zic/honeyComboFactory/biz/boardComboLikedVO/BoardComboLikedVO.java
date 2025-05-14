package zic.honeyComboFactory.biz.boardComboLikedVO;

public class BoardComboLikedVO { // BoardComboLikedDTO
	// 테이블 컬럼
	private long boardComboLikedNumber;
	private long memberNumber;
	private long boardComboNumber;
	// 추가 멤버변수
	private String memberName;
	private long totalCountNumber;
	private String boardComboTitle;
	private int boardComboLikedIndex;
	private int boardComboLikedContentCount;
	
	// getter, setter
	public long getBoardComboLikedNumber() {
		return boardComboLikedNumber;
	}
	public void setBoardComboLikedNumber(long boardComboLikedNumber) {
		this.boardComboLikedNumber = boardComboLikedNumber;
	}
	public long getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}
	public long getBoardComboNumber() {
		return boardComboNumber;
	}
	public void setBoardComboNumber(long boardComboNumber) {
		this.boardComboNumber = boardComboNumber;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public long getTotalCountNumber() {
		return totalCountNumber;
	}
	public void setTotalCountNumber(long totalCountNumber) {
		this.totalCountNumber = totalCountNumber;
	}
	public String getBoardComboTitle() {
		return boardComboTitle;
	}
	public void setBoardComboTitle(String boardComboTitle) {
		this.boardComboTitle = boardComboTitle;
	}
	public int getBoardComboLikedIndex() {
		return boardComboLikedIndex;
	}
	public void setBoardComboLikedIndex(int boardComboLikedIndex) {
		this.boardComboLikedIndex = boardComboLikedIndex;
	}
	public int getBoardComboLikedContentCount() {
		return boardComboLikedContentCount;
	}
	public void setBoardComboLikedContentCount(int boardComboLikedContentCount) {
		this.boardComboLikedContentCount = boardComboLikedContentCount;
	}
	
	// 로그용 toString() 오버라이딩
	@Override
	public String toString() {
		return "BoardComboLikedVO [boardComboLikedNumber=" + boardComboLikedNumber + ", memberNumber=" + memberNumber
				+ ", boardComboNumber=" + boardComboNumber + ", memberName=" + memberName + ", totalCountNumber="
				+ totalCountNumber + ", boardComboTitle=" + boardComboTitle + ", boardComboLikedIndex="
				+ boardComboLikedIndex + ", boardComboLikedContentCount=" + boardComboLikedContentCount + "]";
	}
}