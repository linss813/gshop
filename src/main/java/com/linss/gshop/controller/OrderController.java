package com.linss.gshop.controller;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Order;
import com.linss.gshop.entity.OrderItem;
import com.linss.gshop.entity.dto.OrderRequest;
import com.linss.gshop.service.OrderService;
import com.linss.gshop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getAllOrder")
    public Result getAllOrder(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        //分页查
        PageInfo<Order> orderPageInfo = orderService.getAllOrder(pageNum, pageSize);
        return Result.success(orderPageInfo);
    }

    @GetMapping("/getOrderItemsByOrderId/{orderId}")
    public Result getOrderItemsByOrderId(
            @PathVariable(name = "orderId", required = false) Integer orderId) { // 允许参数缺失
        // 1. 空值校验
        if (orderId == null) {
            return Result.error("无效的订单ID：参数缺失或格式错误");
        }
        // 2. 业务逻辑（调用服务层）
        List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
        return Result.success(items);
    }


    @GetMapping("/getOrderById/{orderId}")
    public Result getOrderById(@PathVariable Integer orderId) {
        return Result.success(orderService.getOrderById(orderId));
    }

    @GetMapping("/getOrderByUserId/{userId}")
    public Result getOrderByUserId(
            @PathVariable(name = "userId", required = false) Integer userId) {
        if (userId == null) {
            return Result.error("用户ID参数无效");
        }
        return Result.success(orderService.getOrderByUserId(userId));
    }

    @PutMapping("/updateOrder")
    public Result updateOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return Result.success("订单更新成功");
    }

    @PutMapping("/updateOrderStatus")
    public Result updateOrderStatus(@RequestBody Order order) {
        orderService.updateOrderStatus(order);
        return Result.success("订单更新成功");
    }

    @PostMapping("/createOrder")
    public Result createOrder(@RequestBody OrderRequest request) {
        Integer userId = request.getUserId();
        List<OrderItem> items = request.getItems();
        Integer orderId = orderService.createOrders(userId, items); // 批量创建订单
        return Result.success(orderId);
    }

    @PostMapping("/payOrder/{orderId}")
    public Result payOrder(
            @PathVariable(name = "orderId", required = false) Integer orderId) {
        if (orderId == null) {
            return Result.error("订单ID参数缺失或格式错误");
        }
        orderService.payOrder(orderId);
        return Result.success("订单支付成功");
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public Result deleteOrder(@PathVariable Integer orderId) {
        List<Order> orderList = orderService.getOrderById(orderId);
        if (orderList == null || orderList.isEmpty()) {
            return Result.error("订单不存在");
        }
        Order order = orderList.get(0);
        if (order.getOrderStatus() != 0) {
            return Result.error("只有已取消的订单才能删除");
        }
        orderService.deleteOrder(orderId);
        return Result.success("订单删除成功");
    }

    //  统计总订单数
    @GetMapping("/countOrders")
    public Result countOrders() {
        int count = orderService.countOrders();
        return Result.success(count);
    }

    @GetMapping("/getSalesTrend")
    public List<Map<String, Object>> getSalesTrend() {
        return orderService.getSalesTrend();
    }

    //
    @GetMapping("/getSalesSummary")
    public Result getSalesSummary() {
        Map<String, Object> summary = orderService.getSalesSummary();
        return Result.success(summary);
    }

    @GetMapping("/getTopSellingProducts/{limit}")
    public Result getTopSellingProducts(@PathVariable int limit) {
        List<Map<String, Object>> topProducts = orderService.getTopSellingProducts(limit);
        return Result.success(topProducts);
    }

    // 获取商品类型销售占比（示例实现）
    @GetMapping("/getCategorySalesRatio")
    public Result getCategorySalesRatio() {
        List<Map<String, Object>> categoryRatios = orderService.getCategorySalesRatio();
        return Result.success(categoryRatios);
    }

    //
    @GetMapping("/countSales")
    public Result countSales() {
        int count = orderService.countSales();
        return Result.success(count);
    }

    @GetMapping("/countSalesAmount")
    public Result countSalesAmount() {
        double count = orderService.countSalesAmount();
        return Result.success(count);
    }

    @GetMapping("/countGoodsSales")
    public Result countGoodsSales() {
        List<Order> orders = orderService.countGoodsSales();
        return Result.success(orders);
    }




}
