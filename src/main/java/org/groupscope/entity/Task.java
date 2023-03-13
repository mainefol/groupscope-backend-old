package org.groupscope.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private T type;

    @Column(name = "info", length = 255)
    private String info;

    @Column(name = "grade")
    private Integer grade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "learner_id")
    private Learner<LearningRole> learner;

    @OneToMany(mappedBy = "group")
    private List<Learner<LearningRole>> learners;

    public Task() {
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
