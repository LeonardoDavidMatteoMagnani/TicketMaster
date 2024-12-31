package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Stato di proseguimento di un bug.
@Entity
@Table(name = "STATO_BUG")
public class StatoBug {

    // Nome dello stato del bug.
    @Id
    private String nome;

    public StatoBug() {
    }

    public StatoBug(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
