package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 管理员实体类
 * 该类代表系统中的管理员用户，存储了管理员的基本信息如用户名、密码等
 * 使用@Data注解自动生成getter和setter方法，简化编码
 * 使用@Entity注解声明这是一个JPA实体类
 * 使用@Table注解指定该实体类对应数据库中的"admin"表
 */
@Data
@Entity
@Table(name = "admin")
public class AdminUser {
    /**
     * 管理员ID
     * 使用@Id注解标识该字段为主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     * 存储管理员的用户名
     */
    private String username;

    /**
     * 密码
     * 存储管理员的密码
     */
    private String password;

    /**
     * 创建时间
     * 使用@Column注解指定数据库表中的列名为"create_at"
     * 存储管理员账户的创建时间
     */
    @Column(name = "create_at")
    private Date createAt;

    /**
     * 最后登录时间
     * 使用@Column注解指定数据库表中的列名为"last_login"
     * 记录管理员最后一次登录的时间
     */
    @Column(name = "last_login")
    private Date lastLogin;

    /**
     * 使用人
     * 记录人员
     */
    private String user;

    /**
     * 管理员类型
     * 存储管理员的类型，可能用于区分不同权限级别的管理员
     */
    private Byte type;

}



