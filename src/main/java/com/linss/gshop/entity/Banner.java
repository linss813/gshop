package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
public class Banner {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bid;
    // 图片
    private String image;
    // 跳转链接
    private String jump;
    // 排序
    private Integer sort;
    // 状态
    private Boolean status;
    // 创建时间
    @Column(name = "created_at")
    private Date createdAt;
}
