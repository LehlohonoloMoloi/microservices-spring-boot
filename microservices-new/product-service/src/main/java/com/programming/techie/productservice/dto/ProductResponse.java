package com.programming.techie.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
