package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LearnerDTO {

    private Long id;

    private String name;

    private String lastname;

    private LearningRole role;

    private String learningGroup;

    private List<GradeDTO> grades;

    public static LearnerDTO from(Learner learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId());
        dto.setName(learner.getName());
        dto.setLastname(learner.getLastname());
        dto.setRole(learner.getRole());

        dto.setLearningGroup(learner.getLearningGroup().toString());

        List<GradeDTO> gradeDTOList = learner.getGrades().stream()
                        .map(GradeDTO::from)
                        .collect(Collectors.toList());

        dto.setGrades(gradeDTOList);

        return dto;
    }

    // When you call this method, you must set Group and Tasks to Learner in
    public Learner toLearner() {
        Learner learner = new Learner();
        //learner.setId(this.getId());
        learner.setName(this.getName());
        learner.setLastname(this.getLastname());
        learner.setRole(this.getRole());

        return learner;
    }
}