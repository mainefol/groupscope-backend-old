package org.groupscope.services;

import org.groupscope.dto.GradeDTO;
import org.groupscope.dto.TaskDTO;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {
    Subject getSubjectByName(String subjectName);

    List<Subject> getAllSubjects();

    List<Task<TaskType>> getAllTasksOfSubject(Subject subject);

    void addSubject(Subject subject);

    void addTask(Task<TaskType> task);

    Learner<LearningRole> getStudentById(int id);

    LearningGroup getGroupById(int id);
}
