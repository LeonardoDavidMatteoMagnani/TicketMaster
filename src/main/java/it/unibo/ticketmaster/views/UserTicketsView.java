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

import it.unibo.ticketmaster.data.model.entity.PrioritaTicket;
import it.unibo.ticketmaster.data.model.entity.ProblemaTicket;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;
import it.unibo.ticketmaster.data.model.entity.StatoTicket;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("My Tickets | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "user_tickets", layout = UserLayout.class)
public class UserTicketsView extends VerticalLayout {
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);
    private final TextField filterText = new TextField("Titolo");
    private final ComboBox<PrioritaTicket> prioritaComboBox = new ComboBox<>("Priorità");
    private final ComboBox<ProblemaTicket> problemaComboBox = new ComboBox<>("Problema");
    private final ComboBox<SistemaOperativo> sistemaOperativoComboBox = new ComboBox<>("SO");
    private final ComboBox<StatoTicket> statoComboBox = new ComboBox<>("Stato");
    private final String userRole;
    private final TicketUtenteService service;
    private final SerializableBiConsumer<Span, Ticket> statusComponentUpdater;

    public UserTicketsView(TicketUtenteService service) {
        if (VaadinSession.getCurrent() == null || (VaadinSession.getCurrent().getAttribute("userRole") != "USER"
                && VaadinSession.getCurrent().getAttribute("userRole") != "OPERATOR")) {
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
        this.userRole = VaadinSession.getCurrent().getAttribute("userRole").toString();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);
        updateList();
    }

    private void updateList() {
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("email") != null) {
            String email = VaadinSession.getCurrent().getAttribute("email").toString();
            grid.setItems(this.service.findAllUserTickets(this.filterText.getValue(), this.prioritaComboBox.getValue(), this.problemaComboBox.getValue(),
                    this.sistemaOperativoComboBox.getValue(), this.statoComboBox.getValue(), email));
        }
    }

    private Component getToolbar() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        // Per prestazioni migliori nella ricerca, si può usare ValueChangeMode.LAZY
        this.filterText.setValueChangeMode(ValueChangeMode.EAGER);
        this.filterText.addValueChangeListener(e -> updateList());
        this.prioritaComboBox.setItems(this.service.findAllPrioritaTicket());
        this.prioritaComboBox.setItemLabelGenerator(PrioritaTicket::getNome);
        this.prioritaComboBox.addValueChangeListener(e -> updateList());
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
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(this.filterText);
        if (this.userRole.equals("OPERATOR")){
            toolbar.add(this.prioritaComboBox);
        }
        toolbar.add(this.problemaComboBox,
                this.sistemaOperativoComboBox,
                this.statoComboBox);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        this.grid.setSizeFull();
        this.grid.setColumns("numero", "titolo");
        if (this.userRole.equals("OPERATOR")) {
            grid.addColumn(ticket -> ticket.getPriorita().getNome()).setHeader("Priorità");
        }
        this.grid.addColumn(ticket -> ticket.getProblema().getNome()).setHeader("Problema");
        this.grid.addColumn(ticket -> ticket.getSistemaOperativo().getNome()).setHeader("SO");
        this.grid.addColumn(this.createStatusComponentRenderer()).setHeader("Stato");
        this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
        this.grid.addItemClickListener(event -> {
            if (this.userRole.equals("OPERATOR") && this.service.findLastFase(event.getItem()).getId().getStatoTicket().getNome().equals("aperto")) {
                    this.service.changeTicketFaseByNome(event.getItem(), "analisi");
            }
            UI.getCurrent().navigate("ticket_details/" + String.valueOf(event.getItem().getNumero()));
        });
    }

    private ComponentRenderer<Span, Ticket> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, this.statusComponentUpdater);
    }

}
