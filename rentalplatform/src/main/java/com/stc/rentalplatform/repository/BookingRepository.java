package com.stc.rentalplatform.repository;

import com.stc.rentalplatform.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("SELECT b FROM Booking b WHERE b.propertyId = :propertyId AND " +
                "b.startDate <= :endDate AND b.endDate >= :startDate")
  List<Booking> findConflictingBookings(@Param("propertyId") Long propertyId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}
