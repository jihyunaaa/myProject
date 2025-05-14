package zic.honeyComboFactory.biz.review.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zic.honeyComboFactory.biz.reviewVO.ReviewService;
import zic.honeyComboFactory.biz.reviewVO.ReviewVO;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService { // 리뷰 서비스
	@Autowired // ReviewDAO 객체가 메모리에 new 되어있어야 가능
	private OracleReviewDAOMybatis reviewDAO; // Oracle DB
	// private MySQLReviewDAO reviewDAO; // MySql DB

	@Override
	public List<ReviewVO> getAll(ReviewVO reviewVO) {
		return this.reviewDAO.getAll(reviewVO);
	}
	
	@Override
	public ReviewVO getOne(ReviewVO reviewVO) {
		return this.reviewDAO.getOne(reviewVO);
	}
	
	@Override
	public boolean insert(ReviewVO reviewVO) {
		return this.reviewDAO.insert(reviewVO);
	}

	@Override
	public boolean update(ReviewVO reviewVO) {
		return this.reviewDAO.update(reviewVO);
	}

	@Override
	public boolean delete(ReviewVO reviewVO) {
		return this.reviewDAO.delete(reviewVO);
	}
}
