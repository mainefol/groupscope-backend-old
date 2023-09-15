package org.groupscope.service;

import org.groupscope.dao.GroupScopeDAO;
import org.groupscope.dto.*;
import org.groupscope.entity.*;
import org.groupscope.entity.grade.Grade;
import org.groupscope.entity.grade.GradeKey;
import org.groupscope.services.GroupScopeService;
import org.groupscope.services.GroupScopeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class GroupScopeServiceTest {
    @Mock
    private GroupScopeDAO groupScopeDAO;

    private GroupScopeService groupScopeService;

    private static final int USED_ONCE = 1;
    private static final int UNUSED = 0;

    @BeforeEach
    void setUp() {
        groupScopeService = new GroupScopeServiceImpl(groupScopeDAO);
    }

    /**This method generates a valid Learner object with a name, last name, learning role,
     * and a learning group. It returns the created Learner object.
     */
    private Learner generateValidLearnerHeadmenAndGroup() {
        Random random = new Random();
        Learner learner = new Learner("Name", "Lastname", LearningRole.HEADMAN, generateValidGroup());
        learner.setId(Math.abs(random.nextLong()) % 100);

        learner.getLearningGroup().setHeadmen(learner);
        learner.getLearningGroup().setLearners(List.of(learner));

        return learner;
    }

    private Learner generateValidLearnerStudent() {
        Random random = new Random();
        Learner learner = new Learner();

        learner.setId(Math.abs(random.nextLong()) % 100);
        learner.setName("Name");
        learner.setLastname("Lastname");
        learner.setRole(LearningRole.STUDENT);

        return learner;
    }

    private Learner generateValidLearnerStudent(LearningGroup linkedGroup) {
        Random random = new Random();
        Learner learner = new Learner("Name", "Lastname", LearningRole.STUDENT, linkedGroup);
        learner.setId(Math.abs(random.nextLong()) % 100);

        List<Learner> learners = new ArrayList<>(linkedGroup.getLearners());
        learners.add(learner);

        learner.getLearningGroup().setLearners(learners);


        return learner;
    }

    /**This method generates a valid LearningGroup object with a name, a randomly generated ID
     * and invite code.
     */
    private LearningGroup generateValidGroup() {
        Random random = new Random();
        LearningGroup group = new LearningGroup("Group");
        group.setId(Math.abs(random.nextLong()) % 100);
        group.generateInviteCode();

        return group;
    }

    /**This method generates a valid Subject object with a given name and a randomly generated ID.
     */
    private Subject generateValidSubject(String name) {
        Random random = new Random();
        Subject subject = new Subject(name);
        subject.setId(Math.abs(random.nextLong()) % 100);

        return subject;
    }

    /** This method generates a valid Task object with a task name, type, description, deadline date,
     * and a linked subject. It also assigns a randomly generated ID to the task.
     */
    private Task generateValidTask(Subject linkedSubject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);
        Random random = new Random();

        Task task = new Task("Pz1", TaskType.PRACTICAL, "Doing something", deadline, linkedSubject);
        task.setId(Math.abs(random.nextLong()) % 100);

        return task;
    }

    private Grade generateValidGrade(Learner learner, Task task) {
        return new Grade(
                new GradeKey(learner.getId(), task.getId()),
                learner, task, false, 0
        );
    }

    @Test
    public void addSubject_WithValidSubject_save() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");
        LearningGroup learningGroup = new LearningGroup();
        learningGroup.setSubjects(Collections.singletonList(new Subject("Іноземна мова", learningGroup)));

        Subject subject = groupScopeService.addSubject(subjectDTO, learningGroup);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).saveSubject(subject);
    }

    @Test
    public void addSubject_WithExistSubjectInGroup_throwsIllegalArgumentException() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");
        LearningGroup learningGroup = new LearningGroup();
        learningGroup.setSubjects(Collections.singletonList(new Subject("Програмування", learningGroup)));

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.addSubject(subjectDTO, learningGroup));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).saveSubject(any());
    }

    @Test
    public void addSubject_WithNullGroup_throwsNullPointerException() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");

        assertThrows(NullPointerException.class, () -> groupScopeService.addSubject(subjectDTO, null));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).saveSubject(any());
    }

    @Test
    public void getSubjectByName_WithValidSubjectName_returnSubject() {
        String subjectName = "Програмування";
        LearningGroup learningGroup = new LearningGroup();
        Subject expectedSubject = new Subject("Програмування", learningGroup);
        learningGroup.setSubjects(Collections.singletonList(expectedSubject));

        Subject subject = groupScopeService.getSubjectByName(subjectName, learningGroup);

        assertEquals(expectedSubject, subject);
    }

    @Test
    public void getSubjectByName_WithWrongSubjectName_throwsNullPointerException() {
        String subjectName = "Програмування";
        LearningGroup learningGroup = new LearningGroup();
        Subject expectedSubject = new Subject("Іноземна мова", learningGroup);
        learningGroup.setSubjects(Collections.singletonList(expectedSubject));

        assertThrows(NullPointerException.class,
                () -> groupScopeService.getSubjectByName(subjectName, learningGroup));
    }

    @Test
    public void getSubjectByName_WithNullGroup_throwsNullPointerException() {
        String subjectName = "Програмування";

        assertThrows(NullPointerException.class, () -> groupScopeService.getSubjectByName(subjectName, null));
    }

    @Test
    public void updateSubject_WithValidArguments_returnSubject() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");
        subjectDTO.setNewName("Прог");
        LearningGroup learningGroup = new LearningGroup();
        learningGroup.setSubjects(Collections.singletonList(new Subject("Програмування", learningGroup)));

        Subject subject = groupScopeService.updateSubject(subjectDTO, learningGroup);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).updateSubject(subject);
    }

    @Test
    public void updateSubject_WithInvalidSubjectDTO_throwsNullPointerException() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");
        subjectDTO.setNewName("Прог");
        LearningGroup learningGroup = new LearningGroup();
        learningGroup.setSubjects(Collections.singletonList(new Subject("Іноземна мова", learningGroup)));

        assertThrows(NullPointerException.class, () -> groupScopeService.updateSubject(subjectDTO, learningGroup));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).updateSubject(any());
    }

    @Test
    public void updateSubject_WithNullArguments_throwsNullPointerException() {
        SubjectDTO subjectDTO = new SubjectDTO("Програмування");
        subjectDTO.setNewName("Прог");
        LearningGroup learningGroup = new LearningGroup();
        learningGroup.setSubjects(Collections.singletonList(new Subject("Програмування", learningGroup)));

        assertThrows(NullPointerException.class, () -> groupScopeService.updateSubject(null, learningGroup));
        assertThrows(NullPointerException.class, () -> groupScopeService.updateSubject(subjectDTO, null));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).updateSubject(any());
    }

    @Test
    public void deleteSubject_WithValidSubjectName_verifyCorrectRunning() {
        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);

        Mockito.doReturn(new Subject(subjectName, group))
                        .when(groupScopeDAO)
                                .findSubjectByNameAndGroupId("Програмування", 1L);

        groupScopeService.deleteSubject(subjectName, group);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findSubjectByNameAndGroupId(any(), any());
        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .deleteSubject(any());
    }

    @Test
    public void deleteSubject_WithNullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.deleteSubject("Програмування", null));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findSubjectByNameAndGroupId(any(), any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .deleteSubject(any());
    }

    @Test
    public void deleteSubject_WithNotExistingSubjectName_throwsNullPointerException() {
        LearningGroup group = new LearningGroup();
        group.setId(1L);
        String subjectName = "Програмування";

        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());

        assertThrows(NullPointerException.class,
                () -> groupScopeService.deleteSubject(subjectName, group));

        Mockito.verify(groupScopeDAO, Mockito.times(1))
                .findSubjectByNameAndGroupId(any(), any());

        Mockito.verify(groupScopeDAO, Mockito.never())
                .deleteSubject(any());
    }

    @Test
    public void getAllSubjectsByGroup_WithValidArguments_returnListOfSubjectDTOs() {
        LearningGroup group = new LearningGroup();

        List<Subject> subjects = List.of(new Subject("Програмування", group),
                new Subject("Іноземна мова", group));

        group.setSubjects(subjects);

        List<SubjectDTO> expectedDTOs = subjects.stream()
                .map(SubjectDTO::from)
                .collect(Collectors.toList());
        List<SubjectDTO> receivedDTOs = groupScopeService.getAllSubjectsByGroup(group);


        assertIterableEquals(expectedDTOs, receivedDTOs);
    }

    @Test
    public void getAllSubjectsByGroup_WithNoArguments_returnEmptyListOfSubjectDTOs() {
        LearningGroup group = new LearningGroup();
        group.setSubjects(new ArrayList<>());

        List<SubjectDTO> receivedDTOs = groupScopeService.getAllSubjectsByGroup(group);

        assertIterableEquals(new ArrayList<>(), receivedDTOs);
    }

    @Test
    public void getAllSubjectsByGroup_WithNullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllSubjectsByGroup(null));
    }

    @Test
    public void addTask_WithValidArguments_returnTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        TaskDTO taskDTO = new TaskDTO("Пз1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline);
        String subjectName = "Програмування";

        LearningGroup group = new LearningGroup();
        group.setId(1L);
        group.setSubjects(List.of(new Subject("Програмування", group)));
        group.setLearners(List.of(new Learner("Danya", "Nikonenko", LearningRole.HEADMAN, group),
                                    new Learner("Nikita", "Liashko", LearningRole.STUDENT, group)));

        Mockito.doReturn(new Subject("Програмування", group))
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        Task task = groupScopeService.addTask(taskDTO, subjectName, group);

        Mockito.verify(groupScopeDAO,
                Mockito.times(USED_ONCE)).saveTask(task);
        Mockito.verify(groupScopeDAO,
                Mockito.times(USED_ONCE)).saveAllGrades(task.getGrades());

        assertNotNull(task.getGrades());
    }

    @Test
    public void addTask_WithInvalidTaskDTO_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().minus(7, ChronoUnit.DAYS).format(formatter);

        TaskDTO taskDTO = new TaskDTO("Пз1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline);

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.addTask(taskDTO, null, null));

        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveTask(any());
        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveAllGrades(anyList());
    }

    @Test
    public void addTask_WithInvalidSubjectName_throwsNullPointerException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        TaskDTO taskDTO = new TaskDTO("Пз1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline);
        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);

        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        assertThrows(NullPointerException.class,
                () -> groupScopeService.addTask(taskDTO, subjectName, group));

        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveTask(any());
        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveAllGrades(anyList());
    }

    @Test
    public void addTask_WithExistingTask_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        TaskDTO taskDTO = new TaskDTO("Пз1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline);

        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);

        Task task = taskDTO.toTask();
        Subject subject = new Subject("Програмування", group);
        task.setSubject(subject);
        subject.setTasks(List.of(task));

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.addTask(taskDTO, subjectName, group));

        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveTask(any());
        Mockito.verify(groupScopeDAO,
                Mockito.times(UNUSED)).saveAllGrades(anyList());
    }

    @Test
    public void getAllTasksOfSubject_WithValidArguments_returnTaskDTOList() {
        LearningGroup group = new LearningGroup();
        group.setId(1L);
        String subjectName = "Програмування";

        Subject subject = new Subject("Програмування", group);
        subject.setTasks(
                List.of(new Task("pz1",
                        TaskType.PRACTICAL,
                        "Doing something",
                        "09.09.2023",
                        subject))
        );

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        List<TaskDTO> receivedTaskDTOs = groupScopeService.getAllTasksOfSubject(subjectName, group);
        List<TaskDTO> expectedTaskDTOs = subject.getTasks()
                                            .stream()
                                            .map(TaskDTO::from)
                                            .collect(Collectors.toList());

        assertIterableEquals(receivedTaskDTOs, expectedTaskDTOs);
    }

    @Test
    public void getAllTasksOfSubject_WithInvalidArguments_throwsNullPointerException() {
        String subjectName = "Програмування";
        LearningGroup group = generateValidGroup();
        when(groupScopeDAO.findSubjectByNameAndGroupId(eq("Програмування"), anyLong())).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllTasksOfSubject(subjectName, group));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllTasksOfSubject(null, new LearningGroup()));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllTasksOfSubject(subjectName, null));
    }

    @Test
    public void updateTask_WithValidArguments_checkRunning() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);
        String newDeadline = LocalDate.now().plus(8, ChronoUnit.DAYS).format(formatter);

        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);
        Subject subject = new Subject("Програмування", group);
        subject.setId(2L);

        Task task = new Task("Pz1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline,
                subject);

        TaskDTO taskDTO = new TaskDTO("Pz1",
                TaskType.PRACTICAL,
                "do nothing",
                newDeadline);
        taskDTO.setNewName("PZ1");

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId(subjectName, group.getId());

        Mockito.doReturn(task)
                .when(groupScopeDAO)
                .findTaskByNameAndSubjectId(taskDTO.getName(), subject.getId());

        groupScopeService.updateTask(taskDTO, subjectName, group);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).updateTask(any());
    }

    @Test
    public void updateTask_WithNullSubject_throwsNullPointerException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);

        TaskDTO taskDTO = new TaskDTO("Pz1",
                TaskType.PRACTICAL,
                "do nothing",
                deadline);

        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateTask(taskDTO, subjectName, group));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).updateTask(any());
    }

    @Test
    public void updateTask_WithNullTask_throwsNullPointerException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);
        Subject subject = new Subject("Програмування", group);
        subject.setId(2L);

        TaskDTO taskDTO = new TaskDTO("Pz1",
                TaskType.PRACTICAL,
                "do nothing",
                deadline);

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findTaskByNameAndSubjectId("Pz1", 2L);

        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateTask(taskDTO, subjectName, group));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).updateTask(any());
    }

    @Test
    public void updateTask_WithInvalidDeadline_throwsIllegalArgumentException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);
        String wrongDeadline = LocalDate.now().minus(7, ChronoUnit.DAYS).format(formatter);

        String subjectName = "Програмування";
        LearningGroup group = generateValidGroup();
        Subject subject = generateValidSubject(subjectName);
        subject.setGroup(group);

        TaskDTO taskDTO = new TaskDTO("Pz1",
                TaskType.PRACTICAL,
                "do nothing",
                wrongDeadline);

        when(groupScopeDAO.findSubjectByNameAndGroupId(eq("Програмування"), anyLong()))
                .thenReturn(subject);

        when(groupScopeDAO.findTaskByNameAndSubjectId(eq("Pz1"), anyLong()))
                .thenReturn(new Task("Pz1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline,
                subject));

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.updateTask(taskDTO, subjectName, group));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).updateTask(any());
    }

    @Test
    public void deleteTask_WithValidArguments_checkRunning() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        String subjectName = "Програмування";
        LearningGroup group = new LearningGroup();
        group.setId(1L);
        Subject subject = new Subject("Програмування", group);
        subject.setId(2L);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Pz1");

        Task task = new Task("Pz1",
                TaskType.PRACTICAL,
                "Doing something",
                deadline,
                subject);
        task.setId(3L);

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId("Програмування", 1L);

        Mockito.doReturn(task)
                .when(groupScopeDAO)
                .findTaskByNameAndSubjectId(any(), eq(2L));

        groupScopeService.deleteTask(subjectName, taskDTO, group);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).deleteGradesByTask(task);
        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).deleteTaskById(task.getId());
    }

    @Test
    public void deleteTask_WithNullSubject_throwsNullPointerException() {
        when(groupScopeDAO.findSubjectByNameAndGroupId(eq("Програмування"), anyLong())).thenReturn(null);
        LearningGroup group = generateValidGroup();

        assertThrows(NullPointerException.class,
                () -> groupScopeService.deleteTask("Програмування", new TaskDTO(), group));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).deleteGradesByTask(any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).deleteTaskById(anyLong());
    }

    @Test
    public void deleteTask_WithNullTask_throwsNullPointerException() {
        when(groupScopeDAO.findSubjectByNameAndGroupId(any(), anyLong()))
                .thenReturn(generateValidSubject("Прог"));

        when(groupScopeDAO.findTaskByNameAndSubjectId(any(), anyLong())).thenReturn(null);

        LearningGroup group = generateValidGroup();

        assertThrows(NullPointerException.class,
                () -> groupScopeService.deleteTask("Програмування", new TaskDTO(), group));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).deleteGradesByTask(any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).deleteTaskById(anyLong());
    }

    @Test
    public void getAllGradesOfSubject_WithValidArguments_returnListGradeDTOs() {
        String subjectName = "Програмування";
        Subject subjectDev = new Subject("Програмування"),
                subjectLanguage = new Subject("Іноземна мова");

        Task task1 = new Task(),
                task2 = new Task(),
                task3 = new Task();
        task1.setSubject(subjectDev); task2.setSubject(subjectDev); task3.setSubject(subjectLanguage);
        task1.setId(1L); task2.setId(2L); task3.setId(3L);

        Learner learner = new Learner();
        learner.setId(4L);

        List<Grade> grades = List.of(
                new Grade(new GradeKey(learner.getId(), task1.getId()), learner, task1, false, 0),
                new Grade(new GradeKey(learner.getId(), task2.getId()), learner, task2, false, 0),
                new Grade(new GradeKey(learner.getId(), task3.getId()), learner, task3, false, 0)
        );

        learner.setGrades(grades);

        List<GradeDTO> expectedGrades = grades.stream()
                .filter(grade -> grade.getTask().getSubject().getName().equals(subjectName))
                .map(GradeDTO::from)
                .collect(Collectors.toList());

        List<GradeDTO> reqiuredGrades = groupScopeService.getAllGradesOfSubject(subjectName, learner);

        assertIterableEquals(reqiuredGrades, expectedGrades);
    }

    @Test
    public void getAllGradesOfSubject_WithInvalidArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllGradesOfSubject(null, new Learner()));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getAllGradesOfSubject("Програмування", null));
    }

    @Test
    public void getAllGradesOfSubject_WithNullGradesFromLearner_returnEmptyListGradeDTOs() {
        String subjectName = "Програмування";
        Learner learner = new Learner();

        List<GradeDTO> reqiuredGrades = groupScopeService.getAllGradesOfSubject(subjectName, learner);
        List<GradeDTO> emptyGrades = new ArrayList<>();

        assertIterableEquals(reqiuredGrades, emptyGrades);
    }

    @Test
    public void getGradesOfSubjectFromGroup_WithValidArguments_returnListLearnerDTOs() {
        String subjectName = "Програмування";
        Subject subjectDev = new Subject("Програмування"),
                subjectLanguage = new Subject("Іноземна мова");

        Task task1 = new Task(),
                task2 = new Task(),
                task3 = new Task();
        task1.setSubject(subjectDev); task2.setSubject(subjectDev); task3.setSubject(subjectLanguage);
        task1.setId(1L); task2.setId(2L); task3.setId(3L);

        Learner learner = new Learner();
        learner.setId(4L);

        List<Grade> grades = List.of(
                new Grade(new GradeKey(learner.getId(), task1.getId()), learner, task1, false, 0),
                new Grade(new GradeKey(learner.getId(), task2.getId()), learner, task2, false, 0),
                new Grade(new GradeKey(learner.getId(), task3.getId()), learner, task3, false, 0)
        );

        LearningGroup group = new LearningGroup();
        group.setId(5L);
        group.setLearners(List.of(learner));

        List<GradeDTO> expectedGrades = grades.stream()
                .filter(grade -> grade.getTask().getSubject().getName().equals(subjectName))
                .map(GradeDTO::from)
                .collect(Collectors.toList());

        Mockito.doReturn(grades)
                .when(groupScopeDAO)
                .findAllGradesByLearner(any());

        List<LearnerDTO> reqiuredLearners = groupScopeService.getGradesOfSubjectFromGroup(subjectName, group);

        for(LearnerDTO learnerDTO : reqiuredLearners) {
            assertIterableEquals(learnerDTO.getGrades(), expectedGrades);
        }

        Mockito.verify(groupScopeDAO, Mockito.times(group.getLearners().size())).findAllGradesByLearner(any());
    }

    @Test
    public void getGradesOfSubjectFromGroup_WithNullArguments_returnListLearnerDTOs() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getGradesOfSubjectFromGroup(null, new LearningGroup()));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getGradesOfSubjectFromGroup("Програмування", null));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).findAllGradesByLearner(any());
    }

    @Test
    public void getGradesOfSubjectFromGroup_WithNotExistSubject_returnListLearnerDTOsWithEmptyGrades() {
        String subjectName = "Програмування";
        Subject subjectDev = new Subject("ООП"),
                subjectLanguage = new Subject("Іноземна мова");

        Task task1 = new Task(),
                task2 = new Task(),
                task3 = new Task();
        task1.setSubject(subjectDev); task2.setSubject(subjectDev); task3.setSubject(subjectLanguage);
        task1.setId(1L); task2.setId(2L); task3.setId(3L);

        Learner learner = new Learner();
        learner.setId(4L);

        List<Grade> grades = List.of(
                new Grade(new GradeKey(learner.getId(), task1.getId()), learner, task1, false, 0),
                new Grade(new GradeKey(learner.getId(), task2.getId()), learner, task2, false, 0),
                new Grade(new GradeKey(learner.getId(), task3.getId()), learner, task3, false, 0)
        );

        LearningGroup group = new LearningGroup();
        group.setId(5L);
        group.setLearners(List.of(learner));

        List<GradeDTO> expectedGrades = new ArrayList<>();

        Mockito.doReturn(grades)
                .when(groupScopeDAO)
                .findAllGradesByLearner(any());

        List<LearnerDTO> reqiuredLearners = groupScopeService.getGradesOfSubjectFromGroup(subjectName, group);

        for(LearnerDTO learnerDTO : reqiuredLearners) {
            assertIterableEquals(learnerDTO.getGrades(), expectedGrades);
        }

        Mockito.verify(groupScopeDAO, Mockito.times(group.getLearners().size())).findAllGradesByLearner(any());
    }

    @Test
    public void getGradesOfSubjectFromGroup_WithNotExistGrades_returnListLearnerDTOsWithEmptyGrades() {
        String subjectName = "Програмування";

        Learner learner = new Learner();
        learner.setId(4L);

        LearningGroup group = new LearningGroup();
        group.setId(5L);
        group.setLearners(List.of(learner));

        List<GradeDTO> expectedGrades = new ArrayList<>();

        Mockito.doReturn(new ArrayList<>())
                .when(groupScopeDAO)
                .findAllGradesByLearner(any());

        List<LearnerDTO> reqiuredLearners = groupScopeService.getGradesOfSubjectFromGroup(subjectName, group);

        for(LearnerDTO learnerDTO : reqiuredLearners) {
            assertIterableEquals(learnerDTO.getGrades(), expectedGrades);
        }

        Mockito.verify(groupScopeDAO, Mockito.times(group.getLearners().size())).findAllGradesByLearner(any());
    }

    @Test
    public void updateGrade_WithValidArguments_checkRunning() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadline = LocalDate.now().plus(7, ChronoUnit.DAYS).format(formatter);

        GradeDTO gradeDTO = new GradeDTO("Програмування",
                "Pz1",
                true,
                100);

        LearningGroup group = new LearningGroup();
        group.setId(0L);

        Subject subject = new Subject("Програмування");
        subject.setId(2L);

        Learner learner = new Learner();
        learner.setLearningGroup(group);
        learner.setId(3L);

        Task task = new Task("Pz1", TaskType.PRACTICAL, "Doing something", deadline);
        task.setId(4L);

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());
        Mockito.doReturn(task)
                .when(groupScopeDAO)
                .findTaskByNameAndSubjectId(eq("Pz1"), anyLong());
        Mockito.doReturn(new Grade())
                .when(groupScopeDAO)
                .findGradeById(any(GradeKey.class));

        groupScopeService.updateGrade(gradeDTO, learner);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());
        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findTaskByNameAndSubjectId(eq("Pz1"), anyLong());
        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findGradeById(any(GradeKey.class));
    }

    @Test
    public void updateGrade_WithInvalidGradeDTO_throwsIllegalArgumentException() {
        GradeDTO gradeDTOTest1 = new GradeDTO("Програмування",
                "Pz1",
                true,
                101);

        GradeDTO gradeDTOTest2 = new GradeDTO("Програмування",
                "Pz1",
                true,
                -1);

        GradeDTO gradeDTOTest3 = new GradeDTO("Програмування",
                null,
                true,
                100);

        GradeDTO gradeDTOTest4 = new GradeDTO(null,
                "Pz1",
                true,
                100);

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.updateGrade(gradeDTOTest1, new Learner()));
        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.updateGrade(gradeDTOTest2, new Learner()));
        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.updateGrade(gradeDTOTest3, new Learner()));
        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.updateGrade(gradeDTOTest4, new Learner()));

        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findSubjectByNameAndGroupId(any(), any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findTaskByNameAndSubjectId(any(), any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findGradeById(any());
    }

    @Test
    public void updateGrade_WithNotExistSubject_throwsNullPointerException() {
        GradeDTO gradeDTO = new GradeDTO("Програмування",
                "Pz1",
                true,
                100);

        LearningGroup group = new LearningGroup();
        group.setId(0L);

        Learner learner = new Learner();
        learner.setLearningGroup(group);
        learner.setId(3L);

        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());

        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateGrade(gradeDTO, learner));

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findTaskByNameAndSubjectId(any(), any());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findGradeById(any());
    }

    @Test
    public void updateGrade_WithNotExistTask_throwsNullPointerException() {
        GradeDTO gradeDTO = new GradeDTO("Програмування",
                "Pz1",
                true,
                100);

        LearningGroup group = new LearningGroup();
        group.setId(0L);

        Subject subject = new Subject("Програмування");
        subject.setId(2L);

        Learner learner = new Learner();
        learner.setLearningGroup(group);
        learner.setId(3L);

        Mockito.doReturn(subject)
                .when(groupScopeDAO)
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());
        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findTaskByNameAndSubjectId(eq("Pz1"), anyLong());

        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateGrade(gradeDTO, learner));

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findSubjectByNameAndGroupId(eq("Програмування"), anyLong());
        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findTaskByNameAndSubjectId(eq("Pz1"), anyLong());
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED))
                .findGradeById(any());
    }

    @Test
    public void addLearner_WithValidArguments_returnSavedLearner() {
        Learner learner = generateValidLearnerStudent();

        LearningGroup newGroup = generateValidLearnerHeadmenAndGroup().getLearningGroup();
        String inviteCode = newGroup.getInviteCode();
        newGroup.setSubjects(List.of(generateValidSubject("Прог")));
        Task task = generateValidTask(newGroup.getSubjects().get(0));
        newGroup.getSubjects().get(0).setTasks(List.of(task));

        Grade gradeOld = generateValidGrade(
                learner,
                generateValidTask(new Subject("Іноземна мова"))
        );
        learner.setGrades(List.of(gradeOld));

        Grade gradeNew = generateValidGrade(
                learner,
                task
        );
        List<Grade> expectedGrades = List.of(gradeNew);

        Mockito.doReturn(newGroup)
                .when(groupScopeDAO)
                .findLearningGroupByInviteCode(inviteCode);

        Mockito.doAnswer(invocationOnMock -> {
            Learner l = invocationOnMock.getArgument(0);
            return l;
                })
                .when(groupScopeDAO)
                .saveLearner(any(Learner.class));

        Learner reqiuredLearner = groupScopeService.addLearner(learner, inviteCode);

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE))
                .findLearningGroupByInviteCode(eq(inviteCode));

        assertIterableEquals(reqiuredLearner.getGrades(), expectedGrades);
        assertEquals(reqiuredLearner.getLearningGroup(), newGroup);
        assertEquals(reqiuredLearner.getRole(), LearningRole.STUDENT);
    }

    @Test
    public void addLearner_WithIncludingLearner_throwsIllegalArgumentException() {
        LearningGroup group = generateValidLearnerHeadmenAndGroup().getLearningGroup();
        Learner learner = generateValidLearnerStudent(group);

        when(groupScopeDAO.findLearningGroupByInviteCode(anyString())).thenReturn(group);

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.addLearner(learner, "Code"));
    }

    @Test
    public void addLearner_WithNullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.addLearner(null, "code"));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.addLearner(new Learner(), null));
    }

    @Test
    public void addLearner_WithNotExistGroup_throwsNullPointerException() {
        Mockito.doReturn(null)
                .when(groupScopeDAO)
                .findLearningGroupByInviteCode(anyString());

        assertThrows(NullPointerException.class,
                () -> groupScopeService.addLearner(new Learner(), "Code"));
    }


    @Test
    public void getLearnerById_WithValidArguments_returnLearner() {
        Learner expectedLearner = generateValidLearnerHeadmenAndGroup();

        Mockito.doReturn(expectedLearner)
                .when(groupScopeDAO)
                .findLearnerById(expectedLearner.getId());

        Learner reqiuredLearner = groupScopeService.getLearnerById(expectedLearner.getId());

        assertEquals(expectedLearner, reqiuredLearner);
    }

    @Test
    public void getLearnerById_WithNotExistLearner_throwsNullPointerException() {
        when(groupScopeDAO.findLearnerById(1L)).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> groupScopeService.getLearnerById(1L));

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).findLearnerById(anyLong());
    }

    @Test
    public void addFreeLerner_WithValidArguments_returnSavedLearner() {
        LearnerDTO learnerDTO = new LearnerDTO("Name", "Lastname", LearningRole.STUDENT);

        doAnswer(invocationOnMock -> {
            return invocationOnMock.getArgument(0);
        }).when(groupScopeDAO).saveLearner(any(Learner.class));

        Learner learner = groupScopeService.addFreeLearner(learnerDTO);

        List<Grade> emptyGrades = new ArrayList<>();

        assertNull(learner.getLearningGroup());
        assertIterableEquals(learner.getGrades(), emptyGrades);
    }

    @Test
    public void addFreeLerner_WithNullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.addFreeLearner(null));
    }

    @Test
    public void updateLearner_WithValidArguments_returnLearner() {
        String newName = "NewName", newLastname = "NewLastname";
        LearnerDTO learnerDTO = new LearnerDTO();
        learnerDTO.setNewName(newName);
        learnerDTO.setNewLastname(newLastname);

        Learner learner = generateValidLearnerHeadmenAndGroup();

        doAnswer(invocationOnMock -> {
            return invocationOnMock.getArgument(0);
        }).when(groupScopeDAO).updateLearner(any(Learner.class));

        Learner requiredLearner = groupScopeService.updateLearner(learnerDTO, learner);

        assertEquals(requiredLearner.getName(), newName);
        assertEquals(requiredLearner.getLastname(), newLastname);
    }

    @Test
    public void updateLearner_WithNullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateLearner(null, new Learner()));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateLearner(new LearnerDTO(), null));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.updateLearner(null, null));
    }

    @Test
    public void deleteLearner_WithValidArguments_checkRunning() {
        doNothing()
                .when(groupScopeDAO)
                .deleteLearner(any(Learner.class));

        groupScopeService.deleteLearner(new Learner());

        Mockito.verify(groupScopeDAO, Mockito.times(USED_ONCE)).deleteLearner(any(Learner.class));
    }

    @Test
    public void deleteLearner_WithInvalidArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.deleteLearner(null));
        Mockito.verify(groupScopeDAO, Mockito.times(UNUSED)).deleteLearner(any(Learner.class));
    }

    @Test
    public void getGroup_WithValidArguments_returnLearningGroup() {
        Learner learner = generateValidLearnerHeadmenAndGroup();

        when(groupScopeDAO.findAllGradesByLearner(any(Learner.class))).thenReturn(new ArrayList<>());

        LearningGroupDTO learningGroupDTO = groupScopeService.getGroup(learner);

        assertNotNull(learningGroupDTO);
    }

    @Test
    public void getGroup_WithNullLearner_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getGroup(null));
    }

    @Test
    public void getGroup_WithNullLearningGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.getGroup(new Learner()));
    }

    @Test
    public void addGroup_WithValidArgumentsAndNewLearnerHeadman_returnSavedLearningGroup() {
        LearningGroupDTO learningGroupDTO = new LearningGroupDTO("Group name",
                new LearnerDTO("Name", "Lastname"));

        when(groupScopeDAO.getAllGroups()).thenReturn(new ArrayList<>());

        doAnswer(invocationOnMock -> {
            LearningGroup group = invocationOnMock.getArgument(0);
            group.setId(1L);
            return group;
        }).when(groupScopeDAO).saveGroup(any(LearningGroup.class));

        LearningGroup reqiuredGroup = groupScopeService.addGroup(learningGroupDTO);

        assertNotNull(reqiuredGroup);
        assertNotNull(reqiuredGroup.getHeadmen());
        assertNotNull(reqiuredGroup.getInviteCode());
    }

    @Test
    public void addGroup_WithValidArgumentsAndExistLearnerHeadman_returnSavedLearningGroup() {
        Learner headman = generateValidLearnerHeadmenAndGroup();

        LearningGroupDTO learningGroupDTO = new LearningGroupDTO("Group name",
                LearnerDTO.from(headman));

        when(groupScopeDAO.getAllGroups()).thenReturn(new ArrayList<>());

        doAnswer(invocationOnMock -> {
            LearningGroup group = invocationOnMock.getArgument(0);
            group.setId(1L);
            return group;
        }).when(groupScopeDAO).saveGroup(any(LearningGroup.class));

        LearningGroup reqiuredGroup = groupScopeService.addGroup(learningGroupDTO);

        List<Grade> emptyList = new ArrayList<>();

        assertNotNull(reqiuredGroup);
        assertNotNull(reqiuredGroup.getHeadmen());
        assertNotNull(reqiuredGroup.getInviteCode());
        assertIterableEquals(reqiuredGroup.getHeadmen().getGrades(), emptyList);

        verify(groupScopeDAO, atLeastOnce()).deleteGradesByLearner(any(Learner.class));
    }

    @Test
    public void addGroup_WithExistGroup_throwsIllegalArgumentException() {
        LearningGroupDTO learningGroupDTO = new LearningGroupDTO("Group name",
                new LearnerDTO("Name", "Lastname"));
        learningGroupDTO.getHeadmen().setRole(LearningRole.HEADMAN);

        when(groupScopeDAO.getAllGroups()).thenReturn(List.of(learningGroupDTO.toLearningGroup()));

        assertThrows(IllegalArgumentException.class, () -> groupScopeService.addGroup(learningGroupDTO));

        verify(groupScopeDAO, never()).saveGroup(any(LearningGroup.class));
    }

    @Test
    public void addGroup_WithNullGroupDTO_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.addGroup(null));
    }

    @Test
    public void getGroupById_WithValidArguments_returnLearningGroup() {
        when(groupScopeDAO.findGroupById(anyLong())).thenReturn(new LearningGroup());

        LearningGroup reqiuredGroup = groupScopeService.getGroupById(1L);

        assertNotNull(reqiuredGroup);
    }

    @Test
    public void getGroupById_WithWrongArguments_returnLearningGroup() {
        when(groupScopeDAO.findGroupById(anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class,
                () ->groupScopeService.getGroupById(1L));
        assertThrows(NullPointerException.class,
                () ->groupScopeService.getGroupById(null));
    }

    @Test
    public void getGroupByInviteCode_WithValidArguments_returnLearningGroup() {
        when(groupScopeDAO.findLearningGroupByInviteCode(anyString())).thenReturn(new LearningGroup());

        LearningGroup reqiuredGroup = groupScopeService.getGroupByInviteCode("Code");

        assertNotNull(reqiuredGroup);
    }

    @Test
    public void getGroupByInviteCode_WithWrongArguments_returnLearningGroup() {
        when(groupScopeDAO.findLearningGroupByInviteCode(anyString())).thenReturn(null);
        assertThrows(NullPointerException.class,
                () ->groupScopeService.getGroupByInviteCode("Code"));
        assertThrows(NullPointerException.class,
                () ->groupScopeService.getGroupByInviteCode(null));
    }

    @Test
    public void refreshLearnerGrades_WithValidArgumentsAndNewLearner_returnRefreshedLearner() {
        Learner headman = generateValidLearnerHeadmenAndGroup();
        LearningGroup group = headman.getLearningGroup();

        Learner learner = generateValidLearnerStudent(group);

        Subject subject = generateValidSubject("Subject_1");
        group.getSubjects().add(subject);

        Task task = generateValidTask(subject);
        subject.getTasks().add(task);

        doAnswer(invocationOnMock -> {
            Learner l = invocationOnMock.getArgument(0);
            return l;
        }).when(groupScopeDAO).saveLearner(any(Learner.class));

        Learner reqiuredLearner = groupScopeService.refreshLearnerGrades(learner, group);

        List<Grade> expectedGrades = List.of(generateValidGrade(learner, task));

        assertNotNull(reqiuredLearner);
        assertIterableEquals(expectedGrades, reqiuredLearner.getGrades());
    }

    @Test
    public void refreshLearnerGrades_WithValidArgumentsAndExistLearner_returnRefreshedLearner() {
        Learner headman = generateValidLearnerHeadmenAndGroup();
        LearningGroup group = headman.getLearningGroup();

        Learner learner = generateValidLearnerStudent(group);

        Subject subject = generateValidSubject("Subject_1");
        group.getSubjects().add(subject);

        Task task = generateValidTask(subject);
        subject.getTasks().add(task);

        doAnswer(invocationOnMock -> {
            Learner l = invocationOnMock.getArgument(0);
            return l;
        }).when(groupScopeDAO).saveLearner(any(Learner.class));

        Learner reqiuredLearner = groupScopeService.refreshLearnerGrades(learner, group);

        List<Grade> expectedGrades = List.of(generateValidGrade(learner, task));

        assertNotNull(reqiuredLearner);
        assertIterableEquals(expectedGrades, reqiuredLearner.getGrades());

        verify(groupScopeDAO, atMostOnce()).deleteGradesByLearner(any());
        verify(groupScopeDAO, atMostOnce()).saveLearner(any(Learner.class));
        verify(groupScopeDAO, atMostOnce()).saveAllGrades(anyList());
    }

    @Test
    public void refreshLearnerGrades_WithNotIncludedLearner_throwsIllegalArgumentException() {
        Learner headman = generateValidLearnerHeadmenAndGroup();
        LearningGroup group = headman.getLearningGroup();

        Learner learner = generateValidLearnerStudent();

        assertThrows(IllegalArgumentException.class,
                () -> groupScopeService.refreshLearnerGrades(learner, group));

        verify(groupScopeDAO, never()).deleteGradesByLearner(any());
        verify(groupScopeDAO, never()).saveLearner(any(Learner.class));
        verify(groupScopeDAO, never()).saveAllGrades(anyList());
    }

    @Test
    public void refreshLearnerGrades_WithNullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> groupScopeService.refreshLearnerGrades(null, new LearningGroup()));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.refreshLearnerGrades(new Learner(), null));
        assertThrows(NullPointerException.class,
                () -> groupScopeService.refreshLearnerGrades(null, null));

        verify(groupScopeDAO, never()).deleteGradesByLearner(any());
        verify(groupScopeDAO, never()).saveLearner(any(Learner.class));
        verify(groupScopeDAO, never()).saveAllGrades(anyList());
    }
}
