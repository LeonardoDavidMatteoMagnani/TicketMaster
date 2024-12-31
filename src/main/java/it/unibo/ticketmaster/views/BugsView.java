package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.service.BugService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Bugs | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "programmer_home", layout = UserLayout.class)
public class BugsView extends VerticalLayout {
    private final Grid<Bug> grid = new Grid<>(Bug.class);
    private final TextField filterText = new TextField("Titolo");
    private final ComboBox<String> prioritaComboBox = new ComboBox<>("Priorità");
    private final ComboBox<String> statoComboBox = new ComboBox<>("Stato");
    private final BugService service;

    public BugsView(BugService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "PROGRAMMER") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid(this.grid);
        add(getToolbar(), this.grid);
        updateList();
    }

    private void updateList() {
        this.grid.setItems(this.service.findAllBugs(this.filterText.getValue(),
                this.prioritaComboBox.getValue(), this.statoComboBox.getValue()));
    }

    private Component getToolbar() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        // Per prestazioni migliori nella ricerca, si può usare ValueChangeMode.LAZY
        this.filterText.setValueChangeMode(ValueChangeMode.EAGER);
        this.filterText.addValueChangeListener(e -> updateList());
        this.statoComboBox.setItems(this.service.findAllStatiBug().stream().map(p -> p.getNome()).toList());
        this.statoComboBox.addValueChangeListener(e -> updateList());
        this.prioritaComboBox.setItems(this.service.findAllPrioritaBug().stream().map(p -> p.getNome()).toList());
        this.prioritaComboBox.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(this.filterText, this.prioritaComboBox, this.statoComboBox);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid(Grid<Bug> grid) {
        grid.setSizeFull();
        grid.setColumns("id", "descrizione");
        grid.addColumn(bug -> bug.getPriorita().getNome()).setHeader("Priorità");
        grid.addColumn(bug -> this.service.findStatoBug(bug).getNome()).setHeader("Stato");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> {
            UI.getCurrent().navigate("bug_details/" + String.valueOf(event.getItem().getId()));
        });
    }

}
