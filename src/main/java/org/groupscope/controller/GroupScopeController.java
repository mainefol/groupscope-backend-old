package org.groupscope.controller;


import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
*
* */
@Slf4j
@RestController
@RequestMapping("/")
public class GroupScopeController {

    private GroupScopeService groupScopeService;

    @Autowired
    public GroupScopeController(GroupScopeService groupScopeService) {
        this.groupScopeService = groupScopeService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        try {
            List<SubjectDTO> subjects = groupScopeService.getAllSubjects()
                    .stream()
                    .map(SubjectDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject(@RequestParam("subjectName") String subjectName) {
        try {
            Subject subject = groupScopeService.getSubjectByName(subjectName);
            List<TaskDTO> tasksOfSubject = groupScopeService.getAllTasksOfSubject(subject)
                    .stream()
                    .map(TaskDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(tasksOfSubject);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            groupScopeService.addSubject(subjectDTO.toSubject());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addTask(@RequestBody TaskDTO taskDTO) {
        try {
            groupScopeService.addTask(taskDTO.toTask());

            return ResponseEntity.ok().build(); // return "ok" to client
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<LearnerDTO> getStudent(@PathVariable("id") int id) {
        try {
            LearnerDTO student = LearnerDTO.fromOneLearner(groupScopeService.getStudentById(id));

            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<LearningGroupDTO> getGroup(@PathVariable("id") int id) {
        try {
            LearningGroupDTO learningGroupDTO = LearningGroupDTO.from(groupScopeService.getGroupById(id));

            return ResponseEntity.ok(learningGroupDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping ResponseEntity<HttpStatus> addGrade(@RequestBody GradeDTO gradeDTO) {
        try {


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
