package org.groupscope.dao;

import org.groupscope.dto.GradeDTO;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeDAO {

    void saveSubject(Subject subject);

    Subject findSubjectByName(String subjectName);

    List<Subject> findAllSubjects();

    void saveTask(Task task);

    void saveAllTasks(List<Task> tasks);

    List<Task> findAllTasksOfSubject(Subject subject);

    void saveStudent(Learner learner);

    Learner findStudentById(Long id);

    void saveGroup(LearningGroup learningGroup);

    LearningGroup findGroupById(Long id);
}
