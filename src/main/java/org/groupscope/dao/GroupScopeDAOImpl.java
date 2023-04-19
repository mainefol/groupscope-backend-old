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
    public Subject findSubjectByName(String subjectName) {
        return subjectRepository.getSubjectByName(subjectName);
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
        log.info("Subject " + subject.toString() + " saved");
    }

    @Override
    public void saveTask(Task<TaskType> task) {
        taskRepository.save(task);
        log.info("Task " + task.toString() + " saved");
    }

    @Override
    public void saveGrade(GradeDTO gradeDTO) {
        List<Task<TaskType>> tasks = gradeDTO.getTasks()
                .stream()
                .map(TaskDTO::toTask)
                .collect(Collectors.toList());

        taskRepository.saveAll(tasks);
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
