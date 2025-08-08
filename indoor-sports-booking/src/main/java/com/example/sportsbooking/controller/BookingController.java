package com.example.sportsbooking.controller;

import com.example.sportsbooking.dto.BookingRequest;
import com.example.sportsbooking.entity.Booking;
import com.example.sportsbooking.entity.User;
import com.example.sportsbooking.repository.UserRepository;
import com.example.sportsbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest request, Authentication auth) {
        Long userId = resolveCurrentUserId(auth);
        Booking created = bookingService.createBooking(userId, request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> myBookings(Authentication auth) {
        Long userId = resolveCurrentUserId(auth);
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication auth) {
        Long userId = resolveCurrentUserId(auth);
        bookingService.cancelBooking(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveCurrentUserId(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getId();
    }
}