package org.groupscope.dto;

import lombok.Data;
import org.groupscope.entity.grade.Grade;

// This class used to get grade changes from the client
// Then update database

@Data
public class GradeDTO {

    private String subjectName;

    private String taskName;

    private Boolean completion;

    private Integer mark;

    public static GradeDTO from(Grade grade) {
        GradeDTO dto = new GradeDTO();
        dto.subjectName = grade.getTask().getSubject().toString();
        dto.taskName = grade.getTask().getName().toString();
        dto.completion = grade.getCompletion();
        dto.mark = grade.getMark();
        return dto;
    }

    public Grade toGrade() {
        Grade grade = new Grade();
        grade.setCompletion(this.completion);
        grade.setMark(this.mark);
        return grade;
    }

    // TODO finish validation
    //  to check: type and deadline
    public boolean isValid() {
        return (mark >= 0 && mark <= 100);
    }
}