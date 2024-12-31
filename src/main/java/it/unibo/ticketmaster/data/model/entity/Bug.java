package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Bug riportato del software.
@Entity
@Table(name = "BUG")
public class Bug {

    // Identificativo del bug.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Ticket associato al bug.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "numero_ticket")
    private Ticket ticket;

    // Descrizione del bug.
    @NotEmpty
    private String descrizione;

    // Priorità del bug.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_priorita")
    private PrioritaBug priorita;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_stato")
    private StatoBug stato;

    public Bug() {
    }

    public Bug(@NotNull Ticket ticket, @NotEmpty String descrizione, @NotNull PrioritaBug priorita, @NotNull StatoBug stato) {
        this.ticket = ticket;
        this.descrizione = descrizione;
        this.priorita = priorita;
        this.stato = stato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public PrioritaBug getPriorita() {
        return priorita;
    }

    public void setPriorita(PrioritaBug priorita) {
        this.priorita = priorita;
    }

    public StatoBug getStato() {
        return stato;
    }

    public void setStato(StatoBug stato) {
        this.stato = stato;
    }

}
