package org.groupscope.services;

import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupScopeServiceImpl implements GroupScopeService{

    private final GroupScopeDAO groupScopeDAO;

    @Autowired
    public GroupScopeServiceImpl(GroupScopeDAO groupScopeDAO) {
        this.groupScopeDAO = groupScopeDAO;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return groupScopeDAO.findAllSubjects();
    }

    @Override
    public List<Task<TaskType>> getAllTasksOfSubject(Subject subject) {
        return groupScopeDAO.findAllTasksOfSubject(subject);
    }

    @Transactional
    @Override
    public void addSubject(Subject subject) {
        groupScopeDAO.saveSubject(subject);
    }

    @Transactional
    @Override
    public void addTask(Task<TaskType> task) {
        groupScopeDAO.saveTask(task);
    }

    @Override
    public Learner<LearningRole> getStudentById(int id) {
        return groupScopeDAO.findStudentById(id);
    }

    @Override
    public LearningGroup getGroupById(int id) {
        return groupScopeDAO.findGroupById(id);
    }
}
