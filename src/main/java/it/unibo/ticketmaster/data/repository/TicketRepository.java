package it.unibo.ticketmaster.data.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import jakarta.persistence.Tuple;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t"
            + " WHERE (:titolo IS NULL OR lower(t.titolo) LIKE lower(concat('%', :titolo, '%')))"
            + " AND (:problema IS NULL OR t.problema = :problema)"
            + " AND (:sistemaOperativo IS NULL OR t.sistemaOperativo = :sistemaOperativo)"
            + " AND (:stato IS NULL OR t.stato = :stato)")
    List<Ticket> search(@Param("titolo") String titolo, @Param("problema") ProblemaTicket problema,
            @Param("sistemaOperativo") SistemaOperativo sistemaOperativo, @Param("stato") StatoTicket stato);

    @Query("SELECT t FROM Ticket t"
            + " WHERE (:titolo IS NULL OR lower(t.titolo) LIKE lower(concat('%', :titolo, '%')))"
            + " AND (:priorita IS NULL OR t.priorita = :priorita)"
            + " AND (:problema IS NULL OR t.problema = :problema)"
            + " AND (:sistemaOperativo IS NULL OR t.sistemaOperativo = :sistemaOperativo)"
            + " AND (:stato IS NULL OR t.stato = :stato)"
            + " AND (t.cliente.email = :email_utente OR t.operatore.email = :email_utente)")
    List<Ticket> searchWithUser(@Param("titolo") String titolo, @Param("priorita") PrioritaTicket priorita,
            @Param("problema") ProblemaTicket problema,
            @Param("sistemaOperativo") SistemaOperativo sistemaOperativo, @Param("stato") StatoTicket stato,
            @Param("email_utente") String email);

    @Query("SELECT COUNT(DISTINCT t) FROM Ticket t"
            + " JOIN Fase f ON f.id.ticket = t"
            + " AND (:dataInizio IS NULL OR f.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR f.dataOraInizio <= :dataFine)")
    Long countTickets(@Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(DISTINCT t) FROM Ticket t"
            + " JOIN Fase f ON f.id.ticket = t"
            + " AND f.id.statoTicket.nome = :stato"
            + " AND f.dataOraInizio = ("
                                    +"SELECT MAX(f2.dataOraInizio) FROM Fase f2"
                                    + " WHERE f2.id.ticket = t" 
                                    + " AND (f2.dataOraInizio BETWEEN COALESCE(:dataInizio, f2.dataOraInizio) AND COALESCE(:dataFine, f2.dataOraInizio))"
                                    + ")")
    Long countByStato(@Param("stato") String stato, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(DISTINCT t) FROM Ticket t"
            + " JOIN Fase f ON f.id.ticket = t"
            + " AND t.problema.nome = :problema"
            + " AND (:dataInizio IS NULL OR f.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR f.dataOraInizio <= :dataFine)")
    Long countByProblema(@Param("problema") String problema, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(DISTINCT t) FROM Ticket t"
            + " JOIN Fase f ON f.id.ticket = t"
            + " AND t.sistemaOperativo.nome = :sistemaOperativo"
            + " AND (:dataInizio IS NULL OR f.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR f.dataOraInizio <= :dataFine)")
    Long countBySistemaOperativo(@Param("sistemaOperativo") String sistemaOperativo, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, f2.dataOraInizio, f.dataOraInizio)) FROM Ticket t"
            + " JOIN Fase f ON f.id.ticket = t"
            + " JOIN Fase f2 ON f2.id.ticket = t"
            + " AND (f.id.statoTicket.nome = 'chiuso - risolto' OR f.id.statoTicket.nome = 'chiuso - non risolto')"
            + " AND f2.id.statoTicket.nome = 'aperto'"
            + " AND (:dataInizio IS NULL OR f.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR f.dataOraInizio <= :dataFine)")
    Optional<Float> averageTicketDuration(@Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT u.email AS email, u.nome AS nome, u.cognome AS cognome, COUNT(t.numero) AS numeroTickets, AVG(TIMESTAMPDIFF(HOUR, f2.dataOraInizio, f.dataOraInizio)) AS tempoMedio, AVG(vo.voto) AS votoMedio FROM Ticket t"
            + " LEFT JOIN ValutazioneOperatore vo ON vo.id.ticket.numero = t.numero"
            + " JOIN Utente u ON u.email = t.operatore.email"
            + " JOIN Fase f ON f.id.ticket.numero = t.numero"
            + " JOIN Fase f2 ON f2.id.ticket.numero = t.numero"
            + " AND (f.id.statoTicket.nome = 'chiuso - risolto' OR f.id.statoTicket.nome = 'chiuso - non risolvibile')"
            + " AND f2.id.statoTicket.nome = 'aperto'"
            + " GROUP BY t.operatore.email"
            + " ORDER BY votoMedio DESC")
    List<Tuple> findBestOperators();

    @Query("SELECT AVG(vo.voto) FROM Ticket t"
            + " JOIN ValutazioneOperatore vo ON vo.id.ticket.numero = t.numero")
    Float averageOperatorsVoto();

}
