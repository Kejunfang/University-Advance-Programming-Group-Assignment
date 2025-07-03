package studentbooking.applications;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.StudentEntity;
import studentbooking.db.DBHelper;

public class MainLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        DBHelper.initializeDataFiles();

        primaryStage.setTitle("Train Ticket System - Login");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 40, 25));

        Text scenetitle = new Text("Welcome to Ticket System");
        scenetitle.setFont(Font.font(22));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username/Student ID:");
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

        Label roleLabel = new Label("Login As:");
        grid.add(roleLabel, 0, 4);

        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("User", "Operator", "Admin"));
        choiceBox.setValue("User");
        grid.add(choiceBox, 1, 4);

        Button btn = new Button("Login");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6, 2, 1);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String account = userTextField.getText().trim();
                String password = pwBox.getText().trim();
                String choice = choiceBox.getValue();

                if (account.isEmpty() || password.isEmpty()) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Please enter both username and password");
                    return;
                }

// Replacing existing User login logic
                if (choice.equals("User")) {
                    // First, check if the username exists
                    if (!DBHelper.isStudentIdentifierExist(account)) {
                        // Automatic registration of new users
                        DBHelper.addStudent(account, password);

                        // Getting Newly Enrolled Students
                        StudentEntity newStudent = DBHelper.findStudent(account, password);

                        if (newStudent != null) {
                            try {
                                actiontarget.setFill(Color.GREEN);
                                actiontarget.setText("Account created and logged in successfully!");

                                // Access to the system after a delay
                                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                pause.setOnFinished(event -> {
                                    try {
                                        UserPage userApp = new UserPage(newStudent);
                                        primaryStage.close();
                                        userApp.start(new Stage());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                });
                                pause.play();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Failed to create new account.");
                        }
                    } else {
                        // User name exists, verify password
                        StudentEntity student = DBHelper.findStudent(account, password);
                        if (student != null) {
                            try {
                                UserPage userApp = new UserPage(student);
                                primaryStage.close();
                                userApp.start(new Stage());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Invalid password.");
                        }
                    }
                } else if (choice.equals("Operator")) {
                    OperatorEntity operator = DBHelper.findOperator(account, password);
                    if (operator != null) {
                        try {
                            OperatorPage operatorApp = new OperatorPage(operator);
                            primaryStage.close();
                            operatorApp.start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Invalid operator credentials.");
                    }
                } else if (choice.equals("Admin")) {
                    if ("admin".equals(account) && "admin123".equals(password)) {
                        try {
                            AdminDashboard adminApp = new AdminDashboard();
                            primaryStage.close();
                            adminApp.start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Invalid admin credentials.");
                    }
                }
            }
        });

        Scene scene = new Scene(grid, 400, 320);
        // Fix CSS path loading issue
        try {
            scene.getStylesheets().add(getClass().getResource("/button.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load CSS: " + e.getMessage());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}