package com.pet_care.product_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pet_care.product_service.enums.CategoryType;
import com.pet_care.product_service.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

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

//    @ManyToOne()
//    @JoinColumn(name = "category_id")
//    Category categories;
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    ProductStatus status = ProductStatus.ACTIVE;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<InvoiceDetail> invoiceDetails;

}
