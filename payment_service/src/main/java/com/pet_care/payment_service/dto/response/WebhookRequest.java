package com.pet_care.payment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebhookResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static
    class Data {
        private long id;
        private long tid;
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
