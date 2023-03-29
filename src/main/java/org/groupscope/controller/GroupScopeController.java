package org.groupscope.controller;


import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
*
* */
@Slf4j
@RestController
@RequestMapping("/")
public class GroupScopeController {

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        try {

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject() {
        try {

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity addSubject(@RequestBody Subject subject) {
        try {

        } catch (Exception e) {
            // Must be release the exception that will say what`s wrong
        }
    }

    @PostMapping
    public ResponseEntity addTask(@RequestBody TaskDTO task) {
        try {

        } catch (Exception e) {
            // Must be release the exception that will say what`s wrong
        }
    }

    @GetMapping
    public ResponseEntity<LearnerDTO> getStudent() {
        try {

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<LearningGroupDTO> getGroup() {
        try {

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // @PostMapping
    // implement grade for every student
}
