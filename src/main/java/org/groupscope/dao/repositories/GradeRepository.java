package org.groupscope.dao.repositories;

import org.groupscope.entity.Learner;
import org.groupscope.entity.Task;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    Grade findGradeByLearnerAndTask(Learner learner, Task task);

    Grade findGradeById(GradeKey id);

    List<Grade> findAllByLearner(Learner learner);

    void deleteGradesByLearner(Learner learner);

    void deleteGradesByTask(Task task);
}