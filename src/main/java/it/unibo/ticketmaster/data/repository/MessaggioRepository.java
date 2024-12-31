package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.MessaggioId;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import jakarta.persistence.Tuple;

public interface MessaggioRepository extends JpaRepository<Messaggio, MessaggioId> {

    @Query("SELECT u.nome AS nome, u.cognome AS cognome, m.testo AS testo FROM Messaggio m"
            + " LEFT JOIN Utente u ON u.email = m.utente.email"
            + " WHERE m.id.ticket = :ticket" 
            + " ORDER BY m.id.dataScrittura ASC")
    List<Tuple> findMessaggiByTicket(@Param("ticket") Ticket ticket);

}
