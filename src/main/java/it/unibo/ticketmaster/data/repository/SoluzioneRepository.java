package it.unibo.ticketmaster.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.relation.Soluzione;
import it.unibo.ticketmaster.data.model.relation.SoluzioneId;

public interface SoluzioneRepository extends JpaRepository<Soluzione, SoluzioneId> {
    
    @Query("SELECT s FROM Soluzione s WHERE s.id.ticket = :ticket")
    public List<Soluzione> findSoluzioniTicket(@Param("ticket") Ticket ticket);

}
