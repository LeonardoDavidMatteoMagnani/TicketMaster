package it.unibo.ticketmaster.data.model.relation;

import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Tutorial;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class SoluzioneId {
    
    @ManyToOne
    @JoinColumn(name = "numero_ticket")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "tutorial_id")
    private Tutorial tutorial;

    public SoluzioneId() {
    }

    public SoluzioneId(Ticket ticket, Tutorial tutorial) {
        this.ticket = ticket;
        this.tutorial = tutorial;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    

}
