package com.linss.gshop.service;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {

    PageInfo<Review> getAllReviews(int pageNum, int pageSize);

    PageInfo<Review> searchReviews(
            Integer gid,
            String content,
            int pageNum,
            int pageSize
    );

    PageInfo<Review> getReviewsByGoodsId(Integer gid, int pageNum, int pageSize);

    Review getReviewById(Integer reviewId);

    int insertReview(Review review);

    int updateReview(Review review);

    int deleteReview(Integer reviewId);

    int countReviews();

    // 新增批量通过审核的方法
    int batchApprove(List<Integer> reviewIds);

    // 新增批量撤销审核的方法
    int batchReject(List<Integer> reviewIds);

    // 修改批量删除方法，支持传入多个 reviewId
    int deleteReviews(List<Integer> reviewIds);

    PageInfo<Review> searchReviewsByContent(String content, int pageNum, int pageSize);

}
