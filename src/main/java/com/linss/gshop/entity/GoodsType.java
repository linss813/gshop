package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "goods_type")
public class GoodsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    private String tname;



}
