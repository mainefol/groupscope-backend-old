package org.groupscope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.groupscope.entity.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    private String name;

    private String newName;

    private TaskType type;

    private String info;

    private String newInfo;

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
    private boolean isValid() {
        return true;
    }

}
