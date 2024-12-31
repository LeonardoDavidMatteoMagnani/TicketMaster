package it.unibo.ticketmaster.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.NotFoundException;

import it.unibo.ticketmaster.data.model.entity.Amministratore;
import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Utente;
import it.unibo.ticketmaster.data.model.relation.SviluppoBug;
import it.unibo.ticketmaster.data.repository.ClienteRepository;
import it.unibo.ticketmaster.data.repository.FaseRepository;
import it.unibo.ticketmaster.data.repository.OperatoreRepository;
import it.unibo.ticketmaster.data.repository.SviluppatoreRepository;
import it.unibo.ticketmaster.data.repository.UtenteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UtenteService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UtenteRepository utenteRepository;
    private final ClienteRepository clienteRepository;
    private final OperatoreRepository operatoreRepository;
    private final SviluppatoreRepository sviluppatoreRepository;
    private final FaseRepository faseRepository;

    public UtenteService(UtenteRepository utenteRepository, ClienteRepository clienteRepository, OperatoreRepository operatoreRepository, SviluppatoreRepository sviluppatoreRepository, FaseRepository faseRepository) {
        this.utenteRepository = utenteRepository;
        this.clienteRepository = clienteRepository;
        this.operatoreRepository = operatoreRepository;
        this.sviluppatoreRepository = sviluppatoreRepository;
        this.faseRepository = faseRepository;
    }

    public boolean authenticate(String email, String password) {
        return this.utenteRepository.auth(email, password);
    }

    public String getUserRole(String email) {
        Optional<Utente> utente = this.utenteRepository.findById(email);
        if (utente.isPresent()) {
            if (utente.get() instanceof Amministratore) {
                return "ADMIN";
            } else if (utente.get() instanceof Operatore) {
                return "OPERATOR";
            } else if (utente.get() instanceof Sviluppatore) {
                return "PROGRAMMER";
            }
            return "USER";
        }
        return "";
    }

    public Utente findUser(String email) {
        return this.utenteRepository.findById(email).orElseThrow(NotFoundException::new);
    }

    public List<Operatore> findAllOperatori(String filterText) {
        return this.operatoreRepository.search(filterText);
    }

    public List<Sviluppatore> findAllSviluppatori(String filterText) {
        return this.sviluppatoreRepository.search(filterText);
    }

    @Transactional
    public void deleteClienteByEmail(String email) {
        Cliente cliente = this.entityManager.find(Cliente.class, email);
        if (cliente != null) {
            List<Ticket> tickets = this.clienteRepository.getClienteTickets(email);
            tickets.forEach(t -> {
                t.setCliente(null);
                this.entityManager.merge(t);
            });
            List<Messaggio> messagges = this.utenteRepository.getUtenteMessagges(email);
            messagges.forEach(m -> {
                m.setUtente(null);
                this.entityManager.merge(m);
            });
            this.entityManager.remove(cliente);
        }
    }

    @Transactional
    public void deleteOperatoreByEmail(String email) {
        Operatore operatore = this.entityManager.find(Operatore.class, email);
        if (operatore != null) {
            List<Ticket> tickets = this.operatoreRepository.getOperatoreTickets(email);
            tickets.forEach(t -> {
                t.setOperatore(null);
                this.entityManager.merge(t);
            });
            List<Messaggio> messagges = this.utenteRepository.getUtenteMessagges(email);
            messagges.forEach(m -> {
                m.setUtente(null);
                this.entityManager.merge(m);
            });
            this.entityManager.remove(operatore);
            tickets.forEach(t -> {
                StatoTicket stato = t.getStato();
                if(stato.getNome().equals("aperto") || stato.getNome().equals("analisi") ){
                    t.setOperatore(this.operatoreRepository.findOperatoreWithLessTickets());
                }
            });
        }
    }

    @Transactional
    public void deleteSviluppatoreByEmail(String email) {
        Sviluppatore sviluppatore = this.entityManager.find(Sviluppatore.class, email);
        if (sviluppatore != null) {
            List<SviluppoBug> sviluppoBugs = this.sviluppatoreRepository.getSviluppatoreSviluppo(email);
            sviluppoBugs.forEach(sb -> {
                sb.setSviluppatore(null);
                this.entityManager.merge(sb);
            });
            this.entityManager.remove(sviluppatore);
        }
    }

}
