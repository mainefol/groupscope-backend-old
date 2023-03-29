package org.groupscope.services;

import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    List<Subject> getAllSubjects();

    List<Task<TaskType>> getAllTasksOfSubject(Subject subject);

    void addSubject(Subject subject);

    void addTask(Task<TaskType> task);

    Learner<LearningRole> getStudentById(int id);

    LearningGroup getGroupById(int id);
}