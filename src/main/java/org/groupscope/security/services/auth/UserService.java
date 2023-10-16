package org.groupscope.security.services.auth;


import org.groupscope.dao.repositories.UserRepository;
import org.groupscope.dto.LearnerDTO;
import org.groupscope.dto.LearningGroupDTO;
import org.groupscope.entity.*;
import org.groupscope.security.dto.RegistrationRequest;
import org.groupscope.security.entity.User;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final GroupScopeService groupScopeService;


    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       GroupScopeService groupScopeService) {
        this.userRepository = userRepository;
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
    public User saveUser(User user, RegistrationRequest request, Provider provider) {
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setProvider(provider);
        LearnerDTO learnerDTO = new LearnerDTO(request.getLearnerName(),
                request.getLearnerLastname(),
                LearningRole.STUDENT);

        // Add new learner to an existing group based on the invite code
        if(request.getInviteCode() != null) {
            Learner student = groupScopeService.addLearner(learnerDTO.toLearner(), request.getInviteCode());
            return processLearner(user, student);

        // Add new learner and create a new group
        } else if (request.getGroupName() != null) {
            LearnerDTO headman = new LearnerDTO(request.getLearnerName(),
                    request.getLearnerLastname(),
                    LearningRole.HEADMAN);
            LearningGroupDTO learningGroupDTO = new LearningGroupDTO(request.getGroupName(), headman);
            LearningGroup learningGroup = groupScopeService.addGroup(learningGroupDTO);
            if(learningGroup != null) {
                user.setLearner(learningGroup.getHeadmen());
                return userRepository.save(user);
            } else {
                return null;
            }
        // Add new learner without group addition
        } else {
            Learner student = groupScopeService.addFreeLearner(learnerDTO);
            return processLearner(user, student);
        }
    }

    private User processLearner(User user, Learner learner) {
        if (learner != null) {
            user.setLearner(learner);
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    /*
     * Find a custom user by their login (username).
     * Returns the custom user if found, or null if not found.
     */
    @Transactional
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /*
     * Find a custom user by their login (username) and password.
     * Returns the custom user if found and the provided password matches the stored password, or null if not found or password doesn't match.
     */
    @Transactional
    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if(user != null) {
            if (passwordEncoder.matches(password, user.getPassword())){
                return user;
            } else
                throw new IllegalArgumentException("Incorrect password");
        } else
            throw new NullPointerException("User with login = " + login + "not found");
    }


}
