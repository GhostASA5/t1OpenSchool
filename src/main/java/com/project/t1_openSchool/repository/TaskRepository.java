package com.project.t1_openSchool.repository;

import com.project.t1_openSchool.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
