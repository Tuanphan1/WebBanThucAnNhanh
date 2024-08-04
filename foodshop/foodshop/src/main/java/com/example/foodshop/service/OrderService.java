package com.example.foodshop.service;

import com.example.foodshop.Entity.Order;
import com.example.foodshop.Entity.OrderItem;
import com.example.foodshop.dto.OrderDetailDto;
import com.example.foodshop.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAllOrders();
    Order createOrder(Order order, List<OrderItem> items);
    OrderDetailDto getOrderDetailByOrderId(Long orderId);
    List<Order> getOrdersByUserId(Long userId);
    Order updateOrderStatus(Long id, int status);
    void deleteOrder(Long id);
}
