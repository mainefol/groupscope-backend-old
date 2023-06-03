package org.groupscope.services;

import org.groupscope.dto.*;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    void addSubject(SubjectDTO subjectDTO, Long group_id);

    Subject getSubjectByName(String subjectName);

    List<Subject> getAllSubjects();

    void updateSubject(SubjectDTO subjectDTO, Long group_id);

    void deleteSubject(SubjectDTO subjectDTO);

    ///////////////////////////////////////////////////
    void addTask(TaskDTO taskDTO, String subjectName);

    List<TaskDTO> getAllTasksOfSubject(String subjectName);

    void updateTask(TaskDTO taskDTO, Long subject_id);

    void deleteTask(TaskDTO taskDTO);

    ///////////////////////////////////////////////////
    void updateGrade(GradeDTO gradeDTO, Long learnerId);

    ///////////////////////////////////////////////////
    void addStudent(LearnerDTO learnerDTO, Long group_id);

    Learner getStudentById(Long id);

    void updateLearner(LearnerDTO learnerDTO, Long group_id);

    void deleteLearner(LearnerDTO learnerDTO);

    ///////////////////////////////////////////////////
    void addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(Long id);
}
