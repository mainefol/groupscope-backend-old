package org.groupscope.dao;

import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeDAO {

    void saveSubject(Subject subject);

    Subject findSubjectByName(String subjectName);

    Subject findSubjectById(Long subject_id);

    List<Subject> findAllSubjects();

    // TODO complete method for getting all subjects
    List<Subject> findAllSubjectsByGroupId(Long group_id);

    void updateSubject(Subject subject);

    void deleteSubjectById(Long id);

    void saveTask(Task task);

    void saveAllTasks(List<Task> tasks);

    List<Task> findAllTasksOfSubject(Subject subject);

    void updateTask(Task task);

    void deleteTaskById(Long id);

    void saveStudent(Learner learner);

    Learner findStudentById(Long id);

    void updateLearner(Learner learner);

    void deleteLearnerById(Long id);

    void saveGroup(LearningGroup learningGroup);

    LearningGroup findGroupById(Long id);

    // TODO: Complete method for deleting group
    // void deleteGroupById(Long id);

    List<LearningGroup> getAllGroups();
}
