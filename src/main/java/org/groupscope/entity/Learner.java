package org.groupscope.entity;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* It`s a main entity, which will use in our project
* */

@Entity
@Table(name = "learners")
public class Learner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    // Learner role, now it`s can be Student or Headmen. The last can monitor student`s grades
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private LearningRole role;

    // Everyone lerner must belong to some group
    @ManyToOne
    @JoinColumn(name = "group_id")
    private LearningGroup learningGroup;

    @ManyToMany
    @JoinTable(
            name = "learners_tasks",
            joinColumns = @JoinColumn(name = "learner_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    public Learner() {
        //tasks = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LearningRole getRole() {
        return role;
    }

    public void setRole(LearningRole role) {
        this.role = role;
    }

    public LearningGroup getLearningGroup() {
        return learningGroup;
    }

    public void setLearningGroup(LearningGroup learningGroup) {
        this.learningGroup = learningGroup;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Learner{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Learner learner = (Learner) o;
        return Objects.equals(id, learner.id) && Objects.equals(name, learner.name) && Objects.equals(lastname, learner.lastname) && role == learner.role && Objects.equals(learningGroup, learner.learningGroup) && Objects.equals(tasks, learner.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, role, learningGroup, tasks);
    }
}
