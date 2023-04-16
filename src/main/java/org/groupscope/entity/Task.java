package org.groupscope.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private T type;

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
    private Learner<LearningRole> learner;

    @OneToMany(mappedBy = "group")
    private List<Learner<LearningRole>> learners;


    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
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

    public Learner<LearningRole> getLearner() {
        return learner;
    }

    public void setLearner(Learner<LearningRole> learner) {
        this.learner = learner;
    }

    public List<Learner<LearningRole>> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner<LearningRole>> learners) {
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
        Task<?> task = (Task<?>) o;
        return Objects.equals(type, task.type) &&
                Objects.equals(info, task.info) &&
                Objects.equals(deadline, task.deadline) &&
                Objects.equals(subject, task.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, info, deadline, subject);
    }
}
