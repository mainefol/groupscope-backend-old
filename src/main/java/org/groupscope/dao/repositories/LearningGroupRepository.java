package org.groupscope.dao.repositories;

import org.groupscope.entity.LearningGroup;
import org.groupscope.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//import org.springframework.transaction.annotation.Transactional;


@Repository
public interface LearningGroupRepository extends CrudRepository<LearningGroup, Long> {
    Optional<LearningGroup> getLearningGroupByInviteCode(String inviteCode);
}
