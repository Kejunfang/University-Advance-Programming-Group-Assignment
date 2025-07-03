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

        // 工单管理标签页 - 添加了分配功能
        Tab ticketsTab = new Tab("Tickets");
        ticketsTab.setContent(createTicketsTab());

        // 用户管理标签页
        Tab usersTab = new Tab("Users");
        usersTab.setContent(createUsersTab());

        // 操作员管理标签页
        Tab operatorsTab = new Tab("Operators");
        operatorsTab.setContent(createOperatorsTab());

        tabPane.getTabs().addAll(ticketsTab, usersTab, operatorsTab);

        // 底部注销按钮
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

        // 加载数据
        loadAllData();

        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    // 创建工单管理标签页内容 - 添加了分配功能
    private VBox createTicketsTab() {
        // 设置表格列
        setupTicketTable();

        // 修复：使用成员变量而不是局部变量
        operatorCombo = new ComboBox<>();
        operatorCombo.setPrefWidth(150);

        // 刷新按钮
        Button refreshButton = new Button("Refresh Tickets");
        refreshButton.setOnAction(e -> loadTicketData());

        // 分配工单区域
        HBox assignBox = new HBox(10);
        assignBox.setAlignment(Pos.CENTER_LEFT);
        assignBox.setPadding(new Insets(0, 10, 10, 10));

        Label assignLabel = new Label("Assign to:");
        // 修复：使用成员变量而不是局部变量
        // ComboBox<String> operatorCombo = new ComboBox<>(); // 删除这行
        // operatorCombo.setPrefWidth(150); // 删除这行

        // 加载操作员列表
        refreshOperatorCombo(); // 直接调用刷新方法

        Button assignButton = new Button("Assign Ticket");
        assignButton.setOnAction(e -> assignTicketToOperator(operatorCombo.getValue()));

        assignBox.getChildren().addAll(assignLabel, operatorCombo, assignButton);

        // 操作按钮区域
        HBox buttonBox = new HBox(10, refreshButton, assignBox);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(10, buttonBox, ticketTable);
        vbox.setPadding(new Insets(10));
        VBox.setVgrow(ticketTable, Priority.ALWAYS);
        return vbox;
    }

    // 添加刷新操作员下拉框的方法
    private void refreshOperatorCombo() {
        operatorCombo.getItems().clear();
        operatorCombo.getItems().addAll(DBHelper.getAllOperatorNames());
    }


    // 将选中的工单分配给操作员
    // 将选中的工单分配给操作员
// 将选中的工单分配给操作员
    private void assignTicketToOperator(String operatorName) {
        TicketEntity selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null && operatorName != null && !operatorName.isEmpty()) {
            selectedTicket.setAssignedTo(operatorName);
            selectedTicket.setStatus("Assigned");
            selectedTicket.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            DBHelper.updateTicket(selectedTicket); // 确保调用更新方法

            ticketTable.refresh();
            new Alert(Alert.AlertType.INFORMATION, "Ticket assigned to " + operatorName).show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a ticket and an operator").show();
        }
    }

    // 创建用户管理标签页内容
    private VBox createUsersTab() {
        // 设置表格列
        setupStudentTable();

        // 操作按钮
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

    // 创建操作员管理标签页内容
    private VBox createOperatorsTab() {
        // 设置表格列
        setupOperatorTable();

        // 操作按钮
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

    // 设置工单表格列
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

    // 设置学生表格列
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

    // 设置操作员表格列
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

    // 加载所有数据
    private void loadAllData() {
        loadTicketData();
        loadStudentData();
        loadOperatorData(); // 确保调用这个方法
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

        // 同时刷新下拉框
        refreshOperatorCombo();
    }

    // 添加用户对话框
    private void showAddUserDialog() {
        Dialog<StudentEntity> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details");

        // 创建表单
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

        // 添加按钮
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // 结果转换器
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

        // 处理结果
        // 处理结果
        dialog.showAndWait().ifPresent(student -> {  // 这里应该是 student，不是 operator
            DBHelper.addStudent(student);  // 改为添加学生
            loadStudentData();  // 刷新学生表格
            new Alert(Alert.AlertType.INFORMATION, "Student added successfully").show();
        });
    }

    // 添加操作员对话框
    private void showAddOperatorDialog() {
        Dialog<OperatorEntity> dialog = new Dialog<>();
        dialog.setTitle("Add New Operator");
        dialog.setHeaderText("Enter operator details");

        // 创建表单
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

        // 添加按钮
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // 结果转换器 - 修复验证逻辑
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    // 验证必填字段
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

        // 处理结果 - 添加实际保存逻辑
        dialog.showAndWait().ifPresent(operator -> {
            DBHelper.addOperator(operator);
            loadOperatorData(); // 刷新操作员表格

            // 修复：添加下拉框刷新
            refreshOperatorCombo();
            new Alert(Alert.AlertType.INFORMATION, "Operator added successfully").show();
        });
    }

    // 删除选中的用户
    private void deleteSelectedUser() {
        StudentEntity selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DBHelper.deleteStudent(selected.getName());
            loadStudentData();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete").show();
        }
    }

    // 删除选中的操作员
    private void deleteSelectedOperator() {
        OperatorEntity selected = operatorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // 确认对话框
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Operator");
            confirm.setContentText("Are you sure you want to delete operator: " + selected.getName() + "?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    DBHelper.deleteOperator(selected.getAccount());
                    loadOperatorData(); // 刷新操作员表格

                    // 修复：添加下拉框刷新
                    refreshOperatorCombo();
                    new Alert(Alert.AlertType.INFORMATION, "Operator deleted successfully").show();
                }
            });
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an operator to delete").show();
        }
    }


}