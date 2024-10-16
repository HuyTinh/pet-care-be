package com.petcare.repository;

import com.petcare.entity.AppointmentService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentServiceRepository extends ElasticsearchRepository<AppointmentService, Long> {
}
