package org.groupscope.entity;

import lombok.Data;
import org.groupscope.entity.grade.Grade;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    @Column(name = "info", length = 255)
    private String info;

    @Column(name = "deadline")
    private String deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
        return Objects.equals(name, task.name) && type == task.type && Objects.equals(info, task.info) && Objects.equals(deadline, task.deadline) && Objects.equals(subject, task.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, info, deadline, subject, grades);
    }
}
