package com.pet_care.report_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionSaleReportRequest {
    Date date;
    Double sale;
}
