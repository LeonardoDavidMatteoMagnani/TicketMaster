package it.unibo.ticketmaster.views;

import java.sql.Timestamp;
import java.time.Instant;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.Messaggio;
import it.unibo.ticketmaster.data.model.entity.MessaggioId;
import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.Tutorial;
import it.unibo.ticketmaster.data.model.entity.Utente;
import it.unibo.ticketmaster.data.model.relation.Soluzione;
import it.unibo.ticketmaster.data.service.TicketService;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Ticket Details | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "ticket_details", layout = UserLayout.class)
public class TicketDetailsView extends HorizontalLayout implements HasUrlParameter<String> {

    private final TicketUtenteService service;
    private final TicketService ticketService;
    private long numeroTicket;
    private Ticket ticket;
    private final TextField titoloField = new TextField("Titolo");
    private final TextArea descrizioneField = new TextArea("Descrizione");
    private final H3 utenteField = new H3();
    private final TextField urgenzaField = new TextField("Urgenza");
    private final TextField problemaField = new TextField("Problema");
    private final TextField sistemaOperativoField = new TextField("Sistema Operativo");
    private final TextField statoField = new TextField("Stato");
    private final TextArea messageField = new TextArea("Message");
    private final TextArea chatArea = new TextArea("Chat");
    private final Button sendButton = new Button("Send");
    private final ComboBox<StatoTicket> statoComboBox = new ComboBox<>("Cambia Stato");
    private final Button closeButton = new Button("Chiudi Ticket");
    private final Button bugButton = new Button("Segnala Bug");
    private final Button valutateButton = new Button("Valuta Operatore");
    private final MultiSelectComboBox<Tutorial> tutorialComboBox = new MultiSelectComboBox<>("Tutorial");

