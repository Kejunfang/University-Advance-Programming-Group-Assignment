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

public class SelectTicket extends Application {

    private StudentEntity studentEntity;
    private TableView<TicketEntity> activeTicketTable = new TableView<>();
    private TableView<TicketEntity> resolvedTicketTable = new TableView<>();
    private final ObservableList<TicketEntity> activeTicketData = FXCollections.observableArrayList();
    private final ObservableList<TicketEntity> resolvedTicketData = FXCollections.observableArrayList();

    public SelectTicket(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    @Override
    public void start(Stage stage) {
        BorderPane mainLayout = new BorderPane();

        // 顶部标题
        Label title = new Label("Customer Support Ticket System");
        title.setFont(Font.font(24));
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);
        mainLayout.setTop(header);

        // 左侧面板 - 创建新工单
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

        // 中央面板 - 两个工单表格
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(15));

        // 活动工单表格
        Label activeTicketsLabel = new Label("Active Tickets");
        activeTicketsLabel.setFont(Font.font(18));
        setupActiveTicketTable();
        VBox activeBox = new VBox(5, activeTicketsLabel, activeTicketTable);
        activeBox.setPadding(new Insets(0, 0, 10, 0));

        // 已解决工单表格
        Label resolvedTicketsLabel = new Label("Resolved Tickets");
        resolvedTicketsLabel.setFont(Font.font(18));
        setupResolvedTicketTable();
        VBox resolvedBox = new VBox(5, resolvedTicketsLabel, resolvedTicketTable);
        resolvedBox.setPadding(new Insets(10, 0, 0, 0));

        centerBox.getChildren().addAll(activeBox, resolvedBox);
        mainLayout.setCenter(centerBox);

        // 底部注销按钮
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

        // 加载用户工单
        loadUserTickets();

        // 增大窗口尺寸以显示更大的表格
        Scene scene = new Scene(mainLayout, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Support Ticket System - " + studentEntity.getName());
        stage.show();
    }

    private void setupActiveTicketTable() {
        activeTicketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        activeTicketTable.setMinHeight(200);
        activeTicketTable.setPrefHeight(250);

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

        // 添加操作员备注列
        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Operator Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setMinWidth(250);

        activeTicketTable.getColumns().addAll(idCol, typeCol, statusCol, createdCol, updatedCol, notesCol);
        activeTicketTable.setItems(activeTicketData);
    }

    private void setupResolvedTicketTable() {
        resolvedTicketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resolvedTicketTable.setMinHeight(200);
        resolvedTicketTable.setPrefHeight(250);

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

        // 添加操作员备注列
        TableColumn<TicketEntity, String> notesCol = new TableColumn<>("Operator Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setMinWidth(250);

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
}