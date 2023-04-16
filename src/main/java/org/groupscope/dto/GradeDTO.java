package org.groupscope.dto;

import lombok.Data;

import java.util.List;
import java.util.Objects;

// This class used to get grade changes from the client
// Then update database

@Data
public class GradeDTO {
    // It can be any type of data, int used for the example
    private int learnerId;

    private SubjectDTO subject;

    private List<TaskDTO> tasks;

}
