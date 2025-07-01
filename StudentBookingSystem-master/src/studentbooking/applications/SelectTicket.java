// 替换整个类为：
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

        // 中央面板 - 工单列表
        setupTicketTable();
        ScrollPane scrollPane = new ScrollPane(tableView);
        mainLayout.setCenter(scrollPane);

        // 加载用户工单
        loadUserTickets();

        Scene scene = new Scene(mainLayout, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Support Ticket System - " + studentEntity.getName());
        stage.show();
    }

    private void setupTicketTable() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TicketEntity, String> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));

        TableColumn<TicketEntity, String> typeCol = new TableColumn<>("Issue Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));

        TableColumn<TicketEntity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<TicketEntity, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));

        TableColumn<TicketEntity, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        tableView.getColumns().addAll(idCol, typeCol, statusCol, createdCol, updatedCol);
        tableView.setItems(ticketData);
    }

    private void loadUserTickets() {
        for (TicketEntity ticket : DBHelper.getAllTickets()) {
            if (ticket.getSubmittedBy().equals(studentEntity.getName())) {
                ticketData.add(ticket);
            }
        }
    }
}