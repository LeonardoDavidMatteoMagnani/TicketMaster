package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.StatoBug;
import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.service.BugService;
import it.unibo.ticketmaster.data.service.UtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Bug Details | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "bug_details", layout = UserLayout.class)
public class BugDetailsView extends HorizontalLayout implements HasUrlParameter<String> {

    private final BugService service;
    private final UtenteService utenteService;
    private Bug bug;
    private final TextArea descrizioneField = new TextArea("Descrizione");
    private final H3 ticketField = new H3();
    private final TextField urgenzaField = new TextField("Urgenza");
    private final TextField statoField = new TextField("Stato");
    private final ComboBox<StatoBug> statoComboBox = new ComboBox<>("Cambia Stato");
    private final Button closeButton = new Button("Cambia Stato");
    private Sviluppatore sviluppatore;

    public BugDetailsView(BugService service, UtenteService utenteService) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "PROGRAMMER") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        this.utenteService = utenteService;
        this.sviluppatore = (Sviluppatore) this.utenteService
                .findUser(VaadinSession.getCurrent().getAttribute("email").toString());
        VerticalLayout container = new VerticalLayout();
        container.setJustifyContentMode(JustifyContentMode.START);
        container.setAlignItems(Alignment.STRETCH);
        container.setWidth("60%");
        container.setHeightFull();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        this.ticketField.getStyle().set("font-weight", "bold");
        this.ticketField.getStyle().set("color", "var(--lumo-secondary-text-color)");
        this.ticketField.getStyle().set("text-align", "center");
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
        this.statoField.setMinWidth("50px");
        this.statoField.setWidthFull();
        this.statoField.setReadOnly(true);
        info.add(this.urgenzaField, this.statoField);
        HorizontalLayout operatorContainer = new HorizontalLayout();
        operatorContainer.setJustifyContentMode(JustifyContentMode.START);
        operatorContainer.setAlignItems(Alignment.END);
        this.closeButton.setEnabled(false);
        this.closeButton.addClickListener(event -> {
            this.service.changeSviluppoBug(this.bug, this.statoComboBox.getValue(), this.sviluppatore);
            UI.getCurrent().getPage().reload();
        });
        this.statoComboBox.addValueChangeListener(event -> {
            StatoBug value = event.getValue();
            if(value != null) {
                this.closeButton.setEnabled(true);
                this.closeButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                if(value.getNome().equals("in revisione")) {
                    this.closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    this.closeButton.setText("Inizia Revisione");
                } else {
                    this.closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                    this.closeButton.setText("Chiudi Bug");
                }
            }
        });
        operatorContainer.add(this.statoComboBox, this.closeButton);
        container.add(this.ticketField, this.descrizioneField, info, operatorContainer);
        add(container);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Long bugId = Long.parseLong(parameter);
        this.bug = this.service.findBug(bugId);
        this.descrizioneField.setValue(this.bug.getDescrizione());
        this.ticketField.setText("Bug " + parameter + "°");
        this.urgenzaField.setValue(this.bug.getPriorita().getNome());
        String stato = this.service.findStatoBug(bug).getNome();
        this.statoField.setValue(stato);
        this.statoComboBox
                .setItems(this.service.findAllStatiTicketLiberi(this.bug));
        this.statoComboBox.setItemLabelGenerator(StatoBug::getNome);
        if (isTicketClosed(stato)) {
            this.statoComboBox.setVisible(false);
            this.closeButton.setVisible(false);
        }
    }

    private boolean isTicketClosed(String stato) {
        return stato.equals("chiuso - risolto") || stato.equals("chiuso - non riproducibile");
    }

}
