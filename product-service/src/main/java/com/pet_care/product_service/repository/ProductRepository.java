package com.pet_care.product_service.repository;

import com.pet_care.product_service.enums.CategoryType;
import com.pet_care.product_service.enums.ProductStatus;
import com.pet_care.product_service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{

//    @Query(" Select p FROM products p where p.categories.id = :categoryId ")
//    List<Product> findByCategoryId(Long categoryId);

    /**
     * Tìm kiếm sản phẩm với các bộ lọc: tìm theo tên hoặc mô tả, theo loại sản phẩm, trạng thái, giá,
     * số lượng, ngày sản xuất và ngày hết hạn.
     *
     * @param searchTerm Tìm kiếm theo tên hoặc mô tả sản phẩm
     * @param category Loại sản phẩm (Enum CategoryType)
     * @param status Trạng thái sản phẩm (Enum ProductStatus)
     * @param minPrice Giá tối thiểu
     * @param maxPrice Giá tối đa
     * @param minQuantity Số lượng tối thiểu
     * @param maxQuantity Số lượng tối đa
     * @param pageable Tham số phân trang
     * @return Danh sách các sản phẩm thỏa mãn các bộ lọc
     */
    @Query("SELECT p FROM products p " +
            "WHERE (:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:minQuantity IS NULL OR p.quantity >= :minQuantity) " +
            "AND (:maxQuantity IS NULL OR p.quantity <= :maxQuantity)")
    Page<Product> findByFilters(
            @Param("searchTerm") String searchTerm,
            @Param("category") CategoryType category,
            @Param("status") ProductStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minQuantity") Integer minQuantity,
            @Param("maxQuantity") Integer maxQuantity,
            Pageable pageable
    );
}
