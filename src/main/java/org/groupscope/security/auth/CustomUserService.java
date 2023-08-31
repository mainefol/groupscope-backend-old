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
import org.springframework.transaction.annotation.Transactional;

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

    /*
     * Saves the custom user to the repository and performs additional actions based on the registration request.
     * If a user is registering with an invite code, they will be added to an existing group.
     * If a user is registering with a group name, a new group will be created and the user will be set as the headman.
     * If a user is registering without a group addition, they will be registered as a free learner.
     * Returns the saved custom user or null if the user couldn't be saved.
     */
    @Transactional
    public CustomUser saveUser(CustomUser customUser, RegistrationRequest request, Provider provider) {
        if(customUser.getPassword() != null) {
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
        }
        customUser.setProvider(provider);
        LearnerDTO learnerDTO = new LearnerDTO(request.getLearnerName(),
                request.getLearnerLastname(),
                LearningRole.STUDENT);

        // Add new learner to an existing group based on the invite code
        if(request.getInviteCode() != null) {
            Learner student = groupScopeService.addStudent(learnerDTO.toLearner(), request.getInviteCode());
            return processLearner(customUser, student);

        // Add new learner and create a new group
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
            Learner student = groupScopeService.addFreeLearner(learnerDTO);
            return processLearner(customUser, student);
        }
    }

    private CustomUser processLearner(CustomUser user, Learner learner) {
        if (learner != null) {
            user.setLearner(learner);
            return customUserRepository.save(user);
        } else {
            return null;
        }
    }

    /*
     * Find a custom user by their login (username).
     * Returns the custom user if found, or null if not found.
     */
    @Transactional
    public CustomUser findByLogin(String login) {
        return customUserRepository.findByLogin(login);
    }

    /*
     * Find a custom user by their login (username) and password.
     * Returns the custom user if found and the provided password matches the stored password, or null if not found or password doesn't match.
     */
    @Transactional
    public CustomUser findByLoginAndPassword(String login, String password) {
        CustomUser customUser = findByLogin(login);
        if(customUser != null) {
            if (passwordEncoder.matches(password, customUser.getPassword())){
                return customUser;
            } else
                throw new IllegalArgumentException("Incorrect password");
        } else
            throw new NullPointerException("User with login = " + login + "not found");
    }


}
