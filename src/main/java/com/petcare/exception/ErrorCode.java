package com.petcare.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    APPOINTMENT_NOT_FOUND(2001, "Appointment id is not found"),
    APPOINTMENT_ALREADY_EXISTS(2002, "Appointment already exists"),

    APPOINTMENT_SERVICE_NOT_FOUND(2003, "Appointment Service id is not found"),
    APPOINTMENT_SERVICE_ALREADY_EXISTS(2004, "Appointment Service id already exists"),

    CACULATION_UNIT_NOT_FOUND(2005, "Caculation Unit id is not found"),
    CACULATION_UNIT_ALREADY_EXISTS(2006, "Caculation Unit id already exists"),

    INVOICE_NOT_FOUND(2007, "Invocie id is not found"),
    INVOICE_ALREADY_EXISTS(2008, "Invocie id already exists"),

    INVOICE_MEDICINE_DETAIL_NOT_FOUND(2009, "Invoice Medicine Detail id is not found"),
    INVOICE_MEDICINE_DETAIL_ALREADY_EXISTS(2010, "Invoice Medicine Detail id already exists"),

    INVOICE_SERVICE_DETAIL_NOT_FOUND(2011, "Invoice Service Detail id is not found"),
    INVOICE_SERVICE_DETAIL_ALREADY_EXISTS(2012, "Invoice Service Detail id already exists"),

    LOCATION_NOT_FOUND(2013, "Location id is not found"),
    LOCATION_ALREADY_EXISTS(2014, "Location id already exists"),

    MANUFACTURER_NOT_FOUND(2015, "Manufacturer id is not found"),
    MANUFACTURER_ALREADY_EXISTS(2016, "Manufacturer id already exists"),

    MEDICINE_NOT_FOUND(2017, "Medicine id is not found"),
    MEDICINE_ALREADY_EXISTS(2018, "Medicine id already exists"),

    OWNER_NOT_FOUND(2019, "Owner id is not found"),
    OWNER_ALREADY_EXISTS(2020, "Owner id already exists"),

    PAYMENT_NOT_FOUND(2021, "Payment id is not found"),
    PAYMENT_ALREADY_EXISTS(2022, "Payment id already exists"),

    PET_NOT_FOUND(2023, "Pet id is not found"),
    PET_ALREADY_EXISTS(2024, "Pet id already exists"),

    PRESCRIPTION_NOT_FOUND(2025, "Prescription id is not found"),
    PRESCRIPTION_ALREADY_EXISTS(2026, "Prescription id already exists"),

    PRESCRIPTION_DETAIL_NOT_FOUND(2027, "Prescription Detail id is not found"),
    PRESCRIPTION_DETAIL_ALREADY_EXISTS(2028, "Prescription Detail id already exists"),

    PROFILE_NOT_FOUND(2029, "Profile id is not found"),
    PROFILE_ALREADY_EXISTS(2030, "Profile id already exists"),

    ROLE_NOT_FOUND(2031, "Role id is not found"),
    ROLE_ALREADY_EXISTS(2032, "Role id already exists"),

    SERVICE_NOT_FOUND(2033, "Service id is not found"),
    SERVICE_ALREADY_EXISTS(2034, "Service id already exists"),

    SERVICE_TYPE_NOT_FOUND(2035, "Service Type id is not found"),
    SERVICE_TYPE_ALREADY_EXISTS(2036, "Service Type id already exists"),

    SPECIES_NOT_FOUND(2037, "Species id is not found"),
    SPECIES_ALREADY_EXISTS(2038, "Species id already exists"),

    ACCOUNT_NOT_FOUND(20039, "Account id is not found"),
    ACCOUNT_ALREADY_EXISTS(20040, "Account id already exists"),

    INVALID_ID(2100, "Invalid id request !!"),
    INVALID_INDEX_NAME(2101, "Error from Backend, check indexName again!!");

    int code;

    String message;

}
