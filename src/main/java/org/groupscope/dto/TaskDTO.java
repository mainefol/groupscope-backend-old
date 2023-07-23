package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Data
public class TaskDTO {

    private Long id;

    private String name;

    private String newName;

    private TaskType type;

    private String info;

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
        Task task = new Task();
        task.setId(this.getId());
        task.setName(this.getName());
        task.setType(this.getType());
        task.setInfo(this.getInfo());
        task.setDeadline(this.getDeadline());

        return task;
    }
}
