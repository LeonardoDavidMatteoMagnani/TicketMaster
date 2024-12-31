package it.unibo.ticketmaster.data.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Amministratore del sistema.
@Entity
@Table(name = "AMMINISTRATORE")
public class Amministratore extends Utente {

    public Amministratore() {
        super();
    }

    public Amministratore(String email, String nome, String cognome, String password) {
        super(email, nome, cognome, password);
    }

}
