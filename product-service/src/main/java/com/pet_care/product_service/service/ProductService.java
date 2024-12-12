package com.pet_care.product_service.service;

import com.pet_care.product_service.client.UploadImageClient;
import com.pet_care.product_service.dto.request.ProductRequest;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.ProductMapper;
import com.pet_care.product_service.model.Category;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.CategoryRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService
{
    ProductRepository productRepository;

    UploadImageClient uploadImageClient;

    CategoryRepository categoryRepository;

    private ProductMapper productMapper;

    static final int PAGE_SIZE = 50;

    public PageableResponse getAllProducts(int page)
    {
        page = page <= 1 ? 0 : page - 1;

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<Product> products = productRepository.findAll(pageable);

        PageableResponse pageableResponse = PageableResponse.builder()
                .totalPages(products.getTotalPages())
                .pageNumber(page)
                .pageSize(products.getSize())
                .content(Collections.singletonList(productMapper.mapperToProductResponses(products.getContent())))
                .build();

        return pageableResponse;
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id)
    {        Product product = productRepository.findById(id)
            .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));

            ProductResponse productResponse = productMapper.mapperToProductResponse(product);

        return productResponse;
    }

    @Transactional
    public Product getProductByIdToMakeInvoice(Long id)
    {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));

        return product;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByCategoryId(Long categoryId)
    {
        List<Product> product = productRepository.findByCategoryId(categoryId);

        List<ProductResponse> productResponse = productMapper.mapperToProductResponses(product);

        return productResponse;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile image) throws IOException {
        System.out.println("name: " +productRequest.getName());
        Product savedProduct = productMapper.mapperToProduct(productRequest);

        checkAndUploadImageProduct(image, savedProduct);

        checkAndConfigCategory(productRequest.getCategory(), savedProduct);

        productRepository.save(savedProduct);
        ProductResponse response = productMapper.mapperToProductResponse(savedProduct);

        return response;
    }

    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));

        Product savedProduct = productMapper.mapperToProduct(productRequest);

        checkAndUploadImageProduct(image, savedProduct);

        checkAndConfigCategory(productRequest.getCategory(), savedProduct);

        productRepository.save(savedProduct);
        ProductResponse response = productMapper.mapperToProductResponse(savedProduct);

        return response;
    }

    @Transactional
    public Product updateProductQuantity(Product product)
    {
        return productRepository.save(product);
    }


    @Transactional
    public void deleteProduct(Long productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.delete(product);

        // Logs the successful deletion of the medicine
        log.info("Delete product successful");
    }

    public void checkAndConfigCategory(String categoryName, Product product)
    {
        Category category = categoryRepository.findByName(categoryName);
        if(category == null)
        {
            throw new APIException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        product.setCategories(category);
    }

    private void checkAndUploadImageProduct(MultipartFile imageFile, Product product) throws IOException {
        // Checks if the image file is not null and is not empty
        if (imageFile != null && !imageFile.isEmpty()) {
            // Uploads the image file and retrieves the image URL
            String imageUrl = uploadImageClient
                    .uploadImage(List.of(imageFile)).get(0);
            // Sets the uploaded image URL to the medicine object
            product.setImage(imageUrl);
        }
    }

}
