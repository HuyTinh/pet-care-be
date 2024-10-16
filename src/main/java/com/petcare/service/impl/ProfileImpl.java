package com.petcare.service.impl;

import com.petcare.entity.Profile;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.ProfileRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileImpl implements EntityService<Profile, Long> {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public List<Profile> getAllEntity() {
        return ArrayMapper.mapperIterableToList(profileRepository.findAll());
    }

    @Override
    public Optional<Profile> getEntityById(Long id) {
        return profileRepository.findById(id);
    }

    @Override
    public Profile createEntity(Profile profile) {
        Optional<Profile> profileOptional = profileRepository.findById(profile.getId());
        if (profileOptional.isPresent()) {
            throw new APIException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        return profileRepository.save(profile);
    }

    @Override
    public Profile updateEntity(Profile profile) {
        Profile existingProfile = profileRepository.findById(profile.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PROFILE_NOT_FOUND));
        return profileRepository.save(profile);
    }

    @Override
    public void deleteEntity(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PROFILE_NOT_FOUND));
        profileRepository.delete(profile);
    }
}

