package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.ProductRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController
{

    ProductService productService;

    @GetMapping("/{page}")
    public APIResponse<PageableResponse> getAllProduct(@PathVariable("page") int _page)
    {
        PageableResponse getAllProducts = productService.getAllProducts(_page);

        return APIResponse.<PageableResponse<PageableResponse>>builder()
                .data(getAllProducts)
                .build();
    }

    @GetMapping("/detail/{productId}")
    public APIResponse<ProductResponse> getProductById(@PathVariable("productId") Long _id)
    {
        ProductResponse getProductById = productService.getProductById(_id);

        return APIResponse.<ProductResponse>builder()
                .data(getProductById)
                .build();
    }

    @GetMapping("/category/{categoryId}")
    public APIResponse<List<ProductResponse>> getProductByCategoryId(@PathVariable("categoryId") Long _id)
    {
        List<ProductResponse> getProductById = productService.getProductByCategoryId(_id);

        return APIResponse.<List<ProductResponse>>builder()
                .data(getProductById)
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<ProductResponse> createCategory(
            @RequestBody ProductRequest _productRequest,
            @RequestPart(value = "image_url", required = false) MultipartFile imageFile
    ) throws IOException {
        ProductResponse productResponse = productService.createProduct(_productRequest, imageFile);

        return APIResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<ProductResponse> uppdateCategory(
            @RequestBody ProductRequest _productRequest,
            @RequestPart(value = "image_url", required = false) MultipartFile imageFile) throws IOException {
        ProductResponse categoryResponse = productService.updateProduct(_productRequest, imageFile);

        return APIResponse.<ProductResponse>builder()
                .data(categoryResponse)
                .build();
    }

    @DeleteMapping("/{productId}")
    public APIResponse<ProductResponse> deleteCategory(@PathVariable("productId") Long _productId)
    {
        productService.deleteProduct(_productId);

        return APIResponse.<ProductResponse>builder()
//                .data(categoryResponse)
                .build();
    }

}
