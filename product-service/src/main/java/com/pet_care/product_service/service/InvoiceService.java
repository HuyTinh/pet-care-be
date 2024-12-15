package com.pet_care.product_service.service;

import com.pet_care.product_service.dto.request.InvoiceDetailRequest;
import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.response.InvoiceDetailResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.enums.StatusAccept;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.InvoiceDetailMapper;
import com.pet_care.product_service.mapper.InvoiceMapper;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.InvoiceDetailRepository;
import com.pet_care.product_service.repository.InvoiceRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    InvoiceDetailRepository invoiceDetailRepository;
    ProductRepository productRepository;
    InvoiceMapper invoiceMapper;
    InvoiceDetailMapper invoiceDetailMapper;

    @Transactional(readOnly = true)
    public PageableResponse<InvoiceResponse> filterInvoices(
            int pageNumber,
            int pageSize,
            String note,
            Long customerId,
            StatusAccept statusAccept,
            Double minPrice,
            Double maxPrice,
            Date startDate,
            Date endDate,
            String sortBy,
            String sortOrder) {

        // Tạo đối tượng Sort theo trường và hướng sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

        // Tạo đối tượng Pageable với thông tin phân trang và sắp xếp
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Lọc các hóa đơn theo các tiêu chí đã truyền vào
        Page<Invoice> invoicePage = invoiceRepository.findByFilters(
                note, customerId, statusAccept, minPrice, maxPrice, startDate, endDate, pageable
        );

        // Chuyển đổi danh sách hóa đơn thành danh sách DTO (InvoiceResponse)
        List<InvoiceResponse> invoiceList = invoicePage.getContent().stream()
                .map(invoice -> {
                    // Sử dụng mapper để chuyển đổi Invoice sang InvoiceResponse
                    InvoiceResponse invoiceResponse = invoiceMapper.toDto(invoice);

                    // Chuyển đổi chi tiết hóa đơn
                    List<InvoiceDetailResponse> detailResponses = invoice.getInvoiceDetails().stream()
                            .map(invoiceDetailMapper::toDto) // Sử dụng mapper để ánh xạ chi tiết hóa đơn
                            .collect(Collectors.toList());

                    // Gán danh sách chi tiết hóa đơn vào InvoiceResponse
                    invoiceResponse.setInvoiceDetailResponses(detailResponses);

                    return invoiceResponse;
                })
                .collect(Collectors.toList());

        // Tạo đối tượng PageableResponse để trả về kết quả phân trang
        PageableResponse<InvoiceResponse> pageableResponse = PageableResponse.<InvoiceResponse>builder()
                .content(invoiceList)
                .totalPages(invoicePage.getTotalPages())
                .pageNumber(invoicePage.getNumber())
                .pageSize(invoicePage.getSize())
                .build();

        // Trả về kết quả phân trang
        return pageableResponse;
    }


    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        // 1. Kiểm tra thông tin từ yêu cầu
        if (invoiceRequest.getInvoiceDetails().isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn phải có ít nhất một chi tiết sản phẩm.");
        }

        // 2. Lấy danh sách sản phẩm từ cơ sở dữ liệu
        List<Long> productIds = invoiceRequest.getInvoiceDetails().stream()
                .map(InvoiceDetailRequest::getProductId)
                .collect(Collectors.toList());
        Map<Long, Product> productsMap = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // 3. Tính toán tổng giá trị hóa đơn
        Double totalPrice = 0.0;
        for (InvoiceDetailRequest detailRequest : invoiceRequest.getInvoiceDetails()) {
            Product product = productsMap.get(detailRequest.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Sản phẩm không tồn tại: " + detailRequest.getProductId());
            }
            Double productPrice = product.getPrice();
            detailRequest.setPrice(productPrice);  // Cập nhật giá sản phẩm trong chi tiết hóa đơn
            totalPrice += productPrice * detailRequest.getQuantity();  // Tính tổng giá trị cho hóa đơn
        }
        // 4. Tạo hóa đơn và lưu vào cơ sở dữ liệu
        Invoice invoice = Invoice.builder()
                .note(invoiceRequest.getNote())
                .totalPrice(totalPrice + invoiceRequest.getShippingFee())
                .customer_id(invoiceRequest.getCustomerId())
                .statusAccept(invoiceRequest.getStatusAccept())
                .fullName(invoiceRequest.getFullName())
                .phoneNumber(invoiceRequest.getPhoneNumber())
                .address(invoiceRequest.getAddress())
                .createdAt(new Date())  // Nếu không dùng @CreationTimestamp
                .build();

        invoice = invoiceRepository.save(invoice);

        // 5. Tạo chi tiết hóa đơn và lưu vào cơ sở dữ liệu
        Invoice finalInvoice = invoice;
        List<InvoiceDetail> invoiceDetails = invoiceRequest.getInvoiceDetails().stream()
                .map(detailRequest -> {
                    Product product = productsMap.get(detailRequest.getProductId());
                    InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                            .quantity(detailRequest.getQuantity())
                            .totalPrice(detailRequest.getPrice() * detailRequest.getQuantity())
                            .price(detailRequest.getPrice())
                            .product(product)
                            .invoice(finalInvoice)
                            .build();
                    return invoiceDetail;
                })
                .collect(Collectors.toList());

        invoiceDetailRepository.saveAll(invoiceDetails);

        // 6. Trả về DTO cho hóa đơn đã tạo
        return invoiceMapper.toDto(invoice);
    }

    /**
     * Retrieves an invoice record by its ID.
     *
     * @param id The ID of the invoice to be retrieved
     * @return The invoice record
     * @throws APIException If the invoice with the given ID is not found
     */
    @Transactional(readOnly = true)  // Marks the method as read-only for better performance in read operations
    public Invoice getInvoiceById(Long id) {
        // Retrieves the invoice by ID, throws an exception if not found
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));
        // Logs the retrieval of the invoice by ID
        log.info("Find invoice by id: {}", invoice.getId());
        // Returns the found  record
        return invoice;
    }



