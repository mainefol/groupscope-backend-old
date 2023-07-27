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
    public Subject saveSubject(Subject subject) {
        Subject result = subjectRepository.save(subject);
        if(result != null)
            log.info("Subject: " + subject.toString() + " saved/updated");
        return result;
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
    public void deleteSubject(Subject subject) {
        subjectRepository.delete(subject);
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
    public Task findTaskByName(String name) {
        return taskRepository.getTaskByName(name);
    }

    @Override
    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Learner saveStudent(Learner learner) {
        Learner result =  learnerRepository.save(learner);
        if(result != null) {
            log.info("Learner " + learner.toString() + " saved");
        }
        return result;
    }

    @Override
    public Learner findStudentByName(String name) {
        return learnerRepository.getLearnerByName(name);
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
    public void deleteLearner(Learner learner) {
        learnerRepository.delete(learner);
    }

    @Override
    public void deleteLearnerById(Long id) {
        learnerRepository.deleteById(id);
    }

    @Override
    public LearningGroup saveGroup(LearningGroup learningGroup) {
        LearningGroup group = learningGroupRepository.save(learningGroup);
        if(group != null) {
            log.info("Learning Group " + group.toString() + " saved");
        }
        return group;
    }

    @Override
    public LearningGroup findGroupById(Long id) {
        Optional<LearningGroup> learningGroup = learningGroupRepository.findById(id);
        return learningGroup.orElse(null);
    }

    @Override
    public LearningGroup findLearningGroupByInviteCode(String inviteCode) {
        return learningGroupRepository.getLearningGroupByInviteCode(inviteCode);
    }

    @Override
    public List<LearningGroup> getAllGroups() {
        return (List<LearningGroup>) learningGroupRepository.findAll();
    }
}
