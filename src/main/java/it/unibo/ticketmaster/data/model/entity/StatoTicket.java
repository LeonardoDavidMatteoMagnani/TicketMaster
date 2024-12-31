package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Stato di proseguimento di un ticket.
@Entity
@Table(name = "STATO_TICKET")
public class StatoTicket {

    // Nome dello stato del ticket.
    @Id
    private String nome;

    public StatoTicket() {
    }

    public StatoTicket(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
