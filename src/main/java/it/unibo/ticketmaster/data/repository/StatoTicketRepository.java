package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;

public interface StatoTicketRepository extends JpaRepository<StatoTicket, String> {

    @Query("SELECT st FROM StatoTicket st WHERE st NOT IN (SELECT f.id.statoTicket FROM Fase f WHERE f.id.ticket = :ticket)")
    List<StatoTicket> findStatiNotInFase(@Param("ticket") Ticket ticket);

}
