package com.pet_care.product_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.product_service.enums.StatusAccept;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRequest
{

     String note;  // Ghi chú của hóa đơn

     Long customerId;  // ID của khách hàng
     Double shippingFee;
     String fullName;
     String phoneNumber;
     String address;

     StatusAccept statusAccept;  // Trạng thái chấp nhận hóa đơn

     List<InvoiceDetailRequest> invoiceDetails;  // Danh sách chi tiết hóa đơn

}
