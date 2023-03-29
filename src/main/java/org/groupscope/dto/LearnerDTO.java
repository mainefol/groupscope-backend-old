package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;

import java.util.List;

@Data
public class LearnerDTO {

    private int id;

    private String name;

    private String lastname;

    private LearningRole role;

    private LearningGroupDTO learningGroup;

    private List<TaskDTO> tasks;

    public static LearnerDTO fromManyLearners(Learner<LearningRole> learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId());
        dto.setName(learner.getName());
        dto.setLastname(learner.getLastname());
        dto.setRole(learner.getRole());

        List<TaskDTO> taskDTOList = learner.getTasks().stream()
                .map(TaskDTO::from)
                .toList();

        dto.setTasks(taskDTOList);
        return dto;
    }

    public static LearnerDTO fromOneLearner(Learner<LearningRole> learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId());
        dto.setName(learner.getName());
        dto.setLastname(learner.getLastname());
        dto.setRole(learner.getRole());

        dto.setLearningGroup(LearningGroupDTO.from(learner.getLearningGroup()));

        List<TaskDTO> taskDTOList = learner.getTasks().stream()
                .map(TaskDTO::from)
                .toList();

        dto.setTasks(taskDTOList);
        return dto;
    }

    public Learner<LearningRole> toLearner() {
        Learner<LearningRole> learner = new Learner<>();
        learner.setId(this.getId());
        learner.setName(this.getName());
        learner.setLastname(this.getLastname());
        learner.setRole(this.getRole());

        List<Task<TaskType>> taskList = this.getTasks().stream()
                        .map(TaskDTO::toTask)
                        .peek(task -> {
                            task.setLearner(learner);
                            task.setLearners(learner.getLearningGroup().getLearners());
                        }).toList();

        learner.setTasks(taskList);
        return learner;
    }
}