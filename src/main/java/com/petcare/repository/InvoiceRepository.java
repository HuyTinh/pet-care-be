package com.petcare.repository;

import com.petcare.entity.Invoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends ElasticsearchRepository<Invoice, Long> {
}
