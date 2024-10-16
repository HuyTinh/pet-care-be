package com.petcare.repository;

import com.petcare.entity.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ElasticsearchRepository<Role, Long> {
}
