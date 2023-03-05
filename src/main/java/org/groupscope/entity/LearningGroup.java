package org.groupscope.entity;

import javax.persistence.*;
import java.util.Objects;

/*
* This class extends from LearnerManager and allow to unite and manage our group of learners
* */

@Entity
@Table(name = "groups")
public class LearningGroup extends LearnerManager<Learner<LearningRole>> {

    // Id entity which database use
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    // Every group have a headman
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "headmen_id")
    private Learner<LearningRole> headmen;

    public LearningGroup() {
    }

    public LearningGroup(String groupName) {
        super();
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
