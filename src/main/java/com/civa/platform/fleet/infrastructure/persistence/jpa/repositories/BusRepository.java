package com.civa.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.civa.platform.fleet.domain.model.agregates.Bus;
import com.civa.platform.fleet.domain.model.valueobjects.BusNumber;
import com.civa.platform.fleet.domain.model.valueobjects.LicensePlate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {
    boolean existsByNumber (BusNumber number);
    boolean existsByLicensePlate (LicensePlate licensePlate);
    
    // Métodos que solo devuelven registros activos
    @Query("SELECT b FROM Bus b WHERE b.isActive = true")
    List<Bus> findAllActive();
    
    @Query("SELECT b FROM Bus b WHERE b.isActive = true")
    Page<Bus> findAllActive(Pageable pageable);
    
    @Query("SELECT b FROM Bus b WHERE b.id = :id AND b.isActive = true")
    Optional<Bus> findByIdAndActive(@Param("id") Long id);
    
    // Paginación con filtros eficientes (solo activos)
    @Query("SELECT b FROM Bus b WHERE b.number.number LIKE %:busNumber% AND b.isActive = true")
    Page<Bus> findByBusNumberContainingAndActive(@Param("busNumber") String busNumber, Pageable pageable);
    
    @Query("SELECT b FROM Bus b WHERE b.licensePlate.plate LIKE %:licensePlate% AND b.isActive = true")
    Page<Bus> findByLicensePlateContainingAndActive(@Param("licensePlate") String licensePlate, Pageable pageable);
    
    // Paginación con ordenamiento por defecto (solo activos)
    @Query("SELECT b FROM Bus b WHERE b.isActive = true ORDER BY b.number.number ASC")
    Page<Bus> findAllActiveOrderedByNumber(Pageable pageable);
    
    // Métodos para validar dependencias con BusBrand
    @Query("SELECT COUNT(b) FROM Bus b WHERE b.busBrand.id = :brandId AND b.isActive = true")
    long countActiveBusesByBrandId(@Param("brandId") Long brandId);
    
    @Query("SELECT COUNT(b) FROM Bus b WHERE b.busBrand.id = :brandId AND b.isActive = false")
    long countInactiveBusesByBrandId(@Param("brandId") Long brandId);
    
    @Query("SELECT COUNT(b) FROM Bus b WHERE b.busBrand.id = :brandId")
    long countTotalBusesByBrandId(@Param("brandId") Long brandId);
    
    @Query("SELECT b FROM Bus b WHERE b.busBrand.id = :brandId AND b.isActive = true")
    List<Bus> findActiveBusesByBrandId(@Param("brandId") Long brandId);
}
