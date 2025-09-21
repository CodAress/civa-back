package com.civa.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.domain.model.valueobjects.BrandName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusBrandRepository extends JpaRepository<BusBrand, Long> {
    // Solo verifica existencia entre marcas activas (consistente con el patrón del sistema)
    @Query("SELECT COUNT(b) > 0 FROM BusBrand b WHERE b.name = :name AND b.isActive = true")
    boolean existsByName(@Param("name") BrandName name);
    
    // Métodos que solo devuelven registros activos
    @Query("SELECT b FROM BusBrand b WHERE b.isActive = true")
    List<BusBrand> findAllActive();
    
    @Query("SELECT b FROM BusBrand b WHERE b.isActive = true")
    Page<BusBrand> findAllActive(Pageable pageable);
    
    @Query("SELECT b FROM BusBrand b WHERE b.id = :id AND b.isActive = true")
    Optional<BusBrand> findByIdAndActive(@Param("id") Long id);
    
    // Paginación con filtros eficientes (solo activos)
    @Query("SELECT b FROM BusBrand b WHERE b.name.name LIKE %:brandName% AND b.isActive = true")
    Page<BusBrand> findByNameContainingAndActive(@Param("brandName") String brandName, Pageable pageable);
    
    // Paginación con ordenamiento por defecto (solo activos)
    @Query("SELECT b FROM BusBrand b WHERE b.isActive = true ORDER BY b.name.name ASC")
    Page<BusBrand> findAllActiveOrderedByName(Pageable pageable);
}
