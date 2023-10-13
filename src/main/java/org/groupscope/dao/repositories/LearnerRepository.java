package org.groupscope.dao.repositories;

import org.groupscope.entity.Learner;
import org.groupscope.entity.LearningRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearnerRepository extends CrudRepository<Learner, Long> {
    Optional<Learner> getLearnerByName(String name);

    Optional<Learner> getLearnersByNameAndLastname(String name, String lastname);
}
