package com.example.sportsbooking.service;

import com.example.sportsbooking.dto.BookingRequest;
import com.example.sportsbooking.entity.Booking;
import com.example.sportsbooking.entity.Court;
import com.example.sportsbooking.entity.User;
import com.example.sportsbooking.enums.BookingStatus;
import com.example.sportsbooking.repository.BookingRepository;
import com.example.sportsbooking.repository.CourtRepository;
import com.example.sportsbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, CourtRepository courtRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking createBooking(Long userId, BookingRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        Court court = courtRepository.findById(request.getCourtId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        LocalDate date = request.getBookingDate();
        LocalTime start = request.getStartTime();
        LocalTime end = request.getEndTime();
        boolean conflict = bookingRepository.existsOverlappingBooking(court.getId(), date, start, end);
        if (conflict) {
            throw new IllegalStateException("Time slot already booked");
        }

        Booking booking = Booking.builder()
                .user(user)
                .court(court)
                .bookingDate(date)
                .startTime(start)
                .endTime(end)
                .status(BookingStatus.CONFIRMED)
                .build();
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Cannot cancel others' bookings");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}