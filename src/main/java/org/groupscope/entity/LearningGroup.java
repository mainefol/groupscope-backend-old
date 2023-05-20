package org.groupscope.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* This class extends from LearnerManager and allow to unite and manage our group of learners
* */

@Entity
@Table(name = "groups")
public class LearningGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    // Every group have a headman
    @OneToOne(cascade = CascadeType.ALL, targetEntity = Learner.class)
    @JoinColumn(name = "headmen_id")
    private Learner headmen;

    // Every group have subjects that the headmen has made
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private List<Subject> subjects;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Learner.class)
    @JoinColumn(name = "group_id")
    private List<Learner> learners;

    public LearningGroup() {
        //this.learners = new ArrayList<>();
        //this.subjects = new ArrayList<>();
    }

    public LearningGroup(String groupName) {
        //this.learners = new ArrayList<>();
        //this.subjects = new ArrayList<>();
        this.name = groupName;
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

    public Learner getHeadmen() {
        return headmen;
    }

    public void setHeadmen(Learner headmen) {
        this.headmen = headmen;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Learner> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner> learners) {
        this.learners = learners;
    }

    @Override
    public String toString() {
        return "LearningGroup{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningGroup that = (LearningGroup) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
