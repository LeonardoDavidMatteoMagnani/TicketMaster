package it.unibo.ticketmaster.data.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SISTEMA_OPERATIVO")
public class SistemaOperativo {

    // Nome del sistema operativo del ticket.
    @Id
    private String nome;

    public SistemaOperativo() {
    }

    public SistemaOperativo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
