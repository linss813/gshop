package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "cart_item")
public class CartItem {

    private Integer itemId;

    private Integer cartId;

    private Integer gid;

    private String title;

    private BigDecimal price;

    private Integer quantity;


}
