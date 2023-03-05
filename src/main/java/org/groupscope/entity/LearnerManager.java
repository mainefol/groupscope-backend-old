package org.groupscope.entity;

import java.util.ArrayList;
import java.util.List;

public class LearnerManager<T extends Learner> {
    private List<T> learners;

    public LearnerManager() {
        this.learners = new ArrayList<>();
    }

    public List<T> getLearners() {
        return learners;
    }
}
