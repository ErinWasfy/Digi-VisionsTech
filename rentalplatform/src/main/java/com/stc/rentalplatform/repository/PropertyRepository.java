package com.stc.rentalplatform.repository;

import com.stc.rentalplatform.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    @Query("SELECT p FROM Property p WHERE p.tenantId = :tenantId")
    Page<Property> findAllByTenant(@Param("tenantId") String tenantId, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR p.location LIKE %:location%) AND " +
            "(:amenities IS NULL OR p.amenities LIKE %:amenities%) AND " +
            "p.available = true AND p.tenantId = :tenantId")
    Page<Property> searchProperties(@Param("location") String location,
                                    @Param("amenities") String amenities,
                                    @Param("tenantId") String tenantId,
                                    Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.location = :location " +
            "p.available=true "+"AND p.id NOT IN (" +
            "SELECT b.propertyId FROM Booking b " +
            ") " +
            "AND (:amenities IS NULL OR EXISTS (" +
            "SELECT a FROM Property prop JOIN prop.amenities a WHERE a IN :amenities AND prop.id = p.id))")
    List<Property> searchByCriteria(
            @Param("location") String location,
            @Param("amenities") List<String> amenities
    );
}

