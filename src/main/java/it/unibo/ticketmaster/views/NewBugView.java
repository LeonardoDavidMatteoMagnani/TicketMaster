package it.unibo.ticketmaster.views;

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

import it.unibo.ticketmaster.data.model.entity.PrioritaBug;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Bug Report | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "bug_report", layout = UserLayout.class)
public class NewBugView extends VerticalLayout implements HasUrlParameter<String> {

    private long numeroTicket;
    private Ticket ticket;
    private final ComboBox<PrioritaBug> prioritaComboBox = new ComboBox<>("Priorita");
    private final TextArea descrizioneField = new TextArea("Descrizione");
    private final Button submitButton = new Button("Reporta Bug");
    private final TicketUtenteService service;

    public NewBugView(TicketUtenteService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "OPERATOR") {
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
        this.prioritaComboBox.setItems(this.service.findAllPrioritaBug());
        this.prioritaComboBox.setItemLabelGenerator(PrioritaBug::getNome);
        this.prioritaComboBox.setRequired(true);
        int charLimit = 500;
        this.descrizioneField.setRequired(true);
        this.descrizioneField.setWidthFull();
        this.descrizioneField.setHeight("200px");
        this.descrizioneField.setMaxLength(charLimit);
        this.descrizioneField.setValueChangeMode(ValueChangeMode.EAGER);
        this.descrizioneField.setHelperText("0/" + charLimit);
        this.descrizioneField.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });
        this.submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        this.submitButton.addClickListener(event -> {
            PrioritaBug priorita = this.prioritaComboBox.getValue();
            String descrizione = this.descrizioneField.getValue();
            if (priorita != null && descrizione != null && !descrizione.isEmpty()) {
                this.service.createBug(this.ticket, descrizione, priorita);
                UI.getCurrent().navigate("ticket_details/" + String.valueOf(this.ticket.getNumero()));
                Notification.show("Bug Reportato con successo!");
            } else {
                Notification.show("Errore: Compila tutti i campi!");
            }
        });

        container.add(this.prioritaComboBox, this.descrizioneField, this.submitButton);
        add(container);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.numeroTicket = Integer.parseInt(parameter);
        this.ticket = this.service.findTicket(this.numeroTicket);
    }

}
