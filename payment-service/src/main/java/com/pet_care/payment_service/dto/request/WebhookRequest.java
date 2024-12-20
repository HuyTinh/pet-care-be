package com.pet_care.payment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebhookRequest {

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Data {
        private long id;
        private String tid;
        private String description;
        private double amount;
        private double cusum_balance;
        private String when;
        private String bank_sub_acc_id;
        private String subAccId;
        private String bankName;
        private String bankAbbreviation;
        private String virtualAccount;
        private String virtualAccountName;
        private String corresponsiveName;
        private String corresponsiveAccount;
        private String corresponsiveBankId;
        private String corresponsiveBankName;
    }

    int error;
    List<Data> data;
}
