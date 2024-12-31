package it.unibo.ticketmaster.data.model.relation;

import com.github.javaparser.quality.NotNull;

import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Tutorial;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "soluzione")
public class Soluzione {
    
    @EmbeddedId
    private SoluzioneId id;

    public Soluzione() {
    }

    public Soluzione(@NotNull Ticket ticket, @NotNull Tutorial tutorial) {
        this.id = new SoluzioneId(ticket, tutorial);
    }

    public SoluzioneId getId() {
        return id;
    }

    public void setId(SoluzioneId id) {
        this.id = id;
    }

}
