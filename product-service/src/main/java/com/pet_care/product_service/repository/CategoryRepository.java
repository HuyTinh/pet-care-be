package com.pet_care.product_service.repository;

import com.pet_care.product_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{

    Category findByName(String name);

}
