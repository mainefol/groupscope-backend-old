package org.groupscope.entity;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/*
* It`s a main entity, which will use in our project
* */

@Entity
@Table(name = "learners")
public class Learner<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    // Learner role, now it`s can be Student or Headmen. The last can monitor student`s grades
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private T role;

    // Everyone lerner must belong to some group
    @ManyToOne
    @JoinColumn(name = "group_id")
    private LearningGroup learningGroup;

    @OneToMany(mappedBy = "learner")
    private List<Task<TaskType>> tasks;

    Learner() {
    }

    public int getId() {
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

    public T getRole() {
        return role;
    }

    public void setRole(T role) {
        this.role = role;
    }

    public LearningGroup getLearningGroup() {
        return learningGroup;
    }

    public void setLearningGroup(LearningGroup learningGroup) {
        this.learningGroup = learningGroup;
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
        Learner<?> learner = (Learner<?>) o;
        return Objects.equals(name, learner.name) && Objects.equals(lastname, learner.lastname) && Objects.equals(role, learner.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastname, role);
    }
}
