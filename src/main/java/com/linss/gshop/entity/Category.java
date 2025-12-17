package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Integer cid;

    @Column(name = "cname")
    private String cname;

    @Column(name = "tid")
    private Integer tid;

    // goods_type表
    @Column(name = "tname")
    private String tname;

}
