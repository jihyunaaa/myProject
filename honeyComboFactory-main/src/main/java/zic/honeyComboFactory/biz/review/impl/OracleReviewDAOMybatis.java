package zic.honeyComboFactory.biz.review.impl;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zic.honeyComboFactory.biz.reviewVO.ReviewVO;

@Repository("oracleReviewDAO")
public class OracleReviewDAOMybatis { // 리뷰 기능 - Oracle DB

	@Autowired
	private SqlSessionTemplate mybatis;
	
	// getAll → R
	public List<ReviewVO> getAll(ReviewVO reviewVO) {
		// 전체 리뷰 목록
		if (reviewVO.getCondition().equals("SELECTALLREVIEWLIST")) {
			System.out.println("전체 리뷰 목록");
			return mybatis.selectList("ReviewDAO.SELECTALLREVIEWLIST", reviewVO);
		}
		// 상품에 대한 리뷰 목록 전체 출력
		else if (reviewVO.getCondition().equals("SELECTALLREVIEWONEPRODUCT")) {
			System.out.println("상품에 대한 리뷰 목록 전체 출력");
			return mybatis.selectList("ReviewDAO.SELECTALLREVIEWONEPRODUCT", reviewVO);
		}
		// 조건에 안맞다면 빈 배열 반환
		else {
			return new ArrayList<>();
		}
	}

	// getOne → R
	public ReviewVO getOne(ReviewVO reviewVO) {
		// 리뷰 상세 조회
		if (reviewVO.getCondition().equals("SELECTONEREVIEW")) {
			System.out.println("리뷰 상세 조회");
			return mybatis.selectOne("ReviewDAO.SELECTONEREVIEW", reviewVO);
		}
		// 조건에 만족하지 못하면 null 반환
		else {
			return null;
		}
	}

	// insert → C
	public boolean insert(ReviewVO reviewVO) {
		System.out.println("리뷰 등록");
		// 리뷰 등록
		int result = mybatis.insert("ReviewDAO.INSERTREVIEW", reviewVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// update → U
	public boolean update(ReviewVO reviewVO) {
		System.out.println("리뷰 수정");
		// 리뷰 수정
		int result = mybatis.update("ReviewDAO.UPDATEREVIEW", reviewVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	// delete → D
	public boolean delete(ReviewVO reviewVO) {
		System.out.println("리뷰 삭제");
		// 리뷰 삭제
		int result = mybatis.delete("ReviewDAO.DELETEREVIEW", reviewVO);
		if (result <= 0) {
			return false;
		}
		return true;
	}
}
