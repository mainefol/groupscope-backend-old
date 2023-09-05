package org.groupscope.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.groupscope.entity.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SubjectDTO {

    private Long id;

    private String name;

    private String newName;

    private List<TaskDTO> tasks;

    private String group;

    public SubjectDTO(String name) {
        this.name = name;
    }

    public static SubjectDTO from(Subject subject) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.group = subject.getGroup().getName();

        List<TaskDTO> taskDTOList = subject.getTasks().stream()
                .map(TaskDTO::from).collect(Collectors.toList());

        dto.setTasks(taskDTOList);
        return dto;
    }

    public Subject toSubject() {
        Subject subject = new Subject(this.getName());
        subject.setId(this.getId());

        if(!CollectionUtils.isEmpty(this.tasks)) {
            List<Task> taskList = this.tasks.stream()
                    .map(TaskDTO::toTask)
                    .peek(learner -> learner.setSubject(subject))
                    .collect(Collectors.toList());

            subject.setTasks(taskList);
        }

        return subject;
    }
}
