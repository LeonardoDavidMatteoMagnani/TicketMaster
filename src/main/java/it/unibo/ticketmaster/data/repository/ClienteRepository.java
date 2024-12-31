package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.Ticket;

public interface ClienteRepository extends UtenteRepository {

    @Query("SELECT EXISTS (SELECT 1 FROM Utente u WHERE lower(:email) = lower(u.email))")
    boolean isEmailFree(@Param("email") String email);

    default void create(Cliente cliente) throws DataIntegrityViolationException {
        if (!this.isEmailFree(cliente.getEmail())) {
            this.save(cliente);
        } else {
            throw new DataIntegrityViolationException("Email already in use");
        }
    }


    @Query("SELECT t FROM Ticket t WHERE lower(:email) = lower(t.cliente.email)")
    List<Ticket> getClienteTickets(@Param("email") String email);

}
