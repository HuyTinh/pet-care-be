package com.pet_care.bill_service.service;

import com.pet_care.bill_service.client.PaymentClient;
import com.pet_care.bill_service.client.PrescriptionClient;
import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.bill_service.dto.request.PaymentRequest;
import com.pet_care.bill_service.dto.response.CheckoutResponseData;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.dto.response.PetMedicineResponse;
import com.pet_care.bill_service.entity.VeterinaryCare;
import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.enums.PaymentMethod;
import com.pet_care.bill_service.enums.PrescriptionStatus;
import com.pet_care.bill_service.exception.APIException;
import com.pet_care.bill_service.exception.ErrorCode;
import com.pet_care.bill_service.mapper.InvoiceMapper;
import com.pet_care.bill_service.entity.Invoice;
import com.pet_care.bill_service.repository.InvoiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {

    InvoiceRepository invoiceRepository;

    InvoiceMapper invoiceMapper;

    PaymentClient paymentClient;

    PrescriptionClient prescriptionClient;
    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoice() {

        List<InvoiceResponse> invoiceResponseList = invoiceRepository.findAll().stream().map(invoiceMapper::toDto).toList();

        log.info("Get all invoice");

        return invoiceResponseList;
    }

    /**
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        InvoiceResponse invoiceResponse = invoiceRepository.findById(id).map(invoiceMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));

        log.info("Get invoice by id: {}", id);

        return invoiceResponse;
    }

    /**
     * @param invoiceCreateRequest
     * @return
     */
    public InvoiceResponse createInvoice(InvoiceCreateRequest invoiceCreateRequest) {
        Invoice saveInvoice = invoiceRepository.save(invoiceMapper.toEntity(invoiceCreateRequest));

        InvoiceResponse invoiceResponse = invoiceMapper.toDto(saveInvoice);

        invoiceResponse.setPrescription(prescriptionClient
                .getPrescriptionById(invoiceCreateRequest.getPrescriptionId()).getData());

        if(invoiceCreateRequest.getPaymentMethod().equals(PaymentMethod.BANKING)){

            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderId(invoiceResponse.getId())
                    .totalMoney(invoiceCreateRequest.getTotalMoney())
                    .build();

            CompletableFuture<Void> petPrescriptionResponsesFuture = CompletableFuture.runAsync(() -> {
                Set<PetMedicineResponse> petPrescriptionResponses = new HashSet<>();

                invoiceResponse.getPrescription().getDetails().parallelStream().forEach(
                        petPrescriptionResponse -> petPrescriptionResponses.addAll(petPrescriptionResponse.getMedicines())
                );

                paymentRequest.setMedicines(petPrescriptionResponses);
            });

            CompletableFuture<Void> petVeterinaryCareResponsesFuture = CompletableFuture.runAsync(() -> {
                Set<VeterinaryCare> petVeterinaryCareResponses = new HashSet<>();

                invoiceResponse.getPrescription().getDetails().parallelStream().forEach(petPrescriptionResponse -> petVeterinaryCareResponses.addAll(petPrescriptionResponse.getVeterinaryCares().stream().map(petVeterinaryCareResponse -> VeterinaryCare.builder()
                        .name(petVeterinaryCareResponse.getVeterinaryCare())
                        .price(petVeterinaryCareResponse.getTotalMoney())
                        .build()).collect(Collectors.toSet())));

                paymentRequest.setServices(petVeterinaryCareResponses);
            });

            CompletableFuture<CheckoutResponseData> checkoutResponseFuture = CompletableFuture.supplyAsync(()-> paymentClient.getPaymentLink(paymentRequest).getData());

            return CompletableFuture.allOf(checkoutResponseFuture,
                    petVeterinaryCareResponsesFuture,
                    petPrescriptionResponsesFuture).thenApply(res -> {
                CheckoutResponseData checkoutResponseData = checkoutResponseFuture.join();

                invoiceRepository.updatePaymentPayOSId(
                        checkoutResponseData.getDescription().substring(0,11),
                        saveInvoice.getId()
                );

                invoiceResponse.setCheckoutResponse(checkoutResponseData);

                return invoiceResponse;
            }).join();

        } else if (invoiceCreateRequest.getPaymentMethod().equals(PaymentMethod.CASH)) {

            if(invoiceRepository.changeStatus(saveInvoice.getId(), InvoiceStatus.SUCCESS) > 0 ){
                invoiceResponse.setStatus(InvoiceStatus.SUCCESS);
            }
            invoiceResponse.getPrescription().setStatus(PrescriptionStatus.APPROVED);
            prescriptionClient.approvedPrescription(invoiceResponse.getPrescription().getId());
        }

        return  invoiceResponse;
    }

    /**
     * @param invoiceUpdatePayOSIdRequest
     * @return
     */
    public InvoiceResponse updateInvoicePayOSId(InvoiceUpdatePayOSIdRequest invoiceUpdatePayOSIdRequest) {
        if(invoiceRepository.updatePaymentPayOSId(invoiceUpdatePayOSIdRequest.getPayOSId(),
                invoiceUpdatePayOSIdRequest.getInvoiceId()) > 0) {
            return invoiceRepository.findById(invoiceUpdatePayOSIdRequest.getInvoiceId()).map(invoiceMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));
        }
        return null;
    }

    /**
     * @param payOSId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getInvoiceIdByPayOSId(String payOSId) {
        return invoiceRepository.getInvoiceIdByPayOSId(payOSId);
    }

    public Integer approvedInvoice(Long invoiceId) {
        return invoiceRepository.changeStatus(invoiceId, InvoiceStatus.SUCCESS);
    }

    public Integer canceledInvoice(Long invoiceId) {
        return invoiceRepository.changeStatus(invoiceId, InvoiceStatus.CANCEL);
    }
}
