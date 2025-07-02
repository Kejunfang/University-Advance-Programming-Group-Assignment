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
    private TableView<TicketEntity> tableView = new TableView<>();
    private final ObservableList<TicketEntity> ticketData = FXCollections.observableArrayList();

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
            ticketData.add(ticket);

            issueTypeField.clear();
            descriptionArea.clear();
        });

        leftPanel.getChildren().addAll(newTicketLabel, issueTypeField, descriptionArea, submitButton);
        mainLayout.setLeft(leftPanel);

        // 中央面板 - 工单列表 (扩大表格区域)
        setupTicketTable();
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        mainLayout.setCenter(scrollPane);

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
        Scene scene = new Scene(mainLayout, 1000, 700); // 从 900x600 增大到 1000x700
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Support Ticket System - " + studentEntity.getName());
        stage.show();
    }

    private void setupTicketTable() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 设置表格最小高度以占据更多空间
        tableView.setMinHeight(400);
        tableView.setPrefHeight(500);

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        idCol.setMinWidth(120); // 增加列宽

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));
        typeCol.setMinWidth(150); // 增加列宽

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setMinWidth(100); // 增加列宽

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        createdCol.setMinWidth(150); // 增加列宽

        TableColumn<TicketEntity, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        updatedCol.setMinWidth(150); // 增加列宽

        tableView.getColumns().addAll(idCol, typeCol, statusCol, createdCol, updatedCol);
        tableView.setItems(ticketData);
    }

    private void loadUserTickets() {
        ticketData.clear();
        for (TicketEntity ticket : DBHelper.getAllTickets()) {
            if (ticket.getSubmittedBy().equals(studentEntity.getName())) {
                ticketData.add(ticket);
            }
        }
    }
}