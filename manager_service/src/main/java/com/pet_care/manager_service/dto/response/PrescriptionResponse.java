package com.pet_care.manager_service.dto.response;

import com.pet_care.manager_service.entity.Pet;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class PrescriptionResponse {
    Long id;
    Date create_date;
    String note;
    Set<PresriptionDetailResponse> presriptionDetailResponse;
}
