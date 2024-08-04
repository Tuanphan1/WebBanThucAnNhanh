package com.example.foodshop.dto;

import com.example.foodshop.Entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    private String fullname;
    private String mobile;
    private String address;
    private int status;
    private double totalPrice;
}
