package com.pet_care.product_service.mapper;

import com.pet_care.product_service.dto.request.CategoryRequest;
import com.pet_care.product_service.dto.response.CategoryResponse;
import com.pet_care.product_service.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper
{

    @Mapping(target = "name", source = "name")
    Category mapperToCategory(CategoryRequest categoryRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "status", source = "status")
    CategoryResponse mapperToCategoryResponse(Category category);
    List<CategoryResponse> mapperToCategoryResponses(List<Category> category);

}
