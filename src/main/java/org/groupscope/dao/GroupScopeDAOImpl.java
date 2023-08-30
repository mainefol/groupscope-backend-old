package org.groupscope.dao;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.repositories.*;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
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

    private final GradeRepository gradeRepository;

    @Autowired
    public GroupScopeDAOImpl(SubjectRepository subjectRepository,
                             TaskRepository taskRepository,
                             LearnerRepository learnerRepository,
                             LearningGroupRepository learningGroupRepository,
                             GradeRepository gradeRepository) {
        this.subjectRepository = subjectRepository;
        this.taskRepository = taskRepository;
        this.learnerRepository = learnerRepository;
        this.learningGroupRepository = learningGroupRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Subject saveSubject(Subject subject) {
        Subject result = subjectRepository.save(subject);
        log.info("Subject: " + subject.toString() + " saved/updated");
        return result;
    }

    @Override
    public Subject findSubjectByName(String name) {
        return subjectRepository.getSubjectByName(name);
    }

    @Override
    public Subject findSubjectById(Long subject_id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subject_id);
        return optionalSubject.orElse(null);
    }

    @Override
    public List<Subject> findAllSubjects() {
        return (List<Subject>) subjectRepository.findAll();
    }

    @Override
    public List<Subject> findAllSubjectsByGroupName(String groupName) {
        return subjectRepository.findAllByGroup_Name(groupName);
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
    public void deleteSubjectByName(String name) {
        subjectRepository.deleteSubjectByName(name);
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
    public Learner updateLearner(Learner learner) {
        log.info("Update " + learner.toString());
        return learnerRepository.save(learner);
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



    @Override
    public Grade findGradeByLearnerAndTask(Learner learner, Task task) {
        return gradeRepository.findGradeByLearnerAndTask(learner, task);
    }

    @Override
    public Grade findGradeById(GradeKey id) {
        return gradeRepository.findGradeById(id);
    }

    @Override
    public List<Grade> findAllByLearner(Learner learner) {
        return gradeRepository.findAllByLearner(learner);
    }

    @Override
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> saveAllGrades(List<Grade> grades) {
        return (List<Grade>) gradeRepository.saveAll(grades);
    }

    @Override
    public void deleteGradesByLearner(Learner learner) {
        gradeRepository.deleteGradesByLearner(learner);
    }

    @Override
    public void deleteGradesByTask(Task task) {
        gradeRepository.deleteGradesByTask(task);
    }
}
