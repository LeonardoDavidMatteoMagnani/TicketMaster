package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Tickets | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "user_home", layout = UserLayout.class)
public class ClientHomeView extends VerticalLayout {
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);
    private final TextField filterText = new TextField("Titolo");
    private final ComboBox<ProblemaTicket> problemaComboBox = new ComboBox<>("Problema");
    private final ComboBox<SistemaOperativo> sistemaOperativoComboBox = new ComboBox<>("SO");
    private final ComboBox<StatoTicket> statoComboBox = new ComboBox<>("Stato");
    private final TicketUtenteService service;
    private final SerializableBiConsumer<Span, Ticket> statusComponentUpdater;

    public ClientHomeView(TicketUtenteService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "USER") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        this.statusComponentUpdater = (span, ticket) -> {
            String stato = ticket.getStato().getNome();
            String theme;
            switch (stato) {
                case "aperto":
                    theme = String.format("badge %s", "success");
                    break;
                case "analisi":
                    theme = String.format("badge %s", "info");
                    break;
                default:
                    theme = String.format("badge %s", "error");
                    break;
            }
            span.getElement().setAttribute("theme", theme);
            span.setText(stato);
        };
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), this.grid);
        updateList();
    }

    private void updateList() {
        this.grid.setItems(this.service.findAllTickets(this.filterText.getValue(), this.problemaComboBox.getValue(),
                this.sistemaOperativoComboBox.getValue(), this.statoComboBox.getValue()));
    }

    private Component getToolbar() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        // Per prestazioni migliori nella ricerca, si può usare ValueChangeMode.LAZY
        this.filterText.setValueChangeMode(ValueChangeMode.EAGER);
        this.filterText.addValueChangeListener(e -> updateList());
        this.problemaComboBox.setItems(this.service.findAllProblemi());
        this.problemaComboBox.setItemLabelGenerator(ProblemaTicket::getNome);
        this.problemaComboBox.addValueChangeListener(e -> updateList());
        this.sistemaOperativoComboBox
                .setItems(this.service.findAllSistemiOperativi());
        this.sistemaOperativoComboBox.setItemLabelGenerator(SistemaOperativo::getNome);
        this.sistemaOperativoComboBox.addValueChangeListener(e -> updateList());
        this.statoComboBox.setItems(this.service.findAllStatiTicket());
        this.statoComboBox.setItemLabelGenerator(StatoTicket::getNome);
        this.statoComboBox.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(this.filterText, this.problemaComboBox,
                this.sistemaOperativoComboBox,
                this.statoComboBox);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        this.grid.setSizeFull();
        this.grid.setColumns("numero", "titolo");
        this.grid.addColumn(ticket -> ticket.getProblema().getNome()).setHeader("Problema");
        this.grid.addColumn(ticket -> ticket.getSistemaOperativo().getNome()).setHeader("SO");
        this.grid.addColumn(this.createStatusComponentRenderer()).setHeader("Stato");
        this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
        this.grid.addItemClickListener(event -> {
            UI.getCurrent().navigate("ticket_details/" + String.valueOf(event.getItem().getNumero()));
        });
    }

    private ComponentRenderer<Span, Ticket> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, this.statusComponentUpdater);
    }

}