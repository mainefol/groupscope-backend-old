package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Data
public class TaskDTO {

    private Long id;

    private TaskType type;

    private String info;

    private Boolean completion;

    private Integer grade;

    private Calendar deadline;


    public static TaskDTO from(Task<TaskType> task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setType(task.getType());
        dto.setInfo(task.getInfo());
        dto.setCompletion(task.getCompletion());
        dto.setGrade(task.getGrade());
        dto.setDeadline(task.getDeadline());

        return dto;
    }

    public Task<TaskType> toTask() {
        Task<TaskType> task = new Task<>();
        task.setId(this.getId());
        task.setType(this.getType());
        task.setInfo(this.getInfo());
        task.setCompletion(this.getCompletion());
        task.setGrade(this.getGrade());
        task.setDeadline(this.getDeadline());

        return task;
    }
}
