package it.unibo.ticketmaster.data.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Tutorial;
import it.unibo.ticketmaster.data.model.relation.Soluzione;
import it.unibo.ticketmaster.data.repository.SoluzioneRepository;
import it.unibo.ticketmaster.data.repository.TicketRepository;
import it.unibo.ticketmaster.data.repository.TutorialRepository;
import jakarta.persistence.Tuple;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TutorialRepository tutorialRepository;
    private final SoluzioneRepository soluzioneTicketRepository;
    
    public TicketService(TicketRepository ticketRepository, TutorialRepository tutorialRepository, SoluzioneRepository soluzioneTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.tutorialRepository = tutorialRepository;
        this.soluzioneTicketRepository = soluzioneTicketRepository;
    }

    public long numberOfTicketsWithStatoFromTo(String stato, Timestamp dataInizio, Timestamp dataFine) {
        return this.ticketRepository.countByStato(stato, dataInizio, dataFine);
    }

    public long numberOfTicketsWithProblemaFromTo(String problema, Timestamp dataInizio, Timestamp dataFine) {
        return this.ticketRepository.countByProblema(problema, dataInizio, dataFine);
    }

    public long numberOfTicketsWithSistemaOperativoFromTo(String sistemaOperativo, Timestamp dataInizio, Timestamp dataFine) {
        return this.ticketRepository.countBySistemaOperativo(sistemaOperativo, dataInizio, dataFine);
    }

    public long numberOfTicketsFromTo(Timestamp dataInizio, Timestamp dataFine) {
        return this.ticketRepository.countTickets(dataInizio, dataFine);
    }

    public float averageTicketDurationFromTo(Timestamp dataInizio, Timestamp dataFine) {
        Optional<Float> avg = this.ticketRepository.averageTicketDuration(dataInizio, dataFine);
        return avg.isPresent() ? avg.get() : 0;
    }

    public List<Tuple> findBestOperators() {
        return this.ticketRepository.findBestOperators();
    }

    public Float averageOperatorsVoto() {
        return this.ticketRepository.averageOperatorsVoto();
    }

    public List<Tutorial> findAllTutorials() {
        return this.tutorialRepository.findAll();
    }

    public void createTicketSolution(Ticket ticket, List<Tutorial> tutorials) {
        tutorials.forEach(tutorial -> {
            Soluzione soluzioneTicket = new Soluzione(ticket, tutorial);
            this.soluzioneTicketRepository.save(soluzioneTicket);
        });
    }

    public List<Soluzione> findSoluzioniTicket(Ticket ticket) {
        return this.soluzioneTicketRepository.findSoluzioniTicket(ticket);
    }

}
