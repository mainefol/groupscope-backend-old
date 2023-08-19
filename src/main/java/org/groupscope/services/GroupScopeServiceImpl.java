package org.groupscope.services;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional(readOnly = true)
public class GroupScopeServiceImpl implements GroupScopeService{

    private final GroupScopeDAO groupScopeDAO;

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
        else {
            throw new NullPointerException("Learning group was null");
        }
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        return groupScopeDAO.findSubjectByName(subjectName);
    }

    @Override
    @Transactional
    public void updateSubject(SubjectDTO subjectDTO, LearningGroup learningGroup) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());
        if(subject != null && Objects.equals(subject.getGroup().getId(), learningGroup.getId())) {
            if (subjectDTO.getNewName() != null) {
                subject.setName(subjectDTO.getNewName());
            }
            subjectDTO.setId(subject.getId());
            subject.setGroup(learningGroup);
            groupScopeDAO.updateSubject(subject);
        }
        else
            if(subject == null)
                throw new NullPointerException("Subject not found with name: " + subjectDTO.getName());
            else
                throw new IllegalArgumentException("Subject" + subjectDTO.getName() +
                        "is not relevant to group: " + learningGroup.toString());
    }

    @Override
    @Transactional
    public void deleteSubject(SubjectDTO subjectDTO) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());
        if (subject != null)
            groupScopeDAO.deleteSubject(subject);
        else
            throw new NullPointerException("Subject not found with name: " + subjectDTO.getName());
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
        else
            throw new NullPointerException("Subject not found with name: " + subjectName);
    }

    @Override
    public List<TaskDTO> getAllTasksOfSubject(SubjectDTO subjectDTO) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectDTO.getName());

        if(subject != null) {
            return subject.getTasks()
                    .stream()
                    .map(TaskDTO::from)
                    .collect(Collectors.toList());
        }
        else
            throw new NullPointerException("Subject not found with name: " + subjectDTO.getName());
    }

    @Override
    @Transactional
    public void updateTask(TaskDTO taskDTO, String subjectName) {
        Task task = groupScopeDAO.findTaskByName(taskDTO.getName());
        Subject subject = groupScopeDAO.findSubjectByName(subjectName);
        if (task != null && subject != null) {
            task.setSubject(subject);
            if (taskDTO.getNewName() != null)
                task.setName(taskDTO.getNewName());
            if (taskDTO.getType() != null)
                task.setType(taskDTO.getType());
            if (taskDTO.getInfo() != null)
                task.setInfo(taskDTO.getInfo());
            if (taskDTO.getDeadline() != null)
                task.setDeadline(taskDTO.getDeadline());
            groupScopeDAO.updateTask(task);
        }
        else {
            throw (task == null) ? new NullPointerException("Task not found with name: " + taskDTO.getName()) :
                                    new NullPointerException("Subject not found with name: " + subjectName);
        }
    }

    @Override
    @Transactional
    public void deleteTask(TaskDTO taskDTO) {
        Task task = groupScopeDAO.findTaskByName(taskDTO.getName());
        if (task != null)
            groupScopeDAO.deleteTask(task);
        else
            throw new NullPointerException("Task not found with name: " + taskDTO.getName());
    }

    @Override
    @Transactional
    public void updateGrade(GradeDTO gradeDTO, Learner learner) {
        Task task = groupScopeDAO.findTaskByName(gradeDTO.getTaskName());
        Subject subject = groupScopeDAO.findSubjectByName(gradeDTO.getSubjectName());
            if(task != null && subject.equals(task.getSubject())) {
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
            else {
                if(task == null)
                    throw new NullPointerException("Task not found with name: " + gradeDTO.getTaskName());
                else
                    throw new IllegalArgumentException("The task is not relevant to the subject : " + gradeDTO.getSubjectName());
            }
    }

    @Override
    @Transactional
    public Learner addStudent(LearnerDTO learnerDTO, String inviteCode) {
        Learner learner = learnerDTO.toLearner();
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
                return groupScopeDAO.saveStudent(learner);
            }
            else
                return null;
        } else {
            throw new NullPointerException("Learning group not found with invite code: " + inviteCode);
        }
    }

    @Override
    @Transactional
    public Learner addFreeLearner(LearnerDTO learnerDTO) {
        Learner learner = learnerDTO.toLearner();

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
        if(learner != null)
            groupScopeDAO.deleteLearner(learner);
        else
            throw new NullPointerException("Learner not found with name: " + learnerDTO.getName());
    }

    @Override
    @Transactional
    public LearningGroup addGroup(LearningGroupDTO learningGroupDTO) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        group.getHeadmen().setLearningGroup(group);
        if(!groupScopeDAO.getAllGroups().contains(group)) {
            group = groupScopeDAO.saveGroup(group);
            group.generateInviteCode();
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
