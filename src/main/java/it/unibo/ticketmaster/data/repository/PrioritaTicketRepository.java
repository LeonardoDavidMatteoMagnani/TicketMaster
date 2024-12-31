package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;

public interface PrioritaTicketRepository extends JpaRepository<PrioritaTicket, String> {
}
