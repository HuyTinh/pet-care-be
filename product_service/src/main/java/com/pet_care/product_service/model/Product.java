package com.pet_care.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "image", nullable = true)
    String image;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    Category categories;

}
