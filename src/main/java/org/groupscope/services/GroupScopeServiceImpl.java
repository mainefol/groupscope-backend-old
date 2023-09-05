package org.groupscope.services;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public Subject addSubject(SubjectDTO subjectDTO, LearningGroup group) {
        Subject subject = subjectDTO.toSubject();
        if(group != null) {
            subject.setGroup(group);

            if(!subject.getGroup().getSubjects().contains(subject)) {
                groupScopeDAO.saveSubject(subject);
                return subject;
            } else
                throw new IllegalArgumentException("Subject = " + subject.toString() + " has been already existing");
        }
        else {
            throw new NullPointerException("Learning group was null");
        }
    }

    @Override
    public Subject getSubjectByName(String subjectName, LearningGroup group) {
        if(group != null && subjectName != null) {
            Optional<Subject> subject = group.getSubjects().stream()
                    .filter(s -> s.getName().equals(subjectName))
                    .findFirst();
            if(subject.isPresent())
                return subject.get();
            else
                throw new NullPointerException("Subject with name = " + subjectName + "not found");
        } else
            throw new NullPointerException("Learning group is null");
    }

    @Override
    @Transactional
    public Subject updateSubject(SubjectDTO subjectDTO, LearningGroup group) {
        if(group != null && subjectDTO != null) {
            Optional<Subject> subject = group.getSubjects().stream()
                    .filter(s -> s.getName().equals(subjectDTO.getName()))
                    .findFirst();

            if(subject.isPresent()) {
                if (subjectDTO.getNewName() != null) {
                    subject.get().setName(subjectDTO.getNewName());
                }
                subject.get().setGroup(group);
                groupScopeDAO.updateSubject(subject.get());
                return subject.get();
            }
            else
                throw new NullPointerException("Subject not found with name: " + subjectDTO.getName());
        } else
            throw new NullPointerException("Learning group or subjectDTO are null");
    }

    @Override
    @Transactional
    public void deleteSubject(String subjectName, LearningGroup group) {
        if (group != null) {
            Subject subject = groupScopeDAO.findSubjectByNameAndGroupId(subjectName, group.getId());
            if (subject != null) {
                groupScopeDAO.deleteSubject(subject);
            } else
                throw new NullPointerException("Subject not found with name: " + subjectName);

        } else
            throw new NullPointerException("Learning group is null");
    }

    @Override
    public List<SubjectDTO> getAllSubjectsByGroup(LearningGroup group) {
        if(group != null) {
            List<Subject> subjects = group.getSubjects();
            if (subjects != null)
                return subjects.stream()
                        .map(SubjectDTO::from)
                        .collect(Collectors.toList());
            else
                return new ArrayList<>();
        } else
            throw new NullPointerException("Group doesnt exist");
    }

    // TODO when new task has added, subject duplicating
    //  P.S. was fixed by GroupScopeDAOImpl.removeDuplicates() function, but still not fixed in Hibernate response
    @Override
    @Transactional
    public Task addTask(TaskDTO taskDTO, String subjectName, LearningGroup group) {
        Task task = taskDTO.toTask();
        Subject subject = groupScopeDAO.findSubjectByNameAndGroupId(subjectName, group.getId());
        if(subject != null) {
            task.setSubject(subject);

            if (!subject.getTasks().contains(task)) {
                subject.getGroup().getLearners()
                        .forEach(learner -> {
                            GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                            Grade grade = new Grade(gradeKey, learner, task, false, 0);

                            task.getGrades().add(grade);
                        });
                groupScopeDAO.saveTask(task);
                groupScopeDAO.saveAllGrades(task.getGrades());
                return task;
            } else
                throw new IllegalArgumentException("Task = " + task.toString() + " has been already existing");
        }
        else
            throw new NullPointerException("Subject not found with name: " + subjectName);
    }

    @Override
    public List<TaskDTO> getAllTasksOfSubject(Learner learner, String subjectName) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectName);

        if(subject != null) {
            if(learner.getLearningGroup().getSubjects().contains(subject)) {
                return subject.getTasks()
                        .stream()
                        .map(TaskDTO::from)
                        .collect(Collectors.toList());
            }
        }
        throw new NullPointerException("Subject not found with name: " + subjectName);
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
    public void deleteTask(String subjectName, TaskDTO taskDTO) {
        Subject subject = groupScopeDAO.findSubjectByName(subjectName);

        if(subject != null) {
            Task task = subject.getTasks().stream()
                    .filter(t -> t.getName().equals(taskDTO.getName()))
                    .findFirst()
                    .orElse(null);
            if (task != null) {
                groupScopeDAO.deleteGradesByTask(task);
                groupScopeDAO.deleteTaskById(task.getId());
            } else
                throw new NullPointerException("Task not found with name: " + taskDTO.getName());
        } else
            throw new NullPointerException("Subject not found with name: " + subjectName);
    }

    @Override
    @Transactional
    public List<GradeDTO> getAllGradesOfSubject(String subjectName, Learner learner) {
        if(subjectName == null || learner == null)
            throw (subjectName == null) ? new NullPointerException("Subject name is null") :
                                        new NullPointerException("Learner is null");

        return learner.getGrades().stream()
                .filter(grade -> grade.getTask().getSubject().getName().equals(subjectName))
                .map(GradeDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearnerDTO> getGradesOfSubjectFromGroup(String subjectName, LearningGroup group) {
        if(subjectName != null && group != null) {
            return group.getLearners().stream()
                    .peek(learner -> {
                        List<Grade> grades = groupScopeDAO.findAllGradesByLearner(learner);
                        learner.setGrades(grades);
                    })
                    .map(LearnerDTO::from)
                    .peek(learnerDTO -> {
                        List<GradeDTO> filteredGrades = learnerDTO.getGrades().stream()
                                .filter(gradeDTO -> gradeDTO.getSubjectName().equals(subjectName))
                                .collect(Collectors.toList());

                        learnerDTO.setGrades(filteredGrades);
                    })
                    .collect(Collectors.toList());
        } else
            throw new NullPointerException("Subject name or group is null");

    }

    @Override
    @Transactional
    public void updateGrade(GradeDTO gradeDTO, Learner learner) {
        if(!gradeDTO.isValid())
            throw new IllegalArgumentException("The gradeDTO not valid ");

        Subject subject = groupScopeDAO.findSubjectByNameAndGroupId(
                gradeDTO.getSubjectName(),
                learner.getLearningGroup().getId()
        );

        if(subject != null) {
            Task task = groupScopeDAO.findTaskByNameAndSubjectId(
                    gradeDTO.getTaskName(),
                    subject.getId()
            );

            if(task != null) {
                GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                Grade grade = groupScopeDAO.findGradeById(gradeKey);
                grade.setCompletion(gradeDTO.getCompletion());
                grade.setMark(gradeDTO.getMark());
                groupScopeDAO.saveGrade(grade);
            } else
                throw new NullPointerException("Task not found with name: " + gradeDTO.getTaskName());
        } else
            throw new NullPointerException("Subject not found with name: " + gradeDTO.getSubjectName());
    }

    /**
     For saving new user
     */
    @Override
    @Transactional
    public Learner addStudent(Learner learner, String inviteCode) {
        LearningGroup learningGroup = groupScopeDAO.findLearningGroupByInviteCode(inviteCode);
        if(learningGroup != null) {
            learner.setLearningGroup(learningGroup);
            learner.setRole(LearningRole.STUDENT);

            return refreshLearnerGrades(learner, learningGroup);
        }
        else
            throw new NullPointerException("Group with inviteCode = " + inviteCode + " not found");
    }

    @Override
    @Transactional
    public Learner addFreeLearner(LearnerDTO learnerDTO) {
        Learner learner = learnerDTO.toLearner();
        return groupScopeDAO.saveStudent(learner);
    }

    @Override
    public Learner getStudentById(Long id) {
        Learner learner = groupScopeDAO.findStudentById(id);
        if(learner != null)
            return learner;
        else
            throw new NullPointerException("Learner with id = " + id + " not found");
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
    public void deleteLearner(String learrnerName) {
        Learner learner = groupScopeDAO.findStudentByName(learrnerName);
        if(learner != null)
            groupScopeDAO.deleteLearner(learner);
        else
            throw new NullPointerException("Learner not found with name: " + learrnerName);
    }

    @Override
    @Transactional
    public LearningGroupDTO getGroup(Learner learner) {

        for(Learner lr : learner.getLearningGroup().getLearners())
            lr.setGrades(groupScopeDAO.findAllGradesByLearner(lr));

        return LearningGroupDTO.from(learner.getLearningGroup());
    }

    @Override
    @Transactional
    public LearningGroup addGroup(LearningGroupDTO learningGroupDTO) {
        LearningGroup group = learningGroupDTO.toLearningGroup();
        group.getHeadmen().setRole(LearningRole.HEADMAN);
        group.getHeadmen().setLearningGroup(group);

        if(group.getHeadmen().getId() != null)
            groupScopeDAO.deleteGradesByLearner(group.getHeadmen());

        if(!groupScopeDAO.getAllGroups().contains(group)) {
            group = groupScopeDAO.saveGroup(group);
            group.generateInviteCode();
            return groupScopeDAO.saveGroup(group);
        } else {
            throw new IllegalArgumentException("Group " + group.getName() + " has been already existing");
        }
    }

    @Override
    public LearningGroup getGroupById(Long id) {
        LearningGroup learningGroup = groupScopeDAO.findGroupById(id);
        if(learningGroup != null)
            return learningGroup;
        else
            throw new NullPointerException("Group with id = " + id + " not found");
    }

    @Override
    public LearningGroup getGroupByInviteCode(String inviteCode) {
        LearningGroup learningGroup = groupScopeDAO.findLearningGroupByInviteCode(inviteCode);
        if(learningGroup != null)
            return learningGroup;
        else
            throw new NullPointerException("Group with inviteCode = " + inviteCode + " not found");
    }

    /**
        The learner must be included in new group
     */
    private Learner refreshLearnerGrades(Learner learner, LearningGroup newGroup) {
        if(learner.getId() != null) {
            groupScopeDAO.deleteGradesByLearner(learner);
            learner.getGrades().clear();
        }
        if(newGroup != null) {
            if(!newGroup.getLearners().contains(learner)) {
                for (Subject subject : newGroup.getSubjects()) {
                    for (Task task : subject.getTasks()) {
                        GradeKey gradeKey = new GradeKey(learner.getId(), task.getId());
                        Grade grade = new Grade(gradeKey, learner, task, false, 0);

                        learner.getGrades().add(grade);
                    }
                }
                Learner l = groupScopeDAO.saveStudent(learner);
                groupScopeDAO.saveAllGrades(learner.getGrades());
                return l;
            }
            else {
                log.info("The learner: " + learner + " is not include in group " + newGroup.getName());
                return null;
            }
        } else
            throw new NullPointerException("Learning group is null in method refreshLearnerGrades()");
    }
}
