package com.pet_care.product_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailRequest {

     Long productId;  // ID của sản phẩm

     Integer quantity;  // Số lượng sản phẩm

     Double price;  // Giá sản phẩm, nếu có (nếu không thì sẽ được tính từ Product)

     Double totalPrice;

}
