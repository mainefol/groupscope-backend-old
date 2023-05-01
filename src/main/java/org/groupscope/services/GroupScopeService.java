package org.groupscope.services;

import org.groupscope.dto.GradeDTO;
import org.groupscope.dto.LearnerDTO;
import org.groupscope.dto.LearningGroupDTO;
import org.groupscope.dto.TaskDTO;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    void addSubject(Subject subject);

    Subject getSubjectByName(String subjectName);

    List<Subject> getAllSubjects();

    void addTask(Task<TaskType> task, int id, String subjectName);

    List<Task<TaskType>> getAllTasksOfSubject(Subject subject);

    void addGrade(GradeDTO gradeDTO);

    void addStudent(LearnerDTO learnerDTO);

    Learner<LearningRole> getStudentById(int id);

    void addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(int id);
}
