package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * A DTO for paginated response containing a list of items.
 * @param <T> The type of content in the response.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableResponse<T> {

    /**
     * The list of content items for the current page.
     */
    List<T> content;

    /**
     * The current page number.
     */
    @JsonProperty("page_number")
    int pageNumber;

    /**
     * The number of items per page.
     */
    @JsonProperty("page_size")
    int pageSize;

    /**
     * The total number of pages available.
     */
    @JsonProperty("total_pages")
    int totalPages;

}
