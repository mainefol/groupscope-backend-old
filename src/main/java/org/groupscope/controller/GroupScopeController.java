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
* TODO Divide the controller class into several small ones, like:
*                               GroupScopeController
*    |                            |                    |                            |
*   \/                           \/                   \/                           \/
*  LearnerController        TaskController          ...                           ...
* */
@Slf4j
@CrossOrigin
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

    @PostMapping("/{id}/addSubject")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO,
                                                 @PathVariable("id") Long groupId) {
        try {
            groupScopeService.addSubject(subjectDTO, groupId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{id}/patchSubject")
    public ResponseEntity<HttpStatus> patchSubject(@RequestBody SubjectDTO subjectDTO,
                                                   @PathVariable("id") Long groupId) {
        try {
            groupScopeService.updateSubject(subjectDTO, groupId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Process dto class with only filled id field
    @DeleteMapping("/deleteSubject")
    public ResponseEntity<HttpStatus> deleteSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            groupScopeService.deleteSubject(subjectDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO rework subject_name
    @GetMapping("/getTasksOfSubject/{subjectId}")
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject(@PathVariable("subjectId") Long subjectId) {
        try {
            List<TaskDTO> tasks = groupScopeService.getAllTasksOfSubject(subjectId);

            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addTask/{subjectId}")
    public ResponseEntity<HttpStatus> addTask(@RequestBody TaskDTO taskDTO,
                                              @PathVariable("subjectId") Long subjectId) {
        try {
            groupScopeService.addTask(taskDTO, subjectId);
            return ResponseEntity.ok().build(); // return "ok" to client
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/patchTask/{id}")
    public ResponseEntity<HttpStatus> patchTask(@RequestBody TaskDTO taskDTO,
                                                @PathVariable("id") Long subjectId) {
        try {
            groupScopeService.updateTask(taskDTO, subjectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Process dto class with only filled name field
    @DeleteMapping("/deleteTask")
    public ResponseEntity<HttpStatus> deleteTask(@RequestBody TaskDTO taskDTO) {
        try {
            groupScopeService.deleteTask(taskDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getStudent/{id}")
    public ResponseEntity<LearnerDTO> getStudent(@PathVariable("id") Long id) {
        try {
            LearnerDTO learner = LearnerDTO.from(groupScopeService.getStudentById(id));

            return ResponseEntity.ok(learner);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addStudent/{id}")
    public ResponseEntity<HttpStatus> addStudent(@RequestBody LearnerDTO learnerDTO,
                                                 @PathVariable("id") Long groupId) {
        try {
            groupScopeService.addStudent(learnerDTO, groupId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/patchStudent/{id}")
    public ResponseEntity<HttpStatus> updateStudent(@RequestBody LearnerDTO learnerDTO,
                                                    @PathVariable("id") Long groupId) {
        try {
            groupScopeService.updateLearner(learnerDTO, groupId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<HttpStatus> deleteStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            groupScopeService.deleteLearner(learnerDTO);
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

    @PostMapping("/updateGrade/{id}")
    public ResponseEntity<HttpStatus> updateGrade(@RequestBody GradeDTO gradeDTO,
                                               @PathVariable("id") Long learnerId) {
        try {
            groupScopeService.updateGrade(gradeDTO, learnerId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updateGrades/{id}")
    public ResponseEntity<HttpStatus> updateGrades(@RequestBody List<GradeDTO> gradeDTOs,
                                                  @PathVariable("id") Long learnerId) {
        try {
            gradeDTOs.forEach(gradeDTO -> groupScopeService.updateGrade(gradeDTO, learnerId));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
