package it.unibo.ticketmaster.data.model.relation;

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.StatoBug;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class SviluppoBugId {

    @ManyToOne
    @JoinColumn(name = "bug_id")
    private Bug bug;

    @ManyToOne
    @JoinColumn(name = "nome_stato")
    private StatoBug statoBug;

    public SviluppoBugId() {
    }

    public SviluppoBugId(@NotNull Bug bug, @NotNull StatoBug statoBug) {
        this.bug = bug;
        this.statoBug = statoBug;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }

    public StatoBug getStatoBug() {
        return statoBug;
    }

    public void setStatoBug(StatoBug statoBug) {
        this.statoBug = statoBug;
    }

}
