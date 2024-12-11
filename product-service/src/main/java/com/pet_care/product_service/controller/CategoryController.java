package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.CategoryRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.dto.response.CategoryResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.model.Category;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController
{

    CategoryService categoryService;

    @GetMapping("/{page}")
    public APIResponse<PageableResponse> getAllCategories(@PathVariable("page") int _page)
    {
        PageableResponse getAllProducts = categoryService.getAllCategories(_page);

        return APIResponse.<PageableResponse<CategoryResponse>>builder()
                .data(getAllProducts)
                .build();
    }

    @GetMapping("/detail/{categoryId}")
    public APIResponse<CategoryResponse> getCategoryById(@PathVariable("categoryId") Long _id)
    {
        CategoryResponse getProductById = categoryService.getProductById(_id);

        return APIResponse.<CategoryResponse>builder()
                .data(getProductById)
                .build();
    }

    @PostMapping()
    public APIResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest _categoryRequest)
    {
        CategoryResponse categoryResponse = categoryService.createCategory(_categoryRequest);

        return APIResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .build();
    }

    @PutMapping()
    public APIResponse<CategoryResponse> uppdateCategory(@RequestBody CategoryRequest _categoryRequest)
    {
        CategoryResponse categoryResponse = categoryService.updateCategory(_categoryRequest);

        return APIResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .build();
    }

    @DeleteMapping("/{categoryId}")
    public APIResponse<CategoryResponse> deleteCategory(@PathVariable("categoryId") Long _categoryId)
    {
        categoryService.deleteCategory(_categoryId);

        return APIResponse.<CategoryResponse>builder()
//                .data(categoryResponse)
                .build();
    }

}
