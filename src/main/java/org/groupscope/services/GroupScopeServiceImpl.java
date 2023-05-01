package org.groupscope.services;

import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.GradeDTO;
import org.groupscope.dto.LearnerDTO;
import org.groupscope.dto.LearningGroupDTO;
import org.groupscope.dto.TaskDTO;
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
    public void addSubject(Subject subject) {
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
    public void addTask(Task<TaskType> task, int id, String subjectName) {
        task.setSubject(groupScopeDAO.findSubjectByName(subjectName));
        task.setLearner(groupScopeDAO.findStudentById(id));

        groupScopeDAO.saveTask(task);
    }

    @Override
    public List<Task<TaskType>> getAllTasksOfSubject(Subject subject) {
        return groupScopeDAO.findAllTasksOfSubject(subject);
    }

    @Transactional
    @Override
    public void addGrade(GradeDTO gradeDTO) {
        List<Task<TaskType>> tasks = gradeDTO.getTasks()
                .stream()
                .map(TaskDTO::toTask)
                .collect(Collectors.toList());

        int learnerId = gradeDTO.getLearnerId();
        String subject = gradeDTO.getSubject();

        for(Task<TaskType> task : tasks) {
            task.setLearner(groupScopeDAO.findStudentById(learnerId));
            task.setSubject(groupScopeDAO.findSubjectByName(subject));
        }

        groupScopeDAO.saveAllTasks(tasks);
    }

    @Override
    public void addStudent(LearnerDTO learnerDTO) {
        Learner<LearningRole> student = learnerDTO.toLearner();
        groupScopeDAO.saveStudent(student);
    }

    @Override
    public Learner<LearningRole> getStudentById(int id) {
        return groupScopeDAO.findStudentById(id);
    }

    @Override
    public void addGroup(LearningGroupDTO learningGroupDTO) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        groupScopeDAO.saveGroup(group);
    }

    @Override
    public LearningGroup getGroupById(int id) {
        return groupScopeDAO.findGroupById(id);
    }
}
