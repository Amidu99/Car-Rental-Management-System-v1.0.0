package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
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
import lk.ijse.wheeldeal.model.ReturnModel;
import lk.ijse.wheeldeal.model.RideModel;
import lk.ijse.wheeldeal.model.RideVehicleModel;
import lk.ijse.wheeldeal.model.VehicleModel;
import lk.ijse.wheeldeal.util.RegExPatterns;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MultipleReturnManageFormController {
    @FXML
    private JFXComboBox<String> cmbVehiNo;

    @FXML
    private TextField txtDistance;

    @FXML
    private Label lblRideNo;

    @FXML
    private Label lblReturnNo;

    @FXML
    private Label lblTotalDistance;

    @FXML
    private Label lblSubCost;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblInvalidDistance;

    @FXML
    private DatePicker datePicker;

    private static double totalDistance;
    private static double subCost;
    private static double totalCost;

    private final ObservableList<String> obList = FXCollections.observableArrayList();

    public void getAllVehicles(String rideNo, String returnNo) {
        try {
            List<String> vehiNos = RideVehicleModel.getVehiNos(rideNo);
            obList.addAll(vehiNos);
            cmbVehiNo.setItems(obList);
            lblRideNo.setText(rideNo);
            lblReturnNo.setText(returnNo);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    public void setDate(LocalDate date) {
        datePicker.setValue(date);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        lblInvalidDistance.setVisible(false);
        if( cmbVehiNo.getValue() != null && !txtDistance.getText().isEmpty()){
            boolean isDistanceMatched = RegExPatterns.getDistancePattern().matcher(txtDistance.getText()).matches();
            if(isDistanceMatched){
                String returnNo = lblReturnNo.getText();
                String rideNo = lblRideNo.getText();
                String vehiNo = cmbVehiNo.getValue();
                double distance = Double.parseDouble(txtDistance.getText());
                double costPerKm;
                double discount;
                boolean addDistance;
                try {
                     addDistance = RideVehicleModel.addDistance(rideNo, vehiNo, distance);

                     if(addDistance){
                         totalDistance = totalDistance + distance;
                         lblTotalDistance.setText(totalDistance+" Km");

                         costPerKm = VehicleModel.getVehiCostPerKM(vehiNo);
                         subCost = subCost + (distance * costPerKm);
                         lblSubCost.setText("Rs. "+subCost);

                         discount = RideModel.getCustomerDiscount(rideNo);
                         totalCost = totalCost + (distance * costPerKm) - (distance * costPerKm * discount / 100);
                         lblCost.setText("Rs. "+totalCost);

                         JOptionPane.showMessageDialog(null, "Distance added successfully!");

                         previewBill(rideNo, returnNo, vehiNo, distance);
                     }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }

                obList.remove(cmbVehiNo.getValue());
                if(obList.isEmpty()) {
                    boolean added;
                    try {
                        added = ReturnModel.addReturn(returnNo, rideNo, totalDistance, totalCost, LocalDate.now());
                        if (added) { new Alert(Alert.AlertType.INFORMATION, "Return added successfully..").showAndWait(); }
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                }
            }else { lblInvalidDistance.setVisible(true); txtDistance.requestFocus(); }
        }
    }

    private void previewBill(String rideNo, String returnNo, String vehiNo, double distance) throws IOException {
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
    }

    public void cmbVehiNoOnAction(ActionEvent actionEvent) {
        txtDistance.requestFocus();
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}