package com.pet_care.product_service.mapper;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.request.ProductRequest;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper
{

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "price", source = "price")
//    @Mapping(target = "quantity", source = "quantity")
//    @Mapping(target = "categories.name", ignore = true)
//    Product mapperToProduct(ProductRequest productRequest);
//
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "price", source = "price")
//    @Mapping(target = "quantity", source = "quantity")
//    @Mapping(target = "imageUrl", source = "image")
//    @Mapping(target = "category", source = "categories.name")
//    ProductResponse mapperToProductResponse(Product product);
//    List<ProductResponse> mapperToProductResponses(List<Product> product);
    /**
     * Converts a Product entity to a ProductResponse DTO.
     *
     * @param product the Product entity to be converted
     * @return a ProductResponse DTO populated with data from the Product entity
     */
    @Mapping(source = "image", target = "image")
    ProductResponse toDto(Product product);

}
