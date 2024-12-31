package it.unibo.ticketmaster.data.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

import com.vaadin.flow.router.NotFoundException;

import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.PrioritaBug;
import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Utente;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatore;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatoreId;
import it.unibo.ticketmaster.data.model.relation.Fase;
import it.unibo.ticketmaster.data.model.relation.FaseId;
import it.unibo.ticketmaster.data.repository.FaseRepository;
import it.unibo.ticketmaster.data.repository.MessaggioRepository;
import it.unibo.ticketmaster.data.repository.OperatoreRepository;
import it.unibo.ticketmaster.data.repository.PrioritaTicketRepository;
import it.unibo.ticketmaster.data.repository.ProblemaTicketRepository;
import it.unibo.ticketmaster.data.repository.SistemaOperativoRepository;
import it.unibo.ticketmaster.data.repository.StatoTicketRepository;
import it.unibo.ticketmaster.data.repository.TicketRepository;
import it.unibo.ticketmaster.data.repository.UtenteRepository;
import it.unibo.ticketmaster.data.repository.ValutazioneOperatoreRepository;
import jakarta.persistence.Tuple;

@Service
public class TicketUtenteService {

    private final UtenteRepository utenteRepository;
    private final OperatoreRepository operatoreRepository;
    private final TicketRepository ticketRepository;
    private final FaseRepository faseRepository;
    private final ProblemaTicketRepository problemaRepository;
    private final SistemaOperativoRepository sistemaOperativoRepository;
    private final PrioritaTicketRepository prioritaTicketRepository;
    private final StatoTicketRepository statoTicketRepository;
    private final MessaggioRepository messaggioRepository;
    private final BugService bugService;
    private final ValutazioneOperatoreRepository valutazioneOperatoreRepository;

    public TicketUtenteService(UtenteRepository utenteRepository, OperatoreRepository operatoreRepository,
            TicketRepository ticketRepository, FaseRepository faseRepository,
            ProblemaTicketRepository problemaRepository,
            SistemaOperativoRepository sistemaOperativoRepository, PrioritaTicketRepository prioritaTicketRepository,
            StatoTicketRepository statoTicketRepository, MessaggioRepository messaggioRepository, BugService bugService,
            ValutazioneOperatoreRepository valutazioneOperatoreRepository) {
        this.utenteRepository = utenteRepository;
        this.operatoreRepository = operatoreRepository;
        this.ticketRepository = ticketRepository;
        this.faseRepository = faseRepository;
        this.problemaRepository = problemaRepository;
        this.sistemaOperativoRepository = sistemaOperativoRepository;
        this.prioritaTicketRepository = prioritaTicketRepository;
        this.statoTicketRepository = statoTicketRepository;
        this.messaggioRepository = messaggioRepository;
        this.bugService = bugService;
        this.valutazioneOperatoreRepository = valutazioneOperatoreRepository;
    }

    public void createTicket(String titolo, String descrizione, PrioritaTicket priorita, ProblemaTicket problema,
            SistemaOperativo sistemaOperativo, Cliente cliente) {
        Operatore operatore = this.operatoreRepository.findOperatoreWithLessTickets();
        if (operatore == null) {
            throw new RuntimeException("No operatore found");
        }
        StatoTicket stato =  new StatoTicket("aperto");
        Ticket ticket = new Ticket(titolo, descrizione, priorita, problema, sistemaOperativo, stato, cliente, operatore);
        this.ticketRepository.save(ticket);
        Fase fase = new Fase(new FaseId(ticket, stato), Timestamp.from(Instant.now()));
        this.faseRepository.save(fase);
    }

    public Ticket createTestTicket(String titolo, String descrizione, PrioritaTicket priorita, ProblemaTicket problema,
            SistemaOperativo sistemaOperativo, Cliente cliente, Timestamp dataOraInizio) {
        Operatore operatore = this.operatoreRepository.findOperatoreWithLessTickets();
        if (operatore == null) {
            throw new RuntimeException("No operatore found");
        }
        StatoTicket stato =  new StatoTicket("aperto");
        Ticket ticket = new Ticket(titolo, descrizione, priorita, problema, sistemaOperativo, stato, cliente, operatore);
        this.ticketRepository.save(ticket);
        Fase fase = new Fase(new FaseId(ticket, stato), dataOraInizio);
        this.faseRepository.save(fase);
        return ticket;
    }

