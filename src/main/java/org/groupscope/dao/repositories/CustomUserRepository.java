package org.groupscope.dao.repositories;

import org.groupscope.security.auth.CustomUser;
import org.springframework.data.repository.CrudRepository;

public interface CustomUserRepository extends CrudRepository<CustomUser, Long> {

    CustomUser findByLogin(String login);
}
