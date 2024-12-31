package it.unibo.ticketmaster.data.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Svillupatore del software.
@Entity
@Table(name = "SVILUPPATORE")
public class Sviluppatore extends Utente {

    public Sviluppatore() {
        super();
    }

    public Sviluppatore(String email, String nome, String cognome, String password) {
        super(email, nome, cognome, password);
    }

}
