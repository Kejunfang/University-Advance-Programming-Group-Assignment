package studentbooking.applications;

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
import studentbooking.bean.StudentEntity;
import studentbooking.bean.TicketEntity;
import studentbooking.bean.OperatorEntity;
import studentbooking.db.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminDashboard extends Application {

    private TableView<TicketEntity> ticketTable = new TableView<>();
    private TableView<StudentEntity> studentTable = new TableView<>();
    private TableView<OperatorEntity> operatorTable = new TableView<>();
    private final ObservableList<TicketEntity> ticketData = FXCollections.observableArrayList();
    private final ObservableList<StudentEntity> studentData = FXCollections.observableArrayList();
    private final ObservableList<OperatorEntity> operatorData = FXCollections.observableArrayList();

    private ComboBox<String> operatorCombo;

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // Work Order Management Tab - Added Assignment Functionality
        Tab ticketsTab = new Tab("Tickets");
        ticketsTab.setContent(createTicketsTab());

        // User Management Tab
        Tab usersTab = new Tab("Users");
        usersTab.setContent(createUsersTab());

        // Operator Management Tab
        Tab operatorsTab = new Tab("Operators");
        operatorsTab.setContent(createOperatorsTab());

        tabPane.getTabs().addAll(ticketsTab, usersTab, operatorsTab);

        // Logout button at the bottom
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            stage.close();
            new MainLogin().start(new Stage());
        });
        logoutButton.getStyleClass().add("button5");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(tabPane);

        HBox bottomBox = new HBox(logoutButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));
        mainLayout.setBottom(bottomBox);

        // Load data
        loadAllData();

        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    // Create Work Order Management Tab Content - Added Assignment Functionality
    private VBox createTicketsTab() {
        // Setting up table columns
        setupTicketTable();

        // Fix: use member variables instead of local variables
        operatorCombo = new ComboBox<>();
        operatorCombo.setPrefWidth(150);

        // refresh button
        Button refreshButton = new Button("Refresh Tickets");
        refreshButton.setOnAction(e -> loadTicketData());

        // Allocation of work order areas
        HBox assignBox = new HBox(10);
        assignBox.setAlignment(Pos.CENTER_LEFT);
        assignBox.setPadding(new Insets(0, 10, 10, 10));

        Label assignLabel = new Label("Assign to:");
        // Fix: Use member variables instead of local variables
        // ComboBox<String> operatorCombo = new ComboBox<>(); // Delete this line
        // operatorCombo.setPrefWidth(150); // Delete this line

        // Load Operator List
        refreshOperatorCombo(); // Calling the refresh method directly

        Button assignButton = new Button("Assign Ticket");
        assignButton.setOnAction(e -> assignTicketToOperator(operatorCombo.getValue()));

        assignBox.getChildren().addAll(assignLabel, operatorCombo, assignButton);

        // Operation button area
        HBox buttonBox = new HBox(10, refreshButton, assignBox);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(10, buttonBox, ticketTable);
        vbox.setPadding(new Insets(10));
        VBox.setVgrow(ticketTable, Priority.ALWAYS);
        return vbox;
    }

    // Ways to add a Refresh Operator drop-down box
    private void refreshOperatorCombo() {
        operatorCombo.getItems().clear();
        // 假设DBHelper.getAllOperatorIds()返回List<String>，每项为操作员ID
        operatorCombo.getItems().addAll(DBHelper.getAllOperatorIds());
    }


    // Assign selected work orders to operators
    private void assignTicketToOperator(String operatorId) {
        TicketEntity selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null && operatorId != null && !operatorId.isEmpty()) {
            selectedTicket.setAssignedTo(operatorId); // Use ID here
            selectedTicket.setStatus("Assigned");
            selectedTicket.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            DBHelper.updateTicket(selectedTicket);

            ticketTable.refresh();
            new Alert(Alert.AlertType.INFORMATION, "Ticket assigned to operator ID: " + operatorId).show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a ticket and an operator ID").show();
        }
    }

    // Creating user management tab content
    private VBox createUsersTab() {
        // Setting up table columns
        setupStudentTable();

        // push button
        Button refreshButton = new Button("Refresh Users");
        refreshButton.setOnAction(e -> loadStudentData());

        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> showAddUserDialog());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> deleteSelectedUser());

        HBox buttonBox = new HBox(10, refreshButton, addButton, deleteButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(10, buttonBox, studentTable);
        vbox.setPadding(new Insets(10));
        VBox.setVgrow(studentTable, Priority.ALWAYS);
        return vbox;
    }

    // Creating Operator Management Tab Content
    private VBox createOperatorsTab() {
        // Setting up table columns
        setupOperatorTable();

        // cpush button
        Button refreshButton = new Button("Refresh Operators");
        refreshButton.setOnAction(e -> loadOperatorData());

        Button addButton = new Button("Add Operator");
        addButton.setOnAction(e -> showAddOperatorDialog());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> deleteSelectedOperator());

        HBox buttonBox = new HBox(10, refreshButton, addButton, deleteButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(10, buttonBox, operatorTable);
        vbox.setPadding(new Insets(10));
        VBox.setVgrow(operatorTable, Priority.ALWAYS);
        return vbox;
    }

    // Setting up work order form columns
    private void setupTicketTable() {
        ticketTable.getColumns().clear();

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));

        TableColumn<TicketEntity, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<TicketEntity, String> submittedCol = new TableColumn<>("Submitted By");
        submittedCol.setCellValueFactory(new PropertyValueFactory<>("submittedBy"));

        TableColumn<TicketEntity, String> assignedCol = new TableColumn<>("Assigned To");
        assignedCol.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));

        TableColumn<TicketEntity, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        ticketTable.getColumns().addAll(idCol, typeCol, descCol, statusCol, submittedCol, assignedCol, notesCol, createdCol, updatedCol);
        ticketTable.setItems(ticketData);
    }

    // Setting up Student Form Columns
    private void setupStudentTable() {
        studentTable.getColumns().clear();

        TableColumn<StudentEntity, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudentEntity, String> sexCol = new TableColumn<>("Gender");
        sexCol.setCellValueFactory(new PropertyValueFactory<>("sex"));

        TableColumn<StudentEntity, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<StudentEntity, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<StudentEntity, String> uniCol = new TableColumn<>("University");
        uniCol.setCellValueFactory(new PropertyValueFactory<>("university"));

        TableColumn<StudentEntity, String> facultyCol = new TableColumn<>("Faculty");
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<StudentEntity, String> professionCol = new TableColumn<>("Profession");
        professionCol.setCellValueFactory(new PropertyValueFactory<>("profession"));

        TableColumn<StudentEntity, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        TableColumn<StudentEntity, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        studentTable.getColumns().addAll(nameCol, sexCol, ageCol, idCol, uniCol, facultyCol, professionCol, phoneCol, addressCol);
        studentTable.setItems(studentData);
    }

    // Setting Up Operator Form Columns
    private void setupOperatorTable() {
        operatorTable.getColumns().clear();

        TableColumn<OperatorEntity, Integer> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));

        TableColumn<OperatorEntity, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<OperatorEntity, String> sexCol = new TableColumn<>("Gender");
        sexCol.setCellValueFactory(new PropertyValueFactory<>("sex"));

        TableColumn<OperatorEntity, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        operatorTable.getColumns().addAll(accountCol, nameCol, sexCol, phoneCol);
        operatorTable.setItems(operatorData);
    }

    // Load all data
    private void loadAllData() {
        loadTicketData();
        loadStudentData();
        loadOperatorData(); // Make sure to call this method
    }



    private void loadTicketData() {
        List<TicketEntity> allTickets = DBHelper.getAllTickets();
        List<TicketEntity> activeTickets = new ArrayList<>();
        for (TicketEntity t : allTickets) {
            if (!"Closed".equals(t.getStatus())) {
                activeTickets.add(t);
            }
        }
        ticketData.setAll(activeTickets);
    }

    private void loadStudentData() {
        studentData.setAll(DBHelper.getAllStudents());
    }

    private void loadOperatorData() {
        System.out.println("Loading operator data...");
        List<OperatorEntity> operators = DBHelper.getAllOperators();
        System.out.println("Found " + operators.size() + " operators");
        operatorData.setAll(operators);

        // Refresh the dropdown box at the same time
        refreshOperatorCombo();
    }

    // Add the User dialog box
    private void showAddUserDialog() {
        Dialog<StudentEntity> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details");

        // Creating Forms
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField sexField = new TextField();
        TextField passwordField = new TextField();
        TextField ageField = new TextField();
        TextField studentIdField = new TextField();
        TextField universityField = new TextField();
        TextField facultyField = new TextField();
        TextField professionField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Gender:"), 0, 1);
        grid.add(sexField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Age:"), 0, 3);
        grid.add(ageField, 1, 3);
        grid.add(new Label("Student ID:"), 0, 4);
        grid.add(studentIdField, 1, 4);
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
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Results Converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    StudentEntity student = new StudentEntity();
                    student.setName(nameField.getText());
                    student.setSex(sexField.getText());
                    student.setPassword(passwordField.getText());
                    student.setAge(Integer.parseInt(ageField.getText()));
                    student.setStudentId(studentIdField.getText());
                    student.setUniversity(universityField.getText());
                    student.setFaculty(facultyField.getText());
                    student.setProfession(professionField.getText());
                    student.setPhoneNum(phoneField.getText());
                    student.setAddress(addressField.getText());
                    return student;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid age format").show();
                    return null;
                }
            }
            return null;
        });

        // Results Converter
        dialog.showAndWait().ifPresent(student -> {  // It's student, not operator.
            DBHelper.addStudent(student);  // Change to add students
            loadStudentData();  // Refresh Student Forms
            new Alert(Alert.AlertType.INFORMATION, "Student added successfully").show();
        });
    }

    // Add Operator dialog box
    private void showAddOperatorDialog() {
        Dialog<OperatorEntity> dialog = new Dialog<>();
        dialog.setTitle("Add New Operator");
        dialog.setHeaderText("Enter operator details");

        // Creating Forms
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField accountField = new TextField();
        TextField nameField = new TextField();
        TextField sexField = new TextField();
        TextField phoneField = new TextField();
        TextField passwordField = new PasswordField(); // 改为密码字段

        grid.add(new Label("Account (Number):"), 0, 0);
        grid.add(accountField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(sexField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Add button
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Result Converter - Fix Validation Logic
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    // Validating Required Fields
                    if (accountField.getText().isEmpty() ||
                            passwordField.getText().isEmpty() ||
                            nameField.getText().isEmpty()) {

                        new Alert(Alert.AlertType.ERROR, "Account, Password and Name are required").show();
                        return null;
                    }

                    OperatorEntity operator = new OperatorEntity();
                    operator.setAccount(Integer.parseInt(accountField.getText()));
                    operator.setPassword(passwordField.getText());
                    operator.setName(nameField.getText());
                    operator.setSex(sexField.getText().isEmpty() ? "Unknown" : sexField.getText());
                    operator.setPhoneNum(phoneField.getText().isEmpty() ? "N/A" : phoneField.getText());

                    return operator;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Account must be a number").show();
                    return null;
                }
            }
            return null;
        });

        // Processing results - adding the actual save logic
        dialog.showAndWait().ifPresent(operator -> {
            DBHelper.addOperator(operator);
            loadOperatorData(); // Refresh Operator Form

            // Fix: Add dropdown box refresh
            refreshOperatorCombo();
            new Alert(Alert.AlertType.INFORMATION, "Operator added successfully").show();
        });
    }

    // Delete selected users
    private void deleteSelectedUser() {
        StudentEntity selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DBHelper.deleteStudent(selected.getName());
            loadStudentData();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete").show();
        }
    }

    // Delete selected operators
    private void deleteSelectedOperator() {
        OperatorEntity selected = operatorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // cConfirmation dialog box
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Operator");
            confirm.setContentText("Are you sure you want to delete operator: " + selected.getName() + "?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    DBHelper.deleteOperator(selected.getAccount());
                    loadOperatorData(); // Refresh Operator Form

                    // Fix: Add dropdown box refresh
                    refreshOperatorCombo();
                    new Alert(Alert.AlertType.INFORMATION, "Operator deleted successfully").show();
                }
            });
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an operator to delete").show();
        }
    }


}