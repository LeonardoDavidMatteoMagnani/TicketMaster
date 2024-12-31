package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Utente;
import it.unibo.ticketmaster.data.service.UtenteService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Login | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView(UtenteService utenteService) {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setWidth("100%");
        VerticalLayout container = new VerticalLayout();
        // set container margin top by height percentage
        container.getStyle().set("margin-top", "5vh");
        container.getStyle()
                .set("border-radius", "4px")
                .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.2)");
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.STRETCH);
        container.setMaxWidth("30%");
        container.setMinWidth("400px");
        H2 title = new H2("Log in");
        title.getElement().getStyle().set("text-align", "center");
        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setErrorMessage("Please enter a valid email");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        Button loginButton = new Button("Login", event -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            boolean isValidCredentials = utenteService.authenticate(email, password);
            if (isValidCredentials) {
                String userRole = utenteService.getUserRole(email);
                Utente utente = utenteService.findUser(email);
                VaadinSession.getCurrent().setAttribute("userRole", userRole);
                VaadinSession.getCurrent().setAttribute("email", email);
                VaadinSession.getCurrent().setAttribute("nome", utente.getNome());
                VaadinSession.getCurrent().setAttribute("cognome", utente.getCognome());
                switch (userRole) {
                    case "OPERATOR":
                        UI.getCurrent().navigate("user_tickets");
                        break;
                    case "PROGRAMMER":
                        UI.getCurrent().navigate("programmer_home");
                        break;
                    case "ADMIN":
                        UI.getCurrent().navigate("statistics");
                        break;
                    default:
                        UI.getCurrent().navigate("user_home");
                        break;
                }
                Notification.show("Login successful!");
            } else {
                Notification.show("Email o Password errata!");
            }
        });
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.getElement().getStyle().set("cursor", "pointer");
        RouterLink registerLink = new RouterLink("Non hai un account? Signup", SignUpView.class);
        container.add(title, emailField, passwordField, loginButton, registerLink);
        add(container);
    }
}
