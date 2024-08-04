package com.example.foodshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailDto {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private int quantity;
}
