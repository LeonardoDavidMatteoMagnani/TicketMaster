package it.unibo.ticketmaster.data.model.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ValutazioneOperatoreId implements Serializable{
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "numero_ticket")
    private Ticket ticket;

    public ValutazioneOperatoreId() {
    }

    public ValutazioneOperatoreId(@NotNull Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}
