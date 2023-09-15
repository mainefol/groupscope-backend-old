package org.groupscope.util;

import org.groupscope.entity.Task;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class DateValidatorOfDeadline {

    public static boolean isValid(Task task) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");

        try {
            dateTimeFormatter.parse(task.getDeadline());
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}
