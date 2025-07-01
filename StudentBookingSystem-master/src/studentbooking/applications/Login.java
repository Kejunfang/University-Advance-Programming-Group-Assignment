package studentbooking.applications;

import studentbooking.db.DBHelper;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.StudentEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.geometry.HPos.LEFT;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Train Ticket Booking System");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 40, 25));

        Text scenetitle = new Text("Welcome！");
        scenetitle.setFont(Font.font(22));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("   Username:");
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label pw = new Label("      Password:");
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

        Label lt = new Label("Login Options:");
        grid.add(lt, 0, 4);

        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("User","Admin"));
        choiceBox.setValue("User");
        grid.add(choiceBox,1,4);

        Button btn = new Button("  Login  ");
        btn.getStyleClass().add("button5");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        grid.setColumnSpan(actiontarget, 2);
        grid.setHalignment(actiontarget, LEFT);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String account = userTextField.getText().trim();
                String password = pwBox.getText().trim();
                String choice = choiceBox.getValue().toString();

                if (choice.equals("User")) {
                    StudentEntity studentEntity = DBHelper.findStudent(account, password);
                    if (studentEntity == null) {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("    Incorrect account number or password！");
                        return;
                    }

                    try {
                        primaryStage.close();
                        newState(primaryStage, studentEntity);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (choice.equals("Operator")) {
                    OperatorEntity operatorEntity = DBHelper.findOperator(account, password);
                    if (operatorEntity == null) {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("    Incorrect work number or password！");
                        return;
                    }

                    try {
                        primaryStage.close();
                        newOperatorState(primaryStage, operatorEntity);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        Scene scene = new Scene(grid, 350, 290);
        scene.getStylesheets().add("studentbooking/css/button.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void newOperatorState(Stage primaryStage, OperatorEntity operatorEntity) throws IOException {
        SelectTicketForOperator selectTicketForOperator = new SelectTicketForOperator(operatorEntity);
        selectTicketForOperator.start(primaryStage);
    }

    public void newState(Stage primaryStage, StudentEntity studentEntity) throws IOException {
        SelectTicket selectTicket = new SelectTicket(studentEntity);
        selectTicket.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}