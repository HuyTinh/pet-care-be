package com.petcare.repository;

import com.petcare.entity.ServiceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends ElasticsearchRepository<ServiceType, Long> {
}
