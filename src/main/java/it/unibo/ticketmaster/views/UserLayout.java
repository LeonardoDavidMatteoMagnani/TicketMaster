package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

import it.unibo.ticketmaster.data.repository.UtenteRepository;
import it.unibo.ticketmaster.data.service.UtenteService;

public class UserLayout extends AppLayout {

    private H1 logo;
    private final UtenteService service;

    public UserLayout(UtenteService service) {
        this.service = service;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        this.logo = new H1("Ticket Master");
        this.logo.addClassNames("text-l", "m-m");
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getElement().getStyle().set("cursor", "pointer");
        HorizontalLayout header = new HorizontalLayout(drawerToggle, logo);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");
        Button logoutButton = new Button("Logout", event -> {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        });
        logoutButton.addClassNames("text-s", "m-s");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        logoutButton.getElement().getStyle().set("cursor", "pointer");
        addToNavbar(header, logoutButton);
    }

    private void createDrawer() {
        String text = "Not Found 404";
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("nome") != null
                && VaadinSession.getCurrent().getAttribute("cognome") != null) {
            text = VaadinSession.getCurrent().getAttribute("nome").toString() + " "
                    + VaadinSession.getCurrent().getAttribute("cognome").toString();
        }
        H2 nomeCognomeUtente = new H2(text);
        nomeCognomeUtente.getElement().getStyle().set("text-align", "center");
        nomeCognomeUtente.getElement().getStyle().set("padding-top", "1em");
        nomeCognomeUtente.getElement().getStyle().set("padding-bottom", "1em");
        nomeCognomeUtente.getElement().getStyle().set("padding-left", "1em");
        nomeCognomeUtente.getElement().getStyle().set("padding-right", "1em");
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.STRETCH);
        layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setWidthFull();
        layout.setHeightFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.add(nomeCognomeUtente);
        if (VaadinSession.getCurrent() != null && VaadinSession.getCurrent().getAttribute("userRole") != null) {
            String role = VaadinSession.getCurrent().getAttribute("userRole").toString();
            switch (role) {
                case "OPERATOR":
                    this.logo.setText("Ticket Master - Operatore");
                    Button homeO = new Button("Home", e -> {
                        UI.getCurrent().navigate(UserTicketsView.class);
                    });
                    homeO.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    homeO.getElement().getStyle().set("cursor", "pointer");
                    homeO.setMinHeight("5vh");
                    homeO.setHeight("10vh");
                    layout.add(homeO);
                    break;
                case "PROGRAMMER":
                    this.logo.setText("Ticket Master - Sviluppatore");
                    Button homeP = new Button("Home", e -> {
                        UI.getCurrent().navigate(BugsView.class);
                    });
                    homeP.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    homeP.getElement().getStyle().set("cursor", "pointer");
                    homeP.setMinHeight("5vh");
                    homeP.setHeight("10vh");
                    layout.add(homeP);
                    break;
                case "ADMIN":
                    this.logo.setText("Ticket Master - Amministratore");
                    Button homeA = new Button("Home", e -> {
                        UI.getCurrent().navigate(StatisticsView.class);
                    });
                    homeA.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    homeA.getElement().getStyle().set("cursor", "pointer");
                    homeA.setMinHeight("5vh");
                    homeA.setHeight("10vh");
                    Button operatorsList = new Button("Operatori", e -> {
                        UI.getCurrent().navigate(OperatorsView.class);
                    });
                    operatorsList.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    operatorsList.getElement().getStyle().set("cursor", "pointer");
                    operatorsList.setMinHeight("5vh");
                    operatorsList.setHeight("10vh");
                    Button newOperator = new Button("Nuovo Operatore", e -> {
                        UI.getCurrent().navigate(NewOperatoreView.class);
                    });
                    newOperator.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    newOperator.getElement().getStyle().set("cursor", "pointer");
                    newOperator.setMinHeight("5vh");
                    newOperator.setHeight("10vh");
                    Button sviluppatoriList = new Button("Sviluppatori", e -> {
                        UI.getCurrent().navigate(SviluppatoriView.class);
                    });
                    sviluppatoriList.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    sviluppatoriList.getElement().getStyle().set("cursor", "pointer");
                    sviluppatoriList.setMinHeight("5vh");
                    sviluppatoriList.setHeight("10vh");
                    Button newSviluppatore = new Button("Nuovo Sviluppatore", e -> {
                        UI.getCurrent().navigate(NewSviluppatoreView.class);
                    });
                    newSviluppatore.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    newSviluppatore.getElement().getStyle().set("cursor", "pointer");
                    newSviluppatore.setMinHeight("5vh");
                    newSviluppatore.setHeight("10vh");
                    layout.add(homeA, operatorsList, newOperator, sviluppatoriList, newSviluppatore);
                    break;
                default:
                    this.logo.setText("Ticket Master");
                    Button homeU = new Button("Home", e -> {
                        UI.getCurrent().navigate(ClientHomeView.class);
                    });
                    homeU.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    homeU.getElement().getStyle().set("cursor", "pointer");
                    homeU.setMinHeight("5vh");
                    homeU.setHeight("10vh");
                    Button myTicket = new Button("Miei Ticket", e -> {
                        UI.getCurrent().navigate(UserTicketsView.class);
                    });
                    myTicket.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    myTicket.getElement().getStyle().set("cursor", "pointer");
                    myTicket.setMinHeight("5vh");
                    myTicket.setHeight("10vh");
                    Button newTicket = new Button("Nuovo Ticket", e -> {
                        UI.getCurrent().navigate(NewTicketView.class);
                    });
                    newTicket.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    newTicket.getElement().getStyle().set("cursor", "pointer");
                    newTicket.setMinHeight("5vh");
                    newTicket.setHeight("10vh");
                    Button deleteAccount = new Button("Elimina Account", e -> {
                        VaadinSession.getCurrent().close();
                        UI.getCurrent().getPage().setLocation("/login");
                        this.service.deleteClienteByEmail(VaadinSession.getCurrent().getAttribute("email").toString());
                    });
                    VerticalLayout deleteAccountLayout = new VerticalLayout();
                    deleteAccountLayout.setAlignItems(Alignment.STRETCH);
                    deleteAccountLayout.setJustifyContentMode(JustifyContentMode.END);
                    deleteAccountLayout.setWidthFull();
                    deleteAccountLayout.setPadding(false);
                    deleteAccountLayout.setSpacing(false);
                    deleteAccountLayout.setMargin(false);
                    deleteAccountLayout.setWidthFull();
                    deleteAccountLayout.setHeightFull();
                    deleteAccountLayout.getElement().getStyle().set("background-color", "#00000000");
                    deleteAccount.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteAccount.getElement().getStyle().set("cursor", "pointer");
                    deleteAccount.setMinHeight("5vh");
                    deleteAccount.setHeight("10vh");
                    deleteAccountLayout.add(deleteAccount);
                    layout.add(homeU, myTicket, newTicket, deleteAccountLayout);
                    break;
            }
        }
        addToDrawer(layout);
    }

}
