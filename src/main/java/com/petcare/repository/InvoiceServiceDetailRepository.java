package com.petcare.repository;

import com.petcare.entity.InvoiceServiceDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceServiceDetailRepository extends ElasticsearchRepository<InvoiceServiceDetail, Long> {
}
