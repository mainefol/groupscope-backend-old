package org.groupscope.security.auth;


import org.groupscope.dao.repositories.CustomUserRepository;
import org.groupscope.dto.LearnerDTO;
import org.groupscope.dto.LearningGroupDTO;
import org.groupscope.entity.Learner;
import org.groupscope.entity.LearningGroup;
import org.groupscope.entity.LearningRole;
import org.groupscope.entity.Provider;
import org.groupscope.security.dto.RegistrationRequest;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class CustomUserService {

    private final CustomUserRepository customUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final GroupScopeService groupScopeService;


    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository,
                             PasswordEncoder passwordEncoder,
                             GroupScopeService groupScopeService) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupScopeService = groupScopeService;
    }

    public CustomUser saveUser(CustomUser customUser, RegistrationRequest request, Provider provider) {
        if(customUser.getPassword() != null) {
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
        }
        customUser.setProvider(provider);

        // Add new learner to existing group
        if(request.getInviteCode() != null) {
            LearnerDTO learnerDTO = new LearnerDTO(request.getLearnerName(),
                    request.getLearnerLastname(),
                    LearningRole.STUDENT);
            Learner learner = groupScopeService.addStudent(learnerDTO, request.getInviteCode());
            if(learner != null) {
                customUser.setLearner(learner);
                return customUserRepository.save(customUser);
            } else {
                return null;
            }
        // Add new learner and create new group
        } else if (request.getGroupName() != null) {
            LearnerDTO headman = new LearnerDTO(request.getLearnerName(),
                    request.getLearnerLastname(),
                    LearningRole.HEADMAN);
            LearningGroupDTO learningGroupDTO = new LearningGroupDTO(request.getGroupName(), headman);
            LearningGroup learningGroup = groupScopeService.addGroup(learningGroupDTO);
            if(learningGroup != null) {
                customUser.setLearner(learningGroup.getHeadmen());
                return customUserRepository.save(customUser);
            } else {
                return null;
            }
        // Add new learner without group addition
        } else {
            LearnerDTO learnerDTO = new LearnerDTO(request.getLearnerName(),
                    request.getLearnerLastname(),
                    LearningRole.STUDENT);
            Learner learner = groupScopeService.addFreeLearner(learnerDTO);
            if(learner != null) {
                customUser.setLearner(learner);
                return customUserRepository.save(customUser);
            } else {
                return null;
            }
        }
    }

    public CustomUser findByLogin(String login) {
        return customUserRepository.findByLogin(login);
    }

    public CustomUser findByLoginAndPassword(String login, String password) {
        CustomUser customUser = findByLogin(login);
        if(customUser != null) {
            if (passwordEncoder.matches(password, customUser.getPassword())){
                return customUser;
            }
        }
        return null;
    }


}