    public TicketDetailsView(TicketUtenteService service, TicketService ticketService) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") == null) {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        this.ticketService = ticketService;
        VerticalLayout container = new VerticalLayout();
        container.setJustifyContentMode(JustifyContentMode.START);
        container.setAlignItems(Alignment.STRETCH);
        container.setWidth("60%");
        container.setHeightFull();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        this.utenteField.getStyle().set("font-weight", "bold");
        this.utenteField.getStyle().set("color", "var(--lumo-secondary-text-color)");
        this.utenteField.getStyle().set("text-align", "center");
        this.titoloField.setReadOnly(true);
        int charLimit = 500;
        this.descrizioneField.setReadOnly(true);
        this.descrizioneField.setWidthFull();
        this.descrizioneField.setHeight("200px");
        this.descrizioneField.setMaxHeight("200px");
        this.descrizioneField.setMaxLength(charLimit);
        this.descrizioneField.setValueChangeMode(ValueChangeMode.EAGER);
        this.descrizioneField.setHelperText("0/" + charLimit);
        this.descrizioneField.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });
        HorizontalLayout info = new HorizontalLayout();
        info.setAlignItems(Alignment.STRETCH);
        info.setJustifyContentMode(JustifyContentMode.CENTER);
        this.urgenzaField.setMinWidth("50px");
        this.urgenzaField.setWidthFull();
        this.urgenzaField.setReadOnly(true);
        this.problemaField.setMinWidth("50px");
        this.problemaField.setWidthFull();
        this.problemaField.setReadOnly(true);
        this.sistemaOperativoField.setMinWidth("50px");
        this.sistemaOperativoField.setWidthFull();
        this.sistemaOperativoField.setReadOnly(true);
        this.statoField.setMinWidth("50px");
        this.statoField.setWidthFull();
        this.statoField.setReadOnly(true);
        VerticalLayout chatContainer = new VerticalLayout();
        chatContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        chatContainer.setAlignItems(Alignment.STRETCH);
        chatContainer.setHeightFull();
        chatContainer.setWidth("25%");
        this.chatArea.setReadOnly(true);
        this.chatArea.setHeight("67%");
        this.chatArea.setMaxHeight("67%");
        this.sendButton.setMinWidth("50px");
        this.sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        this.sendButton.getElement().getStyle().set("cursor", "pointer");
        this.sendButton.addClickListener(event -> sendMessage());
        info.add(this.urgenzaField, this.problemaField, this.sistemaOperativoField, this.statoField);
        HorizontalLayout operatorContainer = new HorizontalLayout();
        operatorContainer.setJustifyContentMode(JustifyContentMode.START);
        operatorContainer.setAlignItems(Alignment.END);
        this.closeButton.setMinWidth("50px");
        this.closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.closeButton.getElement().getStyle().set("cursor", "pointer");
        this.closeButton.setEnabled(false);
        this.closeButton.addClickListener(event -> {
            this.service.changeTicketFase(this.ticket, this.statoComboBox.getValue());
            if(this.tutorialComboBox.getValue() != null) {
                this.ticketService.createTicketSolution(this.ticket, this.tutorialComboBox.getValue().stream().toList());
            }
            UI.getCurrent().getPage().reload();
        });
        this.statoComboBox.setMinWidth("50px");
        this.statoComboBox.setItemLabelGenerator(StatoTicket::getNome);
        this.statoComboBox.addValueChangeListener(event -> {
            this.closeButton.setEnabled(event.getValue() != null);
            this.tutorialComboBox.setVisible(event.getValue().getNome().equals("chiuso - risolto"));
        });
        this.tutorialComboBox.setVisible(false);
        this.tutorialComboBox.setMinWidth("50px");
        this.tutorialComboBox.setItemLabelGenerator(Tutorial::getNome);
        this.bugButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        this.bugButton.addClickListener(event -> {
            UI.getCurrent().navigate("bug_report/" + String.valueOf(this.ticket.getNumero()));
        });
        this.valutateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        this.valutateButton.setVisible(false);
        operatorContainer.add(this.statoComboBox, this.closeButton, this.bugButton, this.tutorialComboBox, this.valutateButton);
        container.add(this.utenteField, this.titoloField, this.descrizioneField, info, operatorContainer);
        chatContainer.add(this.chatArea, this.messageField, this.sendButton);
        add(container, chatContainer);
    }

    private void sendMessage() {
        String message = this.messageField.getValue();
        if (message.isEmpty()) {
            return;
        }
        // Perform logic to send the message
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("email") != null) {
            String email = VaadinSession.getCurrent().getAttribute("email").toString();
            Messaggio messaggio = (new Messaggio(
                    new MessaggioId(ticket, Timestamp.from(Instant.now())), this.service.findUser(email), message));
            this.service.createMessaggio(messaggio);
        }
        this.messageField.clear();
        // Append the sent message to the chatArea
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("nome") != null) {
            this.chatArea.setValue(
                    chatArea.getValue() + this.formatMessage(VaadinSession.getCurrent().getAttribute("nome").toString()
                            + VaadinSession.getCurrent().getAttribute("cognome").toString(), message) + "\n");
        } else {
            this.chatArea.setValue(chatArea.getValue() + this.formatMessage("Anonimo", message) + "\n");
        }
        // Scroll to the bottom of the chatArea
    }

    private String formatMessage(String user, String message) {
        return String.format("\n%s: \n%s", user, message);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.numeroTicket = Integer.parseInt(parameter);
        this.ticket = this.service.findTicket(this.numeroTicket);
        Cliente clienteTicket = this.ticket.getCliente();
        Operatore operatoreTicket = this.ticket.getOperatore();
        this.service.findMessaggiByTicket(ticket).forEach(messaggio -> {
            if (messaggio.get("nome") != null) {
                this.chatArea.setValue(this.chatArea.getValue()
                        + this.formatMessage(messaggio.get("nome").toString() + messaggio.get("cognome").toString(), messaggio.get("testo").toString()) + "\n");
            } else {
                this.chatArea.setValue(this.chatArea.getValue()
                        + this.formatMessage("Anonimo", messaggio.get("testo").toString()) + "\n");
            }
        });
        this.ticketService.findSoluzioniTicket(ticket).forEach(soluzioneTicket -> {
            this.chatArea.setValue(this.chatArea.getValue()
                    + "\n" + soluzioneTicket.getId().getTutorial().getNome() + "\n" + soluzioneTicket.getId().getTutorial().getUrl() + "\n");
        });
        this.titoloField.setValue(this.ticket.getTitolo());
        this.descrizioneField.setValue(this.ticket.getDescrizione());
        if (clienteTicket != null) {
            this.utenteField.setText(this.ticket.getCliente().getNome() + " " + this.ticket.getCliente().getCognome());
        } else {
            this.utenteField.setText("Anonimo");
        }
        this.urgenzaField.setValue(this.ticket.getPriorita().getNome());
        this.problemaField.setValue(this.ticket.getProblema().getNome());
        this.sistemaOperativoField.setValue(this.ticket.getSistemaOperativo().getNome());
        String stato = ticket.getStato().getNome();
        this.statoField.setValue(stato);
        this.statoComboBox
                .setItems(this.service.findAllStatiTicketLiberi(this.ticket));
        this.tutorialComboBox.setItems(this.ticketService.findAllTutorials());
        if (isTicketClosed(stato)) {
            this.cantOperate();
            this.cantChat();
        }
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("email") != null) {
            String email = VaadinSession.getCurrent().getAttribute("email").toString();
            String emailCliente = "";
            String emailOperatore = "";
            if (clienteTicket != null){
                emailCliente = clienteTicket.getEmail();
            }
            if (operatoreTicket != null){
                emailOperatore = operatoreTicket.getEmail();
            }
            if (!email.equals(emailCliente) && !email.equals(emailOperatore)) {
                this.cantChat();
            }
            if (!email.equals(emailOperatore)) {
                this.cantOperate();
            }
            if (email.equals(emailCliente) && isTicketClosed(stato)) {
                if (this.service.isTicketValutato(this.ticket)) {
                    this.valutateButton.setText("Valutato");
                    this.valutateButton.setEnabled(false);
                }
                this.valutateButton.setVisible(true);
                this.valutateButton.addClickListener(event1 -> {
                    UI.getCurrent().navigate("valutazione/" + String.valueOf(this.ticket.getNumero()));
                });
            }
        }
    }

    private boolean isTicketClosed(String stato) {
        return stato.equals("chiuso - risolto") || stato.equals("chiuso - non risolvibile");
    }

    private void cantChat() {
        this.chatArea.setHeight("100%");
        this.chatArea.setMaxHeight("100%");
        this.messageField.setVisible(false);
        this.sendButton.setVisible(false);
    }

    private void cantOperate() {
        this.statoComboBox.setVisible(false);
        this.closeButton.setVisible(false);
        this.bugButton.setVisible(false);
    }

}
