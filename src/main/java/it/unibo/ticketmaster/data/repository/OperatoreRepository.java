package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.Ticket;

public interface OperatoreRepository extends UtenteRepository {

    // Query to get the operatore with less asigned tickets
    /*@Query("SELECT o FROM Operatore o"
            + " LEFT JOIN Ticket t ON o.email = t.operatore.email"
            + " LEFT JOIN (SELECT f1.id.ticket AS ticket, MAX(f1.dataOraInizio) AS maxDataOraInizio FROM Fase f1 GROUP BY f1.id.ticket) AS lf ON t = lf.ticket"
            + " LEFT JOIN Fase f2 ON t = f2.id.ticket AND lf.maxDataOraInizio = f2.dataOraInizio AND (f2.id.statoTicket.nome = 'aperto' OR f2.id.statoTicket.nome = 'analisi')"
            + " GROUP BY o.email"
            + " ORDER BY COUNT(t.operatore.email) ASC"
            + " LIMIT 1")*/
    @Query("SELECT o FROM Operatore o"
            + " LEFT JOIN Ticket t ON o.email = t.operatore.email"
            + " AND (t.stato.nome = 'aperto' OR t.stato.nome = 'analisi')"
            + " GROUP BY o.email"
            + " ORDER BY COUNT(o.email) ASC"
            + " LIMIT 1")
    Operatore findOperatoreWithLessTickets();

    @Query("SELECT EXISTS (SELECT 1 FROM Utente u WHERE lower(:email) = lower(u.email))")
    boolean isEmailFree(@Param("email") String email);

    default void create(Operatore operatore) throws DataIntegrityViolationException {
        if (!this.isEmailFree(operatore.getEmail())) {
            this.save(operatore);
        } else {
            throw new DataIntegrityViolationException("Email already in use");
        }
    }

    @Query("SELECT t FROM Ticket t WHERE lower(:email) = lower(t.operatore.email)")
    List<Ticket> getOperatoreTickets(@Param("email") String email);

    @Query("SELECT o FROM Operatore o"
            + " WHERE :testo IS NULL OR ("
            + "lower(o.email) LIKE lower(concat('%', :testo, '%'))"
            + " OR lower(o.nome) LIKE lower(concat('%', :testo, '%'))"
            + " OR lower(o.cognome) LIKE lower(concat('%', :testo, '%'))"
            + ")")
    List<Operatore> search(@Param("testo") String testo);

}
