package org.groupscope.dao;

import org.groupscope.dto.GradeDTO;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeDAO {
    Subject findSubjectByName(String subjectName);

    List<Subject> findAllSubjects();

    List<Task<TaskType>> findAllTasksOfSubject(Subject subject);

    void saveSubject(Subject subject);

    void saveTask(Task<TaskType> task);

    void saveGrade(GradeDTO gradeDTO);

    Learner<LearningRole> findStudentById(int id);

    LearningGroup findGroupById(int id);
}
