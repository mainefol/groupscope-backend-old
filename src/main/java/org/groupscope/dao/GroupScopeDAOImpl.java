package org.groupscope.dao;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.repositories.LearnerRepository;
import org.groupscope.dao.repositories.LearningGroupRepository;
import org.groupscope.dao.repositories.SubjectRepository;
import org.groupscope.dao.repositories.TaskRepository;
import org.groupscope.dto.GradeDTO;
import org.groupscope.dto.TaskDTO;
import org.groupscope.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GroupScopeDAOImpl implements GroupScopeDAO{
    private SubjectRepository subjectRepository;

    private TaskRepository taskRepository;

    private LearnerRepository learnerRepository;

    private LearningGroupRepository learningGroupRepository;

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
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
        log.info("Subject " + subject.toString() + " saved");
    }

    @Override
    public Subject findSubjectByName(String subjectName) {
        return subjectRepository.getSubjectByName(subjectName);
    }

    @Override
    public List<Subject> findAllSubjects() {
        return (List<Subject>) subjectRepository.findAll();
    }

    @Override
    public void saveTask(Task<TaskType> task) {
        taskRepository.save(task);
        log.info("Task " + task.toString() + " saved");
    }

    @Override
    public void saveAllTasks(List<Task<TaskType>> tasks) {
        taskRepository.saveAll(tasks);
    }

    @Override
    public List<Task<TaskType>> findAllTasksOfSubject(Subject subject) {
        return taskRepository.findTasksBySubject(subject);
    }

    @Override
    public void saveStudent(Learner<LearningRole> learner) {
        learnerRepository.save(learner);
        log.info("Learner " + learner.toString() + " saved");
    }

    @Override
    public Learner<LearningRole> findStudentById(Long id) {
        Optional<Learner<LearningRole>> learner = learnerRepository.findById(id);
        return learner.orElse(null);
    }

    @Override
    public void saveGroup(LearningGroup learningGroup) {
        LearningGroup learningGroup1 = learningGroupRepository.save(learningGroup);
        log.info("Learning Group " + learningGroup.toString() + " saved");
    }

    @Override
    public LearningGroup findGroupById(Long id) {
        Optional<LearningGroup> learningGroup = learningGroupRepository.findById(id);
        return learningGroup.orElse(null);
    }
}
