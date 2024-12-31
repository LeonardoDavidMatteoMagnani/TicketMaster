package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unibo.ticketmaster.data.model.entity.PrioritaBug;

public interface PrioritaBugRepository extends JpaRepository<PrioritaBug, String> {
}