    public List<Ticket> findAllTickets(String titolo, ProblemaTicket problema, SistemaOperativo sistemaOperativo, StatoTicket stato) {
        return this.ticketRepository.search(titolo, problema, sistemaOperativo, stato);
    }

    public Ticket findTicket(long id) {
        return this.ticketRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Ticket> findAllUserTickets(String titolo, PrioritaTicket priorita, ProblemaTicket problema, SistemaOperativo sistemaOperativo, StatoTicket stato,
            String email) {
        return this.ticketRepository.searchWithUser(titolo, priorita, problema, sistemaOperativo, stato, email);
    }

    public void changeTicketFase(Ticket ticket, StatoTicket stato) {
        Timestamp now = Timestamp.from(Instant.now());
        ticket.setStato(stato);
        this.ticketRepository.save(ticket);
        Fase newFase = new Fase(new FaseId(ticket, stato), now);
        this.faseRepository.save(newFase);
    }

    public void changeTicketFaseByNome(Ticket ticket, String stato) {
        Timestamp now = Timestamp.from(Instant.now());
        StatoTicket statoRicavato = this.findStatoTicketByNome(stato);
        ticket.setStato(statoRicavato);
        this.ticketRepository.save(ticket);
        Fase newFase = new Fase(new FaseId(ticket, statoRicavato), now);
        this.faseRepository.save(newFase);
    }

    public void changeTestTicketFase(Ticket ticket, String stato, Timestamp dataOraFine) {
        StatoTicket statoRicavato = this.findStatoTicketByNome(stato);
        ticket.setStato(statoRicavato);
        this.ticketRepository.save(ticket);
        Fase newFase = new Fase(new FaseId(ticket, statoRicavato), dataOraFine);
        this.faseRepository.save(newFase);
    }

    public List<ProblemaTicket> findAllProblemi() {
        return this.problemaRepository.findAll();
    }

    public ProblemaTicket findProblemaByNome(String nome) {
        return this.problemaRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public List<SistemaOperativo> findAllSistemiOperativi() {
        return this.sistemaOperativoRepository.findAll();
    }

    public SistemaOperativo findSistemaOperativoByNome(String nome) {
        return this.sistemaOperativoRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public List<PrioritaTicket> findAllPrioritaTicket() {
        return this.prioritaTicketRepository.findAll();
    }

    public PrioritaTicket findPrioritaTicketByNome(String nome) {
        return this.prioritaTicketRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public List<StatoTicket> findAllStatiTicket() {
        return this.statoTicketRepository.findAll();
    }

    public List<StatoTicket> findAllStatiTicketLiberi(Ticket ticket) {
        return this.statoTicketRepository.findStatiNotInFase(ticket);
    }

    public StatoTicket findStatoTicketByNome(String nome) {
        return this.statoTicketRepository.findById(nome).orElseThrow(NotFoundException::new);
    }

    public void createBug(Ticket ticket, String descrizione, PrioritaBug priorita) {
        this.bugService.createBug(ticket, descrizione, priorita);
    }

    public List<PrioritaBug> findAllPrioritaBug() {
        return this.bugService.findAllPrioritaBug();
    }

    public PrioritaBug findPrioritaBugByNome(String nome) {
        return this.bugService.findPrioritaBugByNome(nome);
    }

    public Utente findUser(String email) {
        return this.utenteRepository.findById(email).orElseThrow(NotFoundException::new);
    }

    public List<Tuple> findMessaggiByTicket(Ticket ticket) {
        return this.messaggioRepository.findMessaggiByTicket(ticket);
    }

    public void createMessaggio(Messaggio messaggio) {
        this.messaggioRepository.save(messaggio);
    }

    public Fase findLastFase(Ticket ticket) {
        return this.faseRepository.findLatestFaseByTicket(ticket).orElseThrow(NotFoundException::new);
    }

    public void createValutazioneOperatore(ValutazioneOperatore valutazioneOperatore) {
        this.valutazioneOperatoreRepository.save(valutazioneOperatore);
    }

    public boolean isTicketValutato(Ticket ticket) {
        ValutazioneOperatoreId id = new ValutazioneOperatoreId(ticket);
        return this.valutazioneOperatoreRepository.findById(id).isPresent();
    }

}
