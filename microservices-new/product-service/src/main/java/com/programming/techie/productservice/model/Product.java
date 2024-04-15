package com.programming.techie.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @Field(name = "id")
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "description")
    private String description;
    @Field(name = "price")
    private BigDecimal price;
}
