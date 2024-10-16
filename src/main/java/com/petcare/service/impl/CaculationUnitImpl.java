package com.petcare.service.impl;

import com.petcare.entity.CaculationUnit;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.CaculationUnitRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaculationUnitImpl implements EntityService<CaculationUnit, Long> {

    @Autowired
    private CaculationUnitRepository caculationUnitRepository;

    @Override
    public List<CaculationUnit> getAllEntity() {

        List<CaculationUnit> caculationUnits = ArrayMapper.mapperIterableToList(caculationUnitRepository.findAll());

        return caculationUnits;
    }

    @Override
    public Optional<CaculationUnit> getEntityById(Long id) {

        Optional<CaculationUnit> caculationUnit = caculationUnitRepository.findById(id);

        return caculationUnit;
    }

    @Override
    public CaculationUnit createEntity(CaculationUnit caculationUnit) {

        Optional<CaculationUnit> savedCaculationUnit = getEntityById(caculationUnit.getId());
        if (savedCaculationUnit.isPresent()) {
            throw new APIException(ErrorCode.CACULATION_UNIT_ALREADY_EXISTS);
        }

        return caculationUnitRepository.save(caculationUnit);
    }

    @Override
    public CaculationUnit updateEntity(CaculationUnit caculationUnit) {

        CaculationUnit getCaculationUnit = getEntityById(caculationUnit.getId())
                .orElseThrow(() -> new APIException(ErrorCode.CACULATION_UNIT_NOT_FOUND));

        return caculationUnitRepository.save(caculationUnit);
    }

    @Override
    public void deleteEntity(Long id) {

        CaculationUnit getCaculationUnit = getEntityById(id)
                .orElseThrow(() -> new APIException(ErrorCode.CACULATION_UNIT_NOT_FOUND));

        caculationUnitRepository.delete(getCaculationUnit);
    }
}
