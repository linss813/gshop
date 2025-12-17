package com.linss.gshop.entity;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "cart")
public class Cart {

    private Integer cartId;

    private Integer userId;

    private List<CartItem> cartItems;
}
