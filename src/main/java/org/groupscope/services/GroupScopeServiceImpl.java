package org.groupscope.services;

import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
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
    public void addSubject(SubjectDTO subjectDTO, Long group_id) {
        Subject subject = subjectDTO.toSubject();
        subject.setGroup(groupScopeDAO.findGroupById(group_id));
        groupScopeDAO.saveSubject(subject);
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        return groupScopeDAO.findSubjectByName(subjectName);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return groupScopeDAO.findAllSubjects();
    }

    @Transactional
    @Override
    public void addTask(Task task, Long id, String subjectName) {
        task.setSubject(groupScopeDAO.findSubjectByName(subjectName));
        task.setLearner(groupScopeDAO.findStudentById(id));

        groupScopeDAO.saveTask(task);
    }

    @Override
    public List<Task> getAllTasksOfSubject(Subject subject) {
        return groupScopeDAO.findAllTasksOfSubject(subject);
    }

    @Transactional
    @Override
    public void addGrade(GradeDTO gradeDTO) {
        List<Task> tasks = gradeDTO.getTasks()
                .stream()
                .map(TaskDTO::toTask)
                .collect(Collectors.toList());

        Long learnerId = gradeDTO.getLearnerId();
        String subject = gradeDTO.getSubject();

        for(Task task : tasks) {
            task.setLearner(groupScopeDAO.findStudentById(learnerId));
            task.setSubject(groupScopeDAO.findSubjectByName(subject));
        }

        groupScopeDAO.saveAllTasks(tasks);
    }

    @Override
    @Transactional
    public void addStudent(LearnerDTO learnerDTO, Long group_id) {
        Learner student = learnerDTO.toLearner();
        student.setLearningGroup(groupScopeDAO.findGroupById(group_id));
        groupScopeDAO.saveStudent(student);
    }

    @Override
    public Learner getStudentById(Long id) {
        return groupScopeDAO.findStudentById(id);
    }

    @Override
    @Transactional
    public void addGroup(LearningGroupDTO learningGroupDTO) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        groupScopeDAO.saveGroup(group);
    }

    @Override
    public LearningGroup getGroupById(Long id) {
        return groupScopeDAO.findGroupById(id);
    }
}
