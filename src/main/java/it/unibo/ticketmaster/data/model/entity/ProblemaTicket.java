package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Problema di un ticket.
@Entity
@Table(name = "PROBLEMA")
public class ProblemaTicket {

    // Nome del problema del ticket.
    @Id
    private String nome;

    public ProblemaTicket() {
    }

    public ProblemaTicket(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
