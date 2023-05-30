package org.groupscope.entity.grade;

import org.groupscope.entity.Learner;
import org.groupscope.entity.Task;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Grade {
    @EmbeddedId
    GradeKey id;

    @ManyToOne
    @MapsId("learnerId")
    @JoinColumn(name = "learner_id")
    Learner learner;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    Task task;

    @Column(name = "completion")
    private Boolean completion;

    @Column(name = "mark")
    private Integer mark;

    public Grade(){
    }

    public Grade(GradeKey id, Learner learner, Task task, Boolean completion, Integer mark) {
        this.id = id;
        this.learner = learner;
        this.task = task;
        this.completion = completion;
        this.mark = mark;
    }

    public GradeKey getId() {
        return id;
    }

    public void setId(GradeKey id) {
        this.id = id;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id) && Objects.equals(learner, grade.learner) && Objects.equals(task, grade.task) && Objects.equals(completion, grade.completion) && Objects.equals(mark, grade.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, learner, task, completion, mark);
    }
}
