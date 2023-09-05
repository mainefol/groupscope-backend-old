package org.groupscope.services;

import org.groupscope.dto.*;
import org.groupscope.entity.*;

import java.util.List;

public interface GroupScopeService {

    Subject addSubject(SubjectDTO subjectDTO, LearningGroup group);

    Subject getSubjectByName(String subjectName, LearningGroup group);

    List<SubjectDTO> getAllSubjectsByGroup(LearningGroup group);

    Subject updateSubject(SubjectDTO subjectDTO, LearningGroup group);

    void deleteSubject(String subjectName, LearningGroup group);

    ///////////////////////////////////////////////////
    Task addTask(TaskDTO taskDTO, String subjectName, LearningGroup group);

    List<TaskDTO> getAllTasksOfSubject(Learner learner, String subjectName);

    void updateTask(TaskDTO taskDTO, String subjectName);

    void deleteTask(String subjectName, TaskDTO taskDTO);

    ///////////////////////////////////////////////////

    List<GradeDTO> getAllGradesOfSubject(String subjectName, Learner learner);

    List<LearnerDTO> getGradesOfSubjectFromGroup(String subjectName, LearningGroup group);

    void updateGrade(GradeDTO gradeDTO, Learner learner);

    ///////////////////////////////////////////////////

    Learner addStudent(Learner learner, String inviteCode);

    Learner addFreeLearner(LearnerDTO learnerDTO);

    Learner getStudentById(Long id);

    void updateLearner(LearnerDTO learnerDTO, Learner learner);

    void deleteLearner(String learnerName);

    ///////////////////////////////////////////////////

    LearningGroupDTO getGroup(Learner learner);

    LearningGroup addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(Long id);

    LearningGroup getGroupByInviteCode(String inviteCode);

}
