package com.petcare.repository;

import com.petcare.entity.Medicine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends ElasticsearchRepository<Medicine, Long> {
}
