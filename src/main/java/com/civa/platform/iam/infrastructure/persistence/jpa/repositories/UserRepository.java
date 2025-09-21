package com.civa.platform.iam.infrastructure.persistence.jpa.repositories;

import com.civa.platform.iam.domain.model.aggregates.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    // Métodos que solo devuelven registros activos
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActive();
    
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Page<User> findAllActive(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isActive = true")
    Optional<User> findByIdAndActive(@Param("id") Long id);
    
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isActive = true")
    Optional<User> findByUsernameAndActive(@Param("username") String username);
    
    // Paginación con filtros (solo activos)
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username% AND u.isActive = true")
    Page<User> findByUsernameContainingAndActive(@Param("username") String username, Pageable pageable);
}
