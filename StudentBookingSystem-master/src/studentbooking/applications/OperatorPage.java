package studentbooking.applications;

import studentbooking.bean.TicketEntity;
import studentbooking.db.DBHelper;
import studentbooking.bean.OperatorEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperatorPage extends Application {

    private OperatorEntity operatorEntity;
    private TableView<TicketEntity> tableView = new TableView<>();
    private final ObservableList<TicketEntity> ticketData = FXCollections.observableArrayList();

    public OperatorPage(OperatorEntity operatorEntity) {
        this.operatorEntity = operatorEntity;
    }

    @Override
    public void start(Stage stage) {
        BorderPane mainLayout = new BorderPane();

        // top heading
        Label title = new Label("Support Ticket Management System");
        title.setFont(Font.font(24));
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);
        mainLayout.setTop(header);

        // Left Panel - Operation Options
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(250);

        Label operatorLabel = new Label("Operator: " + operatorEntity.getName());
        operatorLabel.setFont(Font.font(16));

        Button refreshButton = new Button("Refresh Tickets");
        refreshButton.setOnAction(e -> loadMyTickets());

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "New", "In Progress", "Resolved", "Closed");
        statusFilter.setValue("All");

        Button filterButton = new Button("Apply Filter");
        filterButton.setOnAction(e -> {
            if ("All".equals(statusFilter.getValue())) {
                loadMyTickets();
            } else {
                filterTicketsByStatus(statusFilter.getValue());
            }
        });

        leftPanel.getChildren().addAll(operatorLabel, refreshButton, statusFilter, filterButton);
        mainLayout.setLeft(leftPanel);

        // Central Panel - Work Order Form
        setupTicketTable();
        ScrollPane scrollPane = new ScrollPane(tableView);
        mainLayout.setCenter(scrollPane);

        // Bottom Panel - Operation Buttons
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setAlignment(Pos.CENTER_RIGHT);

        TextField notesField = new TextField();
        notesField.setPromptText("Add notes...");
        notesField.setPrefWidth(300);

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("New", "In Progress", "Resolved", "Closed");

        Button updateButton = new Button("Update Selected");
        updateButton.setOnAction(e -> updateSelectedTicket(
                statusCombo.getValue(),
                notesField.getText()
        ));

        // Add the logout button to the bottom right corner
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button5"); // Use the blue style
        logoutButton.setOnAction(e -> {
            stage.close();
            new MainLogin().start(new Stage());
        });

        // Adding a spring pushes the logout button to the far right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomPanel.getChildren().addAll(
                new Label("Status:"), statusCombo,
                new Label("Notes:"), notesField,
                updateButton,
                spacer, // sprung
                logoutButton // logout button
        );
        mainLayout.setBottom(bottomPanel);

        // Load the work order assigned to the current operator
        loadMyTickets();

        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Ticket Management - Operator: " + operatorEntity.getName());
        stage.show();
    }

    private void setupTicketTable() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        idCol.setPrefWidth(120);

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));

        TableColumn<TicketEntity, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<TicketEntity, String> submittedCol = new TableColumn<>("Submitted By");
        submittedCol.setCellValueFactory(new PropertyValueFactory<>("submittedBy"));

        TableColumn<TicketEntity, String> assignedCol = new TableColumn<>("Assigned To");
        assignedCol.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setPrefWidth(150);

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));

        tableView.getColumns().addAll(idCol, typeCol, descCol, statusCol, submittedCol, assignedCol, notesCol, createdCol);
        tableView.setItems(ticketData);
    }

    // Load only work orders assigned to the current operator
    private void loadMyTickets() {
        ticketData.clear();
        for (TicketEntity ticket : DBHelper.getAllTickets()) {
            // Show only work orders assigned to the current operator that have not been closed
            if (operatorEntity.getName().equals(ticket.getAssignedTo()) &&
                    !"Closed".equals(ticket.getStatus())) {
                ticketData.add(ticket);
            }
        }
    }

    private void filterTicketsByStatus(String status) {
        ticketData.clear();
        for (TicketEntity ticket : DBHelper.getAllTickets()) {
            // Only work orders assigned to the current operator that meet the status filter criteria are displayed.
            if (operatorEntity.getName().equals(ticket.getAssignedTo()) &&
                    ("All".equals(status) || ticket.getStatus().equals(status))) {
                ticketData.add(ticket);
            }
        }
    }

    private void updateSelectedTicket(String newStatus, String newNotes) {
        TicketEntity selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (newStatus != null) {
                selected.setStatus(newStatus);
            }

            // Modify here: replace note instead of appending
            if (!newNotes.isEmpty()) {
                // Direct replacement of old notes with new notes
                selected.setNotes(newNotes);
            }

            // Ensure that they are assigned to themselves
            selected.setAssignedTo(operatorEntity.getName());

            // update timestamp
            selected.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            DBHelper.updateTicket(selected);

            // If a work order is closed, immediately remove it from the form
            if ("Closed".equals(newStatus)) {
                tableView.getItems().remove(selected);
            } else {
                tableView.refresh();
            }
        }
    }
}