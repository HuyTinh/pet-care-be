package com.petcare.repository;

import com.petcare.entity.Manufacturer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends ElasticsearchRepository<Manufacturer, Long> {
}
