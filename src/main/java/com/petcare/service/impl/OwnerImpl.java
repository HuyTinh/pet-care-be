package com.petcare.service.impl;

import com.petcare.entity.Owner;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.OwnerRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerImpl implements EntityService<Owner, Long> {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public List<Owner> getAllEntity() {

        List<Owner> owners = ArrayMapper.mapperIterableToList(ownerRepository.findAll());

        return owners;
    }

    @Override
    public Optional<Owner> getEntityById(Long id) {

        Optional<Owner> owner = ownerRepository.findById(id);

        return owner;
    }

    @Override
    public Owner createEntity(Owner owner) {

        Optional<Owner> ownerOptional = ownerRepository.findById(owner.getId());

        if (ownerOptional.isPresent()) {
            throw new APIException(ErrorCode.OWNER_ALREADY_EXISTS);
        }

        return ownerRepository.save(owner);
    }

    @Override
    public Owner updateEntity(Owner owner) {

        Owner ownerOptional = ownerRepository.findById(owner.getId())
                .orElseThrow(() -> new APIException(ErrorCode.OWNER_NOT_FOUND));

        return ownerRepository.save(owner);
    }

    @Override
    public void deleteEntity(Long id) {

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.OWNER_NOT_FOUND));

        ownerRepository.delete(owner);
    }
}
