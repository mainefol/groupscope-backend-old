package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LearningGroupDTO {

    private Long id;

    private String name;

    private LearnerDTO headmen;

    private List<SubjectDTO> subjects;

    private List<LearnerDTO> learners;

    public static LearningGroupDTO from(LearningGroup learningGroup) {
        LearningGroupDTO dto = new LearningGroupDTO();
        dto.setId(learningGroup.getId());
        dto.setName(learningGroup.getName());
        dto.setHeadmen(LearnerDTO.fromManyLearners(learningGroup.getHeadmen()));

        List<SubjectDTO> subjectDTOList = learningGroup.getSubjects().stream()
                .map(SubjectDTO::from)
                .peek(subjectDTO -> subjectDTO.setGroup(dto))
                .collect(Collectors.toList());

        dto.setSubjects(subjectDTOList);

        List<LearnerDTO> learnerDTOList = learningGroup.getLearners().stream()
                .map(LearnerDTO::fromManyLearners)
                .peek(learnerDTO -> learnerDTO.setLearningGroup(dto))
                .collect(Collectors.toList());

        dto.setLearners(learnerDTOList);
        return dto;
    }

    public LearningGroup toLearningGroup() {
        LearningGroup learningGroup = new LearningGroup(this.getName());
        //learningGroup.setId(this.getId());
        learningGroup.setHeadmen(this.getHeadmen().toLearner());

        learningGroup.getHeadmen().setLearningGroup(learningGroup);
        //learningGroup.getHeadmen().setTasks(new ArrayList<>());

        if(!CollectionUtils.isEmpty(this.learners)) {
            List<Learner<LearningRole>> learnersList = this.learners.stream()
                    .map(LearnerDTO::toLearner)
                    .peek(learner -> learner.setLearningGroup(learningGroup))
                    .collect(Collectors.toList());
            learningGroup.setLearners(learnersList);
        }

        return learningGroup;
    }
}