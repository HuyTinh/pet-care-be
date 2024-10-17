package com.pet_care.warehouse_manager_service.service.serviceImpl;

import com.pet_care.warehouse_manager_service.entity.Caculation_Unit;
import com.pet_care.warehouse_manager_service.repositories.CaculationUnitRepository;
import com.pet_care.warehouse_manager_service.service.CaculationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaculationUnitServiceImpl implements CaculationUnitService {
    @Autowired
    private CaculationUnitRepository caculationUnitRepository;

    public List<Caculation_Unit> getCaculationUnitTrue(){

        return caculationUnitRepository.findByStatusTrue();
    }

    public <S extends Caculation_Unit> S create(S entity) {
        return caculationUnitRepository.save(entity);
    }

    public <S extends Caculation_Unit> S update(S entity) {
        return caculationUnitRepository.save(entity);
    }

    public List<Caculation_Unit> findAll() {
        return caculationUnitRepository.findAll();
    }

    public void deleteCaculationunits (List<Long> ids){
        caculationUnitRepository.deleteCaculations(ids);
    }
}
