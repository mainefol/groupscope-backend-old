package org.groupscope.dao.repositories;

import org.groupscope.entity.grade.Grade;
import org.groupscope.security.auth.CustomUser;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<Grade, Long> {
}
