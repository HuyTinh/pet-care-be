package com.pet_care.product_service.repository;

import com.pet_care.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{

    @Query(" Select p FROM products p where p.categories.id = :categoryId ")
    List<Product> findByCategoryId(Long categoryId);

}
