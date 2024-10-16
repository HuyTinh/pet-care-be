package com.petcare.repository;

import com.petcare.entity.Prescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends ElasticsearchRepository<Prescription, Long> {
}
