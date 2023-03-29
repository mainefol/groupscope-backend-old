package org.groupscope.dao.repositories;

import org.groupscope.entity.LearningGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningGroupRepository extends JpaRepository<LearningGroup, Integer> {

}
