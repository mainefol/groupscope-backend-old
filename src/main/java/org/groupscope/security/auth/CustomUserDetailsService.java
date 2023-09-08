package org.groupscope.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.GroupScopeDAOImpl;
import org.groupscope.dao.repositories.CustomUserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    @Transactional
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = customUserRepository.findByLogin(username);

        if(customUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        Hibernate.initialize(customUser.getLearner().getGrades());
        if(customUser.getLearner().getLearningGroup() != null)
            GroupScopeDAOImpl.removeDuplicates(customUser.getLearner().getLearningGroup().getSubjects());
        return customUser;
    }
}
