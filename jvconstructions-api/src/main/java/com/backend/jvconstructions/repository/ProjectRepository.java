package com.backend.jvconstructions.repository;

import com.backend.jvconstructions.entity.Project;
import com.backend.jvconstructions.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByCode(String code);
    Page<Project> findByProjectStatus(ProjectStatus projectStatus, Pageable pageable);
    boolean existsByCode(String code);
}
