package it.unibo.ticketmaster.data.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class MessaggioId implements Serializable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "numero_ticket", referencedColumnName = "numero")
    private Ticket ticket;

    @NotNull
    private Timestamp dataScrittura;

    public MessaggioId() {
    }

    public MessaggioId(@NotNull Ticket ticket, @NotNull Timestamp dataScrittura) {
        this.ticket = ticket;
        this.dataScrittura = dataScrittura;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Timestamp getDataScrittura() {
        return dataScrittura;
    }

    public void setDataScrittura(Timestamp dataScrittura) {
        this.dataScrittura = dataScrittura;
    }

}
