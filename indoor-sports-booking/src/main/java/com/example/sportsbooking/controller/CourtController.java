package com.example.sportsbooking.controller;

import com.example.sportsbooking.entity.Court;
import com.example.sportsbooking.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAll() {
        return ResponseEntity.ok(courtService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Court> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courtService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Court> create(@Valid @RequestBody Court court) {
        return ResponseEntity.ok(courtService.create(court));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Court> update(@PathVariable Long id, @Valid @RequestBody Court court) {
        return ResponseEntity.ok(courtService.update(id, court));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}