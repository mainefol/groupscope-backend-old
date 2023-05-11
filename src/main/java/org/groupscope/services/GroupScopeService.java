package org.groupscope.services;

import org.groupscope.dto.*;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    void addSubject(SubjectDTO subjectDTO, Long group_id);

    Subject getSubjectByName(String subjectName);

    List<Subject> getAllSubjects();

    void addTask(Task<TaskType> task, Long id, String subjectName);

    List<Task<TaskType>> getAllTasksOfSubject(Subject subject);

    void addGrade(GradeDTO gradeDTO);

    void addStudent(LearnerDTO learnerDTO, Long group_id);

    Learner<LearningRole> getStudentById(Long id);

    void addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(Long id);
}
