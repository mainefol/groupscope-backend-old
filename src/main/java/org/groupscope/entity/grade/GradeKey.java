package org.groupscope.entity.grade;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

    public Long getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(Long learnerId) {
        this.learnerId = learnerId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
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
