package studentbooking.applications;

import studentbooking.bean.TicketEntity;
import studentbooking.db.DBHelper;
import studentbooking.bean.StudentEntity;
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

public class UserPage extends Application {

    private StudentEntity studentEntity;
    private TableView<TicketEntity> activeTicketTable = new TableView<>();
    private TableView<TicketEntity> resolvedTicketTable = new TableView<>();
    private final ObservableList<TicketEntity> activeTicketData = FXCollections.observableArrayList();
    private final ObservableList<TicketEntity> resolvedTicketData = FXCollections.observableArrayList();

    public UserPage(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    @Override
    public void start(Stage stage) {
        BorderPane mainLayout = new BorderPane();

        // top headline
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER);
        topBar.setSpacing(20);

        Label title = new Label("Customer Support Ticket System");
        title.setFont(Font.font(24));

        HBox buttonBar = createTopButtonBar();

        // Adding a spring pushes the button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(title, spacer, buttonBar);
        mainLayout.setTop(topBar);

        // Left Panel - Create a New Work Order
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(300);

        Label newTicketLabel = new Label("Create New Ticket");
        newTicketLabel.setFont(Font.font(18));

        TextField issueTypeField = new TextField();
        issueTypeField.setPromptText("Issue Type");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefRowCount(5);

        Button submitButton = new Button("Submit Ticket");
        submitButton.setOnAction(e -> {
            TicketEntity ticket = new TicketEntity();
            ticket.setTicketId(DBHelper.generateTicketId());
            ticket.setIssueType(issueTypeField.getText());
            ticket.setDescription(descriptionArea.getText());
            ticket.setStatus("New");
            ticket.setSubmittedBy(studentEntity.getName());

            DBHelper.addTicket(ticket);
            activeTicketData.add(ticket);

            issueTypeField.clear();
            descriptionArea.clear();
        });

        leftPanel.getChildren().addAll(newTicketLabel, issueTypeField, descriptionArea, submitButton);
        mainLayout.setLeft(leftPanel);

        // Central panel - two work order forms
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(15));

        // Activity Work Order Form
        Label activeTicketsLabel = new Label("Active Tickets");
        activeTicketsLabel.setFont(Font.font(18));
        setupActiveTicketTable();
        VBox activeBox = new VBox(5, activeTicketsLabel, activeTicketTable);
        activeBox.setPadding(new Insets(0, 0, 10, 0));

        // Resolved Work Order Form
        Label resolvedTicketsLabel = new Label("Resolved Tickets");
        resolvedTicketsLabel.setFont(Font.font(18));
        setupResolvedTicketTable();
        VBox resolvedBox = new VBox(5, resolvedTicketsLabel, resolvedTicketTable);
        resolvedBox.setPadding(new Insets(10, 0, 0, 0));

        centerBox.getChildren().addAll(activeBox, resolvedBox);
        mainLayout.setCenter(centerBox);

        // Logout button at the bottom
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button5");
        logoutButton.setOnAction(e -> {
            stage.close();
            new MainLogin().start(new Stage());
        });

