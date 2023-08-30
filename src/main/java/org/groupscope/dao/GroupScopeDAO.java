package org.groupscope.dao;

import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;

import java.util.List;

public interface GroupScopeDAO {

    Subject saveSubject(Subject subject);

    Subject findSubjectByName(String subjectName);

    Subject findSubjectById(Long subject_id);

    List<Subject> findAllSubjects();

    List<Subject> findAllSubjectsByGroupName(String groupName);

    void updateSubject(Subject subject);

    void deleteSubject(Subject subject);

    void deleteSubjectByName(String name);


    void saveTask(Task task);

    void saveAllTasks(List<Task> tasks);

    List<Task> findAllTasksOfSubject(Subject subject);

    Task findTaskByName(String name);

    void updateTask(Task task);

    void deleteTask(Task task);

    void deleteTaskById(Long id);


    Learner saveStudent(Learner learner);

    Learner findStudentByName(String name);

    Learner findStudentById(Long id);

    Learner updateLearner(Learner learner);

    void deleteLearner(Learner learner);

    void deleteLearnerById(Long id);


    LearningGroup saveGroup(LearningGroup learningGroup);

    LearningGroup findGroupById(Long id);

    LearningGroup findLearningGroupByInviteCode(String inviteCode);

    // TODO: Complete method for deleting group
    // void deleteGroupById(Long id);

    List<LearningGroup> getAllGroups();


    Grade saveGrade(Grade grade);

    List<Grade> saveAllGrades(List<Grade> grades);

    Grade findGradeByLearnerAndTask(Learner learner, Task task);

    Grade findGradeById(GradeKey id);

    List<Grade> findAllByLearner(Learner learner);

    void deleteGradesByLearner(Learner learner);

    void deleteGradesByTask(Task task);
}
