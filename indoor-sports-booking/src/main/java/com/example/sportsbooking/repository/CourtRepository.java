package com.example.sportsbooking.repository;

import com.example.sportsbooking.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}