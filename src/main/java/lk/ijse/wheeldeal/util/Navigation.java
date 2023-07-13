package lk.ijse.wheeldeal.util;

import animatefx.animation.Pulse;
import animatefx.animation.SlideInLeft;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Navigation {
    public static void navigateToDashBoard(AnchorPane root) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(Navigation.class.getResource("/view/dashboard_form.fxml")));
        new Pulse(anchorPane).play();
        Scene scene = new Scene(anchorPane);
        Stage dashboard = (Stage) root.getScene().getWindow();
        dashboard.setScene(scene);
        dashboard.setTitle("WheelDeal Auto Rental");
        dashboard.centerOnScreen();
        dashboard.setResizable(false);
    }

    public static void setWindow(AnchorPane root, String fxml) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(Navigation.class.getResource("/view/"+fxml)));
        new SlideInLeft(anchorPane).play();
        Scene scene = new Scene(anchorPane);
        Stage window = (Stage) root.getScene().getWindow();
        window.setScene(scene);
        window.centerOnScreen();
        window.setResizable(false);
        switch (fxml) {
            case "employee_form.fxml" -> window.setTitle("Employee Manage");
            case "user_form.fxml" -> window.setTitle("User Manage");
            case "membership_form.fxml" -> window.setTitle("Membership Manage");
            case "customer_form.fxml" -> window.setTitle("Customer Manage");
            case "driver_form.fxml" -> window.setTitle("Driver Manage");
            case "vehicle_form.fxml" -> window.setTitle("Vehicle Manage");
            case "ride_form.fxml" -> window.setTitle("Ride Manage");
            case "return_form.fxml" -> window.setTitle("Return Manage");
            default -> window.setTitle("WheelDeal Auto Rental");
        }
    }
}