package com.linss.gshop.service.impl;

import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Review;
import com.linss.gshop.mapper.ReviewMapper;
import com.linss.gshop.service.GoodsService;
import com.linss.gshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private GoodsService goodsService;

    @Override
    public PageInfo<Review> getAllReviews(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Review> reviews = reviewMapper.getAllReviews();
        return PageInfo.of(reviews);
    }

    @Override
    public PageInfo<Review> searchReviews(
            Integer gid,
            String content,
            int pageNum,
            int pageSize
    ) {
        PageHelper.startPage(pageNum, pageSize);
        List<Review> reviews = reviewMapper.searchReviews(gid, content); // 参数正确
        return PageInfo.of(reviews);
    }

    @Override
    public PageInfo<Review> getReviewsByGoodsId(Integer gid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Review> reviews = reviewMapper.getReviewsByGoodsId(gid);
        return PageInfo.of(reviews);
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        return reviewMapper.getReviewById(reviewId);
    }

    @Override
    public int insertReview(Review review) {
        return reviewMapper.insertReview(review);
    }

    @Override
    public int updateReview(Review review) {
        return reviewMapper.updateReview(review);
    }

    @Override
    public int deleteReview(Integer reviewId) {
        return reviewMapper.deleteReview(reviewId);
    }

    @Override
    public int countReviews() {
        return reviewMapper.countReviews();
    }

    @Override
    public PageInfo<Review> searchReviewsByContent(String content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Review> reviews = reviewMapper.searchReviewsByContent(content);
        return PageInfo.of(reviews);
    }

    @Override
    public int batchApprove(List<Integer> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return 0;
        }
        return updateReviewStatus(reviewIds, true);
    }

    @Override
    public int batchReject(List<Integer> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return 0;
        }
        return updateReviewStatus(reviewIds, false);
    }

    @Override
    public int deleteReviews(List<Integer> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return 0;
        }
        return reviewMapper.deleteReviews(reviewIds);
    }

    // 新增通用方法，用于更新评论状态
    private int updateReviewStatus(List<Integer> reviewIds, boolean status) {
        return reviewMapper.updateReviewStatus(reviewIds, status);
    }
}
