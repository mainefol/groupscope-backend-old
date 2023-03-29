package org.groupscope.dao;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.repositories.LearnerRepository;
import org.groupscope.dao.repositories.LearningGroupRepository;
import org.groupscope.dao.repositories.SubjectRepository;
import org.groupscope.dao.repositories.TaskRepository;
import org.groupscope.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class GroupScopeDAOImpl implements GroupScopeDAO{

    private final SubjectRepository subjectRepository;

    private final TaskRepository taskRepository;

    private final LearnerRepository learnerRepository;

    private final LearningGroupRepository learningGroupRepository;

    @Autowired
    public GroupScopeDAOImpl(SubjectRepository subjectRepository,
                             TaskRepository taskRepository,
                             LearnerRepository learnerRepository,
                             LearningGroupRepository learningGroupRepository) {
        this.subjectRepository = subjectRepository;
        this.taskRepository = taskRepository;
        this.learnerRepository = learnerRepository;
        this.learningGroupRepository = learningGroupRepository;
    }

    @Override
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Task<TaskType>> findAllTasksOfSubject(Subject subject) {
        return taskRepository.findTasksBySubject(subject);
    }

    @Override
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
        log.info("Subject " + subject + " saved");
    }

    @Override
    public void saveTask(Task<TaskType> task) {
        taskRepository.save(task);
        log.info("Task " + task + " saved");
    }

    @Override
    public Learner<LearningRole> findStudentById(int id) {
        Optional<Learner<LearningRole>> learner = learnerRepository.findById(id);
        return learner.orElse(null);
    }

    @Override
    public LearningGroup findGroupById(int id) {
        Optional<LearningGroup> learningGroup = learningGroupRepository.findById(id);
        return learningGroup.orElse(null);
    }
}
