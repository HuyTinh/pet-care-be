package com.pet_care.product_service.service;

import com.pet_care.product_service.dto.request.CategoryRequest;
import com.pet_care.product_service.dto.response.CategoryResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.CategoryMapper;
import com.pet_care.product_service.model.Category;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService
{

    static final int PAGE_SIZE = 50;

    CategoryMapper categoryMapper;

    CategoryRepository categoryRepository;

    public PageableResponse getAllCategories(int page)
    {
        page = page <= 1 ? 0 : page - 1;

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<Category> categories = categoryRepository.findAll(pageable);

        PageableResponse pageableResponse = PageableResponse.builder()
                .totalPages(categories.getTotalPages())
                .pageNumber(page)
                .pageSize(categories.getSize())
                .content(Collections.singletonList(categoryMapper.mapperToCategoryResponses(categories.getContent())))
                .build();

        return pageableResponse;
    }

    @Transactional(readOnly = true)
    public CategoryResponse getProductById(Long id)
    {        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new APIException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponse categoryResponse = categoryMapper.mapperToCategoryResponse(category);

        return categoryResponse;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest)
    {
        if (findCategoryByName(categoryRequest.getName()))
        {
            throw new APIException(ErrorCode.CATEGORY_EXIST);
        }
        Category mapperToCategory = categoryMapper.mapperToCategory(categoryRequest);
        mapperToCategory.setStatus(false);
        Category savedCategory = categoryRepository.save(mapperToCategory);
        CategoryResponse response = categoryMapper.mapperToCategoryResponse(savedCategory);

        log.info("Create category: {}", savedCategory);

        return response;
    }

    @Transactional
    public CategoryResponse updateCategory(CategoryRequest categoryRequest)
    {
        if (findCategoryByName(categoryRequest.getName()))
        {
            throw new APIException(ErrorCode.CATEGORY_EXIST);
        }
        Category category = categoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new APIException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setName(categoryRequest.getName());
        Category savedCategory = categoryRepository.save(category);
        CategoryResponse response = categoryMapper.mapperToCategoryResponse(savedCategory);
        log.info("Update category: {}", savedCategory);

        return response;
    }

    @Transactional
    public void deleteCategory(Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new APIException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);

        // Logs the successful deletion of the medicine
        log.info("Delete category successful");
    }

    public boolean findCategoryByName(String categoryName)
    {
        Category validCategory = categoryRepository.findByName(categoryName);

        return validCategory != null;
    }

}
