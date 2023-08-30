package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;


@Data
public class TaskDTO {

    private Long id;

    private String name;

    private String newName;

    private TaskType type;

    private String info;

    // TODO added ability to change info
    private String newInfo;

    private String deadline;

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
