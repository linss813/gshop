package com.linss.gshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Goods;
import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;
import com.linss.gshop.mapper.GoodsMapper;
import com.linss.gshop.mapper.OrderMapper;
import com.linss.gshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodsMapper goodsService;


    @Override
    public PageInfo<Order> getAllOrder(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.getAllOrder();
        System.out.println("原始查询结果: " + orders); // 查看分页前的数据
        return PageInfo.of(orders);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderMapper.selectOrderItemsByOrderIds(Collections.singletonList(orderId));
    }

    @Override
    public List<Order> getOrderById(Integer orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Override
    public List<Order> getOrderByUserId(Integer userId) {
        return orderMapper.getOrderByUserId(userId);
    }

    @Override
    public void updateOrder(Order order) {
        //order_status为0时，不可以再修改
//        if (order.getOrderStatus() == 0) {
//            throw new RuntimeException("订单状态为0，不能修改");
//        }
        orderMapper.updateOrder(order);
    }

    @Override
    public void updateOrderStatus(Order order) {
        orderMapper.updateOrderStatus(order);
    }

    //订单生成，通过点击购买生成
    @Transactional
    @Override
    public Integer createOrders(Integer userId, List<OrderItem> items) {
        // 1. 参数校验
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("订单项不能为空");
        }
        for (OrderItem item : items) {
            if (item.getGid() == null || item.getQuantity() == null || item.getQuantity() < 1) {
                throw new IllegalArgumentException("商品ID或数量无效");
            }
        }

        // 2. 创建主订单（公共信息）
        Order mainOrder = new Order();
        mainOrder.setUserId(userId);
        mainOrder.setOrderTime(new Date());
        mainOrder.setOrderStatus(0); // 初始状态：未支付
        mainOrder.setPaymentStatus(0); // 未支付
        mainOrder.setTotalAmount(BigDecimal.ZERO); // 新增
        orderMapper.insertOrder(mainOrder); // 先保存主订单获取 orderId
        Integer orderId = mainOrder.getOrderId();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. 处理每个订单项
        for (OrderItem item : items) {
            Integer gid = item.getGid();
            Integer quantity = item.getQuantity();

            // 3.1 获取商品信息并校验库存
            Goods goods = goodsService.getGoodsById(gid);
            if (goods == null) {
                throw new RuntimeException("商品不存在，ID：" + gid);
            }
            if (goods.getStock() < quantity) {
                throw new RuntimeException("商品库存不足，ID：" + gid);
            }

            // 3.2 计算单项金额并累加总金额
            BigDecimal itemAmount = goods.getPrice().multiply(new BigDecimal(quantity));
            totalAmount = totalAmount.add(itemAmount);

            // 3.3 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setGid(gid);
            orderItem.setTitle(goods.getTitle());
            orderItem.setQuantity(quantity);
            orderItem.setPrice(goods.getPrice());
            orderMapper.insertOrderItem(orderItem);

            // 3.4 扣减库存
            goods.setStock(goods.getStock() - quantity);
            goodsService.updateStock(goods);
        }

        // 4. 更新主订单的总金额
        mainOrder.setTotalAmount(totalAmount);
        mainOrder.setExpireTime(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 设置过期时间
        orderMapper.updateOrder(mainOrder);
        return orderId;
    }

    @Transactional
    @Override
    public void payOrder(Integer orderId) {
        List<Order> orderList = orderMapper.getOrderById(orderId);
        if (orderList == null || orderList.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }
        Order order = orderList.get(0);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getPaymentStatus() != 0) throw new RuntimeException("订单已支付");

        // 生成流水号
        String payNum = UUID.randomUUID().toString().substring(0, 10);
        order.setPayNum(payNum);
        order.setPaymentStatus(1); // 已支付
        order.setOrderStatus(1); // 待发货
        order.setPaymentStatus(1);// 已支付
        order.setPayTime(new Date());
        // 支付成功后订单状态变为待发货
        order.setOrderStatus(1);   // 待发货
        orderMapper.updateOrder(order);
    }


    @Override
    public List<Order> getExpiredOrders(Date cutoff) {
        return orderMapper.getExpiredOrders(cutoff);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        List<Order> order = orderMapper.getOrderById(orderId);
        if (order == null || order.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }
        if (order.get(0).getOrderStatus() != 0) {
            throw new RuntimeException("只有已取消的订单才能删除");
        }
        orderMapper.deleteOrder(orderId);
    }

    @Override
    public int countOrders() {
        return orderMapper.countOrders();
    }

    @Override
    public int countSales() {
        return orderMapper.countSale();
    }

    @Override
    public double countSalesAmount() {
        return orderMapper.countSalesAmount();
    }

    @Override
    public List<Order> countGoodsSales() {
        return orderMapper.countGoodsSales();
    }

    @Override
    public Map<String, Object> getSalesSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", orderMapper.countOrders());
        summary.put("totalSales", orderMapper.countSalesAmount());
        return summary;
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        return orderMapper.getTopSellingProducts(limit);
    }

    @Override
    public List<Map<String, Object>> getSalesTrend() {
        return orderMapper.getSalesTrend();
    }

    @Override
    public List<Map<String, Object>> getCategorySalesRatio() {
        return orderMapper.getCategorySalesRatio();
    }
}
