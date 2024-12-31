package it.unibo.ticketmaster.data.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Tecnico del sistema.
@Entity
@Table(name = "OPERATORE")
public class Operatore extends Utente {

    public Operatore() {
        super();
    }

    public Operatore(String email, String nome, String cognome, String password) {
        super(email, nome, cognome, password);
    }

}
