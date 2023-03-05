package org.groupscope.entity;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

/*
* This class was written for LearningGroup and allow to manage any entities like Learner
* */

@MappedSuperclass
public class LearnerManager<T extends Learner> {
    // Entities array
    private List<T> learners;

    public LearnerManager() {
        this.learners = new ArrayList<>();
    }

    public List<T> getLearners() {
        return learners;
    }
}
