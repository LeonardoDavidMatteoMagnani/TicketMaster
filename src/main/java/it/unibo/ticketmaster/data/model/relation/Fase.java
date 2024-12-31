package it.unibo.ticketmaster.data.model.relation;

import java.sql.Timestamp;

import jakarta.annotation.Nullable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "FASE")
public class Fase {

    @EmbeddedId
    private FaseId id;

    @NotNull
    private Timestamp dataOraInizio;
    
    public Fase() {
    }

    public Fase(FaseId id, Timestamp dataOraInizio) {
        this.id = id;
        this.dataOraInizio = dataOraInizio;
    }

    public FaseId getId() {
        return id;
    }

    public void setId(FaseId id) {
        this.id = id;
    }

    public Timestamp getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(Timestamp dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

}
