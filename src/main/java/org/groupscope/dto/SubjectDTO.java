package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.LearningGroup;
import org.groupscope.entity.Subject;
import org.groupscope.entity.Task;
import org.groupscope.entity.TaskType;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SubjectDTO {

    private Long id;

    private String name;

    private List<TaskDTO> tasks;

    private LearningGroupDTO group;

    public static SubjectDTO from(Subject subject) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());

        List<TaskDTO> taskDTOList = subject.getTasks().stream()
                .map(TaskDTO::from).collect(Collectors.toList());

        dto.setTasks(taskDTOList);
        return dto;
    }

    public Subject toSubject() {
        Subject subject = new Subject(this.getName());
        subject.setId(this.getId());
        subject.setGroup(this.getGroup().toLearningGroup());
        return subject;
    }
}
