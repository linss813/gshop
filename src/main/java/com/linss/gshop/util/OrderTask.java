package com.linss.gshop.util;


import com.linss.gshop.entity.Goods;
import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;
import com.linss.gshop.mapper.OrderMapper;
import com.linss.gshop.service.GoodsService;
import com.linss.gshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OrderTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void checkExpiredOrders() {
        Date cutoff = new Date();
        List<Order> expiredOrders = orderService.getExpiredOrders(cutoff);
        OrderItem orderItem = new OrderItem();
        for (Order order : expiredOrders) {
            // 1. 恢复库存
            Goods goods = goodsService.getGoodsById(orderItem.getGid());
            goods.setStock(goods.getStock() + orderItem.getQuantity());
            goodsService.updateStock(goods);

            // 2. 更新订单状态
            order.setOrderStatus(0);
            orderMapper.updateOrder(order);
        }
    }
}