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

    List<TaskDTO> getAllTasksOfSubject(String subjectName, LearningGroup group);

    void updateTask(TaskDTO taskDTO, String subjectName, LearningGroup group);

    void deleteTask(String subjectName, TaskDTO taskDTO, LearningGroup group);

    ///////////////////////////////////////////////////

    List<GradeDTO> getAllGradesOfSubject(String subjectName, Learner learner);

    List<LearnerDTO> getGradesOfSubjectFromGroup(String subjectName, LearningGroup group);

    void updateGrade(GradeDTO gradeDTO, Learner learner);

    ///////////////////////////////////////////////////

    Learner addLearner(Learner learner, String inviteCode);

    Learner addFreeLearner(LearnerDTO learnerDTO);

    Learner getLearnerById(Long id);

    Learner updateLearner(LearnerDTO learnerDTO, Learner learner);

    void deleteLearner(Learner learner);

    Learner refreshLearnerGrades(Learner learner, LearningGroup newGroup);

    void processLearnerWithdrawal(Learner learner);

    ///////////////////////////////////////////////////

    LearningGroupDTO getGroup(Learner learner);

    LearningGroup addGroup(LearningGroupDTO learningGroupDTO);

    LearningGroup getGroupById(Long id);

    LearningGroup getGroupByInviteCode(String inviteCode);

    LearningGroup updateHeadmanOfGroup(LearningGroup group, LearnerDTO learnerDTO);

}