//    private static final int PAGE_SIZE = 50;
//
//    private final InvoiceRepository invoiceRepository;
//    private final ProductService productService;
//    private final InvoiceDetailService invoiceDetailService;
//    private final InvoiceMapper invoiceMapper;
//    private final InvoiceDetailMapper invoiceDetailMapper;
//
//    public PageableResponse getAllInvoice(int page) {
//        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), PAGE_SIZE, Sort.by("id").descending());
//        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);
//
//        List<InvoiceResponse> responses = invoicePage.getContent().stream()
//                .map(this::mapToInvoiceResponse)
//                .collect(Collectors.toList());
//
//        return buildPageableResponse(invoicePage, responses);
//    }
//
//    public InvoiceResponse createInvoice(List<InvoiceRequest> invoiceRequests) {
//        Invoice savedInvoice = createInvoiceEntity(invoiceRequests);
//        Invoice finalInvoice = saveInvoiceWithDetails(savedInvoice, invoiceRequests);
//
//        return mapToInvoiceResponse(finalInvoice);
//    }
//
//    public InvoiceResponse updateStatusInvoice(long id, StatusAccept action) {
//        Invoice invoice = findInvoiceById(id);
//        validateStatusAccept(invoice.getStatusAccept(), action);
//
//        invoice.setStatusAccept(action);
//        Invoice updatedInvoice = invoiceRepository.save(invoice);
//
//        return mapToInvoiceResponse(updatedInvoice);
//    }
//
//    public InvoiceResponse deleteInvoice(long id, StatusAccept status) {
//        Invoice invoice = findInvoiceById(id);
//        validateStatusAccept(invoice.getStatusAccept(), status);
//
//        invoice.setStatusAccept(StatusAccept.DELETED);
//        handleProductQuantityDuringDeletion(invoice);
//
//        Invoice deletedInvoice = invoiceRepository.save(invoice);
//
//        return mapToInvoiceResponse(deletedInvoice);
//    }
//
//    private InvoiceResponse mapToInvoiceResponse(Invoice invoice) {
//        InvoiceResponse response = invoiceMapper.mapperToInvoiceResponse(invoice);
//        List<InvoiceDetailResponse> detailResponse = invoiceDetailMapper.mapperToInvoiceDetailResponses(invoice.getInvoiceDetails());
//        response.setInvoiceDetailResponses(detailResponse);
//        return response;
//    }
//
//    private PageableResponse buildPageableResponse(Page<Invoice> invoicePage, List<InvoiceResponse> responses) {
//        return PageableResponse.builder()
//                .totalPages(invoicePage.getTotalPages())
//                .pageNumber(invoicePage.getNumber())
//                .pageSize(invoicePage.getSize())
//                .content(Collections.singletonList(responses))
//                .build();
//    }
//
//    private Invoice createInvoiceEntity(List<InvoiceRequest> invoiceRequests) {
//        double totalPrice = 0;
//        Invoice savedInvoice = Invoice.builder()
//                .note(invoiceRequests.get(0).getNote())
//                .statusAccept(StatusAccept.CREATED)
//                .createdAt(new Date())
//                .totalPrice(totalPrice)
//                .build();
//
//        return invoiceRepository.save(savedInvoice);
//    }
//
//    private Invoice saveInvoiceWithDetails(Invoice savedInvoice, List<InvoiceRequest> invoiceRequests) {
//        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
//        double totalPrice = 0;
//
//        for (InvoiceRequest invoiceRequest : invoiceRequests) {
//            Product product = getProductValidQuantityAndSave(invoiceRequest.getProductId(), invoiceRequest.getQuantity(), "create");
//            InvoiceDetail detail = invoiceDetailService.setDataInvoiceDetail(invoiceRequest.getQuantity(), product, savedInvoice);
//            totalPrice += invoiceRequest.getQuantity() * product.getPrice();
//            invoiceDetails.add(detail);
//        }
//
//        savedInvoice.setTotalPrice(totalPrice);
//        savedInvoice.setInvoiceDetails(invoiceDetails);
//
//        return invoiceRepository.save(savedInvoice);
//    }
//
//    private Invoice findInvoiceById(long id) {
//        return invoiceRepository.findById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));
//    }
//
//    private void validateStatusAccept(StatusAccept currentStatus, StatusAccept action) {
//        if (!validStatusAccept(currentStatus, action)) {
//            throw new APIException(ErrorCode.INVALID_STATUS_ACCEPT);
//        }
//    }
//
//    private boolean validStatusAccept(StatusAccept currentStatus, StatusAccept action) {
//        switch (action) {
//            case PENDING:
//                return currentStatus == StatusAccept.CREATED;
//            case SUCCESS:
//                return currentStatus == StatusAccept.PENDING;
//            case DELETED:
//                return currentStatus == StatusAccept.CREATED;
//            default:
//                return false;
//        }
//    }
//
//    private void handleProductQuantityDuringDeletion(Invoice invoice) {
//        invoice.getInvoiceDetails().forEach(detail -> {
//            try {
//                getProductValidQuantityAndSave(detail.getProduct().getId(), detail.getQuantity(), "delete");
//            } catch (Exception e) {
//                throw new APIException(ErrorCode.INVALID_DELETE_INVOICE);
//            }
//        });
//    }
//
//    private Product getProductValidQuantityAndSave(long productId, int quantity, String requirement) {
//        Product product = productService.getProductByIdToMakeInvoice(productId);
//        int finalQuantity = calculateProductQuantity(product, quantity, requirement);
//        product.setQuantity(finalQuantity);
//        return productService.updateProductQuantity(product);
//    }
//
//    private int calculateProductQuantity(Product product, int quantity, String requirement) {
//        int finalQuantity = 0;
//        switch (requirement) {
//            case "create":
//                finalQuantity = product.getQuantity() - quantity;
//                if (finalQuantity < 0) {
//                    throw new APIException(ErrorCode.INVALID_QUANTITY);
//                }
//                break;
//            case "delete":
//                finalQuantity = product.getQuantity() + quantity;
//                break;
//            default:
//                throw new APIException(ErrorCode.INVALID_QUANTITY);
//        }
//        return finalQuantity;
//    }
}
