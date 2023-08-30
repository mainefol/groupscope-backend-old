package org.groupscope.dao.repositories;

import org.groupscope.entity.Learner;
import org.groupscope.entity.Task;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.groupscope.security.auth.CustomUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    // TODO final method
    Grade findGradeByLearnerAndTask(Learner learner, Task task);

    List<Grade> findAllByLearner(Learner learner);

    Grade findGradeById(GradeKey id);

    void deleteGradesByLearner(Learner learner);

    void deleteGradesByTask(Task task);
}