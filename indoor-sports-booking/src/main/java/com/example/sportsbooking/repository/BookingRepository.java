package com.example.sportsbooking.repository;

import com.example.sportsbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
            "WHERE b.court.id = :courtId AND b.bookingDate = :bookingDate AND b.status <> 'CANCELLED' AND " +
            "((b.startTime < :endTime AND b.endTime > :startTime))")
    boolean existsOverlappingBooking(@Param("courtId") Long courtId,
                                     @Param("bookingDate") LocalDate bookingDate,
                                     @Param("startTime") LocalTime startTime,
                                     @Param("endTime") LocalTime endTime);

    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);
}