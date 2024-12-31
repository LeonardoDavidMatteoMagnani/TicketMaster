package it.unibo.ticketmaster.data.model.relation;

import java.io.Serializable;

import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class FaseId implements Serializable {

    // Ticket associato al cambiamento di fase.
    @ManyToOne
    @JoinColumn(name = "numero_ticket")
    private Ticket ticket;

    // Stato del ticket dopo il cambiamento di fase.
    @ManyToOne
    @JoinColumn(name = "nome_stato")
    private StatoTicket statoTicket;

    public FaseId() {
    }

    public FaseId(Ticket ticket, StatoTicket statoTicket) {
        this.ticket = ticket;
        this.statoTicket = statoTicket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public StatoTicket getStatoTicket() {
        return statoTicket;
    }

    public void setStatoTicket(StatoTicket statoTicket) {
        this.statoTicket = statoTicket;
    }

}
