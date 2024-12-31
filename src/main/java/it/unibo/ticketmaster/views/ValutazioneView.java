package it.unibo.ticketmaster.views;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatore;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatoreId;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Operatore Assesment | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "valutazione", layout = UserLayout.class)
public class ValutazioneView extends VerticalLayout implements HasUrlParameter<String> {

    private long numeroTicket;
    private Ticket ticket;
    private final ComboBox<Integer> votoComboBox = new ComboBox<>("Voto");
    private final TextArea descrizioneField = new TextArea("Descrizione");
    private final Button submitButton = new Button("Valuta");
    private final TicketUtenteService service;

    public ValutazioneView(TicketUtenteService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "USER") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        VerticalLayout container = new VerticalLayout();
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.STRETCH);
        container.setWidth("60%");
        container.setHeightFull();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        this.votoComboBox.setItems(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));
        this.votoComboBox.setRequired(true);
        int charLimit = 500;
        this.descrizioneField.setWidthFull();
        this.descrizioneField.setHeight("200px");
        this.descrizioneField.setMaxLength(charLimit);
        this.descrizioneField.setValueChangeMode(ValueChangeMode.EAGER);
        this.descrizioneField.setHelperText("0/" + charLimit);
        this.descrizioneField.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });
        this.submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        this.submitButton.addClickListener(event -> {
            Integer voto = this.votoComboBox.getValue();
            String descrizione = this.descrizioneField.getValue();
            if (voto != null) {
                ValutazioneOperatore valutazione = new ValutazioneOperatore(new ValutazioneOperatoreId(this.ticket),
                        voto, descrizione);
                this.service.createValutazioneOperatore(valutazione);
                UI.getCurrent().navigate("ticket_details/" + String.valueOf(this.ticket.getNumero()));
                Notification.show("Operatore Valutato con successo!");
            } else {
                Notification.show("Inserisci un voto!");
            }
        });

        container.add(this.votoComboBox, this.descrizioneField, this.submitButton);
        add(container);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.numeroTicket = Integer.parseInt(parameter);
        this.ticket = this.service.findTicket(this.numeroTicket);
    }

}
