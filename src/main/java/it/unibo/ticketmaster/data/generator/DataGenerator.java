package it.unibo.ticketmaster.data.generator;

import java.sql.Timestamp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.spring.annotation.SpringComponent;

import it.unibo.ticketmaster.data.model.entity.Amministratore;
import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.MessaggioId;
import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.PrioritaBug;
import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoBug;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Tutorial;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatore;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatoreId;
import it.unibo.ticketmaster.data.repository.AmministratoreRepository;
import it.unibo.ticketmaster.data.repository.ClienteRepository;
import it.unibo.ticketmaster.data.repository.OperatoreRepository;
import it.unibo.ticketmaster.data.repository.PrioritaBugRepository;
import it.unibo.ticketmaster.data.repository.PrioritaTicketRepository;
import it.unibo.ticketmaster.data.repository.ProblemaTicketRepository;
import it.unibo.ticketmaster.data.repository.SistemaOperativoRepository;
import it.unibo.ticketmaster.data.repository.StatoBugRepository;
import it.unibo.ticketmaster.data.repository.StatoTicketRepository;
import it.unibo.ticketmaster.data.repository.SviluppatoreRepository;
import it.unibo.ticketmaster.data.repository.TicketRepository;
import it.unibo.ticketmaster.data.repository.TutorialRepository;
import it.unibo.ticketmaster.data.service.BugService;
import it.unibo.ticketmaster.data.service.TicketUtenteService;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(StatoTicketRepository statoTicketRepository,
            PrioritaTicketRepository prioritaTicketRepository, ProblemaTicketRepository problemaTicketRepository,
            SistemaOperativoRepository sistemaOperativoRepository, ClienteRepository clienteRepository,
            OperatoreRepository operatoreRepository, SviluppatoreRepository sviluppatoreRepository, AmministratoreRepository amministratoreRepository,
            TicketRepository ticketRepository, TicketUtenteService ticketUtenteService, BugService bugService,
            PrioritaBugRepository prioritaBugRepository, StatoBugRepository statoBugRepository, TutorialRepository tutorialRepository) {
        return args -> {

            tutorialRepository.save(new Tutorial("Come installare app", "https://www.youtube.com/watch?v=1y0-7HbKkX8"));
            tutorialRepository.save(new Tutorial("Come utilizzare app", "https://www.youtube.com/watch?v=1y0-8UJBD678"));

            // Insert data into STATO_TICKET table
            statoTicketRepository.save(new StatoTicket("aperto"));
            statoTicketRepository.save(new StatoTicket("analisi"));
            statoTicketRepository.save(new StatoTicket("chiuso - risolto"));
            statoTicketRepository.save(new StatoTicket("chiuso - non risolvibile"));

            // Insert data into PRIORITA_TICKET table
            prioritaTicketRepository.save(new PrioritaTicket("bassa"));
            prioritaTicketRepository.save(new PrioritaTicket("media"));
            prioritaTicketRepository.save(new PrioritaTicket("alta"));

            // Insert data into PROBLEMA table
            problemaTicketRepository.save(new ProblemaTicket("software"));
            problemaTicketRepository.save(new ProblemaTicket("rete"));
            problemaTicketRepository.save(new ProblemaTicket("installazione"));
            problemaTicketRepository.save(new ProblemaTicket("aggiornamento"));
            problemaTicketRepository.save(new ProblemaTicket("sicurezza"));
            problemaTicketRepository.save(new ProblemaTicket("performance"));

            // Insert data into SISTEMA_OPERATIVO table
            sistemaOperativoRepository.save(new SistemaOperativo("windows"));
            sistemaOperativoRepository.save(new SistemaOperativo("mac"));
            sistemaOperativoRepository.save(new SistemaOperativo("linux"));

            // Insert data into STATO_TICKET table
            statoBugRepository.save(new StatoBug("non in correzione"));
            statoBugRepository.save(new StatoBug("in revisione"));
            statoBugRepository.save(new StatoBug("chiuso - risolto"));
            statoBugRepository.save(new StatoBug("chiuso - non riproducibile"));

            // Insert data into PRIORITA_BUG table
            prioritaBugRepository.save(new PrioritaBug("bassa"));
            prioritaBugRepository.save(new PrioritaBug("media"));
            prioritaBugRepository.save(new PrioritaBug("alta"));

            // Inserimento dati cliente.
            Cliente cliente1 = new Cliente("c1@gmail.com", "Marco", "Magnani", "p");
            clienteRepository.save(cliente1);
            Cliente cliente2 = new Cliente("c2@gmail.com", "Marcus", "Magnanus", "p");
            clienteRepository.save(cliente2);
            Cliente cliente3 = new Cliente("c3@gmail.com", "Noid", "Gudman", "p");
            clienteRepository.save(cliente3);
            Cliente cliente4 = new Cliente("c4@gmail.com", "Filip", "Shackle", "p");
            clienteRepository.save(cliente4);
            Cliente cliente5 = new Cliente("c5@gmail.com", "Popo", "Pipo", "p");
            clienteRepository.save(cliente5);

            Operatore operatore1 = new Operatore("p1@gmail.com", "Giulio", "Cesare", "p");
            operatoreRepository.save(operatore1);
            Operatore operatore2 = new Operatore("p2@gmail.com", "Gizmo", "Gizmato", "p");
            operatoreRepository.save(operatore2);
            Operatore operatore3 = new Operatore("p3@gmail.com", "Lizmo", "Lizmato", "p");
            operatoreRepository.save(operatore3);

            Sviluppatore sviluppatore1 = new Sviluppatore("s@gmail.com", "Ner", "Weby", "p");
            sviluppatoreRepository.save(sviluppatore1);

            Amministratore amministratore = new Amministratore("a@gmail.com", "Capo", "Capi", "p");
            amministratoreRepository.save(amministratore);

            // Inserimento dati ticket e messaggi.
            Ticket ticket1 = ticketUtenteService.createTestTicket("Il software si blocca",
                    "Quando lo apro dopo un paio di secondi si blocca.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente1,
                    Timestamp.valueOf("2023-05-23 10:10:10.0"));
            ticketUtenteService.changeTestTicketFase(ticket1, "analisi", Timestamp.valueOf("2023-05-24 8:20:10.0"));
            ticketUtenteService.createMessaggio(
                    new Messaggio(new MessaggioId(ticket1, Timestamp.valueOf("2023-05-24 8:30:00.0")),
                            ticket1.getOperatore(), "Problema noto, capita dopo un update di windows, prova disinstallare e riscaricarlo."));
            ticketUtenteService.createMessaggio(
                    new Messaggio(new MessaggioId(ticket1, Timestamp.valueOf("2023-05-24 10:00:00.0")),
                            ticket1.getCliente(), "Si grazie ha funzionato!"));
            ticketUtenteService.changeTestTicketFase(ticket1, "chiuso - risolto",
                    Timestamp.valueOf("2023-05-24 10:30:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket1), 10, "Stato gentile e bravo"));

            Ticket ticket2 = ticketUtenteService.createTestTicket("Non mi si apre il software",
                    "Provo ad aprirlo ma non si apre.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("mac").get(), cliente2,
                    Timestamp.valueOf("2023-07-05 17:00:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket2, "analisi", Timestamp.valueOf("2023-07-07 8:00:00.0"));
            Bug bug1 = bugService.createTestBug(ticket2, "Il software non si apre.", prioritaBugRepository.findById("alta").get(), Timestamp.valueOf("2023-07-07 9:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket2, Timestamp.valueOf("2023-07-07 10:00:00.0")),
                    ticket2.getOperatore(), "Non abbiamo informazioni riguardo questo problema l'ho reportato agli sviluppatori. Perfavore potresti darci più informazioni al riguardo?"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket2, Timestamp.valueOf("2023-07-07 15:00:00.0")),
                    ticket2.getCliente(), "Si allora praticamente quando provo ad aprirlo mi da errore. Ho provato a reinstallarlo ma non funziona. Da messaggio di errore 'Impossibile aprire il file'"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket2, Timestamp.valueOf("2023-07-07 15:30:00.0")),
                    ticket2.getOperatore(), "Ok i nostri svilluppatori hanno trovato la causa adesso ci lavoreranno per risolvere il problema. Ti aggiorneremo appena possibile."));
            bugService.changeTestSviluppoBug(bug1, "in revisione", sviluppatore1, Timestamp.valueOf("2023-07-08 8:00:00.0"));
            bugService.changeTestSviluppoBug(bug1, "chiuso - risolto", sviluppatore1, Timestamp.valueOf("2023-07-10 8:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket2, Timestamp.valueOf("2023-07-10 8:30:00.0")),
                    ticket2.getOperatore(), "Il bug è stato risolto, puoi scaricare la nuova versione del software."));
            ticketUtenteService.changeTestTicketFase(ticket2, "chiuso - risolto", Timestamp.valueOf("2023-07-10 8:35:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket2), 6, "Ci ha messo un po' di tempo a sistemarlo."));

            Ticket ticket3 = ticketUtenteService.createTestTicket("Non riesco ad scaricare il software",
                    "Quando provo a scaricare is software mi da errore.",
                    prioritaTicketRepository.findById("media").get(),
                    problemaTicketRepository.findById("installazione").get(),
                    sistemaOperativoRepository.findById("linux").get(), cliente3,
                    Timestamp.valueOf("2023-07-04 12:20:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket3, "analisi", Timestamp.valueOf("2023-07-04 15:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket3, Timestamp.valueOf("2023-07-04 15:30:00.0")),
                    ticket3.getOperatore(), "Prova a scaricare la versione per windows."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket3, Timestamp.valueOf("2023-07-04 16:00:00.0")),
                    ticket3.getCliente(), "Si grazie ha funzionato!"));
            ticketUtenteService.changeTestTicketFase(ticket3, "chiuso - risolto", Timestamp.valueOf("2023-07-04 16:30:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket3), 10, "Stato gentile e bravo"));

            Ticket ticket4 = ticketUtenteService.createTestTicket("Non si aggiorna",
                    "Quando provo ad aggiornare mi da errore.",
                    prioritaTicketRepository.findById("bassa").get(),
                    problemaTicketRepository.findById("aggiornamento").get(),
                    sistemaOperativoRepository.findById("linux").get(), cliente4,
                    Timestamp.valueOf("2023-06-13 20:00:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket4, "analisi", Timestamp.valueOf("2023-06-14 8:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket4, Timestamp.valueOf("2023-06-14 8:30:00.0")),
                    ticket4.getOperatore(), "Prova a scaricare la versione per windows."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket4, Timestamp.valueOf("2023-06-14 9:00:00.0")),
                    ticket4.getCliente(), "Non funziona neanche quella."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket4, Timestamp.valueOf("2023-06-14 9:30:00.0")),
                    ticket4.getOperatore(), "Ok allora è un problema del nostro server, ci stiamo lavorando."));
            ticketUtenteService.changeTestTicketFase(ticket4, "chiuso - non risolvibile", Timestamp.valueOf("2023-06-14 10:00:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket4), 1, "Non mi ha aiutato per niente."));

            Ticket ticket5 = ticketUtenteService.createTestTicket("Software non si connette alla rete",
                    "Quando uso il software non si connette alla rete. Non riesco a fare nulla.",
                    prioritaTicketRepository.findById("bassa").get(),
                    problemaTicketRepository.findById("rete").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente5,
                    Timestamp.valueOf("2023-03-23 23:00:00.0"));

            Ticket ticket6 = ticketUtenteService.createTestTicket("Problemi di prestazioni",
                    "Il software è molto lento nell'eseguire le operazioni.",
                    prioritaTicketRepository.findById("media").get(),
                    problemaTicketRepository.findById("performance").get(),
                    sistemaOperativoRepository.findById("mac").get(), cliente1,
                    Timestamp.valueOf("2023-07-12 14:30:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket6, "analisi", Timestamp.valueOf("2023-07-12 15:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket6, Timestamp.valueOf("2023-07-12 15:30:00.0")),
                    ticket6.getOperatore(), "Potrebbe essere dovuto a un'elevata quantità di dati da elaborare. Stiamo indagando."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket6, Timestamp.valueOf("2023-07-12 16:00:00.0")),
                    ticket6.getCliente(), "Capisco, attendo vostre notizie."));
            
            Ticket ticket7 = ticketUtenteService.createTestTicket("Errore durante l'installazione",
                    "Durante l'installazione del software, ricevo un messaggio di errore e l'installazione si interrompe.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("installazione").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente2,
                    Timestamp.valueOf("2023-07-10 11:45:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket7, "analisi", Timestamp.valueOf("2023-07-10 12:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket7, Timestamp.valueOf("2023-07-10 12:30:00.0")),
                    ticket7.getOperatore(), "Potrebbe esserci un problema durante il download. Abbiamo bisogno di ulteriori informazioni."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket7, Timestamp.valueOf("2023-07-10 13:00:00.0")),
                    ticket7.getCliente(), "Ho provato a scaricare da diversi siti, ma il problema persiste."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket7, Timestamp.valueOf("2023-07-10 13:30:00.0")),
                    ticket7.getOperatore(), "Grazie per le informazioni. Stiamo indagando ulteriormente sul problema."));
            ticketUtenteService.changeTestTicketFase(ticket7, "chiuso - non risolvibile", Timestamp.valueOf("2023-07-14 09:00:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket7), 5, "Gentile ma non utile."));
            
            Ticket ticket8 = ticketUtenteService.createTestTicket("Crash improvviso",
                    "Il software si chiude improvvisamente senza alcun messaggio di errore.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("linux").get(), cliente3,
                    Timestamp.valueOf("2023-07-09 16:20:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket8, "analisi", Timestamp.valueOf("2023-07-09 17:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket8, Timestamp.valueOf("2023-07-09 17:30:00.0")),
                    ticket8.getOperatore(), "Stiamo effettuando dei test per identificare la causa del crash."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket8, Timestamp.valueOf("2023-07-09 18:00:00.0")),
                    ticket8.getCliente(), "Grazie per l'aggiornamento. Spero che riusciate a risolvere il problema."));
            ticketUtenteService.changeTestTicketFase(ticket8, "chiuso - non risolvibile", Timestamp.valueOf("2023-07-14 10:00:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket8), 6, "Ci ha provato."));
            
            Ticket ticket9 = ticketUtenteService.createTestTicket("Interfaccia non reattiva",
                    "L'interfaccia del software risulta lenta e poco reattiva ai comandi dell'utente.",
                    prioritaTicketRepository.findById("media").get(),
                    problemaTicketRepository.findById("performance").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente4,
                    Timestamp.valueOf("2023-07-08 10:10:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket9, "analisi", Timestamp.valueOf("2023-07-08 11:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket9, Timestamp.valueOf("2023-07-08 11:30:00.0")),
                    ticket9.getOperatore(), "Potrebbe essere un problema di risorse del sistema. Stiamo effettuando dei test."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket9, Timestamp.valueOf("2023-07-08 12:00:00.0")),
                    ticket9.getCliente(), "Ho notato che il problema si verifica soprattutto quando ci sono molti dati da elaborare."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket9, Timestamp.valueOf("2023-07-08 12:30:00.0")),
                    ticket9.getOperatore(), "Grazie per il feedback. Stiamo lavorando per migliorare le prestazioni del software."));
            ticketUtenteService.changeTestTicketFase(ticket9, "chiuso - non risolvibile", Timestamp.valueOf("2023-07-09 08:30:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket9), 8, "Gentile e disponibile."));
                    
            Ticket ticket10 = ticketUtenteService.createTestTicket("Problemi di connessione",
                    "Il software non riesce a connettersi al server per sincronizzare i dati.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("rete").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente5,
                    Timestamp.valueOf("2023-07-07 09:00:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket10, "analisi", Timestamp.valueOf("2023-07-07 10:30:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket10, Timestamp.valueOf("2023-07-07 11:00:00.0")),
                    ticket10.getOperatore(), "Potrebbe essere un problema di configurazione delle impostazioni di rete. Stiamo indagando."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket10, Timestamp.valueOf("2023-07-07 11:30:00.0")),
                    ticket10.getCliente(), "Ho notato che il problema si verifica solo con il vostro software, gli altri programmi funzionano correttamente."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId( ticket10, Timestamp.valueOf("2023-07-07 12:00:00.0")),
                    ticket10.getOperatore(), "Grazie per le informazioni. Stiamo cercando una soluzione al problema di connessione."));
            
            Ticket ticket11 = ticketUtenteService.createTestTicket("Errore di autenticazione",
                    "Quando inserisco le credenziali per accedere al software, ricevo un errore di autenticazione.",
                    prioritaTicketRepository.findById("media").get(),
                    problemaTicketRepository.findById("sicurezza").get(),
                    sistemaOperativoRepository.findById("mac").get(), cliente1,
                    Timestamp.valueOf("2023-07-06 15:45:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket11, "analisi", Timestamp.valueOf("2023-07-06 16:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket11, Timestamp.valueOf("2023-07-06 16:30:00.0")),
                    ticket11.getOperatore(), "Potrebbe essere un problema di autenticazione. Abbiamo bisogno di ulteriori dettagli."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket11, Timestamp.valueOf("2023-07-06 17:00:00.0")),
                    ticket11.getCliente(), "Sicuro, ricevo l'errore 'Credenziali non valide' anche se inserisco i dati corretti."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket11, Timestamp.valueOf("2023-07-06 17:30:00.0")),
                    ticket11.getOperatore(), "Grazie per la conferma. Abbiamo risolto problema di autenticazione."));
            ticketUtenteService.changeTestTicketFase(ticket11, "chiuso - risolto", Timestamp.valueOf("2023-07-07 11:30:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket11), 10, "Gentile e utile."));
            
            Ticket ticket12 = ticketUtenteService.createTestTicket("Problema di compatibilità",
                    "Il software non funziona correttamente con la versione più recente del sistema operativo.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("mac").get(), cliente2,
                    Timestamp.valueOf("2023-07-05 13:30:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket12, "analisi", Timestamp.valueOf("2023-07-05 14:00:00.0"));
             ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket12, Timestamp.valueOf("2023-07-05 14:30:00.0")),
                    ticket12.getOperatore(), "Potrebbe essere un problema di incompatibilità con la versione del sistema operativo. Stiamo cercando una soluzione."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket12, Timestamp.valueOf("2023-07-05 15:00:00.0")),
                    ticket12.getCliente(), "Ho notato che il problema si è verificato dopo l'ultimo aggiornamento del sistema operativo."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket12, Timestamp.valueOf("2023-07-05 15:30:00.0")),
                    ticket12.getOperatore(), "Grazie per la segnalazione. Abbiamo risolto il problema di compatibilità."));
            ticketUtenteService.changeTestTicketFase(ticket12, "chiuso - risolto", Timestamp.valueOf("2023-07-06 10:00:00.0"));
            ticketUtenteService.createValutazioneOperatore(new ValutazioneOperatore(new ValutazioneOperatoreId(ticket12), 9, "Gentile e disponibile."));
            
            Ticket ticket13 = ticketUtenteService.createTestTicket("Crash durante l'aggiornamento",
                    "Durante il processo di aggiornamento del software, si verifica un crash improvviso.",
                    prioritaTicketRepository.findById("alta").get(),
                    problemaTicketRepository.findById("aggiornamento").get(),
                    sistemaOperativoRepository.findById("windows").get(), cliente3,
                    Timestamp.valueOf("2023-07-04 11:20:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket13, "analisi", Timestamp.valueOf("2023-07-04 12:30:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket13, Timestamp.valueOf("2023-07-04 13:00:00.0")),
                    ticket13.getOperatore(), "Potrebbe esserci un problema durante il processo di aggiornamento. Abbiamo bisogno di ulteriori dettagli."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket13, Timestamp.valueOf("2023-07-04 13:30:00.0")),
                    ticket13.getCliente(), "Ho notato che l'errore si verifica sempre quando il software cerca di scaricare gli aggiornamenti."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket13, Timestamp.valueOf("2023-07-04 14:00:00.0")),
                    ticket13.getOperatore(), "Grazie per il feedback. Stiamo investigando sul problema di aggiornamento."));
            
            Ticket ticket14 = ticketUtenteService.createTestTicket("Messaggi di errore frequenti",
                    "Il software mostra frequentemente messaggi di errore durante l'utilizzo.",
                    prioritaTicketRepository.findById("media").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("linux").get(), cliente4,
                    Timestamp.valueOf("2023-07-03 17:15:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket14, "analisi", Timestamp.valueOf("2023-07-03 18:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket14, Timestamp.valueOf("2023-07-03 18:30:00.0")),
                    ticket14.getOperatore(), "Potrebbe esserci un problema nel caricamento corretto delle risorse del software. Stiamo cercando una soluzione."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket14, Timestamp.valueOf("2023-07-03 19:00:00.0")),
                    ticket14.getCliente(), "Ho notato che i messaggi di errore si verificano spesso quando cerco di aprire determinate sezioni."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket14, Timestamp.valueOf("2023-07-03 19:30:00.0")),
                    ticket14.getOperatore(), "Grazie per il feedback. Stiamo lavorando per risolvere i problemi di visualizzazione."));
            
            Ticket ticket15 = ticketUtenteService.createTestTicket("Problemi di visualizzazione",
                    "Alcuni elementi dell'interfaccia grafica del software non vengono visualizzati correttamente.",
                    prioritaTicketRepository.findById("bassa").get(),
                    problemaTicketRepository.findById("software").get(),
                    sistemaOperativoRepository.findById("mac").get(), cliente5,
                    Timestamp.valueOf("2023-07-02 10:50:00.0"));
            ticketUtenteService.changeTestTicketFase(ticket15, "analisi", Timestamp.valueOf("2023-07-02 11:00:00.0"));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket15, Timestamp.valueOf("2023-07-02 11:30:00.0")),
                    ticket15.getOperatore(), "Potrebbe essere un problema di compatibilità con la configurazione del sistema. Stiamo effettuando dei test."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket15, Timestamp.valueOf("2023-07-02 12:00:00.0")),
                    ticket15.getCliente(), "Ho notato che alcuni elementi dell'interfaccia grafica sono fuori posizione o mancanti."));
            ticketUtenteService.createMessaggio(new Messaggio(
                    new MessaggioId(ticket15, Timestamp.valueOf("2023-07-02 12:30:00.0")),
                    ticket15.getOperatore(), "Grazie per la segnalazione. Stiamo lavorando per risolvere i problemi di visualizzazione."));

        };
    }

}
