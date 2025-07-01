package studentbooking.applications;

import studentbooking.db.DBHelper;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.OrdersEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text; // 添加 Text 类的导入
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SelectTicketForOperator extends Application {

    private OperatorEntity operatorEntity;
    private TableView<OrdersEntity> orderTable = new TableView<>();
    private final ObservableList<OrdersEntity> orderData = FXCollections.observableArrayList();

    public SelectTicketForOperator(OperatorEntity operatorEntity) {
        this.operatorEntity = operatorEntity;
    }

    @Override
    public void start(Stage stage) {
        // Creating a Master Layout
        BorderPane mainLayout = new BorderPane();

        // top heading
        HBox header = createHeader();
        mainLayout.setTop(header);

        // Left Operator Panel
        GridPane controlPanel = createControlPanel();
        mainLayout.setLeft(controlPanel);

        // Central Order Form
        setupOrderTable();
        mainLayout.setCenter(orderTable);

        // Configuring scenes and stages
        Scene scene = new Scene(mainLayout, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Operator Dashboard - Train Ticket System");
        stage.show();

        // Load All Orders
        loadAllOrders();
        orderTable.refresh(); // Force Refresh Form
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10, 20, 35, 20));
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #f0f0f0;");

        Text title = new Text("Operator Dashboard");
        title.setFill(Color.valueOf("#FF9913"));
        title.setFont(Font.font(35));

        header.getChildren().add(title);
        return header;
    }

    private GridPane createControlPanel() {
        GridPane panel = new GridPane();
        panel.setHgap(10);
        panel.setVgap(10);
        panel.setPadding(new Insets(10, 20, 0, 10));

        // Operator Information
        Text operatorInfo = new Text("Operator: " + operatorEntity.getName());
        operatorInfo.setFont(Font.font(20));
        panel.add(operatorInfo, 1, 0);

        Text accountInfo = new Text("Account #: " + operatorEntity.getAccount());
        accountInfo.setFont(Font.font(20));
        panel.add(accountInfo, 1, 1);

        // search box
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name");
        panel.add(searchField, 1, 3);

        // buttons
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button1");
        panel.add(searchButton, 1, 4);

        Button showAllButton = new Button("Show All Orders");
        showAllButton.getStyleClass().add("button2");
        panel.add(showAllButton, 1, 5);

        Button cancelButton = new Button("Cancel Selected");
        cancelButton.getStyleClass().add("button3");
        panel.add(cancelButton, 1, 6);

        // pushbutton event
        searchButton.setOnAction(e -> searchOrders(searchField.getText().trim()));
        showAllButton.setOnAction(e -> loadAllOrders());
        cancelButton.setOnAction(e -> cancelSelectedOrders());

        return panel;
    }

    private void setupOrderTable() {
        orderTable.setEditable(true);

        // Creating Columns
        TableColumn<OrdersEntity, String> orderNumCol = new TableColumn<>("Order #");
        orderNumCol.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<OrdersEntity, String> nameCol = new TableColumn<>("Customer");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<OrdersEntity, String> timeCol = new TableColumn<>("Order Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<OrdersEntity, String> trainCol = new TableColumn<>("Train");
        trainCol.setCellValueFactory(new PropertyValueFactory<>("trainName"));

        TableColumn<OrdersEntity, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getStartPlace() + " to " + cellData.getValue().getEndPlace()
                )
        );

        TableColumn<OrdersEntity, String> departCol = new TableColumn<>("Departure");
        departCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<OrdersEntity, String> arriveCol = new TableColumn<>("Arrival");
        arriveCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<OrdersEntity, String> typeCol = new TableColumn<>("Ticket Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("ticketType"));

        TableColumn<OrdersEntity, Integer> seatsCol = new TableColumn<>("Seats");
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("remainTickets"));

        TableColumn<OrdersEntity, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("fare"));



        // Adding columns to a table
        orderTable.getColumns().addAll(
                orderNumCol, nameCol, timeCol, trainCol, routeCol,
                departCol, arriveCol, typeCol, seatsCol, priceCol
        );
        orderTable.setItems(orderData);
    }

    private void loadAllOrders() {
        orderData.clear();
        orderData.addAll(DBHelper.getAllOrders());
    }

    private void searchOrders(String name) {
        orderData.clear();
        if (name.isEmpty()) {
            loadAllOrders();
        } else {
            orderData.addAll(DBHelper.getOrdersByName(name));
        }
    }

    private void cancelSelectedOrders() {
        List<OrdersEntity> toRemove = new ArrayList<>();
        for (OrdersEntity order : orderTable.getSelectionModel().getSelectedItems()) {
            DBHelper.removeOrder(order.getOrderNum());
            toRemove.add(order);
        }
        orderData.removeAll(toRemove);
    }
}