package org.groupscope.entity.grade;

import lombok.Data;
import org.groupscope.entity.Learner;
import org.groupscope.entity.Task;

import javax.persistence.*;
import java.util.Objects;

@Data
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
