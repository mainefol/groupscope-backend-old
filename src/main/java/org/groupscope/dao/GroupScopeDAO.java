package org.groupscope.dao;

import org.groupscope.dto.GradeDTO;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeDAO {

    void saveSubject(Subject subject);

    Subject findSubjectByName(String subjectName);

    List<Subject> findAllSubjects();

    void saveTask(Task<TaskType> task);

    void saveAllTasks(List<Task<TaskType>> tasks);

    List<Task<TaskType>> findAllTasksOfSubject(Subject subject);

    void saveStudent(Learner<LearningRole> learner);

    Learner<LearningRole> findStudentById(Long id);

    void saveGroup(LearningGroup learningGroup);

    LearningGroup findGroupById(Long id);
}
