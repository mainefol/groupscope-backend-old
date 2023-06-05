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
        log.info("Subject: " + subject.toString() + " saved/updated");
    }

    @Override
    public Subject findSubjectByName(String subjectName) {
        return subjectRepository.getSubjectByName(subjectName);
    }

    @Override
    public Subject findSubjectById(Long subject_id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subject_id);
        if (optionalSubject.isPresent()) {
            return optionalSubject.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Subject> findAllSubjects() {
        return (List<Subject>) subjectRepository.findAll();
    }

    @Override
    public List<Subject> findAllSubjectsByGroupId(Long group_id) {
        List<Subject> subjects = (List<Subject>) subjectRepository.findAll();
        return subjects.stream()
                .filter(subject -> subject.getGroup().getId().equals(group_id))
                .collect(Collectors.toList());
    }

    @Override
    public void updateSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
        log.info("Task " + task.toString() + " saved");
    }

    @Override
    public void saveAllTasks(List<Task> tasks) {
        taskRepository.saveAll(tasks);
    }

    @Override
    public List<Task> findAllTasksOfSubject(Subject subject) {
        return taskRepository.findTasksBySubject(subject);
    }

    @Override
    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void saveStudent(Learner learner) {
        learnerRepository.save(learner);
        log.info("Learner " + learner.toString() + " saved");
    }

    @Override
    public Learner findStudentById(Long id) {
        Optional<Learner> learner = learnerRepository.findById(id);
        return learner.orElse(null);
    }

    @Override
    public void updateLearner(Learner learner) {
        log.info("Update " + learner.toString());
        learnerRepository.save(learner);
    }

    @Override
    public void deleteLearnerById(Long id) {
        learnerRepository.deleteById(id);
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

    @Override
    public List<LearningGroup> getAllGroups() {
        return (List<LearningGroup>) learningGroupRepository.findAll();
    }
}
