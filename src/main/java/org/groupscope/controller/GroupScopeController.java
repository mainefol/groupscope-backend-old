package org.groupscope.controller;


import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.security.auth.CustomUser;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class GroupScopeController {

    @Value("${server.port}")
    private int serverPort;

    private final String API_LINE;

    private final GroupScopeService groupScopeService;

    @Autowired
    public GroupScopeController(GroupScopeService groupScopeService) {
        this.groupScopeService = groupScopeService;
        API_LINE = "http://localhost:" + serverPort;
    }

    private void logRequestMapping(CustomUser user, HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        log.info("{} {}{} {}", request.getMethod(), API_LINE, requestPath, user);
    }

    @GetMapping("/subject/all")
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            List<SubjectDTO> subjectsDto = groupScopeService.getAllSubjectsByGroup(user.getLearner().getLearningGroup());

            return ResponseEntity.ok(subjectsDto);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/subject/add")
    public ResponseEntity<HttpStatus> addSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.addSubject(subjectDTO, user.getLearner().getLearningGroup());

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PatchMapping("/subject/patch")
    public ResponseEntity<HttpStatus> patchSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.updateSubject(subjectDTO, user.getLearner().getLearningGroup());

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @DeleteMapping("/subject/{subject-name}/delete")
    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.deleteSubject(subjectName);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/subject/{subject-name}/task/all")
    public ResponseEntity<List<TaskDTO>> getTasksOfSubject(@PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            List<TaskDTO> tasks = groupScopeService.getAllTasksOfSubject(user.getLearner(), subjectName);

            return ResponseEntity.ok(tasks);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }


    @GetMapping("/subject/{subject-name}/grade/all")
    public ResponseEntity<List<GradeDTO>> getGradesOfSubject(@PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            List<GradeDTO> gradeDTOs = groupScopeService.getAllGradesOfSubject(subjectName, user.getLearner());

            return ResponseEntity.ok(gradeDTOs);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/subject/{subject-name}/task/add")
    public ResponseEntity<HttpStatus> addTask(@RequestBody TaskDTO taskDTO,
                                              @PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.addTask(taskDTO, subjectName);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PatchMapping("/subject/{subject-name}/task/patch")
    public ResponseEntity<HttpStatus> patchTask(@RequestBody TaskDTO taskDTO,
                                                @PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.updateTask(taskDTO, subjectName);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    // Process dto class with only filled name field
    @DeleteMapping("/subject/{subject-name}/task/delete")
    public ResponseEntity<HttpStatus> deleteTask(@RequestBody TaskDTO taskDTO,
                                                 @PathVariable("subject-name") String subjectName) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            if(user.getLearner().getRole().equals(LearningRole.HEADMAN)) {
                groupScopeService.deleteTask(subjectName, taskDTO);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/student")
    public ResponseEntity<LearnerDTO> getStudent() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            LearnerDTO learnerDTO = LearnerDTO.from(user.getLearner());

            return ResponseEntity.ok(learnerDTO);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    // It maybe will be in service in the future
    @PostMapping("/student/add")
    public ResponseEntity<HttpStatus> addStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            //groupScopeService.addStudent(learnerDTO, groupId);

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PatchMapping("/student/patch")
    public ResponseEntity<HttpStatus> updateStudent(@RequestBody LearnerDTO learnerDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            groupScopeService.updateLearner(learnerDTO, user.getLearner());

            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    //TODO added user deleting
    @DeleteMapping("/student/delete")
    public ResponseEntity<HttpStatus> deleteStudent() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            // delete user
            groupScopeService.deleteLearner(user.getLearner().getName());
            // delete learner
            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/group")
    public ResponseEntity<LearningGroupDTO> getGroup() {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            LearningGroupDTO learningGroupDTO = groupScopeService.getGroup(user.getLearner());

            return ResponseEntity.ok(learningGroupDTO);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/group/create")
    public ResponseEntity<HttpStatus> createGroup(@RequestBody LearningGroupDTO learningGroupDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            learningGroupDTO.setHeadmen(LearnerDTO.from(user.getLearner()));
            groupScopeService.addGroup(learningGroupDTO);

            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/group/join")
    public ResponseEntity<HttpStatus> joinToGroup(@RequestBody LearningGroupDTO learningGroupDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            groupScopeService.addStudent(user.getLearner(), learningGroupDTO.getInviteCode());

            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

    }

    @PostMapping("/grade")
    public ResponseEntity<HttpStatus> updateGrade(@RequestBody GradeDTO gradeDTO) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            groupScopeService.updateGrade(gradeDTO, user.getLearner());

            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/grades")
    public ResponseEntity<HttpStatus> updateGrades(@RequestBody List<GradeDTO> gradeDTOs) {
        try {
            CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            logRequestMapping(user, request);

            gradeDTOs.forEach(gradeDTO -> groupScopeService.updateGrade(gradeDTO, user.getLearner()));

            return ResponseEntity.ok().build();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
