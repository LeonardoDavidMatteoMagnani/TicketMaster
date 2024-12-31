package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.Utente;

public interface UtenteRepository extends JpaRepository<Utente, String> {

    @Query("SELECT m FROM Messaggio m WHERE lower(:email) = lower(m.utente.email)")
    List<Messaggio> getUtenteMessagges(@Param("email") String email);

    @Query("SELECT EXISTS (SELECT 1 FROM Utente u WHERE lower(:email) = lower(u.email) AND u.password = :password)")
    Boolean auth(@Param("email") String email, @Param("password") String password);

}
