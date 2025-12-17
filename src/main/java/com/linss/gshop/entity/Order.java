package com.linss.gshop.entity;

import lombok.Data;


import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "orders")
public class Order {

    private Integer orderId;

    //用户id
    private Integer userId;

    //流水号
    private String payNum;

    //总金额
    private BigDecimal totalAmount;

    //支付方式默认为alipay
    private String paymentMethod = "alipay";

    //支付状态（0未支付，1已支付，2支付失败）
    private Integer paymentStatus;

    //支付时间
    private Date payTime;

    //订单状态（0取消，1待发货，2已发货，3已签收）
    private Integer orderStatus;

    //下单时间
    private Date orderTime;

    //订单过期时间
    private Date expireTime;

    private List<OrderItem> items;

}
