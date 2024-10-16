package com.petcare.repository;

import com.petcare.entity.InvoiceMedicineDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceMedicineDetailRepository extends ElasticsearchRepository<InvoiceMedicineDetail, Long> {
}
