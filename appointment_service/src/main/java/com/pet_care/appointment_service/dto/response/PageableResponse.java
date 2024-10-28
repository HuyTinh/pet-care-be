package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableResponse<T> {

    List<T> content;

    @JsonProperty("page_number")
    int pageNumber;

    @JsonProperty("page_size")
    int pageSize;

    @JsonProperty("total_pages")
    int totalPages;

}
