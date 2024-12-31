package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Priorità di un bug.
@Entity
@Table(name = "PRIORITA_BUG")
public class PrioritaBug {

    // Nome della priorità del bug.
    @Id
    private String nome;

    public PrioritaBug() {
    }

    public PrioritaBug(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
