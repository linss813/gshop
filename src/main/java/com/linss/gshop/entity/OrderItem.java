package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Data
public class OrderItem {

    private Integer itemId;

    private Integer orderId;

    private Integer gid;

    private Integer quantity;

    private BigDecimal price;

    private String title;


}
