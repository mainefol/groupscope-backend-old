package org.groupscope.dao.repositories;

import org.groupscope.entity.LearningGroup;
import org.groupscope.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;


@Repository
public interface LearningGroupRepository extends CrudRepository<LearningGroup, Long> {
    LearningGroup getLearningGroupByInviteCode(String inviteCode);
}
