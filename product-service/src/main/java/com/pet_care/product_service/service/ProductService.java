package com.pet_care.product_service.service;

import com.pet_care.product_service.client.UploadImageClient;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.enums.CategoryType;
import com.pet_care.product_service.enums.ProductStatus;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.ProductMapper;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService
{
    ProductRepository productRepository;

    UploadImageClient uploadImageClient;


    ProductMapper productMapper;


    /**
     * Lọc sản phẩm dựa trên các tham số như số trang, từ khóa tìm kiếm, ngày sản xuất, ngày hết hạn,
     * loại sản phẩm, trạng thái, khoảng giá và phân trang.
     *
     * @param pageNumber Số trang
     * @param pageSize Số lượng sản phẩm mỗi trang
     * @param searchTerm Từ khóa tìm kiếm (tên hoặc mô tả)
     * @param category Loại sản phẩm
     * @param status Trạng thái của sản phẩm
     * @param minPrice Giá tối thiểu
     * @param maxPrice Giá tối đa
     * @param minQuantity Số lượng tối thiểu
     * @param maxQuantity Số lượng tối đa
     * @param sortBy Trường sắp xếp (tên sản phẩm, giá, ... )
     * @param sortOrder Hướng sắp xếp ("asc" hoặc "desc")
     * @return PageableResponse chứa danh sách sản phẩm lọc được và thông tin phân trang
     */
    @Transactional(readOnly = true)
    public PageableResponse<ProductResponse> filterProducts(int pageNumber, int pageSize, String searchTerm,
                                                            CategoryType category, ProductStatus status,
                                                            Double minPrice, Double maxPrice, Integer minQuantity,
                                                            Integer maxQuantity, String sortBy, String sortOrder) {
        // Tạo đối tượng Sort theo trường và hướng sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

        // Tạo đối tượng Pageable với thông tin phân trang và sắp xếp
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Lọc sản phẩm theo các tiêu chí đã truyền vào
        Page<Product> productPage = productRepository.findByFilters(
                searchTerm, category, status, minPrice, maxPrice, minQuantity, maxQuantity,
                pageable
        );

        // Chuyển đổi danh sách sản phẩm thành danh sách DTO (ProductResponse)
        List<ProductResponse> productList = productPage.getContent().stream()
                .map(productMapper::toDto)  // Nếu bạn có mapper để chuyển đổi từ Product sang ProductResponse
                .collect(Collectors.toList());

        // Tạo đối tượng PageableResponse để trả về kết quả phân trang
        PageableResponse<ProductResponse> pageableResponse = PageableResponse.<ProductResponse>builder()
                .content(productList)
                .totalPages(productPage.getTotalPages())
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .build();

        // Log thông tin thành công
        log.info("Find all products with filters");

        // Trả về kết quả phân trang
        return pageableResponse;
    }

    /**
     * Retrieves a medicine record by its ID.
     *
     * @param id The ID of the product to be retrieved
     * @return The product record
     * @throws APIException If the product with the given ID is not found
     */
    @Transactional(readOnly = true)  // Marks the method as read-only for better performance in read operations
    public Product getProductById(Long id) {
        // Retrieves the product by ID, throws an exception if not found
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));

        // Logs the retrieval of the product by ID
        log.info("Find product by id: {}", product.getId());

        // Returns the found product record
        return product;
    }


//
//    static final int PAGE_SIZE = 50;
//
//    public PageableResponse getAllProducts(int page)
//    {
//        page = page <= 1 ? 0 : page - 1;
//
//        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
//        Page<Product> products = productRepository.findAll(pageable);
//
//        PageableResponse pageableResponse = PageableResponse.builder()
//                .totalPages(products.getTotalPages())
//                .pageNumber(page)
//                .pageSize(products.getSize())
//                .content(Collections.singletonList(productMapper.mapperToProductResponses(products.getContent())))
//                .build();
//
//        return pageableResponse;
//    }
//
//    @Transactional(readOnly = true)
//    public ProductResponse getProductById(Long id)
//    {        Product product = productRepository.findById(id)
//            .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));
//
//            ProductResponse productResponse = productMapper.mapperToProductResponse(product);
//
//        return productResponse;
//    }
//
//    @Transactional
//    public Product getProductByIdToMakeInvoice(Long id)
//    {
//        Product product = productRepository.findById(id)
//            .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        return product;
//    }
//
//    @Transactional(readOnly = true)
//    public List<ProductResponse> getProductByCategoryId(Long categoryId)
//    {
//        List<Product> product = productRepository.findByCategoryId(categoryId);
//
//        List<ProductResponse> productResponse = productMapper.mapperToProductResponses(product);
//
//        return productResponse;
//    }
//
//    @Transactional
//    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile image) throws IOException {
//        System.out.println("name: " +productRequest.getName());
//        Product savedProduct = productMapper.mapperToProduct(productRequest);
//
//        checkAndUploadImageProduct(image, savedProduct);
//
//        checkAndConfigCategory(productRequest.getCategory(), savedProduct);
//
//        productRepository.save(savedProduct);
//        ProductResponse response = productMapper.mapperToProductResponse(savedProduct);
//
//        return response;
//    }
//
//    @Transactional
//    public ProductResponse updateProduct(ProductRequest productRequest, MultipartFile image) throws IOException {
//        Product product = productRepository.findById(productRequest.getId())
//                .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        Product savedProduct = productMapper.mapperToProduct(productRequest);
//
//        checkAndUploadImageProduct(image, savedProduct);
//
//        checkAndConfigCategory(productRequest.getCategory(), savedProduct);
//
//        productRepository.save(savedProduct);
//        ProductResponse response = productMapper.mapperToProductResponse(savedProduct);
//
//        return response;
//    }
//
//    @Transactional
//    public Product updateProductQuantity(Product product)
//    {
//        return productRepository.save(product);
//    }
//
//
//    @Transactional
//    public void deleteProduct(Long productId)
//    {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        productRepository.delete(product);
//
//        // Logs the successful deletion of the medicine
//        log.info("Delete product successful");
//    }
//
//    public void checkAndConfigCategory(String categoryName, Product product)
//    {
//        Category category = categoryRepository.findByName(categoryName);
//        if(category == null)
//        {
//            throw new APIException(ErrorCode.CATEGORY_NOT_FOUND);
//        }
//
//        product.setCategories(category);
//    }
//
//    private void checkAndUploadImageProduct(MultipartFile imageFile, Product product) throws IOException {
//        // Checks if the image file is not null and is not empty
//        if (imageFile != null && !imageFile.isEmpty()) {
//            // Uploads the image file and retrieves the image URL
//            String imageUrl = uploadImageClient
//                    .uploadImage(List.of(imageFile)).get(0);
//            // Sets the uploaded image URL to the medicine object
//            product.setImage(imageUrl);
//        }
//    }

}
