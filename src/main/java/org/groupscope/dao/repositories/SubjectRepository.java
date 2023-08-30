package org.groupscope.dao.repositories;

import org.groupscope.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    Subject getSubjectByName(String name);

    List<Subject> findAllByGroup_Name(String groupName);

    void deleteSubjectByName(String name);
}
