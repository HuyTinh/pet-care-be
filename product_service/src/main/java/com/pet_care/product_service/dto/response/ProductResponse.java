package com.pet_care.product_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse
{

    Long id;

    String name;

    String description;

    Integer price;

    Integer quantity;

    @JsonProperty("image_url")
    String imageUrl;

    String category;

}
