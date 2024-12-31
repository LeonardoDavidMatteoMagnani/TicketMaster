package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Cliente;
import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("New Ticket | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "new_ticket", layout = UserLayout.class)
public class NewTicketView extends VerticalLayout {

    private final ComboBox<PrioritaTicket> prioritaComboBox = new ComboBox<>("Urgenza");
    private final ComboBox<ProblemaTicket> problemaComboBox = new ComboBox<>("Problema");
    private final ComboBox<SistemaOperativo> sistemaOperativoComboBox = new ComboBox<>("SO");
    private final Button submitButton = new Button("Crea");
    private final TicketUtenteService service;
    private final Cliente cliente;

    public NewTicketView(TicketUtenteService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "USER") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        this.cliente = (Cliente) this.service.findUser(VaadinSession.getCurrent().getAttribute("email").toString());
        VerticalLayout container = new VerticalLayout();
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.STRETCH);
        container.setWidth("60%");
        container.setHeightFull();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        H1 creaTicketTitolo = new H1("Nuovo ticket");
        creaTicketTitolo.getElement().getStyle().set("text-align", "center");
        TextField titoloField = new TextField("Titolo");
        titoloField.setRequired(true);
        int charLimit = 500;
        TextArea descrizioneField = new TextArea("Descrizione");
        descrizioneField.setRequired(true);
        descrizioneField.setWidthFull();
        descrizioneField.setHeight("200px");
        descrizioneField.setMaxLength(charLimit);
        descrizioneField.setValueChangeMode(ValueChangeMode.EAGER);
        descrizioneField.setHelperText("0/" + charLimit);
        descrizioneField.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });
        this.prioritaComboBox.setItems(service.findAllPrioritaTicket());
        this.prioritaComboBox.setItemLabelGenerator(PrioritaTicket::getNome);
        this.prioritaComboBox.setMinWidth("50px");
        this.prioritaComboBox.setWidthFull();
        this.prioritaComboBox.setRequired(true);
        this.problemaComboBox.setItems(service.findAllProblemi());
        this.problemaComboBox.setItemLabelGenerator(ProblemaTicket::getNome);
        this.problemaComboBox.setMinWidth("50px");
        this.problemaComboBox.setWidthFull();
        this.problemaComboBox.setRequired(true);
        this.sistemaOperativoComboBox
                .setItems(service.findAllSistemiOperativi());
        this.sistemaOperativoComboBox.setItemLabelGenerator(SistemaOperativo::getNome);
        this.sistemaOperativoComboBox.setMinWidth("50px");
        this.sistemaOperativoComboBox.setWidthFull();
        this.sistemaOperativoComboBox.setRequired(true);
        this.submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.submitButton.getElement().getStyle().set("cursor", "pointer");
        this.submitButton.addClickListener(event -> {
            String titolo = titoloField.getValue();
            String descrizione = descrizioneField.getValue();
            PrioritaTicket priorita = this.prioritaComboBox.getValue();
            ProblemaTicket problema = this.problemaComboBox.getValue();
            SistemaOperativo sistemaOperativo = this.sistemaOperativoComboBox.getValue();
            if (!titolo.isEmpty() && !descrizione.isEmpty() && priorita != null
                    && problema != null && sistemaOperativo != null) {
                this.service.createTicket(titolo, descrizione, priorita, problema, sistemaOperativo,
                        this.cliente);
                UI.getCurrent().navigate("user_tickets");
                Notification.show("Ticket Creato con successo!");
            } else {
                Notification.show("Errore: Compila tutti i campi!");
            }
        });
        HorizontalLayout info = new HorizontalLayout();
        info.setAlignItems(Alignment.STRETCH);
        info.setJustifyContentMode(JustifyContentMode.CENTER);
        info.add(this.prioritaComboBox, this.problemaComboBox, this.sistemaOperativoComboBox);
        container.add(creaTicketTitolo, titoloField, descrizioneField, info, this.submitButton);
        add(container);
    }

}
