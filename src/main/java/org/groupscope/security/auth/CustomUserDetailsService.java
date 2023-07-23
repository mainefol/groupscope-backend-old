package org.groupscope.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = customUserRepository.findByLogin(username);

        if(customUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        return customUser;
    }
}
