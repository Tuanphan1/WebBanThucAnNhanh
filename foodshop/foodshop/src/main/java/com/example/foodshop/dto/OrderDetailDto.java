package com.example.foodshop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDetailDto {
    private Long id;
    private String fullname;
    private String mobile;
    private String address;
    private int status;
    private double totalPrice;
    private List<ProductDetailDto> items;
}
