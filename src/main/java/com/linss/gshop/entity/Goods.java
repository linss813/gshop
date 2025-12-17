package com.linss.gshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linss.gshop.util.StringListConverter;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gid;

    private String title;

    private Integer tid;

    private String tname;

    private Integer cid;

    private String cname;

    private String image;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal price;

    private Integer stock;

    @Column(name = "is_shelved")
    private Boolean isShelved;

    private String details;

    private Float rating;

    @Column(name = "created_at")
    private Date createdAt;

}
