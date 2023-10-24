package org.groupscope.assignment_management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.groupscope.assignment_management.entity.Subject;
import org.groupscope.assignment_management.entity.Task;
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

    private Boolean isExam;


    public SubjectDTO(String name) {
        this.name = name;
    }

    public static SubjectDTO from(Subject subject) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.group = subject.getGroup().getName();
        dto.isExam = subject.getIsExam();

        List<TaskDTO> taskDTOList = subject.getTasks().stream()
                .map(TaskDTO::from).collect(Collectors.toList());

        dto.setTasks(taskDTOList);
        return dto;
    }

    public Subject toSubject() {
        Subject subject = new Subject(this.name);
        subject.setId(this.id);
        subject.setIsExam(this.isExam);

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
