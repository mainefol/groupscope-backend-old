package org.groupscope.entity;

import lombok.Data;
import org.groupscope.entity.grade.Grade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a task in the learning system.
 * Tasks are associated with a specific subject and can have multiple grades associated with them.
 */

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    // Represents the type of the task (e.g., practical, laboratory, test).
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    // Additional information or description about the task.
    @Column(name = "info", length = 255)
    private String info;

    // The deadline for completing the task.
    @Column(name = "deadline")
    private String deadline;

    // Many-to-one relationship with the Subject entity. Each task belongs to a subject.
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // One-to-many relationship with the Grade entity. Each task can have multiple grades.
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    public Task() {
        grades = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", subject=" + subject.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                type == task.type &&
                Objects.equals(info, task.info) &&
                Objects.equals(deadline, task.deadline) &&
                Objects.equals(subject, task.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, info, deadline, subject);
    }
}
