package it.unibo.ticketmaster.views;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import it.unibo.ticketmaster.data.model.entity.Operatore;
import it.unibo.ticketmaster.data.service.BugService;
import it.unibo.ticketmaster.data.service.TicketService;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.Tuple;

@PageTitle("Statistics | TicketMaster")
@AnonymousAllowed
@PermitAll
@Route(value = "statistics", layout = UserLayout.class)
public class StatisticsView extends FormLayout {

    private final Grid<Tuple> grid = new Grid<>();
    private final DatePicker fromDatepicker;
    private final DatePicker toDatepicker;
    private final TicketService ticketService;
    private final BugService bugService;

    private final BigDecimalField openedTicketsField;
    private final BigDecimalField inProgressTicketsField;
    private final BigDecimalField closedTicketsField;
    private final BigDecimalField totalTicketsField;
    private final BigDecimalField percentageTicketsClosed;
    private final BigDecimalField averageTicketsUptimeField;
    private final BigDecimalField softwareProblemField;
    private final BigDecimalField networkProblemField;
    private final BigDecimalField installationProblemField;
    private final BigDecimalField updateProblemField;
    private final BigDecimalField securityProblemField;
    private final BigDecimalField performanceProblemField;
    private final BigDecimalField windowsSystemField;
    private final BigDecimalField linuxSystemField;
    private final BigDecimalField macSystemField;
    /*private final BigDecimalField openedBugsField;
    private final BigDecimalField inProgressBugsField;
    private final BigDecimalField closedBugsField;
    private final BigDecimalField totalBugsField;
    private final BigDecimalField percentageBugsClosed;
    private final BigDecimalField averageBugsUptimeField;
    private final BigDecimalField softwareBugField;
    private final BigDecimalField networkBugField;
    private final BigDecimalField installationBugField;
    private final BigDecimalField updateBugField;
    private final BigDecimalField securityBugField;
    private final BigDecimalField performanceBugField;
    private final BigDecimalField windowsSystemBugField;
    private final BigDecimalField linuxSystemBugField;
    private final BigDecimalField macSystemBugField;*/

    public StatisticsView(TicketService ticketService, BugService bugService) {
        if (VaadinSession.getCurrent() == null || VaadinSession.getCurrent().getAttribute("userRole") != "ADMIN") {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().close();
            }
            UI.getCurrent().getPage().setLocation("/login");
        }
        this.ticketService = ticketService;
        this.bugService = bugService;
        setWidthFull();
        setHeightFull();
        setResponsiveSteps(new ResponsiveStep("0", 1)/* , new ResponsiveStep("42em", 2)*/);

