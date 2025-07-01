package studentbooking.applications;


import javafx.geometry.Pos;
import studentbooking.db.DBHelper;
import studentbooking.bean.OrdersEntity;
import studentbooking.bean.StudentEntity;
import studentbooking.bean.TicketInfoEntity;
import studentbooking.bean.TrainInfoEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectTicket extends Application {

    private StudentEntity studentEntity;
    private TableView<TicketInfoEntity> tableView = new TableView<>();
    private final String defaultStart = "Departure";
    private final String defaultEnd = "Destination";
    private Label routeLabel = new Label(defaultStart + " ——> " + defaultEnd);
    private final ObservableList<TicketInfoEntity> ticketData = FXCollections.observableArrayList();

    public SelectTicket() {
        this.studentEntity = new StudentEntity();
        this.studentEntity.setName("default");
    }

    public SelectTicket(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    @Override
    public void start(Stage stage) {
        // Creating a Master Layout
        BorderPane mainLayout = new BorderPane();

        // top heading
        HBox header = createHeader();
        mainLayout.setTop(header);

        // Left User Panel
        GridPane userPanel = createUserPanel();
        mainLayout.setLeft(userPanel);

        // Central content area
        GridPane centerPanel = createCenterPanel();
        mainLayout.setCenter(centerPanel);

        // Configuring scenes and stages
        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Train Ticket Booking System");
        stage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10, 20, 35, 20));
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #f0f0f0;");

        Text title = new Text("Train Ticket Booking System");
        title.setFill(Color.valueOf("#0795F4"));
        title.setFont(Font.font(35));

        Reflection reflection = new Reflection();
        reflection.setFraction(0.7f);
        title.setEffect(reflection);

        header.getChildren().add(title);
        return header;
    }

    private GridPane createUserPanel() {
        GridPane userPanel = new GridPane();
        userPanel.setHgap(10);
        userPanel.setVgap(10);
        userPanel.setPadding(new Insets(10, 20, 0, 10));

        // user information
        Text userInfo = new Text("User: " + studentEntity.getName());
        userInfo.setFont(Font.font(20));
        userPanel.add(userInfo, 1, 0);

        Text universityInfo = new Text("University: " + studentEntity.getUniversity());
        universityInfo.setFont(Font.font(20));
        userPanel.add(universityInfo, 1, 1);

        // search box
        TextField departureField = new TextField();
        departureField.setPromptText("Departure");
        userPanel.add(departureField, 1, 2);

        TextField destinationField = new TextField();
        destinationField.setPromptText("Destination");
        userPanel.add(destinationField, 1, 3);

        // buttons
        Button searchButton = new Button("Search Tickets");
        searchButton.getStyleClass().add("button1");
        userPanel.add(searchButton, 1, 5);

        Button viewOrdersButton = new Button("View My Orders");
        viewOrdersButton.getStyleClass().add("button2");
        userPanel.add(viewOrdersButton, 1, 7);

        // Button Event Handling
        searchButton.setOnAction(e -> {
            String departure = departureField.getText().trim();
            String destination = destinationField.getText().trim();

            if (departure.isEmpty()) departure = defaultStart;
            if (destination.isEmpty()) destination = defaultEnd;

            routeLabel.setText(departure + " ——> " + destination);
            searchTickets(departure, destination);
        });

        viewOrdersButton.setOnAction(e -> {
            loadUserOrders();
        });

        return userPanel;
    }

    private GridPane createCenterPanel() {
        GridPane centerPanel = new GridPane();
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setHgap(10);
        centerPanel.setVgap(10);
        centerPanel.setPadding(new Insets(10));

        // route label
        routeLabel.getStyleClass().add("label1");
        centerPanel.add(routeLabel, 0, 0);

        // Operation buttons
        Button bookButton = new Button("Book Selected");
        bookButton.getStyleClass().add("button3");
        centerPanel.add(bookButton, 1, 0);

        Button cancelButton = new Button("Cancel Selected");
        cancelButton.getStyleClass().add("button3");
        centerPanel.add(cancelButton, 2, 0);

        // Ticket Form
        setupTicketTable();
        centerPanel.add(tableView, 0, 1, 3, 1);

        // button event
        bookButton.setOnAction(e -> bookSelectedTickets());
        cancelButton.setOnAction(e -> cancelSelectedTickets());

        return centerPanel;
    }

    private void setupTicketTable() {
        tableView.setEditable(true);

        // Creating Columns
        TableColumn<TicketInfoEntity, String> trainCol = new TableColumn<>("Train");
        trainCol.setCellValueFactory(new PropertyValueFactory<>("trainName"));

        TableColumn<TicketInfoEntity, String> fromCol = new TableColumn<>("From");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("startPlace"));

        TableColumn<TicketInfoEntity, String> toCol = new TableColumn<>("To");
        toCol.setCellValueFactory(new PropertyValueFactory<>("endPlace"));

        TableColumn<TicketInfoEntity, String> departCol = new TableColumn<>("Departure");
        departCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<TicketInfoEntity, String> arriveCol = new TableColumn<>("Arrival");
        arriveCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<TicketInfoEntity, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("ticketType"));

        TableColumn<TicketInfoEntity, Integer> seatsCol = new TableColumn<>("Seats");
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("remainTickets"));

        TableColumn<TicketInfoEntity, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("fare"));

        // Adding columns to a table
        tableView.getColumns().addAll(trainCol, fromCol, toCol, departCol, arriveCol, typeCol, seatsCol, priceCol);
        tableView.setItems(ticketData);
    }

    private void searchTickets(String departure, String destination) {
        ticketData.clear();
        List<TrainInfoEntity> allTrainInfo = DBHelper.getAllTrainInfos();
        List<OrdersEntity> allOrders = DBHelper.getAllOrders();

        for (TrainInfoEntity departureInfo : allTrainInfo) {
            if (departureInfo.getStationName().equals(departure)) {
                for (TrainInfoEntity arrivalInfo : allTrainInfo) {
                    if (arrivalInfo.getStationName().equals(destination) &&
                            arrivalInfo.getTrainName().equals(departureInfo.getTrainName()) &&
                            arrivalInfo.getNum() > departureInfo.getNum()) {

                        // 创建车票信息
                        TicketInfoEntity ticket = new TicketInfoEntity();
                        ticket.setTrainName(departureInfo.getTrainName());
                        ticket.setStartPlace(departure);
                        ticket.setEndPlace(destination);
                        ticket.setStartTime(departureInfo.getStartTime());
                        ticket.setEndTime(arrivalInfo.getArriveTime());

                        // Determine the type of ticket
                        ticket.setTicketType(studentEntity.getAddress().contains(destination) ?
                                "Student Ticket" : "Adult Ticket");

                        // Calculate price
                        float price = arrivalInfo.getFare() - departureInfo.getFare() + 13.5f;
                        ticket.setFare(price);

                        // Calculation of remaining tickets
                        int availableSeats = 300; // Initial seating capacity
                        for (OrdersEntity order : allOrders) {
                            if (order.getTrainName().equals(ticket.getTrainName()) &&
                                    order.getStartPlace().equals(departure) &&
                                    order.getEndPlace().equals(destination)) {
                                availableSeats = Math.min(availableSeats, order.getRemainTickets());
                            }
                        }
                        ticket.setRemainTickets(availableSeats);

                        ticketData.add(ticket);
                    }
                }
            }
        }
    }

    private void loadUserOrders() {
        ticketData.clear();
        List<OrdersEntity> userOrders = DBHelper.getOrdersByName(studentEntity.getName());

        for (OrdersEntity order : userOrders) {
            TicketInfoEntity ticket = new TicketInfoEntity();
            ticket.setOrderNum(order.getOrderNum());
            ticket.setTrainName(order.getTrainName());
            ticket.setStartPlace(order.getStartPlace());
            ticket.setEndPlace(order.getEndPlace());
            ticket.setStartTime(order.getStartTime());
            ticket.setEndTime(order.getEndTime());
            ticket.setTicketType(order.getTicketType());
            ticket.setRemainTickets(order.getRemainTickets());
            ticket.setFare(order.getFare());
            ticketData.add(ticket);
        }
    }

    private void bookSelectedTickets() {
        for (TicketInfoEntity ticket : ticketData) {
            if (ticket.getIsIsSelected()) {
                // Create an order
                OrdersEntity order = new OrdersEntity();
                order.setOrderNum(generateOrderNumber());
                order.setName(studentEntity.getName());
                order.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                order.setIspaid("No");
                order.setTrainName(ticket.getTrainName());
                order.setStartPlace(ticket.getStartPlace());
                order.setEndPlace(ticket.getEndPlace());
                order.setStartTime(ticket.getStartTime());
                order.setEndTime(ticket.getEndTime());
                order.setTicketType(ticket.getTicketType());
                order.setRemainTickets(ticket.getRemainTickets() - 1);
                order.setFare(ticket.getFare());

                // Save Order
                DBHelper.addOrder(order);

                // Updated Remaining Tickets
                ticket.setRemainTickets(order.getRemainTickets());
            }
        }
        tableView.refresh();
    }

    private void cancelSelectedTickets() {
        List<TicketInfoEntity> toRemove = new ArrayList<>();
        for (TicketInfoEntity ticket : ticketData) {
            if (ticket.getIsIsSelected() && ticket.getOrderNum() != null) {
                DBHelper.removeOrder(ticket.getOrderNum());
                toRemove.add(ticket);
            }
        }
        ticketData.removeAll(toRemove);
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
}