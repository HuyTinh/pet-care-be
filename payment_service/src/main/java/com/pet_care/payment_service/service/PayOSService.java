package com.pet_care.payment_service.service;

import com.pet_care.payment_service.client.BillClient;
import com.pet_care.payment_service.dto.request.PaymentRequest;
import com.pet_care.payment_service.dto.request.WebhookRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayOSService {

    PayOS payOS;

    BillClient billClient;

    public CheckoutResponseData createPaymentQRCode(PaymentRequest paymentRequest) throws Exception {

        Instant now = Instant.now();

        Instant expiredAt = now.plus(30, ChronoUnit.MINUTES);

        long expiredAtSeconds = expiredAt.getEpochSecond();

        String domain = "https://tsm885rc-5173.asse.devtunnels.ms";

        List<ItemData> itemsData = new ArrayList<>();

        paymentRequest.getMedicines().forEach(medicinePrescriptionResponse -> itemsData.add(
                ItemData.builder()
                .name(medicinePrescriptionResponse.getName())
                        .quantity(
                                (int) Math.ceil(medicinePrescriptionResponse.getQuantity())
                        )
                        .price(
                                (int) Math.ceil(medicinePrescriptionResponse.getTotalMoney()) *
                                        (int) Math.ceil(medicinePrescriptionResponse.getQuantity())
                        )
                .build()
        ));

        paymentRequest.getServices().forEach(hospitalServiceResponse -> itemsData.add(
                ItemData.builder()
                        .name(hospitalServiceResponse.getName())
                        .quantity(1)
                        .price(
                                (int) Math.ceil(hospitalServiceResponse.getPrice())
                        )
                        .build()
        ));

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(paymentRequest.getOrderId())
                .amount(
                        (int) Math.ceil( paymentRequest.getTotalMoney())
                )
                .description("Thanh toán đơn hàng")
                .returnUrl(domain)
                .cancelUrl(domain)
                .items(itemsData)
                .expiredAt(expiredAtSeconds)
                .build();

        CheckoutResponseData result = payOS.createPaymentLink(paymentData);
        result.setQrCode(
                "https://quickchart.io/qr?text=" + result.getQrCode().replace(" ", "%20") +
                        "&centerImageUrl=https://res.cloudinary.com/dprkvtle0/image/upload/v1730557423/image_7_dt4t0t.png"
        );
        return result;
    }

    public Integer cancelPaymentLink(Integer orderId) throws Exception {
        try {
            payOS.cancelPaymentLink(orderId,"");
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
        return 1;
    }

    public Long getOrderCode(WebhookRequest webhookRequest) throws Exception {

        String text = webhookRequest.getData().get(0).getDescription();

        int lastDashIndex = text.lastIndexOf('-');

        String code = text.substring(lastDashIndex + 1, text.indexOf(' ', lastDashIndex));

        return billClient.getInvoiceIdByOSId(code).getData();
    }

//    public Long getOrderCode(WebhookRequest webhookRequest) throws Exception {
//
//        String text = webhookRequest.getData().get(0).getDescription();
//
//        int lastDashIndex = text.lastIndexOf('-');
//
//        String code = text.substring(lastDashIndex + 1, text.indexOf(' ', lastDashIndex));
//
//        billClient.getBillByDescriptionCode(code);
//
//        return payOS.getPaymentLinkInformation(2L).getOrderCode();
//    }

}
