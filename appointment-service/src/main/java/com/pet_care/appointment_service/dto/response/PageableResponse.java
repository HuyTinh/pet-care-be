package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * A pageable response structure used for pagination in API responses.
 * It wraps the list of content and pagination information.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableResponse<T> {

    // List of content to be returned in the current page
    List<T> content;

    // Current page number (starting from 0)
    @JsonProperty("page_number")
    int pageNumber;

    // The number of items per page
    @JsonProperty("page_size")
    int pageSize;

    // Total number of pages available
    @JsonProperty("total_pages")
    int totalPages;
}
