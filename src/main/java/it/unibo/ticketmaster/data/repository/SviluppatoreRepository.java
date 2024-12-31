package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.model.relation.SviluppoBug;

public interface SviluppatoreRepository extends UtenteRepository {

    @Query("SELECT EXISTS (SELECT 1 FROM Utente u WHERE lower(:email) = lower(u.email))")
    boolean isEmailFree(@Param("email") String email);

    default void create(Sviluppatore sviluppatore) throws DataIntegrityViolationException {
        if (!this.isEmailFree(sviluppatore.getEmail())) {
            this.save(sviluppatore);
        } else {
            throw new DataIntegrityViolationException("Email already in use");
        }
    }

    @Query("SELECT sb FROM SviluppoBug sb WHERE lower(:email) = lower(sb.sviluppatore.email)")
    List<SviluppoBug> getSviluppatoreSviluppo(@Param("email") String email);

    @Query("SELECT s FROM Sviluppatore s"
            + " WHERE :testo IS NULL OR ("
            + "lower(s.email) LIKE lower(concat('%', :testo, '%'))"
            + " OR lower(s.nome) LIKE lower(concat('%', :testo, '%'))"
            + " OR lower(s.cognome) LIKE lower(concat('%', :testo, '%'))"
            + ")")
    List<Sviluppatore> search(@Param("testo") String testo);

}
