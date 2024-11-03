package com.pet_care.payment_service.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayOSService {

    @Value("${PAYOS_CLIENT_ID}")
    String clientId;

    @Value("${PAYOS_API_KEY}")
    String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    String checksumKey;

    public CheckoutResponseData createPaymentQRCode() throws Exception {
        final PayOS payOS = new PayOS(clientId, apiKey, checksumKey);
        Long orderCode = System.currentTimeMillis() / 1000;
        String domain = "https://tsm885rc-5173.asse.devtunnels.ms";
        ItemData itemData = ItemData
                .builder()
                .name("Mỳ tôm Hảo Hảo ly")
                .quantity(1)
                .price(10000)
                .build();

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(orderCode)
                .amount(10000)
                .description("Thanh toán đơn hàng")
                .returnUrl(domain)
                .cancelUrl(domain)
                .item(itemData)
                .build();

        CheckoutResponseData result = payOS.createPaymentLink(paymentData);
        result.setQrCode(
                "https://quickchart.io/qr?text=" + result.getQrCode().replace(" ", "%20") +
                        "&centerImageUrl=https://res.cloudinary.com/dprkvtle0/image/upload/v1730557423/image_7_dt4t0t.png"
        );
        return result;
    }
}
