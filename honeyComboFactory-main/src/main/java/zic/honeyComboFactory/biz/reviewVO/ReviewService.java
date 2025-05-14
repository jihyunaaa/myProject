package zic.honeyComboFactory.biz.reviewVO;

import java.util.List;

public interface ReviewService { // 리뷰 인터페이스
	List<ReviewVO> getAll(ReviewVO reviewVO); // SelectAll()
	ReviewVO getOne(ReviewVO reviewVO); // SelectOne()
	
	boolean insert(ReviewVO reviewVO);
	boolean update(ReviewVO reviewVO);
	boolean delete(ReviewVO reviewVO);
}
