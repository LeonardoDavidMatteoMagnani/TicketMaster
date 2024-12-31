package it.unibo.ticketmaster.data.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaadin.flow.router.NotFoundException;

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.PrioritaBug;
import it.unibo.ticketmaster.data.model.entity.StatoBug;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.relation.SviluppoBug;
import it.unibo.ticketmaster.data.model.relation.SviluppoBugId;
import it.unibo.ticketmaster.data.repository.BugRepository;
import it.unibo.ticketmaster.data.repository.PrioritaBugRepository;
import it.unibo.ticketmaster.data.repository.StatoBugRepository;
import it.unibo.ticketmaster.data.repository.SviluppoBugRepository;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final PrioritaBugRepository prioritaBugRepository;
    private final StatoBugRepository statoBugRepository;
    private final SviluppoBugRepository sviluppoBugRepository;

    public BugService(BugRepository bugRepository, PrioritaBugRepository prioritaBugRepository,
            StatoBugRepository statoBugRepository, SviluppoBugRepository sviluppoBugRepository) {
        this.bugRepository = bugRepository;
        this.prioritaBugRepository = prioritaBugRepository;
        this.statoBugRepository = statoBugRepository;
        this.sviluppoBugRepository = sviluppoBugRepository;
    }

    public List<Bug> findAllBugs(String descrizione, String priorita, String stato) {
        return this.bugRepository.search(descrizione, priorita, stato);
    }

    public Bug findBug(Long id) {
        return this.bugRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void createBug(Ticket ticket, String descrizione, PrioritaBug priorita) {
        StatoBug stato = new StatoBug("non in correzione");
        Bug bug = new Bug(ticket, descrizione, priorita, stato);
        this.bugRepository.save(bug);
        SviluppoBug sviluppoBug = new SviluppoBug(new SviluppoBugId(bug,
                stato), Timestamp.from(Instant.now()));
        this.sviluppoBugRepository.save(sviluppoBug);
    }

    public Bug createTestBug(Ticket ticket, String descrizione, PrioritaBug priorita, Timestamp dataOraInizio) {
        StatoBug stato = new StatoBug("non in correzione");
        Bug bug = new Bug(ticket, descrizione, priorita, stato);
        this.bugRepository.save(bug);
        SviluppoBug sviluppoBug = new SviluppoBug(new SviluppoBugId(bug,
                stato), dataOraInizio);
        this.sviluppoBugRepository.save(sviluppoBug);
        return bug;
    }

    public void changeSviluppoBug(Bug bug, StatoBug stato, Sviluppatore sviluppatore) {
        Timestamp now = Timestamp.from(Instant.now());
        bug.setStato(stato);
        this.bugRepository.save(bug);
        SviluppoBug newSviluppo = new SviluppoBug(new SviluppoBugId(bug, stato),
                sviluppatore, now);
        this.sviluppoBugRepository.save(newSviluppo);
    }

    public void changeTestSviluppoBug(Bug bug, String stato, Sviluppatore sviluppatore, Timestamp dataOraFine) {
        StatoBug statoRicavato = this.findStatoBugByNome(stato);
        bug.setStato(statoRicavato);
        this.bugRepository.save(bug);
        SviluppoBug newSviluppo = new SviluppoBug(new SviluppoBugId(bug, statoRicavato),
                sviluppatore, dataOraFine);
        this.sviluppoBugRepository.save(newSviluppo);
    }

    public StatoBug findStatoBugByNome(String nome) {
        return this.statoBugRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public List<StatoBug> findAllStatiBug() {
        return this.statoBugRepository.findAll();
    }

    public List<StatoBug> findAllStatiTicketLiberi(Bug bug) {
        return this.statoBugRepository.findStatiNotInSviluppoBug(bug);
    }

    public StatoBug findStatoBug(Bug bug) {
        return this.sviluppoBugRepository.findStatoBug(bug);
    }

    public SviluppoBug findLastSviluppoBug(Bug bug) {
        return this.sviluppoBugRepository.findLatestSviluppoBugByBug(bug).orElseThrow(NotFoundException::new);
    }

    public List<PrioritaBug> findAllPrioritaBug() {
        return this.prioritaBugRepository.findAll();
    }

    public PrioritaBug findPrioritaBugByNome(String nome) {
        return this.prioritaBugRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public long numberOfBugs(Timestamp dataInizio, Timestamp dataFine) {
        return this.bugRepository.countBugs(dataInizio, dataFine);
    }

    public long numberOfBugsWithStatoFromTo(String stato, Timestamp dataInizio, Timestamp dataFine) {
        return this.bugRepository.countByStato(stato, dataInizio, dataFine);
    }

    public long numberOfBugsWithProblemaFromTo(String problema, Timestamp dataInizio, Timestamp dataFine) {
        return this.bugRepository.countByProblema(problema, dataInizio, dataFine);
    }

    public long numberOfBugsWithSistemaOperativoFromTo(String sistemaOperativo, Timestamp dataInizio, Timestamp dataFine) {
        return this.bugRepository.countBySistemaOperativo(sistemaOperativo, dataInizio, dataFine);
    }

    public float averageBugDurationFromTo(Timestamp dataInizio, Timestamp dataFine) {
        Optional<Float> avg = this.bugRepository.averageBugDuration(dataInizio, dataFine);
        return avg.isPresent() ? avg.get() : 0;
    }
}
