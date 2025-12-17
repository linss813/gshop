package com.linss.gshop.entity.dto;


import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Integer userId;
    private Order order;
    private List<OrderItem> items; // 每个订单项包含 gid 和 quantity




}
