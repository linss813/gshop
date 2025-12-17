package com.linss.gshop.entity.dto;

import com.linss.gshop.entity.Goods;
import com.linss.gshop.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {
    private Integer reviewId;
    private User user;    // 已过滤敏感信息的 User 对象
    private Goods goods;  // 已过滤的 Goods 对象
    private String rating;
    private String comment;
    private Date createdAt;
}
