package com.linss.gshop.service;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    PageInfo<Order> getAllOrder(int pageNum, int pageSize);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    List<Order> getOrderById(Integer orderId);

    List<Order> getOrderByUserId(Integer userId);

    //修改
    void updateOrder(Order order);

    void updateOrderStatus(Order order);

    // 在 OrderService 接口和实现类中添加批量方法
    Integer createOrders(Integer userId, List<OrderItem> items);

    void payOrder(Integer orderId);

    List<Order> getExpiredOrders(Date cutoff);

    // 删除订单方法
    void deleteOrder(Integer orderId);

    // 统计订单总数量
    int countOrders();

    //查找总销售量
    int countSales();

    //查找总销售额
    double countSalesAmount();

    //查找所有商品的销售量
    List<Order> countGoodsSales();

    // 获取销售汇总信息
    Map<String, Object> getSalesSummary();

    // 获取热销商品排行榜
    List<Map<String, Object>> getTopSellingProducts(int limit);

    // 获取商品类型销售占比
    List<Map<String, Object>> getCategorySalesRatio();

    List<Map<String, Object>> getSalesTrend();
}
