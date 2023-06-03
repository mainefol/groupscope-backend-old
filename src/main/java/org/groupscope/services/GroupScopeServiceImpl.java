package org.groupscope.services;

import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GroupScopeServiceImpl implements GroupScopeService{

    private GroupScopeDAO groupScopeDAO;

    @Autowired
    public GroupScopeServiceImpl(GroupScopeDAO groupScopeDAO) {
        this.groupScopeDAO = groupScopeDAO;
    }

    @Transactional
    @Override
    public void addSubject(SubjectDTO subjectDTO, Long groupId) {
        Subject subject = subjectDTO.toSubject();
        subject.setGroup(groupScopeDAO.findGroupById(groupId));
        groupScopeDAO.saveSubject(subject);
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        return groupScopeDAO.findSubjectByName(subjectName);
    }

    @Override
    public void updateSubject(SubjectDTO subjectDTO, Long groupId) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());
        subjectDTO.setId(subject.getId());
        subject.setGroup(groupScopeDAO.findGroupById(groupId));
        groupScopeDAO.updateSubject(subject);
    }

    @Override
    public void deleteSubject(SubjectDTO subjectDTO) {
        groupScopeDAO.deleteSubjectById(subjectDTO.getId());
    }

    @Override
    public List<Subject> getAllSubjects() {
        return groupScopeDAO.findAllSubjects();
    }

    @Transactional
    @Override
    public void addTask(TaskDTO taskDTO, String subjectName) {
        Task task = taskDTO.toTask();
        Subject subject = groupScopeDAO.findSubjectByName(subjectName);
        task.setSubject(subject);

        subject.getGroup().getLearners()
                .forEach(learner -> {
                    GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                    Grade grade = new Grade(gradeKey, learner, task, false, 0);

                    learner.getGrades().add(grade);
                    task.getGrades().add(grade);
                });

        groupScopeDAO.saveTask(task);
    }

    @Override
    public List<TaskDTO> getAllTasksOfSubject(String subjectName) {
        Subject subject = getSubjectByName(subjectName);

        return subject.getTasks()
                .stream()
                .map(TaskDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(TaskDTO taskDTO, Long subject_id) {
        Task task = taskDTO.toTask();
        task.setSubject(groupScopeDAO.findSubjectById(subject_id));
        groupScopeDAO.updateTask(task);
    }

    @Override
    public void deleteTask(TaskDTO taskDTO) {
        groupScopeDAO.deleteTaskById(taskDTO.getId());
    }

    @Transactional
    @Override
    public void updateGrade(GradeDTO gradeDTO, Long learnerId) {
        Learner learner = getStudentById(learnerId);

        Subject subject = learner.getLearningGroup().getSubjects().stream()
                .filter(subjectTmp -> subjectTmp.getName().equals(gradeDTO.getSubjectName()))
                .findFirst()
                .orElse(null);

        Task task = subject.getTasks().stream()
                .filter(taskTmp -> taskTmp.getName().equals(gradeDTO.getTaskName()))
                .findFirst()
                .orElse(null);

        GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());

        learner.getGrades().stream()
                .filter(grade -> grade.getId().equals(gradeKey))
                .findFirst()
                .ifPresent(grade -> {
                    grade.setCompletion(gradeDTO.getCompletion());
                    grade.setMark(gradeDTO.getMark());
                });

        task.getGrades().stream()
                .filter(grade -> grade.getId().equals(gradeKey))
                .findFirst()
                .ifPresent(grade -> {
                    grade.setCompletion(gradeDTO.getCompletion());
                    grade.setMark(gradeDTO.getMark());
                });

        groupScopeDAO.saveStudent(learner);
        groupScopeDAO.saveTask(task);
    }

    @Override
    @Transactional
    public void addStudent(LearnerDTO learnerDTO, Long groupId) {
        Learner learner = learnerDTO.toLearner();
        learner.setLearningGroup(groupScopeDAO.findGroupById(groupId));

        for (Subject subject : learner.getLearningGroup().getSubjects()) {
            for (Task task : subject.getTasks()) {
                GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                Grade grade = new Grade(gradeKey, learner, task, false, 0);

                learner.getGrades().add(grade);
                task.getGrades().add(grade);
            }
        }

        groupScopeDAO.saveStudent(learner);
    }

    @Override
    public Learner getStudentById(Long id) {
        return groupScopeDAO.findStudentById(id);
    }

    @Override
    public void updateLearner(LearnerDTO learnerDTO, Long group_id) {
        Learner learner = learnerDTO.toLearner();
        learner.setLearningGroup(getGroupById(group_id));
        learner.setGrades(this.getStudentById(learner.getId()).getGrades());
        groupScopeDAO.updateLearner(learner);
    }

    @Override
    public void deleteLearner(LearnerDTO learnerDTO) {
        groupScopeDAO.deleteLearnerById(learnerDTO.getId());
    }

    @Override
    @Transactional
    public void addGroup(LearningGroupDTO learningGroupDTO) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        group.getHeadmen().setLearningGroup(group);
        groupScopeDAO.saveGroup(group);
    }

    @Override
    public LearningGroup getGroupById(Long id) {
        return groupScopeDAO.findGroupById(id);
    }
}
