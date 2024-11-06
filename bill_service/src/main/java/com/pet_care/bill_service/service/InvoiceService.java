package com.pet_care.bill_service.service;

import com.pet_care.bill_service.client.PaymentClient;
import com.pet_care.bill_service.client.PrescriptionClient;
import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.bill_service.dto.request.PaymentRequest;
import com.pet_care.bill_service.dto.response.CheckoutResponseData;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.dto.response.MedicinePrescriptionResponse;
import com.pet_care.bill_service.enums.PaymentMethod;
import com.pet_care.bill_service.exception.APIException;
import com.pet_care.bill_service.exception.ErrorCode;
import com.pet_care.bill_service.mapper.InvoiceMapper;
import com.pet_care.bill_service.model.Invoice;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;

    InvoiceMapper invoiceMapper;

    PaymentClient paymentClient;

    private final PrescriptionClient prescriptionClient;

    /**
     * @return
     */
    public List<InvoiceResponse> getAllInvoice() {

        List<InvoiceResponse> invoiceResponseList = invoiceRepository.findAll().stream().map(invoiceMapper::toDto).toList();

        log.info("Get all invoice");

        return invoiceResponseList;
    }

    /**
     * @param id
     * @return
     */
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
                    .services(invoiceResponse.getPrescription().getAppointmentResponse().getServices())
                    .totalMoney(invoiceCreateRequest.getTotalMoney())
                    .build();

            Set<MedicinePrescriptionResponse> petPrescriptionResponses = new HashSet<>();

            invoiceResponse.getPrescription().getDetails().forEach(
                    petPrescriptionResponse -> petPrescriptionResponses.addAll(petPrescriptionResponse.getMedicines())
            );

            paymentRequest.setMedicines(petPrescriptionResponses);

            CheckoutResponseData checkoutResponse = paymentClient.getPaymentLink(paymentRequest).getData();

            invoiceRepository.updatePaymentPayOSId(
                    checkoutResponse.getDescription().substring(0,11),
                    saveInvoice.getId()
            );

            invoiceResponse.setCheckoutResponse(checkoutResponse);
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

}
