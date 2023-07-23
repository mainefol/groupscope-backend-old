package org.groupscope.controller;


import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.security.auth.CustomUser;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/")
public class GroupScopeController {

    private final GroupScopeService groupScopeService;

    @Autowired
    public GroupScopeController(GroupScopeService groupScopeService) {
        this.groupScopeService = groupScopeService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LearningGroup group = user.getLearner().getLearningGroup();
            List<SubjectDTO> subjects = group.getSubjects()
                    .stream()
                    .map(SubjectDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subject/add")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.addSubject(subjectDTO, user.getLearner().getLearningGroup());

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/subject/patch")
    public ResponseEntity<HttpStatus> patchSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.updateSubject(subjectDTO, user.getLearner().getLearningGroup());

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Process dto class with only filled name field
    @DeleteMapping("/subject/delete")
    public ResponseEntity<HttpStatus> deleteSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.deleteSubject(subjectDTO);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Process dto class with only filled name field
    @GetMapping("/subject/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            List<TaskDTO> tasks = groupScopeService.getAllTasksOfSubject(subjectDTO);

            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{subject-name}/task/add")
    public ResponseEntity<HttpStatus> addTask(@RequestBody TaskDTO taskDTO,
                                              @PathVariable("subject-name") String subjectName) {
        try {
            groupScopeService.addTask(taskDTO, subjectName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{subject-name}/task/patch")
    public ResponseEntity<HttpStatus> patchTask(@RequestBody TaskDTO taskDTO,
                                                @PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.updateTask(taskDTO, subjectName);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Process dto class with only filled name field
    @DeleteMapping("/{subject-name}/task/delete")
    public ResponseEntity<HttpStatus> deleteTask(@RequestBody TaskDTO taskDTO,
                                                 @PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.deleteTask(taskDTO);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/student")
    public ResponseEntity<LearnerDTO> getStudent() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LearnerDTO learnerDTO = LearnerDTO.from(user.getLearner());

            return ResponseEntity.ok(learnerDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Now it`s not working
    @PostMapping("/student/add")
    public ResponseEntity<HttpStatus> addStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            //groupScopeService.addStudent(learnerDTO, groupId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/student/patch")
    public ResponseEntity<HttpStatus> updateStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            groupScopeService.updateLearner(learnerDTO, user.getLearner());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/student/delete")
    public ResponseEntity<HttpStatus> deleteStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            groupScopeService.deleteLearner(learnerDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/group")
    public ResponseEntity<LearningGroupDTO> getGroup() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LearningGroupDTO learningGroupDTO = LearningGroupDTO.from(user.getLearner().getLearningGroup());

            return ResponseEntity.ok(learningGroupDTO);
        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/grade")
    public ResponseEntity<HttpStatus> updateGrade(@RequestBody GradeDTO gradeDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            groupScopeService.updateGrade(gradeDTO, user.getLearner());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/grades")
    public ResponseEntity<HttpStatus> updateGrades(@RequestBody List<GradeDTO> gradeDTOs) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            gradeDTOs.forEach(gradeDTO -> groupScopeService.updateGrade(gradeDTO, user.getLearner()));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