        HBox bottomBox = new HBox(logoutButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));
        mainLayout.setBottom(bottomBox);

        // Loading User Work Orders
        loadUserTickets();

        // Increase window size to display larger tables
        Scene scene = new Scene(mainLayout, 1200, 900); // Increase window size
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Support Ticket System - " + studentEntity.getName());
        stage.show();
    }

    private void setupActiveTicketTable() {
        // Using the Adjustable Column Width Policy
        activeTicketTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        activeTicketTable.setMinHeight(200);
        activeTicketTable.setPrefHeight(300); // cIncrease table height

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        idCol.setMinWidth(120);

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));
        typeCol.setMinWidth(150);

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setMinWidth(100);

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        createdCol.setMinWidth(150);

        TableColumn<TicketEntity, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        updatedCol.setMinWidth(150);

        // Add Operator Notes Column - Increase Width
        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Operator Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setMinWidth(400); // Increase the width of the Notes column

        activeTicketTable.getColumns().addAll(idCol, typeCol, statusCol, createdCol, updatedCol, notesCol);
        activeTicketTable.setItems(activeTicketData);
    }

    private void setupResolvedTicketTable() {
        // Using the Adjustable Column Width Policy
        resolvedTicketTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        resolvedTicketTable.setMinHeight(200);
        resolvedTicketTable.setPrefHeight(300); // cIncrease table height

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        idCol.setMinWidth(120);

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));
        typeCol.setMinWidth(150);

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setMinWidth(100);

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        createdCol.setMinWidth(150);

        TableColumn<TicketEntity, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        updatedCol.setMinWidth(150);

        // Add Operator Notes Column - Increase Width
        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Operator Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setMinWidth(400); // cIncrease the width of the Notes column

        resolvedTicketTable.getColumns().addAll(idCol, typeCol, statusCol, createdCol, updatedCol, notesCol);
        resolvedTicketTable.setItems(resolvedTicketData);
    }

    private void loadUserTickets() {
        activeTicketData.clear();
        resolvedTicketData.clear();

        for (TicketEntity ticket : DBHelper.getAllTickets()) {
            if (ticket.getSubmittedBy().equals(studentEntity.getName())) {
                if ("Resolved".equals(ticket.getStatus()) || "Closed".equals(ticket.getStatus())) {
                    resolvedTicketData.add(ticket);
                } else {
                    activeTicketData.add(ticket);
                }
            }
        }
    }
    private HBox createTopButtonBar() {
        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.getStyleClass().add("button-blue");
        editProfileButton.setOnAction(e -> showEditProfileDialog());

        HBox buttonBar = new HBox(10, editProfileButton);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(0, 15, 10, 0));
        return buttonBar;
    }
    private void showEditProfileDialog() {
        Dialog<StudentEntity> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your personal information");

        // Creating Forms
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // read-only field
        TextField idField = new TextField(studentEntity.getStudentId());
        idField.setEditable(false);
        idField.setDisable(true);

        // Editable fields
        TextField nameField = new TextField(studentEntity.getName());
        TextField sexField = new TextField(studentEntity.getSex());
        TextField ageField = new TextField(String.valueOf(studentEntity.getAge()));
        TextField universityField = new TextField(studentEntity.getUniversity());
        TextField facultyField = new TextField(studentEntity.getFaculty());
        TextField professionField = new TextField(studentEntity.getProfession());
        TextField phoneField = new TextField(studentEntity.getPhoneNum());
        TextField addressField = new TextField(studentEntity.getAddress());

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Leave blank to keep current password");

        grid.add(new Label("Student ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Gender:"), 0, 2);
        grid.add(sexField, 1, 2);
        grid.add(new Label("Age:"), 0, 3);
        grid.add(ageField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new Label("University:"), 0, 5);
        grid.add(universityField, 1, 5);
        grid.add(new Label("Faculty:"), 0, 6);
        grid.add(facultyField, 1, 6);
        grid.add(new Label("Profession:"), 0, 7);
        grid.add(professionField, 1, 7);
        grid.add(new Label("Phone:"), 0, 8);
        grid.add(phoneField, 1, 8);
        grid.add(new Label("Address:"), 0, 9);
        grid.add(addressField, 1, 9);

        dialog.getDialogPane().setContent(grid);

        // Add button
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Results Converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                try {
                    // Creating an Updated Student Object
                    StudentEntity updatedStudent = new StudentEntity();
                    updatedStudent.setStudentId(studentEntity.getStudentId()); // 学号不变
                    updatedStudent.setName(nameField.getText());
                    updatedStudent.setSex(sexField.getText());

                    // If the password field is not empty, update the password
                    if (!passwordField.getText().isEmpty()) {
                        updatedStudent.setPassword(passwordField.getText());
                    } else {
                        updatedStudent.setPassword(studentEntity.getPassword());
                    }

                    updatedStudent.setAge(Integer.parseInt(ageField.getText()));
                    updatedStudent.setUniversity(universityField.getText());
                    updatedStudent.setFaculty(facultyField.getText());
                    updatedStudent.setProfession(professionField.getText());
                    updatedStudent.setPhoneNum(phoneField.getText());
                    updatedStudent.setAddress(addressField.getText());

                    return updatedStudent;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid age format").show();
                    return null;
                }
            }
            return null;
        });
        // Disposal results
        dialog.showAndWait().ifPresent(updatedStudent -> {
            // Updating student information in the database
            DBHelper.updateStudent(studentEntity, updatedStudent);

            // Update current student entities
            studentEntity.setName(updatedStudent.getName());
            studentEntity.setSex(updatedStudent.getSex());
            studentEntity.setPassword(updatedStudent.getPassword());
            studentEntity.setAge(updatedStudent.getAge());
            studentEntity.setUniversity(updatedStudent.getUniversity());
            studentEntity.setFaculty(updatedStudent.getFaculty());
            studentEntity.setProfession(updatedStudent.getProfession());
            studentEntity.setPhoneNum(updatedStudent.getPhoneNum());
            studentEntity.setAddress(updatedStudent.getAddress());

            // Update window title
            Stage stage = (Stage) activeTicketTable.getScene().getWindow();
            stage.setTitle("Support Ticket System - " + studentEntity.getName());

            new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully").show();
        });
    }
}