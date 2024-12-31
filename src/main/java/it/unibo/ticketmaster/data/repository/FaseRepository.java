package it.unibo.ticketmaster.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.relation.Fase;
import it.unibo.ticketmaster.data.model.relation.FaseId;

public interface FaseRepository extends JpaRepository<Fase, FaseId> {

    @Query("SELECT f FROM Fase f WHERE f.id.ticket = :ticket ORDER BY f.dataOraInizio DESC LIMIT 1")
    Optional<Fase> findLatestFaseByTicket(@Param("ticket") Ticket ticket);

}
