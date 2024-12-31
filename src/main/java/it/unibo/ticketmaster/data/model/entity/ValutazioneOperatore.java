package it.unibo.ticketmaster.data.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

// Valutazione di un operatore da parte di un cliente.
@Entity
@Table(name = "VALUTAZIONE_OPERATORE")
public class ValutazioneOperatore {

    // Identificativo della valutazione.
    @EmbeddedId
    private ValutazioneOperatoreId id;

    // Voto assegnato al operatore.
    @NotNull
    @Column(name = "voto")
    private int voto;

    // Commento del cliente.
    @Nullable
    @Column(name = "commento")
    private String commento;

    public ValutazioneOperatore() {
    }

    public ValutazioneOperatore(@NotNull ValutazioneOperatoreId id, @NotNull int voto, @Nullable String commento) {
        this.id = id;
        this.voto = voto;
        this.commento = commento;
    }

    public ValutazioneOperatoreId getId() {
        return id;
    }

    public void setId(ValutazioneOperatoreId id) {
        this.id = id;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

}
