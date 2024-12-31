package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;

public interface ProblemaTicketRepository extends JpaRepository<ProblemaTicket, String> {
}
