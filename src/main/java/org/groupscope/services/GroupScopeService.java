package org.groupscope.services;

import org.groupscope.dto.*;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    void addSubject(SubjectDTO subjectDTO, LearningGroup learningGroup);

    Subject getSubjectByName(String subjectName);

    List<Subject> getAllSubjects();

    void updateSubject(SubjectDTO subjectDTO, LearningGroup learningGroup);

    void deleteSubject(String subjectName);

    ///////////////////////////////////////////////////
    void addTask(TaskDTO taskDTO, String subjectName);

    List<TaskDTO> getAllTasksOfSubject(String subjectName);

    void updateTask(TaskDTO taskDTO, String subjectName);

    void deleteTask(TaskDTO taskDTO);

    ///////////////////////////////////////////////////
    void updateGrade(GradeDTO gradeDTO, Learner learner);

    ///////////////////////////////////////////////////

    Learner addStudent(Learner learner, String inviteCode);

    Learner addFreeLearner(LearnerDTO learnerDTO);

    Learner getStudentById(Long id);

    void updateLearner(LearnerDTO learnerDTO, Learner learner);

    void deleteLearner(LearnerDTO learnerDTO);

    ///////////////////////////////////////////////////
    LearningGroup addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(Long id);

    LearningGroup getGroupByInviteCode(String inviteCode);

}
