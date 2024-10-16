package com.petcare.repository;

import com.petcare.entity.PrescriptionDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionDetailRepository extends ElasticsearchRepository<PrescriptionDetail, Long> {
}
