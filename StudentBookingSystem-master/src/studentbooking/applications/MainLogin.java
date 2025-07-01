package studentbooking.applications;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos; // 添加 HPos 的导入
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import studentbooking.bean.OperatorEntity;
import studentbooking.bean.StudentEntity;
import studentbooking.db.DBHelper;

// 静态导入 HPos.LEFT
import java.io.File;

import static javafx.geometry.HPos.LEFT;

public class MainLogin extends Application {

    @Override
    public void start(Stage primaryStage) {

        System.out.println("正在初始化数据文件..."); // 调试输出
        File ordersFile = new File("data/orders.txt");
        System.out.println("orders.txt 是否存在: " + ordersFile.exists());
        // 确保数据目录和文件存在
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

        Label userName = new Label("Username:");
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

        Label roleLabel = new Label("Login As:");
        grid.add(roleLabel, 0, 4);

        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("User", "Admin"));
        choiceBox.setValue("User");
        grid.add(choiceBox, 1, 4);

        Button btn = new Button("Login");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        grid.setColumnSpan(actiontarget, 2);
        grid.setHalignment(actiontarget, LEFT); // 这里使用 LEFT
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String account = userTextField.getText().trim();
                String password = pwBox.getText().trim();
                String choice = choiceBox.getValue();

                if (choice.equals("User")) {
                    StudentEntity student = DBHelper.findStudent(account, password);
                    if (student != null) {
                        try {
                            // 启动用户界面
                            SelectTicket userApp = new SelectTicket(student);
                            primaryStage.close();
                            userApp.start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Invalid user credentials.");
                    }
                } else if (choice.equals("Admin")) {
                    OperatorEntity operator = DBHelper.findOperator(account, password);
                    if (operator != null) {
                        try {
                            // 启动管理员界面
                            SelectTicketForOperator adminApp = new SelectTicketForOperator(operator);
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

        Scene scene = new Scene(grid, 380, 300);
        scene.getStylesheets().add(getClass().getResource("/studentbooking/css/button.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}