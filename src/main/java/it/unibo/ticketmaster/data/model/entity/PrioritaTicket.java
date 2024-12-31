package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Priorità di un ticket.
@Entity
@Table(name = "PRIORITA_TICKET")
public class PrioritaTicket {

    // Nome della priorità del ticket.
    @Id
    private String nome;

    public PrioritaTicket() {
    }

    public PrioritaTicket(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
