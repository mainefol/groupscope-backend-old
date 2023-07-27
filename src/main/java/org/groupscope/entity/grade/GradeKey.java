package org.groupscope.entity.grade;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/*
 * This class represents the composite primary key for the Grade entity.
 * It is used to uniquely identify each grade, based on the combination of learnerId and taskId.
 */

@Data
@Embeddable
public class GradeKey implements Serializable {
    @Column(name = "learner_id")
    Long learnerId;

    @Column(name = "task_id")
    Long taskId;

    public GradeKey() {
    }

    public GradeKey(Long learnerId, Long taskId) {
        this.learnerId = learnerId;
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeKey gradeKey = (GradeKey) o;
        return Objects.equals(learnerId, gradeKey.learnerId) && Objects.equals(taskId, gradeKey.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learnerId, taskId);
    }
}
