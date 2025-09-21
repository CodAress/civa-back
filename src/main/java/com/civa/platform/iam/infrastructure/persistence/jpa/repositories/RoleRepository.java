package com.civa.platform.iam.infrastructure.persistence.jpa.repositories;

import com.civa.platform.iam.domain.model.entities.Role;
import com.civa.platform.iam.domain.model.valueobjects.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles name);
    boolean existsByName(Roles name);
    
    // Métodos que solo devuelven registros activos
    @Query("SELECT r FROM Role r WHERE r.isActive = true")
    List<Role> findAllActive();
    
    @Query("SELECT r FROM Role r WHERE r.isActive = true")
    Page<Role> findAllActive(Pageable pageable);
    
    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.isActive = true")
    Optional<Role> findByIdAndActive(@Param("id") Long id);
    
    @Query("SELECT r FROM Role r WHERE r.name = :name AND r.isActive = true")
    Optional<Role> findByNameAndActive(@Param("name") Roles name);
}
