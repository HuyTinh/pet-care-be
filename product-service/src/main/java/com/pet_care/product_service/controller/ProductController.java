package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.ProductRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.enums.CategoryType;
import com.pet_care.product_service.enums.ProductStatus;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController
{

    ProductService productService;

    /**
     * Lọc sản phẩm dựa trên các tham số như số trang, từ khóa tìm kiếm, loại sản phẩm, trạng thái,
     * khoảng giá, số lượng và phân trang.
     *
     * @param pageNumber Số trang (mặc định là 0)
     * @param pageSize Số sản phẩm mỗi trang (mặc định là 10)
     * @param searchTerm Từ khóa tìm kiếm (tên hoặc mô tả)
     * @param category Loại sản phẩm
     * @param status Trạng thái của sản phẩm
     * @param minPrice Giá tối thiểu
     * @param maxPrice Giá tối đa
     * @param minQuantity Số lượng tối thiểu
     * @param maxQuantity Số lượng tối đa
     * @param sortBy Trường sắp xếp (mặc định là "id")
     * @param sortOrder Hướng sắp xếp (mặc định là "asc")
     * @return Một đối tượng `PageableResponse` chứa danh sách sản phẩm thỏa mãn bộ lọc và thông tin phân trang
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<ProductResponse>> filterProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) CategoryType category,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        PageableResponse<ProductResponse> response = productService.filterProducts(
                pageNumber, pageSize, searchTerm, category, status, minPrice, maxPrice,
                minQuantity, maxQuantity, sortBy, sortOrder
        );

        return APIResponse.<PageableResponse<ProductResponse>>builder()
                .data(response)
                .build();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return A response containing the product details.
     */
    @GetMapping("{productId}")
    public APIResponse<Product> getProductById(@PathVariable("productId") Long productId) {
        return APIResponse.<Product>builder()
                .data(productService.getProductById(productId))
                .build();
    }

//    @GetMapping("/{page}")
//    public APIResponse<PageableResponse> getAllProduct(@PathVariable("page") int _page)
//    {
//        PageableResponse getAllProducts = productService.getAllProducts(_page);
//
//        return APIResponse.<PageableResponse<PageableResponse>>builder()
//                .data(getAllProducts)
//                .build();
//    }
//
//    @GetMapping("/detail/{productId}")
//    public APIResponse<ProductResponse> getProductById(@PathVariable("productId") Long _id)
//    {
//        ProductResponse getProductById = productService.getProductById(_id);
//
//        return APIResponse.<ProductResponse>builder()
//                .data(getProductById)
//                .build();
//    }
//
//    @GetMapping("/category/{categoryId}")
//    public APIResponse<List<ProductResponse>> getProductByCategoryId(@PathVariable("categoryId") Long _id)
//    {
//        List<ProductResponse> getProductById = productService.getProductByCategoryId(_id);
//
//        return APIResponse.<List<ProductResponse>>builder()
//                .data(getProductById)
//                .build();
//    }
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public APIResponse<ProductResponse> createCategory(
//            @ModelAttribute ProductRequest _productRequest,
//            @RequestPart(value = "image_url", required = false) MultipartFile imageFile
//    ) throws IOException {
//        ProductResponse productResponse = productService.createProduct(_productRequest, imageFile);
//
//        return APIResponse.<ProductResponse>builder()
//                .data(productResponse)
//                .build();
//    }
//
//    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public APIResponse<ProductResponse> uppdateCategory(
//            @ModelAttribute ProductRequest _productRequest,
//            @RequestPart(value = "image_url", required = false) MultipartFile imageFile) throws IOException {
//        ProductResponse categoryResponse = productService.updateProduct(_productRequest, imageFile);
//
//        return APIResponse.<ProductResponse>builder()
//                .data(categoryResponse)
//                .build();
//    }
//
//    @DeleteMapping("/{productId}")
//    public APIResponse<ProductResponse> deleteCategory(@PathVariable("productId") Long _productId)
//    {
//        productService.deleteProduct(_productId);
//
//        return APIResponse.<ProductResponse>builder()
////                .data(categoryResponse)
//                .build();
//    }



}
