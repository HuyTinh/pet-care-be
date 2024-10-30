package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.*;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.InvoiceMedicineDetailService;
import com.pet_care.manager_service.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    private InvoiceMedicineDetailRepository invoiceMedicineDetailRepository;

    @Autowired
    private InvoiceServiceRepository invoiceServiceRepository;

    @Autowired
    private AppointmentServiceRepository appointmentServiceRepository;

    @Override
    public InvoiceReportResponse getInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date) {
        return convertInvoiceReport(from_date, to_date, create_date);
    }

    @Override
    public Set<InvoiceResponse> getSetInvoice(LocalDate from_date, LocalDate to_date, LocalDate create_date) {
        List<Invoice> listInvoice = invoiceRepository.getInvoiceByDate(from_date,to_date,create_date);
//        listInvoice.stream()
//                .sorted(Comparator.comparing(Invoice::getId))
//                .collect(Collectors.toList());

        Set<InvoiceResponse> invoiceSets = new HashSet<>();
        for (Invoice invoice : listInvoice) {
            InvoiceResponse response = getInvoice(invoice.getId());
            invoiceSets.add(response);
        }
        Set<InvoiceResponse> sortedInvoiceSets = invoiceSets.stream()
                .sorted(Comparator.comparing(InvoiceResponse::getInvoice_id).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortedInvoiceSets;
    }

    public InvoiceReportResponse convertInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date) {
        Optional<Object[]> getRevenue = invoiceRepository.getRevenueByDate(from_date, to_date, create_date);
        if(getRevenue.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_NOT_EXIST);
        }
        Object[] invoiceReport = (Object[]) getRevenue.get()[0];
        Long count_invoice = (Long) invoiceReport[0];
        Double total_price = (Double) invoiceReport[1];

        return InvoiceReportResponse.builder()
                .count_invoice(count_invoice)
                .total_invoice(total_price)
                .invoiceResponseSet(getSetInvoice(from_date, to_date, create_date))
                .build();
    }

    @Override
    public InvoiceResponse getInvoice(Long id){
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);
        if(invoiceOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_NOT_EXIST);
        }
        Invoice invoice = invoiceOptional.get();
        return InvoiceResponse.builder()
                .invoice_id(invoice.getId())
                .total(invoice.getTotal())
                .create_date(invoice.getCreate_date())
                .payment_status(invoice.getPayment_status())
                .invoice_service_detail(getInvoiceServiceDetailResponses(invoice.getId()))
                .invoice_medicine_detail(getInvoiceMedicineDetails(invoice.getId()))
                .build();
    }

    @Override
    public Set<RevenueAndAppointmentResponse> getRevenueAndAppointment(Long id) {
        List<Object[]> listRevenueAndAppointment = invoiceRepository.getInvoiceAndAppointmentByYear(id);
        Set<RevenueAndAppointmentResponse> revenueAndAppointmentSet = new HashSet<>();
        for (Object[] objects : listRevenueAndAppointment) {
            RevenueAndAppointmentResponse response = new RevenueAndAppointmentResponse();
            response.setMonth((Long) objects[0]);
            response.setMonthName((String) objects[1]);
            response.setTotal((Double) objects[2]);
            response.setAppointments((Long) objects[3]);
            revenueAndAppointmentSet.add(response);
        }
        Set<RevenueAndAppointmentResponse> sortRevenueAndAppointment = revenueAndAppointmentSet.stream()
                .sorted(Comparator.comparing(RevenueAndAppointmentResponse::getMonth))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortRevenueAndAppointment;
    }

    @Override
    public InvoiceResponse deleteInvoice(Long id) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);
        if(invoiceOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_NOT_EXIST);
        }
        Invoice invoice = invoiceOptional.get();
        invoice.setStatus(false);
        invoiceRepository.save(invoice);
        return InvoiceResponse.builder()
                .invoice_id(invoice.getId())
                .total(invoice.getTotal())
                .create_date(invoice.getCreate_date())
                .payment_status(invoice.getPayment_status())
                .invoice_service_detail(getInvoiceServiceDetailResponses(invoice.getId()))
                .invoice_medicine_detail(getInvoiceMedicineDetails(invoice.getId()))
                .build();
    }



    public InvoiceMedicineDetailResponse getIMD(Long invoiceMD_id){
        Optional<Invoice_Medicine_Detail> imdOptional = invoiceMedicineDetailRepository.findById(invoiceMD_id);
        if(imdOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_MEDICINE_DETAIL_NOT_EXIST);
        }
        Invoice_Medicine_Detail imd = imdOptional.get();
        return InvoiceMedicineDetailResponse.builder()
                .id(imd.getId())
                .price(imd.getPrice())
                .quantity(imd.getQuantity())
                .note(imd.getNote())
                .medicineResponse(getMedicine(imd.getId()))
                .build();
    }

    public MedicineResponse getMedicine(Long invoiceMD_id){
        Optional<Invoice_Medicine_Detail> imdOptional = invoiceMedicineDetailRepository.findById(invoiceMD_id);
        if(imdOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_MEDICINE_DETAIL_NOT_EXIST);
        }
        Invoice_Medicine_Detail imd = imdOptional.get();
        Medicine medicine = medicineRepository.findById(imd.getMedicine().getId()).get();

        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .build();
    }

    public Set<InvoiceMedicineDetailResponse> getInvoiceMedicineDetails(Long invoice_id){
        List<Invoice_Medicine_Detail> listIMD = invoiceMedicineDetailRepository.findByInvoice_id(invoice_id);
        Set<InvoiceMedicineDetailResponse> getIMD = new HashSet<>();
        for (Invoice_Medicine_Detail imd : listIMD) {
            InvoiceMedicineDetailResponse imdResponse = getIMD(imd.getId());
            getIMD.add(imdResponse);
        }
        return getIMD;
    }

    public Set<InvoiceServiceDetailResponse> getInvoiceServiceDetailResponses(Long invoice_id){
        List<Invoice_Service_Detail> listISD = invoiceServiceRepository.findByInvoiceId(invoice_id);
        Set<InvoiceServiceDetailResponse> setISD = new HashSet<>();
        for (Invoice_Service_Detail imd : listISD) {
            InvoiceServiceDetailResponse isdResponse = getISD(imd.getId());
            setISD.add(isdResponse);
        }
        return setISD;
    }

    public InvoiceServiceDetailResponse getISD(Long isd_id){
        Optional<Invoice_Service_Detail> isdOptional = invoiceServiceRepository.findById(isd_id);
        if(isdOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_SERVICE_DETAIL_NOT_EXIST);
        }
        Invoice_Service_Detail isdResponse = isdOptional.get();
        return InvoiceServiceDetailResponse.builder()
                .id(isdResponse.getId())
                .appointmentServiceResponse(getAppointmentService(isdResponse.getId()))
                .build();
    }

    public AppointmentServiceResponse getAppointmentService(Long isd_id){
        Optional<Invoice_Service_Detail> isdOptional = invoiceServiceRepository.findById(isd_id);
        if(isdOptional.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_SERVICE_DETAIL_NOT_EXIST);
        }
        Invoice_Service_Detail isdResponse = isdOptional.get();
        Optional<Appointment_Service> apsOptional = appointmentServiceRepository.findById(isdResponse.getAppointment_service().getId());
        if(apsOptional.isEmpty()){
            throw new AppException(ErrorCode.APPOINTMENT_SERVICE_NOT_EXIST);
        }
        Appointment_Service apsService = apsOptional.get();

        Services service = apsService.getServices();
        ServiceResponse serviceResponse = ServiceResponse.builder()
                .id(apsService.getId())
                .name(service.getName())
                .price(service.getPrice())
                .build();
        return AppointmentServiceResponse.builder()
                .id(apsService.getId())
                .serviceResponse(serviceResponse)
                .build();
    }
}
