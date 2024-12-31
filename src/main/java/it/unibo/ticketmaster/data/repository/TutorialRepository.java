package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.unibo.ticketmaster.data.model.entity.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
}
