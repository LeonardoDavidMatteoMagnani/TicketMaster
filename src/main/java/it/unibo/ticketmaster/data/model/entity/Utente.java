package it.unibo.ticketmaster.data.model.entity;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Persona registrata al sistema.
@Entity
@Table(name = "UTENTE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utente {

    // Email persona.
    @Id
    @Email
    @NotEmpty
    private String email;

    // Nome persona.
    @NotEmpty
    private String nome;
    // Cognome persona.
    @NotEmpty
    private String cognome;
    // Password persona.
    @NotEmpty
    private String password;

    public Utente() {
    }

    public Utente(@NotEmpty String email, @NotEmpty String nome, @NotEmpty String cognome, @NotEmpty String password) {
        this.email = email.toLowerCase();
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
