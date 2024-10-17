package com.pet_care.warehouse_manager_service.repositories;

import com.pet_care.warehouse_manager_service.entity.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByStatusTrue();

//    List<Location> findAllById(List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "update locations  " +
            " set status = true where id in ?1 ")
    Integer deleteLocations(List<Long> ids);
}
