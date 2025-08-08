package com.example.sportsbooking.service;

import com.example.sportsbooking.entity.Court;
import com.example.sportsbooking.repository.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<Court> findAll() { return courtRepository.findAll(); }

    public Court findById(Long id) { return courtRepository.findById(id).orElseThrow(); }

    public Court create(Court court) { return courtRepository.save(court); }

    public Court update(Long id, Court updated) {
        Court existing = findById(id);
        existing.setName(updated.getName());
        existing.setSportType(updated.getSportType());
        existing.setLocation(updated.getLocation());
        existing.setHourlyRate(updated.getHourlyRate());
        existing.setImageUrl(updated.getImageUrl());
        return courtRepository.save(existing);
    }

    public void delete(Long id) { courtRepository.deleteById(id); }
}