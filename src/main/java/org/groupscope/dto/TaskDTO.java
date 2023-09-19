package org.groupscope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.groupscope.entity.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    private String name;

    private String newName;

    private TaskType type;

    private String info;

    private String deadline;

    public TaskDTO(String name, TaskType type, String info, String deadline) {
        this.name = name;
        this.type = type;
        this.info = info;
        this.deadline = deadline;
    }

    public static TaskDTO from(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setType(task.getType());
        dto.setInfo(task.getInfo());
        dto.setDeadline(task.getDeadline());

        return dto;
    }

    public Task toTask() {
        if(this.isValid()) {
            Task task = new Task();
            task.setId(this.getId());
            task.setName(this.getName());
            task.setType(this.getType());
            task.setInfo(this.getInfo());
            task.setDeadline(this.getDeadline());

            return task;
        } else
            throw new IllegalArgumentException("Wrong task type or date format in Task object");
    }

    // TODO finish validation
    //  to check: type and deadline
    public boolean isValid() {
        return this.isValidDeadline();
    }

    public boolean isValidDeadline() {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate now = LocalDate.now();
            LocalDate deadline = LocalDate.parse(this.getDeadline(), dateFormatter);

            if (deadline.isBefore(now))
                return false;

            dateFormatter.parse(this.getDeadline());
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return Objects.equals(name, taskDTO.name) && type == taskDTO.type && Objects.equals(info, taskDTO.info) && Objects.equals(deadline, taskDTO.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, info, deadline);
    }
}