        VerticalLayout periodicStatsContainer = new VerticalLayout();
        periodicStatsContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        periodicStatsContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        this.fromDatepicker = new DatePicker("From Date");
        this.toDatepicker = new DatePicker("To Date");
        this.fromDatepicker.setMax(LocalDate.now().minusDays(1));
        this.toDatepicker.setMax(LocalDate.now());
        this.fromDatepicker.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                this.toDatepicker.setMin(e.getValue().plusDays(1));
            }else {
                this.toDatepicker.setMin(null);
            }
        });
        this.toDatepicker.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                this.fromDatepicker.setMax(e.getValue().minusDays(1));
            }else {
                this.fromDatepicker.setMax(LocalDate.now().minusDays(1));
            }
        });
        HorizontalLayout dateRangeLayout = new HorizontalLayout(this.fromDatepicker, this.toDatepicker);
        dateRangeLayout.setSpacing(true);

        Button resetButton = new Button("Reset");
        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        resetButton.getElement().getStyle().set("cursor", "pointer");
        resetButton.addClickListener(e -> {
            this.reset();
        });
        Button generateButton = new Button("Genera Statistiche");
        generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        generateButton.getElement().getStyle().set("cursor", "pointer");
        generateButton.addClickListener(e -> {
            this.updateStats();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(resetButton, generateButton);
        dateRangeLayout.setSpacing(true);
        buttonLayout.add(resetButton, generateButton);

        periodicStatsContainer.add(dateRangeLayout, buttonLayout);

        // TICKET --------------
        VerticalLayout ticketStatsContainer = new VerticalLayout();
        ticketStatsContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        ticketStatsContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        ticketStatsContainer.getStyle()
                .set("border-radius", "4px")
                .set("box-shadow", "0 1px 3px rgba(0, 0, 0, 0.2)");
        ticketStatsContainer.setHeightFull();

        H2 ticketStatsTitle = new H2("Ticket Statistics");
        this.openedTicketsField = new BigDecimalField("Ticket Aperti");
        this.openedTicketsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.openedTicketsField.setReadOnly(true);
        this.inProgressTicketsField = new BigDecimalField("Ticket in Analisi");
        this.inProgressTicketsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.inProgressTicketsField.setReadOnly(true);
        this.closedTicketsField = new BigDecimalField("Ticket Chiusi");
        this.closedTicketsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.closedTicketsField.setReadOnly(true);
        this.totalTicketsField = new BigDecimalField("Tickets Totali");
        this.totalTicketsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.totalTicketsField.setReadOnly(true);
        Div percentageSuffixTicket = new Div();
        percentageSuffixTicket.setText("%");
        Div hoursSuffixTicket = new Div();
        hoursSuffixTicket.setText("h");
        this.percentageTicketsClosed = new BigDecimalField("Percentuale Ticket Chiusi");
        this.percentageTicketsClosed.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.percentageTicketsClosed.setSuffixComponent(percentageSuffixTicket);
        this.percentageTicketsClosed.setReadOnly(true);
        this.averageTicketsUptimeField = new BigDecimalField("Tempo Medio di Risoluzione");
        this.averageTicketsUptimeField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.averageTicketsUptimeField.setSuffixComponent(hoursSuffixTicket);
        this.averageTicketsUptimeField.setReadOnly(true);

        H3 ticketProblemsTitle = new H3("Tipologia Ticket");
        ticketProblemsTitle.getStyle().set("margin-top", "1em");
        ticketProblemsTitle.getStyle().set("margin-bottom", "0.5em");
        ticketProblemsTitle.getElement().getStyle().set("text-align", "center");
        this.softwareProblemField = new BigDecimalField("Software");
        this.softwareProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.softwareProblemField.setReadOnly(true);
        this.networkProblemField = new BigDecimalField("Rete");
        this.networkProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.networkProblemField.setReadOnly(true);
        this.installationProblemField = new BigDecimalField("Installazione");
        this.installationProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.installationProblemField.setReadOnly(true);
        this.updateProblemField = new BigDecimalField("Aggiornamento");
        this.updateProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.updateProblemField.setReadOnly(true);
        this.securityProblemField = new BigDecimalField("Sicurezza");
        this.securityProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.securityProblemField.setReadOnly(true);
        this.performanceProblemField = new BigDecimalField("Performance");
        this.performanceProblemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.performanceProblemField.setReadOnly(true);

        H3 ticketSystemsTitle = new H3("SO dei Ticket");
        ticketSystemsTitle.getStyle().set("margin-top", "1em");
        ticketSystemsTitle.getStyle().set("margin-bottom", "0.5em");
        ticketSystemsTitle.getElement().getStyle().set("text-align", "center");
        this.windowsSystemField = new BigDecimalField("Windows");
        this.windowsSystemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.windowsSystemField.setReadOnly(true);
        this.linuxSystemField = new BigDecimalField("Linux");
        this.linuxSystemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.linuxSystemField.setReadOnly(true);
        this.macSystemField = new BigDecimalField("Mac");
        this.macSystemField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.macSystemField.setReadOnly(true);

        FormLayout ticketStatsForm = new FormLayout(this.openedTicketsField, this.inProgressTicketsField,
                this.closedTicketsField,
                this.totalTicketsField, this.percentageTicketsClosed, this.averageTicketsUptimeField,
                ticketProblemsTitle,
                this.softwareProblemField, this.networkProblemField, this.installationProblemField,
                this.updateProblemField, this.securityProblemField, this.performanceProblemField, ticketSystemsTitle,
                this.windowsSystemField, this.linuxSystemField, this.macSystemField);
        ticketStatsForm.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("21em", 2),
                new ResponsiveStep("22em", 3));
        ticketStatsForm.setColspan(this.totalTicketsField, 3);
        ticketStatsForm.setColspan(this.percentageTicketsClosed, 3);
        ticketStatsForm.setColspan(this.averageTicketsUptimeField, 3);
        ticketStatsForm.setColspan(ticketProblemsTitle, 3);
        ticketStatsForm.setColspan(ticketSystemsTitle, 3);
        ticketStatsContainer.add(ticketStatsTitle, ticketStatsForm);
        // --------------------

        /* BUG --------------
        VerticalLayout bugStatsContainer = new VerticalLayout();
        bugStatsContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        bugStatsContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        bugStatsContainer.getStyle()
                .set("border-radius", "4px")
                .set("box-shadow", "0 1px 3px rgba(0, 0, 0, 0.2)");
        bugStatsContainer.setHeightFull();

        H2 bugStatsTitle = new H2("Bug Statistics");
        this.openedBugsField = new BigDecimalField("Da Correggere");
        this.openedBugsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.openedBugsField.setReadOnly(true);
        this.inProgressBugsField = new BigDecimalField("Bug in Revisione");
        this.inProgressBugsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.inProgressBugsField.setReadOnly(true);
        this.closedBugsField = new BigDecimalField("Bug Chiusi");
        this.closedBugsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.closedBugsField.setReadOnly(true);
        this.totalBugsField = new BigDecimalField("Bugs Totali");
        this.totalBugsField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.totalBugsField.setReadOnly(true);
        Div percentageSuffixBug = new Div();
        percentageSuffixBug.setText("%");
        Div hoursSuffixBug = new Div();
        hoursSuffixBug.setText("h");
        this.percentageBugsClosed = new BigDecimalField("Percentuale Bug Chiusi");
        this.percentageBugsClosed.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.percentageBugsClosed.setSuffixComponent(percentageSuffixBug);
        this.percentageBugsClosed.setReadOnly(true);
        this.averageBugsUptimeField = new BigDecimalField("Tempo Medio di Risoluzione");
        this.averageBugsUptimeField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.averageBugsUptimeField.setSuffixComponent(hoursSuffixBug);
        this.averageBugsUptimeField.setReadOnly(true);

        H3 bugProblemsTitle = new H3("Tipologia Bug");
        bugProblemsTitle.getStyle().set("margin-top", "1em");
        bugProblemsTitle.getStyle().set("margin-bottom", "0.5em");
        bugProblemsTitle.getElement().getStyle().set("text-align", "center");
        this.softwareBugField = new BigDecimalField("Software");
        this.softwareBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.softwareBugField.setReadOnly(true);
        this.networkBugField = new BigDecimalField("Rete");
        this.networkBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.networkBugField.setReadOnly(true);
        this.installationBugField = new BigDecimalField("Installazione");
        this.installationBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.installationBugField.setReadOnly(true);
        this.updateBugField = new BigDecimalField("Aggiornamento");
        this.updateBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.updateBugField.setReadOnly(true);
        this.securityBugField = new BigDecimalField("Sicurezza");
        this.securityBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.securityBugField.setReadOnly(true);
        this.performanceBugField = new BigDecimalField("Performance");
        this.performanceBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.performanceBugField.setReadOnly(true);

        H3 bugSystemsTitle = new H3("SO bug");
        bugSystemsTitle.getStyle().set("margin-top", "1em");
        bugSystemsTitle.getStyle().set("margin-bottom", "0.5em");
        bugSystemsTitle.getElement().getStyle().set("text-align", "center");
        this.windowsSystemBugField = new BigDecimalField("Windows");
        this.windowsSystemBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.windowsSystemBugField.setReadOnly(true);
        this.linuxSystemBugField = new BigDecimalField("Linux");
        this.linuxSystemBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.linuxSystemBugField.setReadOnly(true);
        this.macSystemBugField = new BigDecimalField("Mac");
        this.macSystemBugField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        this.macSystemBugField.setReadOnly(true);

        FormLayout bugStatsForm = new FormLayout(openedBugsField, inProgressBugsField, closedBugsField, totalBugsField,
                percentageBugsClosed, averageBugsUptimeField, bugProblemsTitle, softwareBugField, networkBugField,
                installationBugField, updateBugField, securityBugField, performanceBugField, bugSystemsTitle,
                windowsSystemBugField, linuxSystemBugField, macSystemBugField);
        bugStatsForm.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("21em", 2),
                new ResponsiveStep("22em", 3));
        bugStatsForm.setColspan(totalBugsField, 3);
        bugStatsForm.setColspan(percentageBugsClosed, 3);
        bugStatsForm.setColspan(averageBugsUptimeField, 3);
        bugStatsForm.setColspan(bugProblemsTitle, 3);
        bugStatsForm.setColspan(bugSystemsTitle, 3);

        bugStatsContainer.add(bugStatsTitle, bugStatsForm);

        // ----------------- */

        // OPERATOR ------------
        VerticalLayout operatorStatsContainer = new VerticalLayout();
        operatorStatsContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        operatorStatsContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        operatorStatsContainer.getStyle()
                .set("border-radius", "4px")
                .set("box-shadow", "0 1px 3px rgba(0, 0, 0, 0.2)");
        operatorStatsContainer.setHeightFull();

        H2 operatorStatsTitle = new H2("Operator Statistics");

        BigDecimalField averageOperatorValuationField = new BigDecimalField("Valutazione Media");
        averageOperatorValuationField.setValue(BigDecimal.valueOf(this.ticketService.averageOperatorsVoto()));
        averageOperatorValuationField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        averageOperatorValuationField.setReadOnly(true);

        this.configureGrid();

        FormLayout operatorStatsForm = new FormLayout(this.grid, averageOperatorValuationField);
        operatorStatsForm.setResponsiveSteps(new ResponsiveStep("0", 1));
        operatorStatsContainer.add(operatorStatsTitle, operatorStatsForm);
        // --------------------

        this.updateStats();

        add(periodicStatsContainer, ticketStatsContainer, /*bugStatsContainer,*/ operatorStatsContainer);
        setColspan(periodicStatsContainer, 2);
        setColspan(operatorStatsContainer, 2);
    }

    private void updateStats() {
        Timestamp from = null;
        Timestamp to = null;
        LocalDate fromDatePicker = this.fromDatepicker.getValue();
        LocalDate toDatePicker = this.toDatepicker.getValue();
        if (fromDatePicker != null) {
            from = Timestamp.valueOf(fromDatePicker.atStartOfDay());
        }
        if (toDatePicker != null) {
            to = Timestamp.valueOf(toDatePicker.atStartOfDay());
        }
        // TICKET --------------
        this.openedTicketsField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithStatoFromTo("aperto", from, to)));
        this.inProgressTicketsField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithStatoFromTo("analisi", from, to)));
        this.closedTicketsField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithStatoFromTo("chiuso - risolto", from, to)
                        + this.ticketService.numberOfTicketsWithStatoFromTo("chiuso - non risolvibile", from, to)));
        this.totalTicketsField.setValue(new BigDecimal(this.ticketService.numberOfTicketsFromTo(from, to)));
        if (this.totalTicketsField.getValue().doubleValue() == 0) {
            this.percentageTicketsClosed.setValue(BigDecimal.valueOf(0));
        } else {
            this.percentageTicketsClosed.setValue(BigDecimal.valueOf(
                    closedTicketsField.getValue().doubleValue() / totalTicketsField.getValue().doubleValue() * 100));
        }
        this.averageTicketsUptimeField
                .setValue(BigDecimal.valueOf(this.ticketService.averageTicketDurationFromTo(from, to)));
        this.softwareProblemField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithProblemaFromTo("software", from, to)));
        this.networkProblemField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithProblemaFromTo("rete", from, to)));
        this.installationProblemField
                .setValue(new BigDecimal(
                        this.ticketService.numberOfTicketsWithProblemaFromTo("installazione", from, to)));
        this.updateProblemField.setValue(
                new BigDecimal(this.ticketService.numberOfTicketsWithProblemaFromTo("aggiornamento", from, to)));
        this.securityProblemField
                .setValue(new BigDecimal(this.ticketService.numberOfTicketsWithProblemaFromTo("sicurezza", from, to)));
        this.performanceProblemField.setValue(
                new BigDecimal(this.ticketService.numberOfTicketsWithProblemaFromTo("performance", from, to)));
        this.windowsSystemField.setValue(
                new BigDecimal(this.ticketService.numberOfTicketsWithSistemaOperativoFromTo("windows", from, to)));
        this.linuxSystemField.setValue(
                new BigDecimal(this.ticketService.numberOfTicketsWithSistemaOperativoFromTo("linux", from, to)));
        this.macSystemField.setValue(
                new BigDecimal(this.ticketService.numberOfTicketsWithSistemaOperativoFromTo("mac", from, to)));
        // --------------------

        /* BUG --------------
        this.openedBugsField.setValue(
                new BigDecimal(this.bugService.numberOfBugsWithStatoFromTo("non ancora in correzione", from, to)));
        this.inProgressBugsField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithStatoFromTo("in revisione", from, to)));
        this.closedBugsField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithStatoFromTo("chiuso - risolto", from, to)
                        + this.bugService.numberOfBugsWithStatoFromTo("chiuso - non riproducibile", from, to)));
        this.totalBugsField.setValue(new BigDecimal(this.bugService.numberOfBugs(from, to)));
        if (this.totalBugsField.getValue().doubleValue() == 0) {
            this.percentageBugsClosed.setValue(BigDecimal.valueOf(0));
        } else {
            this.percentageBugsClosed.setValue(BigDecimal.valueOf(
                    closedBugsField.getValue().doubleValue() / totalBugsField.getValue().doubleValue() * 100));
        }
        this.averageBugsUptimeField.setValue(BigDecimal.valueOf(this.bugService.averageBugDurationFromTo(from, to)));
        this.softwareBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("software", from, to)));
        this.networkBugField.setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("rete", from, to)));
        this.installationBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("installazione", from, to)));
        this.updateBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("aggiornamento", from, to)));
        this.securityBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("sicurezza", from, to)));
        this.performanceBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithProblemaFromTo("performance", from, to)));
        this.windowsSystemBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithSistemaOperativoFromTo("windows", from, to)));
        this.linuxSystemBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithSistemaOperativoFromTo("linux", from, to)));
        this.macSystemBugField
                .setValue(new BigDecimal(this.bugService.numberOfBugsWithSistemaOperativoFromTo("mac", from, to)));
        // --------------------*/
    }

    private void reset() {
        this.fromDatepicker.clear();
        this.toDatepicker.clear();
        this.updateStats();
    }

    private void configureGrid() {
        this.grid.setItems(this.ticketService.findBestOperators());
        this.grid.addColumn(createOperatoreRenderer()).setHeader("Operatore").setFlexGrow(1);
        this.grid.addColumn(t -> t.get("numeroTickets")).setHeader("Tickets Chiusi");
        this.grid.addColumn(t -> t.get("tempoMedio")).setHeader("Tempo Medio");
        this.grid.addColumn(t -> t.get("votoMedio")).setHeader("Voto Medio");
        this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private static Renderer<Tuple> createOperatoreRenderer(){
        return LitRenderer.<Tuple> of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "<vaadin-avatar name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>"
                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                        + "    <span> ${item.fullName} </span>"
                        + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                        + "      ${item.email}" + "    </span>"
                        + "  </vaadin-vertical-layout>"
                        + "</vaadin-horizontal-layout>")
                .withProperty("fullName", t -> t.get("nome") + " " + t.get("cognome"))
                .withProperty("email", t -> t.get("email"));
    }

}