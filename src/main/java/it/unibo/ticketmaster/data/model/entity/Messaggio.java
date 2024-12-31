package it.unibo.ticketmaster.data.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Commento associato ad un ticket.
@Entity
@Table(name = "MESSAGGIO")
public class Messaggio {

    @EmbeddedId
    private MessaggioId id;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "email_utente", referencedColumnName = "email")
    private Utente utente;

    // Testo del commento.
    @NotEmpty
    private String testo;

    public Messaggio() {
    }

    public Messaggio(@NotNull MessaggioId id, @Nullable Utente utente, @NotEmpty String testo) {
        this.id = id;
        this.testo = testo;
        this.utente = utente;
    }

    public MessaggioId getId() {
        return id;
    }

    public void setId(MessaggioId id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

}
