package com.petcare.service.impl;

import com.petcare.entity.Payment;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.PaymentRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentImpl implements EntityService<Payment, Long> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllEntity() {

        List<Payment> payments = ArrayMapper.mapperIterableToList(paymentRepository.findAll());

        return payments;
    }

    @Override
    public Optional<Payment> getEntityById(Long id) {

        Optional<Payment> payment = paymentRepository.findById(id);

        return payment;
    }

    @Override
    public Payment createEntity(Payment payment) {

        Optional<Payment> paymentOptional = paymentRepository.findById(payment.getId());

        if (paymentOptional.isPresent()) {
            throw new APIException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updateEntity(Payment payment) {

        Payment paymentOptional = paymentRepository.findById(payment.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PAYMENT_NOT_FOUND));

        return paymentRepository.save(payment);
    }

    @Override
    public void deleteEntity(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PAYMENT_NOT_FOUND));

        paymentRepository.delete(payment);
    }
}
