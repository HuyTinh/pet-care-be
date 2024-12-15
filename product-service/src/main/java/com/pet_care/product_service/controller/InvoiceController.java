package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.enums.StatusAccept;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.InvoiceRepository;
import com.pet_care.product_service.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("invoice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController 
{
    
    InvoiceService invoiceService;
    /**
     * Lọc hóa đơn dựa trên các tham số như số trang, từ khóa tìm kiếm, ngày tạo, trạng thái, khoảng giá và phân trang.
     *
     * @param pageNumber Số trang
     * @param pageSize Số lượng hóa đơn mỗi trang
     * @param note Từ khóa tìm kiếm trong nội dung ghi chú
     * @param customerId ID của khách hàng
     * @param statusAccept Trạng thái chấp nhận hóa đơn
     * @param minPrice Giá tối thiểu
     * @param maxPrice Giá tối đa
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param sortBy Trường sắp xếp (mặc định là "id")
     * @param sortOrder Hướng sắp xếp (mặc định là "asc")
     * @return PageableResponse chứa danh sách hóa đơn lọc được và thông tin phân trang
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<InvoiceResponse>> filterInvoices(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) StatusAccept statusAccept,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        PageableResponse<InvoiceResponse> response = invoiceService.filterInvoices(
                pageNumber, pageSize, note, customerId, statusAccept, minPrice, maxPrice,
                startDate, endDate, sortBy, sortOrder
        );

        return APIResponse.<PageableResponse<InvoiceResponse>>builder()
                .data(response)
                .build();
    }

    /**
     * Tạo hóa đơn mới.
     *
     * @param invoiceRequest Dữ liệu yêu cầu tạo hóa đơn
     * @return Hóa đơn đã được tạo
     */
    @PostMapping
    public APIResponse<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        // Gọi service để tạo hóa đơn
        InvoiceResponse invoiceResponse = invoiceService.createInvoice(invoiceRequest);

        // Trả về APIResponse với hóa đơn đã tạo
        return APIResponse.<InvoiceResponse>builder()
                .data(invoiceResponse)
                .build();
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to retrieve.
     * @return A response containing the invoice details.
     */
    @GetMapping("{invoiceId}")
    public APIResponse<Invoice> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
        return APIResponse.<Invoice>builder()
                .data(invoiceService.getInvoiceById(invoiceId))
                .build();
    }

//    InvoiceRepository invoiceRepository;
//
//    @GetMapping("/{page}")
//    public APIResponse<List<Invoice>> getAllInvoice(@PathVariable("page") int _page)
//    {
//        List<Invoice> response = invoiceRepository.findAll();
//
//        return APIResponse.<List<Invoice>>builder()
//                .data(response)
//                .build();
//    }

//    @GetMapping("/{page}")
//    public APIResponse<PageableResponse> getAllInvoice(@PathVariable("page") int _page)
//    {
//        PageableResponse response = invoiceService.getAllInvoice(_page);
//
//        return APIResponse.<PageableResponse>builder()
//                .data(response)
//                .build();
//    }

//    @PostMapping("/create")
//    public APIResponse<Invoice> createInvoice(@RequestBody List<InvoiceRequest> _invoiceRequests)
//    {
//        Invoice response = invoiceService.createInvoice(_invoiceRequests);
//
//        return APIResponse.<Invoice>builder()
//                .data(response)
//                .build();
//    }

//    @PostMapping("/create")
//    public APIResponse<InvoiceResponse> createInvoice(@RequestBody List<InvoiceRequest> _invoiceRequests)
//    {
//        InvoiceResponse response = invoiceService.createInvoice(_invoiceRequests);
//
//        return APIResponse.<InvoiceResponse>builder()
//                .data(response)
//                .build();
//    }
//
//    @PutMapping("/accept/{invoiceId}")
//    public APIResponse<InvoiceResponse> acceptInvoice(@PathVariable("invoiceId") Long _invoiceId)
//    {
//        InvoiceResponse response = invoiceService.updateStatusInvoice(_invoiceId, StatusAccept.PENDING);
//
//        return APIResponse.<InvoiceResponse>builder()
//                .data(response)
//                .build();
//    }
//
//    @PutMapping("/success/{invoiceId}")
//    public APIResponse<InvoiceResponse> successInvoice(@PathVariable("invoiceId") Long _invoiceId)
//    {
//        InvoiceResponse response = invoiceService.updateStatusInvoice(_invoiceId, StatusAccept.SUCCESS);
//
//        return APIResponse.<InvoiceResponse>builder()
//                .data(response)
//                .build();
//    }
//
//    @PutMapping("/detele/{invoiceId}")
//    public APIResponse<InvoiceResponse> deleteInvoice(@PathVariable("invoiceId") Long _invoiceId)
//    {
//        InvoiceResponse response = invoiceService.deleteInvoice(_invoiceId, StatusAccept.DELETED);
//
//        return APIResponse.<InvoiceResponse>builder()
//                .data(response)
//                .build();
//    }
    
}
