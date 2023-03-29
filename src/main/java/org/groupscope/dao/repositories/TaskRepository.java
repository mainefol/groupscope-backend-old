package org.groupscope.dao.repositories;

import org.groupscope.entity.Subject;
import org.groupscope.entity.Task;
import org.groupscope.entity.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task<TaskType>,Integer> {
    List<Task<TaskType>> findTasksBySubject(Subject subject);
}
