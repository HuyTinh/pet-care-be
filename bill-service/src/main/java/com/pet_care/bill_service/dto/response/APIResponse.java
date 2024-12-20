package com.pet_care.bill_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes fields with null values from the JSON output
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponse<T> {
    @Builder.Default
    int code = 1000;
    String message;
    T data;
}
