package com.linss.gshop.mapper;

import com.linss.gshop.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> getAllReviews();

    // 新增按商品ID查询评论的方法
    List<Review> getReviewsByGid(Integer gid);

    // 新增按评论ID查询评论的方法
    Review getReviewById(@Param("reviewId") Integer reviewId);

    // 新增插入评论的方法
    int insertReview(Review review);

    // 新增更新评论的方法
    int updateReview(Review review);

    // 新增删除评论的方法
    int deleteReview(@Param("reviewId") Integer reviewId);

    // 新增统计评论数量的方法
    int countReviews();

    // 新增批量通过审核的 SQL 方法
    int batchApprove(@Param("reviewIds") List<Integer> reviewIds);

    // 新增批量撤销审核的 SQL 方法
    int batchReject(@Param("reviewIds") List<Integer> reviewIds);

    // 修改批量删除的 SQL 方法，支持传入多个 reviewId
    int deleteReviews(@Param("reviewIds") List<Integer> reviewIds);

    int updateReviewStatus(
            @Param("reviewIds") List<Integer> reviewIds,
            @Param("status") boolean status
    );
    List<Review> searchReviewsByContent(@Param("content") String content);

    List<Review> searchReviews(
            @Param("gid") Integer gid,
            @Param("content") String content
    );

    List<Review> getReviewsByGoodsId(Integer gid);
}
