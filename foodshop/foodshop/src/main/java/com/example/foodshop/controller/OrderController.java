package com.example.foodshop.controller;

import com.example.foodshop.Entity.Order;
import com.example.foodshop.dto.OrderDetailDto;
import com.example.foodshop.dto.OrderDto;
import com.example.foodshop.response.ResponseObject;
import com.example.foodshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllOrders() {
        try {
            var listOrders = orderService.findAllOrders();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Get a list of successful orders ",true, listOrders)
            );
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred", false, "")
            );
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(404, "No orders found for the user", false, null));
            }
            return ResponseEntity.ok(new ResponseObject(200, "Orders retrieved successfully", true, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(500, "Error retrieving orders: " + e.getMessage(), false, null));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseObject> updateOrderStatus(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, orderDto.getStatus());
            if (updatedOrder != null) {
                return ResponseEntity.ok(new ResponseObject(200, "Order status updated successfully", true, updatedOrder));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(404, "Order not found", false, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(500, "Error updating order status: " + e.getMessage(), false, null));
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<ResponseObject> checkoutOrder(@RequestBody Order order) {
        try {
            var saveOrder = orderService.createOrder(order, order.getItems());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Order created successfully", true, saveOrder)
            );
        } catch (Exception e) {
            e.printStackTrace();  // This will help you see the error in the logs.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(400, "Error creating order: " + e.getMessage(), false, null)
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderDetailsById(@PathVariable Long id) {
        try {
            OrderDetailDto orderDetail = orderService.getOrderDetailByOrderId(id);
            if (orderDetail != null) {
                return ResponseEntity.ok(new ResponseObject(200, "Order details retrieved successfully", true, orderDetail));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(404, "Order not found", false, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(500, "Error retrieving order details: " + e.getMessage(), false, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
