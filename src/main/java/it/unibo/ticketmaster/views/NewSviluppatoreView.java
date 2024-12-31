package it.unibo.ticketmaster.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Sviluppatore;
import it.unibo.ticketmaster.data.repository.SviluppatoreRepository;
import jakarta.annotation.security.PermitAll;

@PageTitle("New Programmer | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "new_programmer", layout = UserLayout.class)
public class NewSviluppatoreView extends VerticalLayout {

    public NewSviluppatoreView(SviluppatoreRepository sviluppatoreRepository) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "ADMIN") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setWidth("100%");

        VerticalLayout container = new VerticalLayout();
        container.getStyle().set("margin-top", "5vh");
        container.getStyle()
                .set("border-radius", "4px")
                .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.2)");
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.STRETCH);
        container.setMaxWidth("30%");
        container.setMinWidth("400px");

        H2 title = new H2("Crea Sviluppatore");
        title.getElement().getStyle().set("text-align", "center");

        TextField nomeField = new TextField("Nome");
        nomeField.setRequired(true);
        TextField cognomeField = new TextField("Cognome");
        cognomeField.setRequired(true);
        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setErrorMessage("Please enter a valid email");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        Button signupButton = new Button("Crea", event -> {
            String nome = nomeField.getValue();
            String cognome = cognomeField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Compila tutti i campi");
                return;
            }
            Sviluppatore sviluppatore = new Sviluppatore(email, nome, cognome, password);
            try {
                sviluppatoreRepository.create(sviluppatore);
            } catch (DataIntegrityViolationException e) {
                Notification.show("Email già in uso");
                return;
            }
            nomeField.clear();
            cognomeField.clear();
            emailField.clear();
            passwordField.clear();
            Notification.show("Account creato con successo!");
        });
        signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signupButton.getElement().getStyle().set("cursor", "pointer");
        container.add(title, nomeField, cognomeField, emailField, passwordField, signupButton);
        add(container);
    }
}
