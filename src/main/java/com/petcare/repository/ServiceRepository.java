package com.petcare.repository;

import com.petcare.entity.Services;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends ElasticsearchRepository<Services, Long> {
}
