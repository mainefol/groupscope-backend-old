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

    private String newName;

    private String lastname;

    private String newLastname;

    private LearningRole role;

    private String learningGroup;

    private List<GradeDTO> grades;

    public LearnerDTO() {

    }

    public LearnerDTO(String name, String lastname, LearningRole role) {
        this.name = name;
        this.lastname = lastname;
        this.role = role;
    }

    public static LearnerDTO from(Learner learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId());
        dto.setName(learner.getName());
        dto.setLastname(learner.getLastname());
        dto.setRole(learner.getRole());

        if(learner.getLearningGroup() != null) {
            dto.setLearningGroup(learner.getLearningGroup().toString());
        }

        if(learner.getGrades() != null) {
            List<GradeDTO> gradeDTOList = learner.getGrades().stream()
                    .map(GradeDTO::from)
                    .collect(Collectors.toList());

            dto.setGrades(gradeDTOList);
        }
        return dto;
    }

    // When you call this method, you must set Group and Tasks to Learner in
    public Learner toLearner() {
        Learner learner = new Learner();
        learner.setId(this.getId());
        learner.setName(this.getName());
        learner.setLastname(this.getLastname());
        learner.setRole(this.getRole());

        return learner;
    }
}