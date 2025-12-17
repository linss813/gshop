package com.linss.gshop.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * User类代表用户实体，映射到数据库中的'user'表
 * 使用Lombok的@Data注解自动生成getter和setter方法
 * 使用Spring Data的@EntityListeners注解启用自动审计功能，如自动更新时间戳
 */
@Data
@Entity
@Table(name = "user") // 显式指定表名
@EntityListeners(AuditingEntityListener.class) // 启用自动时间戳更新
public class User {

    /**
     * 用户ID，主键，由数据库自动生成
     * 映射到数据库表的'uid'列
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    /**
     * 用户名，用于登录和显示
     */
    private String username;

    /**
     * 用户密码，用于登录验证
     */
    private String password;

    /**
     * 用户头像的URL或路径
     */
    private String avatar;

    /**
     * 用户邮箱，可用于验证和找回密码
     */
    @Column(unique = true)
    private String email;

    /**
     * 用户电话号码，用于联系用户
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    private String role = "USER"; // 角色字段，默认值USER


    /**
     * 用户最后一次登录时间，自动更新以记录用户的最后登录时间
     * 映射到数据库表的'last'列
     */
    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date last;

    /**
     * 用户创建时间，自动记录用户创建的时间
     * 映射到数据库表的'created'列
     */
    @CreatedDate // 自动填充创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
}
