package com.linss.gshop.mapper;

import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    List<Order> getAllOrder();

    List<Order> getOrderById(Integer orderId);

    List<Order> getOrderByUserId(Integer userId);

    int insertOrder(Order order);

    // 新增订单项
    int insertOrderItem(OrderItem orderItem);

    // 更新订单状态（过期处理）
    int updateExpiredOrders(Date cutoffTime);

    int updateOrder(Order order);

    List<Order> getExpiredOrders(Date cutoff);

    // 删除订单
    int deleteOrder(Integer orderId);

    // 统计订单总数量
    int countOrders();

    int countSale();

    double countSalesAmount();

    List<Order> countGoodsSales();

    List<OrderItem> selectOrderItemsByOrderIds(@Param("orderIds") List<Integer> orderIds);

    void updateOrderStatus(Order order);

    // 获取热销商品排行榜
    List<Map<String, Object>> getTopSellingProducts(@Param("limit") int limit);

    // 获取商品类型销售占比
    List<Map<String, Object>> getCategorySalesRatio();

    @MapKey("orderDate")
    List<Map<String, Object>> getSalesTrend();
}
