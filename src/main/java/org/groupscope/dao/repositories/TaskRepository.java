package org.groupscope.dao.repositories;

import org.groupscope.entity.Subject;
import org.groupscope.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task,Long> {
    List<Task> findTasksBySubject(Subject subject);

    Task getTaskByNameAndSubject_Id(String name, Long subject_id);

    Task getTaskByName(String name);
}
