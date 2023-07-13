package lk.ijse.wheeldeal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.dto.*;
import lk.ijse.wheeldeal.model.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class BillFormController {
    @FXML
    private AnchorPane billPane;

    @FXML
    private Label lblCostPerKM;

    @FXML
    private Label lblCustName;

    @FXML
    private Label lblDiscountPrice;

    @FXML
    private Label lblDiscountRate;

    @FXML
    private Label lblDistance;

    @FXML
    private Label lblMembership;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblRideDate;

    @FXML
    private Label lblRideNo;

    @FXML
    private Label lblSubTotal;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblVehicleModel;

    @FXML
    private Label lblVehicleNo;

    public void setLabels(String returnNo) {
        try {
            Return returnSet = ReturnModel.getReturn(returnNo);
            assert returnSet != null;
            String rideNo = returnSet.getRideNo();
            double distance = returnSet.getDistance();
            String returnDate = returnSet.getReDate();

            Ride rideSet = RideModel.getRide(rideNo);
            assert rideSet != null;
            String custID = rideSet.getCustID();
            String rideDate = rideSet.getRideDate();

            Customer customerSet = CustomerModel.getCustomer(custID);
            assert customerSet != null;
            String custName = customerSet.getName();
            String membCode = customerSet.getMembCode();

            Membership membershipSet = MembershipModel.getMembership(membCode);
            assert membershipSet != null;
            String membType = membershipSet.getType();
            double discountRate = membershipSet.getDiscount();

            String vehiNo = RideVehicleModel.getVehicle(rideNo);

            Vehicle vehicleSet = VehicleModel.getVehicle(vehiNo);
            assert vehicleSet != null;
            String vehiModel = vehicleSet.getModel();
            double costPerKM = vehicleSet.getCostPerKM();

            double subTotal = distance * costPerKM;
            double discountPrice = subTotal/100 * discountRate;
            double total = subTotal - discountPrice;

            lblRideNo.setText(rideNo);
            lblRideDate.setText(rideDate);
            lblReturnDate.setText(returnDate);
            lblCustName.setText(custName);
            lblMembership.setText(membType);
            lblDiscountRate.setText(""+discountRate+"%");
            lblVehicleModel.setText(vehiModel);
            lblCostPerKM.setText("Rs. "+costPerKM);
            lblVehicleNo.setText(vehiNo);
            lblDistance.setText(""+distance+" Km");
            lblSubTotal.setText("Rs. "+subTotal);
            lblDiscountPrice.setText("Rs. "+discountPrice);
            lblTotal.setText("Rs. "+total);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong\n"+e).show();
        }
    }

    public void setBill(String rideNo, String returnNo, String vehiNo, double distance) {
        try {
            String returnDate = LocalDate.now().toString();
            String date = ReturnModel.getDate(returnNo);
            if(date != null) {
                returnDate = date;
            }

            Ride rideSet = RideModel.getRide(rideNo);
            assert rideSet != null;
            String custID = rideSet.getCustID();
            String rideDate = rideSet.getRideDate();

            Customer customerSet = CustomerModel.getCustomer(custID);
            assert customerSet != null;
            String custName = customerSet.getName();
            String membCode = customerSet.getMembCode();

            Membership membershipSet = MembershipModel.getMembership(membCode);
            assert membershipSet != null;
            String membType = membershipSet.getType();
            double discountRate = membershipSet.getDiscount();

            Vehicle vehicleSet = VehicleModel.getVehicle(vehiNo);
            assert vehicleSet != null;
            String vehiModel = vehicleSet.getModel();
            double costPerKM = vehicleSet.getCostPerKM();

            double subTotal = distance * costPerKM;
            double discountPrice = subTotal/100 * discountRate;
            double total = subTotal - discountPrice;

            lblRideNo.setText(rideNo);
            lblRideDate.setText(rideDate);
            lblReturnDate.setText(returnDate);
            lblCustName.setText(custName);
            lblMembership.setText(membType);
            lblDiscountRate.setText(""+discountRate+"%");
            lblVehicleModel.setText(vehiModel);
            lblCostPerKM.setText("Rs. "+costPerKM);
            lblVehicleNo.setText(vehiNo);
            lblDistance.setText(""+distance+" Km");
            lblSubTotal.setText("Rs. "+subTotal);
            lblDiscountPrice.setText("Rs. "+discountPrice);
            lblTotal.setText("Rs. "+total);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong\n"+e).show();
        }
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
            job.printPage(billPane);
            new Alert(Alert.AlertType.INFORMATION, "Successfully print the bill..").show();
            job.endJob();
        }
    }
}