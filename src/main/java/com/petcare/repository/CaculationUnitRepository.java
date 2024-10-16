package com.petcare.repository;

import com.petcare.entity.CaculationUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaculationUnitRepository extends ElasticsearchRepository<CaculationUnit, Long> {
}
