package com.pet_care.manager_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {

    ACCOUNT_NOTFOUND(2001, "Account Not Found"),
    ACCOUNT_ALREADY_EXIST(2004, "Account Already Exist"),
    ACCOUNT_IS_EMPTY(2005, "Account Is Empty"),
    ROLE_NOTFOUND(2002, "Role Not Found"),
    ROLE_IS_EMPTY(2003, "List Role Is Empty"),
    CUSTOMER_NOTFOUND(2006, "Customer Not Found"),
    SERVICE_NOTFOUND(2015, "Service Not Found"),

    PET_NOTFOUND(2030, "Pet Not Found"),
    INVOICE_NOT_EXIST(2050, "Invoice Not Exist"),
    PRESCRIPTION_NOTFOUND(2040, "Prescription Not Found"),
    INVOICE_MEDICINE_DETAIL_NOT_EXIST(2060, "Invoice Medicine Detail Not Exist"),
    INVOICE_SERVICE_DETAIL_NOT_EXIST(2070, "Invoice Service Detail Not Exist"),
    APPOINTMENT_SERVICE_NOT_EXIST(2070, "Appointment Service Not Exist"),

    ;
    int code;
    String message;
}
