package com.example.foodshop.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Long status;
    private Long quantity;
    private String image;
}
