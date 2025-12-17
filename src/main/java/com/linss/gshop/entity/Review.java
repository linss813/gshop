package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class Review {

    @Column(name = "review_id")
    private Integer reviewId;

    private Integer uid;

    private String username;

    private Integer gid;

    private String title;

    private Integer rating;

    private String comment;

    private boolean status = true;

    private Date createdAt;



}
