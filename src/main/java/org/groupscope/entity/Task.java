package org.groupscope.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    @Column(name = "info", length = 255)
    private String info;

    @Column(name = "completion")
    private Boolean completion;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "deadline")
    private Calendar deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "learner_id")
    private Learner learner;

    @ManyToMany
    private List<Learner> learners;

    public Task() {
        learners = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public List<Learner> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner> learners) {
        this.learners = learners;
    }

    @Override
    public String toString() {
        return "Task{" +
                "type=" + type +
                ", info='" + info + '\'' +
                ", deadline=" + deadline +
                ", subject=" + subject.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && type == task.type && Objects.equals(info, task.info) && Objects.equals(completion, task.completion) && Objects.equals(grade, task.grade) && Objects.equals(deadline, task.deadline) && Objects.equals(subject, task.subject) && Objects.equals(learner, task.learner) && Objects.equals(learners, task.learners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, info, completion, grade, deadline, subject, learner, learners);
    }
}
