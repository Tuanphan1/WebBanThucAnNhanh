package com.example.foodshop.service.IMPL;

import com.example.foodshop.Entity.Order;
import com.example.foodshop.Entity.OrderItem;
import com.example.foodshop.Entity.Product;
import com.example.foodshop.dto.OrderDetailDto;
import com.example.foodshop.dto.OrderDto;
import com.example.foodshop.dto.ProductDetailDto;
import com.example.foodshop.repository.OrderRepository;
import com.example.foodshop.repository.ProductRepository;
import com.example.foodshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getUserId(),
                        order.getFullname(),
                        order.getMobile(),
                        order.getAddress(),
                        order.getStatus(),
                        order.getTotalPrice()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order createOrder(Order order, List<OrderItem> items) {
        double totalPrice = 0.0;  // Initialize total price

        for (OrderItem item : items) {
            // Retrieve the product using product ID
            Product product = productRepository.findById(item.getProductId()).orElseThrow(
                    () -> new RuntimeException("Product with ID " + item.getProductId() + " not found"));
            double itemTotal = item.getQuantity() * product.getPrice();
            totalPrice += itemTotal;

            item.setOrder(order);  // Link the item to the order
            item.setProductId(item.getProductId());  // Optionally set the product to the order item for reference
        }

        order.setItems(items);
        order.setTotalPrice(totalPrice);  // Set the total price of the order
        order.setStatus(0);  // Assuming status 1 means 'created'
        return orderRepository.save(order);
    }

    @Override
    public OrderDetailDto getOrderDetailByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found"));

        List<ProductDetailDto> products = order.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product with ID " + item.getProductId() + " not found"));
            return new ProductDetailDto(item.getProductId(), product.getName(), product.getDescription(), product.getPrice(), item.getQuantity());
        }).collect(Collectors.toList());

        return new OrderDetailDto(
                order.getId(),
                order.getFullname(),
                order.getMobile(),
                order.getAddress(),
                order.getStatus(),
                order.getTotalPrice(),
                products
        );
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, int status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found on :: " + id));
        orderRepository.delete(order);
    }
}
