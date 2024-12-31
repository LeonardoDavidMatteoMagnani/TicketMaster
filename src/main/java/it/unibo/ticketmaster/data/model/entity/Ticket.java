package it.unibo.ticketmaster.data.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Ticket di supporto tecnico.
@Entity
@Table(name = "TICKET")
public class Ticket {

    // Numero ticket univoco.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numero;

    // Titolo del ticket.
    @NotEmpty
    private String titolo;

    // Descrizione del ticket.
    @NotEmpty
    private String descrizione;

    // Priorità del ticket.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_priorita")
    private PrioritaTicket priorita;

    // Problema del ticket.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_problema")
    private ProblemaTicket problema;

    // Sistema operativo del ticket.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_so")
    private SistemaOperativo sistemaOperativo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "nome_stato")
    private StatoTicket stato;

    // Utente che ha aperto il ticket.
    @Nullable
    @ManyToOne
    @JoinColumn(name = "email_cliente")
    private Cliente cliente;

    // Utente che ha aperto il ticket.
    @Nullable
    @ManyToOne
    @JoinColumn(name = "email_operatore")
    private Operatore operatore;

    public Ticket() {
    }

    public Ticket(@NotEmpty String titolo, @NotEmpty String descrizione, @NotNull PrioritaTicket priorita,
            @NotNull ProblemaTicket problema, @NotNull SistemaOperativo sistemaOperativo, @NotNull StatoTicket stato, @Nullable Cliente cliente,
            @Nullable Operatore operatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.priorita = priorita;
        this.problema = problema;
        this.sistemaOperativo = sistemaOperativo;
        this.stato = stato;
        this.cliente = cliente;
        this.operatore = operatore;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public PrioritaTicket getPriorita() {
        return priorita;
    }

    public void setPriorita(PrioritaTicket priorita) {
        this.priorita = priorita;
    }

    public ProblemaTicket getProblema() {
        return problema;
    }

    public void setProblema(ProblemaTicket problema) {
        this.problema = problema;
    }

    public SistemaOperativo getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(SistemaOperativo sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public StatoTicket getStato() {
        return stato;
    }

    public void setStato(StatoTicket stato) {
        this.stato = stato;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Operatore getOperatore() {
        return operatore;
    }

    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

}
