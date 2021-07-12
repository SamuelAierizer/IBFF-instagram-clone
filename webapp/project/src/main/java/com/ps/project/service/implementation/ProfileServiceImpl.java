package com.ps.project.service.implementation;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Profile;
import com.ps.project.model.User;
import com.ps.project.repository.ProfileRepository;
import com.ps.project.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void addProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void deleteUser(Profile profile) {
        profileRepository.delete(profile);
    }

    @Override
    public void updateProfile(Profile dto) {
        Profile persistedProfile = profileRepository.findById(dto.getId()).get();
        persistedProfile.setTitle(dto.getTitle());
        persistedProfile.setDescription(dto.getDescription());
        persistedProfile.setUrl(dto.getUrl());

        profileRepository.save(persistedProfile);
    }

    @Override
    public void followUpdateProfile(Profile profile) {
        Profile persistedProfile = profileRepository.findById(profile.getId()).get();
        persistedProfile.setFollowers(profile.getFollowers());
        profileRepository.save(persistedProfile);
    }

    @Override
    public Profile findByUser(User user) throws ApiExceptionResponse {
        Profile profile = profileRepository.findFirstByUser(user);

        if (profile == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No profile with user: " + user.getUsername()))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return profile;
    }

    @Override
    public Profile findById(Long Id) throws ApiExceptionResponse {
        Profile profile = profileRepository.findFirstById(Id);

        if (profile == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No profile with id: " + Id))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return profile;
    }
}
