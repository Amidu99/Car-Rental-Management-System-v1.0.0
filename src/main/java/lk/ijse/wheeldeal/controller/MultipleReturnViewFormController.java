package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.model.RideVehicleModel;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MultipleReturnViewFormController {
    @FXML
    private JFXButton btnViewBill;

    @FXML
    private JFXComboBox<String> cmbVehiNo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblReturnNo;

    @FXML
    private Label lblRideNo;

    @FXML
    private TextField txtDistance;

    public void setAll(String rideNo, String returnNo, LocalDate date) {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> vehiNos = RideVehicleModel.getVehiNos(rideNo);
            obList.addAll(vehiNos);
            cmbVehiNo.setItems(obList);
            lblRideNo.setText(rideNo);
            lblReturnNo.setText(returnNo);
            datePicker.setValue(date);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    public void cmbVehiNoOnAction(ActionEvent actionEvent) {
        String rideNo = lblRideNo.getText();
        String vehiNo = cmbVehiNo.getValue();
        double distance=0;
        try {
            distance = RideVehicleModel.getDistance(rideNo, vehiNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtDistance.setText(String.valueOf(distance));
        btnViewBill.requestFocus();
    }

    public void btnViewBillOnAction(ActionEvent actionEvent) throws IOException {
        if (cmbVehiNo.getValue() != null && !txtDistance.getText().isEmpty()) {
            String rideNo = lblRideNo.getText();
            String returnNo = lblReturnNo.getText();
            String vehiNo = cmbVehiNo.getValue();
            double distance = Double.parseDouble(txtDistance.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bill_form.fxml"));
            Parent root = loader.load();
            BillFormController billFormController = loader.getController();
            billFormController.setBill(rideNo, returnNo, vehiNo, distance);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            new FadeInUp(root).play();
            stage.setScene(scene);
            stage.setTitle("Invoice");
            stage.getIcons().add(new Image("asset/icons/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        }else { new Alert(Alert.AlertType.INFORMATION, "Select a vehicle to view bill").show(); cmbVehiNo.requestFocus(); }
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}