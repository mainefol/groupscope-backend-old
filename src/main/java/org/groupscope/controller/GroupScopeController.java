package org.groupscope.controller;


import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getSubjects")
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

    @GetMapping("/getTasksOfSubject/{subject_name}")
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject(@PathVariable("subject_name") String subjectName) {
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

    @PostMapping("/{id}/addSubject")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO,
                                                 @PathVariable("id") Long groupId) {
        try {
            groupScopeService.addSubject(subjectDTO, groupId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/addTask/{subject_name}")
    public ResponseEntity<HttpStatus> addTask(@RequestBody TaskDTO taskDTO,
                                              @PathVariable("subject_name") String subjectName) {
        try {
            groupScopeService.addTask(taskDTO, subjectName);
            return ResponseEntity.ok().build(); // return "ok" to client
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getStudent/{id}")
    public ResponseEntity<LearnerDTO> getStudent(@PathVariable("id") Long id) {
        try {
            LearnerDTO student = LearnerDTO.from(groupScopeService.getStudentById(id));

            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/addStudent")
    public ResponseEntity<HttpStatus> addStudent(@RequestBody LearnerDTO learnerDTO,
                                                 @PathVariable("id") Long groupId) {
        try {
            groupScopeService.addStudent(learnerDTO, groupId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getGroup/{id}")
    public ResponseEntity<LearningGroupDTO> getGroup(@PathVariable("id") Long id) {
        try {
            LearningGroupDTO learningGroupDTO = LearningGroupDTO.from(groupScopeService.getGroupById(id));

            return ResponseEntity.ok(learningGroupDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addGroup")
    public ResponseEntity<HttpStatus> addGroup(@RequestBody LearningGroupDTO learningGroupDTO) {
        try {
            groupScopeService.addGroup(learningGroupDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info("ERROR: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addGrade/{id}")
    public ResponseEntity<HttpStatus> addGrade(@RequestBody GradeDTO gradeDTO,
                                               @PathVariable("id") Long learnerId) {
        try {
            groupScopeService.updateGrade(gradeDTO, learnerId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
