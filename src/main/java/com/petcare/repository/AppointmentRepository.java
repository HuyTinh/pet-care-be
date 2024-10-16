package com.petcare.repository;

import com.petcare.entity.Appointment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends ElasticsearchRepository<Appointment, Long> {
}
