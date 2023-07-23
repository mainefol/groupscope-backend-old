package org.groupscope.services;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@Transactional(readOnly = true)
public class GroupScopeServiceImpl implements GroupScopeService{

    private GroupScopeDAO groupScopeDAO;

    @Autowired
    public GroupScopeServiceImpl(GroupScopeDAO groupScopeDAO) {
        this.groupScopeDAO = groupScopeDAO;
    }

    @Override
    @Transactional
    public void addSubject(SubjectDTO subjectDTO, LearningGroup learningGroup) {
        Subject subject = subjectDTO.toSubject();
        if(learningGroup != null) {
            subject.setGroup(learningGroup);

            if(!subject.getGroup().getSubjects().contains(subject)) {
                groupScopeDAO.saveSubject(subject);
            }
        }
        // TODO: Make exception for duplicate
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        return groupScopeDAO.findSubjectByName(subjectName);
    }

    @Override
    @Transactional
    public void updateSubject(SubjectDTO subjectDTO, LearningGroup learningGroup) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());
        if(subjectDTO.getNewName() != null) {
            subject.setName(subjectDTO.getNewName());
        }
        subjectDTO.setId(subject.getId());
        subject.setGroup(learningGroup);
        groupScopeDAO.updateSubject(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(SubjectDTO subjectDTO) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());
        groupScopeDAO.deleteSubject(subject);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return groupScopeDAO.findAllSubjects();
    }

    @Override
    @Transactional
    public void addTask(TaskDTO taskDTO, String subjectName) {
        Task task = taskDTO.toTask();
        Subject subject = groupScopeDAO.findSubjectByName(subjectName);
        if(subject != null) {
            task.setSubject(subject);

            if (!subject.getTasks().contains(task)) {
                subject.getGroup().getLearners()
                        .forEach(learner -> {
                            GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                            Grade grade = new Grade(gradeKey, learner, task, false, 0);

                            learner.getGrades().add(grade);
                            task.getGrades().add(grade);
                        });

                groupScopeDAO.saveTask(task);
            }
        }
    }

    @Override
    public List<TaskDTO> getAllTasksOfSubject(SubjectDTO subjectDTO) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());

        return subject.getTasks()
                .stream()
                .map(TaskDTO::from)
                .collect(Collectors.toList());
    }

    // TODO rewrite
    @Override
    @Transactional
    public void updateTask(TaskDTO taskDTO, String subjectName) {
        Task task = groupScopeDAO.findTaskByName(taskDTO.getName());
        task.setSubject(groupScopeDAO.findSubjectByName(subjectName));
        if(taskDTO.getNewName() != null)
            task.setName(taskDTO.getNewName());
        if(taskDTO.getType() != null)
            task.setType(taskDTO.getType());
        if(taskDTO.getInfo() != null)
            task.setInfo(taskDTO.getInfo());
        if(taskDTO.getDeadline() != null)
            task.setDeadline(taskDTO.getDeadline());
        groupScopeDAO.updateTask(task);
    }

    @Override
    @Transactional
    public void deleteTask(TaskDTO taskDTO) {
        Task task = groupScopeDAO.findTaskByName(taskDTO.getName());
        groupScopeDAO.deleteTask(task);
    }

    @Transactional
    @Override
    public void updateGrade(GradeDTO gradeDTO, Learner learner) {
        if(learner.getLearningGroup().getSubjects() != null) {
            Subject subject = learner.getLearningGroup().getSubjects().stream()
                    .filter(subjectTmp -> subjectTmp.getName().equals(gradeDTO.getSubjectName()))
                    .findFirst()
                    .orElse(null);
            if (subject != null && subject.getTasks() != null) {
                Task task = subject.getTasks().stream()
                        .filter(taskTmp -> taskTmp.getName().equals(gradeDTO.getTaskName()))
                        .findFirst()
                        .orElse(null);
                if(task != null) {
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
            }
        }
    }

    @Override
    @Transactional
    public Learner addStudent(LearnerDTO learnerDTO, String inviteCode, Provider provider) {
        Learner learner = learnerDTO.toLearner();
        learner.setProvider(provider);
        LearningGroup learningGroup = groupScopeDAO.findLearningGroupByInviteCode(inviteCode);

        if(learningGroup != null) {
            if(!learningGroup.getLearners().contains(learner)) {
                learner.setLearningGroup(learningGroup);

                for (Subject subject : learningGroup.getSubjects()) {
                    for (Task task : subject.getTasks()) {
                        GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                        Grade grade = new Grade(gradeKey, learner, task, false, 0);

                        learner.getGrades().add(grade);
                        task.getGrades().add(grade);
                    }
                }
            }
            return groupScopeDAO.saveStudent(learner);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Learner addFreeLearner(LearnerDTO learnerDTO, Provider provider) {
        Learner learner = learnerDTO.toLearner();
        learner.setProvider(provider);

        return groupScopeDAO.saveStudent(learner);
    }

    @Override
    public Learner getStudentById(Long id) {
        return groupScopeDAO.findStudentById(id);
    }

    @Override
    @Transactional
    public void updateLearner(LearnerDTO learnerDTO, Learner learner) {
        if(learnerDTO.getNewName() != null)
            learner.setName(learnerDTO.getNewName());
        if(learnerDTO.getNewLastname() != null)
            learner.setLastname(learnerDTO.getLastname());
        groupScopeDAO.updateLearner(learner);
    }

    @Override
    @Transactional
    public void deleteLearner(LearnerDTO learnerDTO) {
        Learner learner = groupScopeDAO.findStudentByName(learnerDTO.getName());
        groupScopeDAO.deleteLearner(learner);
    }

    @Override
    @Transactional
    public LearningGroup addGroup(LearningGroupDTO learningGroupDTO, Provider provider) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        group.getHeadmen().setLearningGroup(group);
        group.getHeadmen().setProvider(provider);
        if(!groupScopeDAO.getAllGroups().contains(group)) {
            return groupScopeDAO.saveGroup(group);
        } else {
            return null;
        }
    }

    @Override
    public LearningGroup getGroupById(Long id) {
        return groupScopeDAO.findGroupById(id);
    }

    @Override
    public LearningGroup getGroupByInviteCode(String inviteCode) {
        return groupScopeDAO.findLearningGroupByInviteCode(inviteCode);
    }
}
