package com.pet_care.warehouse_manager_service.repositories;

import com.pet_care.warehouse_manager_service.entity.Caculation_Unit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaculationUnitRepository extends JpaRepository<Caculation_Unit,Long> {
    List<Caculation_Unit> findByStatusTrue();

    @Modifying
    @Transactional
    @Query(value = "update caculation_units set status = true where id in ?1 ")
    Integer deleteCaculations(List<Long> ids);
}
