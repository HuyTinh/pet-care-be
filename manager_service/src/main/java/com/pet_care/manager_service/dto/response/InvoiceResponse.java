package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    Long invoice_id;
    LocalDate create_date;
    String payment_status;
    Double total;
    Set<InvoiceServiceDetailResponse> invoice_service_detail;
    Set<InvoiceMedicineDetailResponse> invoice_medicine_detail;
}
