package it.unibo.ticketmaster.data.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Utente del sistema.
@Entity
@Table(name = "CLIENTE")
public class Cliente extends Utente {

    public Cliente() {
        super();
    }

    public Cliente(String email, String nome, String cognome, String password) {
        super(email, nome, cognome, password);
    }

}