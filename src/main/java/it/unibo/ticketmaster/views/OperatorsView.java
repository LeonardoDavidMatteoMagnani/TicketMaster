package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.model.entity.Ticket;
import it.unibo.ticketmaster.data.service.TicketUtenteService;
import it.unibo.ticketmaster.data.service.UtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Operators | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "operators", layout = UserLayout.class)
public class OperatorsView extends VerticalLayout {
    private final Grid<Operatore> grid = new Grid<>(Operatore.class);
    private final TextField filterText = new TextField("Titolo");
    private final UtenteService service;

    public OperatorsView(UtenteService service) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "ADMIN") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), this.grid);
        updateList();
    }

    private void updateList() {
        this.grid.setItems(this.service.findAllOperatori(this.filterText.getValue()));
    }

    private Component getToolbar() {
        this.filterText.setPlaceholder("Filter by name...");
        this.filterText.setClearButtonVisible(true);
        // Per prestazioni migliori nella ricerca, si può usare ValueChangeMode.LAZY
        this.filterText.setValueChangeMode(ValueChangeMode.EAGER);
        this.filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(this.filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        this.grid.setSizeFull();
        this.grid.setColumns("email", "nome", "cognome");
        this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
        this.grid.addColumn(new ComponentRenderer<>(Button::new, (button, operatore) -> {
                button.addThemeVariants(ButtonVariant.LUMO_ICON,
                        ButtonVariant.LUMO_ERROR,
                        ButtonVariant.LUMO_TERTIARY);
                button.addClickListener(e -> {
                    this.service.deleteOperatoreByEmail(operatore.getEmail());
                    updateList();
                });
                button.setIcon(new Icon(VaadinIcon.TRASH));
                button.getElement().getStyle().set("cursor", "pointer");
            })).setHeader("Elimina");
    }

}
