package it.unibo.ticketmaster.data.model.relation;

import java.sql.Timestamp;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import jakarta.annotation.Nullable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SVILUPPO_BUG")
public class SviluppoBug {

    @EmbeddedId
    private SviluppoBugId id;

    @NotNull
    private Timestamp dataOraInizio;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "email_sviluppatore")
    private Sviluppatore sviluppatore;

    public SviluppoBug() {
    }

    public SviluppoBug(@NotNull SviluppoBugId id, @NotNull Timestamp dataOraInizio) {
        this.id = id;
        this.dataOraInizio = dataOraInizio;
    }

    public SviluppoBug(@NotNull SviluppoBugId id, @Nullable Sviluppatore sviluppatore,
            @NotNull Timestamp dataOraInizio) {
        this.id = id;
        this.sviluppatore = sviluppatore;
        this.dataOraInizio = dataOraInizio;
    }

    public SviluppoBugId getId() {
        return id;
    }

    public void setId(SviluppoBugId id) {
        this.id = id;
    }

    public Sviluppatore getSviluppatore() {
        return sviluppatore;
    }

    public void setSviluppatore(Sviluppatore sviluppatore) {
        this.sviluppatore = sviluppatore;
    }

}
