package com.linss.gshop.controller;


import com.linss.gshop.entity.Review;
import com.linss.gshop.service.ReviewService;
import com.linss.gshop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 默认加载所有评论
    @GetMapping("/getAllReviews")
    public Result getAllReviews(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(reviewService.getAllReviews(pageNum, pageSize));
    }

    // 新增按商品ID查询评论的接口
    @GetMapping("/getReviewsByGoodsId")
    public Result getReviewsByGoodsId(
        @RequestParam Integer gid,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(reviewService.getReviewsByGoodsId(gid, pageNum, pageSize));
    }

    // 新增按评论ID查询评论的接口
    @GetMapping("/getReviewById")
    public Result getReviewById(@RequestParam Integer reviewId) {
        return Result.success(reviewService.getReviewById(reviewId));
    }

    // 新增插入评论的接口
    @PostMapping("/insertReview")
    public Result insertReview(@RequestBody Review review) {
        int result = reviewService.insertReview(review);
        return result > 0 ? Result.success("评论成功") : Result.error("评论失败");
    }

    // 新增更新评论的接口
    @PutMapping("/updateReview")
    public Result updateReview(@RequestBody Review review) {
        int result = reviewService.updateReview(review);
        return result > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    // 新增删除评论的接口
    @DeleteMapping("/deleteReview")
    public Result deleteReview(@RequestParam Integer reviewId) {
        int result = reviewService.deleteReview(reviewId);
        return result > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 新增统计评论数量的接口
    @GetMapping("/countReviews")
    public Result countReviews() {
        return Result.success(reviewService.countReviews());
    }

    @GetMapping("/searchReviewsByContent")
    public Result searchReviewsByContent(
        @RequestParam String content,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(reviewService.searchReviewsByContent(content, pageNum, pageSize));
    }

    @PostMapping("/batchApprove")
    public Result batchApprove(@RequestBody List<Integer> reviewIds) {
        int result = reviewService.batchApprove(reviewIds);
        return result > 0 ? Result.success("批量审核通过成功") : Result.error("批量审核通过失败");
    }

    @PostMapping("/batchReject")
    public Result batchReject(@RequestBody List<Integer> reviewIds) {
        int result = reviewService.batchReject(reviewIds);
        return result > 0 ? Result.success("批量撤销审核成功") : Result.error("批量撤销审核失败");
    }

    @PostMapping("/deleteReviews")
    public Result deleteReviews(@RequestBody List<Integer> reviewIds) {
        int result = reviewService.deleteReviews(reviewIds);
        return result > 0 ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }
}
