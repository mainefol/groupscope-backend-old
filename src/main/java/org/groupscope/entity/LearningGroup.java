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
    private int id;

    @Column(name = "name")
    private String name;

    // Every group have a headman
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "headmen_id")
    private Learner<LearningRole> headmen;

    // Every group have subjects that the headmen has made
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private List<Subject> subjects;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private List<Learner<LearningRole>> learners;

    public LearningGroup() {
    }

    public LearningGroup(String groupName) {
        super();
        this.learners = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.name = groupName;
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

    public Learner<LearningRole> getHeadmen() {
        return headmen;
    }

    public void setHeadmen(Learner<LearningRole> headmen) {
        this.headmen = headmen;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Learner<LearningRole>> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner<LearningRole>> learners) {
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
