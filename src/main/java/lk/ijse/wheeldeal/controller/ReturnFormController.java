package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.dto.*;
import lk.ijse.wheeldeal.dto.tm.ReturnTM;
import lk.ijse.wheeldeal.model.*;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnCalCost;

    @FXML
    private JFXButton btnViewBill;

    @FXML
    private Label lblInvalidRideNo;

    @FXML
    private Label lblInvalidDistance;

    @FXML
    private Label lblInvalidDate;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colDistance;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private TableColumn<?, ?> colReturnNo;

    @FXML
    private TableColumn<?, ?> colRideNo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblNextReturnNo;

    @FXML
    private TableView<ReturnTM> tblReturns;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtReturnNo;

    @FXML
    private JFXComboBox<String> cmbRideNo;

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

    @FXML
    private Label lblClock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        generateNextReturnNo();
        setCellValueFactory();
        getLast20Returns();
        loadRideNos();
        setLastBill();
        setToday();
    }

    private void setCellValueFactory() {
        colReturnNo.setCellValueFactory(new PropertyValueFactory<>("ReturnNo"));
        colRideNo.setCellValueFactory(new PropertyValueFactory<>("RideNo"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("ReDate"));
    }

    private void generateNextReturnNo() {
        try {
            String nextNo = ReturnModel.generateNextReturnNo();
            lblNextReturnNo.setText(nextNo);
            txtReturnNo.setText(nextNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadRideNos() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> rideNos = RideModel.getPendingRideNos();
            for (String no : rideNos) {
                obList.add(no);
            }
            cmbRideNo.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }
    private void setToday() {
        datePicker.setValue(LocalDate.now());
    }

    private void getLast20Returns() {
        try {
            ObservableList<ReturnTM> obList = FXCollections.observableArrayList();
            List<Return> returnList = ReturnModel.getLast20Returns();

            for(Return aReturn : returnList) {
                obList.add(new ReturnTM(
                        aReturn.getReturnNo(),
                        aReturn.getRideNo(),
                        aReturn.getDistance(),
                        aReturn.getCost(),
                        aReturn.getReDate()
                ));
            }
            tblReturns.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    private void setDefaultWarnings() {
        lblInvalidRideNo.setVisible(false);
        lblInvalidDistance.setVisible(false);
        lblInvalidDate.setVisible(false);
    }

    public void cmbRideNoOnAction(ActionEvent actionEvent) throws IOException {
        if (checkMultiple() && txtCost.getText().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/multiple_return_manage_form.fxml"));
            Parent root = loader.load();
            MultipleReturnManageFormController multipleReturnManageFormController = loader.getController();
            multipleReturnManageFormController.setDate(datePicker.getValue());
            multipleReturnManageFormController.getAllVehicles(cmbRideNo.getValue(), txtReturnNo.getText());
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            new FadeInUp(root).play();
            stage.setScene(scene);
            stage.setTitle("Manage Multiple Returns");
            stage.getIcons().add(new Image("asset/icons/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

        } else if (checkMultiple() && !txtCost.getText().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/multiple_return_view_form.fxml"));
            Parent root = loader.load();
            MultipleReturnViewFormController multipleReturnViewFormController = loader.getController();
            multipleReturnViewFormController.setAll(cmbRideNo.getValue(), txtReturnNo.getText(), datePicker.getValue());
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            new FadeInUp(root).play();
            stage.setScene(scene);
            stage.setTitle("View Multiple Returns");
            stage.getIcons().add(new Image("asset/icons/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

        }else{ txtDistance.requestFocus(); }
    }
    public boolean checkMultiple(){
        int vehiCount;
        try {
            vehiCount = RideVehicleModel.getCount(cmbRideNo.getValue());
            if (vehiCount > 1){return true;}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void txtDistanceOnAction(ActionEvent actionEvent) {
        btnCalCost.requestFocus();
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblReturns.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        cmbRideNo.setValue(null);
        txtDistance.clear();
        txtCost.clear();
        generateNextReturnNo();
        setDefaultWarnings();
        getLast20Returns();
        loadRideNos();
        setLastBill();
        cmbRideNo.requestFocus();
    }
    public void btnCalCostOnAction(ActionEvent actionEvent) {
        if (txtCost.getText().isEmpty()) {
            setDefaultWarnings();
            if (cmbRideNo.getValue() != null && !txtDistance.getText().isEmpty()) {
                boolean isRideNoMatched = RegExPatterns.getRideNoPattern().matcher(cmbRideNo.getValue()).matches();
                boolean isDistanceMatched = RegExPatterns.getDistancePattern().matcher(txtDistance.getText()).matches();
                String rideNo;
                double distance;
                double costPerKM = -1;
                double discount = -1;
                if (isRideNoMatched) {
                    if (isDistanceMatched) {
                        rideNo = cmbRideNo.getValue();
                        distance = Double.parseDouble(txtDistance.getText());
                        try {
                            costPerKM = RideVehicleModel.getVehiCostPerKM(rideNo);
                            discount = RideModel.getCustomerDiscount(rideNo);
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "Something went wrong\n" + e).show();
                        }
                        if (costPerKM != -1 && discount != -1) {
                            double cost = distance * costPerKM - (distance * costPerKM * discount / 100);
                            txtCost.setText(String.valueOf(cost));
                        }
                    } else {
                        setDefaultWarnings();
                        lblInvalidDistance.setVisible(true);
                        txtDistance.requestFocus();
                    }
                } else {
                    setDefaultWarnings();
                    lblInvalidRideNo.setVisible(true);
                    cmbRideNo.requestFocus();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter Ride No & Distance").show();
            }
        }else{ btnViewBill.requestFocus(); }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(cmbRideNo.getValue() != null && !txtDistance.getText().isEmpty() && datePicker.getValue() != null ) {
            boolean isRideNoMatched = RegExPatterns.getRideNoPattern().matcher(cmbRideNo.getValue()).matches();
            boolean isDistanceMatched = RegExPatterns.getDistancePattern().matcher(txtDistance.getText()).matches();
            boolean isDateValid = datePicker.getValue().isEqual(LocalDate.now());
            boolean isCostValid = !txtCost.getText().isEmpty();
            if(isRideNoMatched){
                if(isDistanceMatched){
                    if(isDateValid){
                        if(isCostValid){
                            String returnNo = txtReturnNo.getText();
                            String rideNo = cmbRideNo.getValue();
                            LocalDate reDate = datePicker.getValue();
                            double distance = Double.parseDouble(txtDistance.getText());
                            double cost = Double.parseDouble(txtCost.getText());
                            boolean added;
                            try {
                                added = ReturnModel.addReturn(returnNo, rideNo, distance, cost, reDate);
                                if(added){
                                    new Alert(Alert.AlertType.INFORMATION, "Return added successfully..").showAndWait();
                                    btnViewBillOnAction(actionEvent);
                                    btnResetOnAction(actionEvent);
                                }
                            } catch (SQLException | IOException e) { new Alert(Alert.AlertType.ERROR, "Something went wrong\n"+e).show(); }
                        }else { new Alert(Alert.AlertType.ERROR, "Cost cannot be empty:\nClick 'Cal.Cost' to get cost").show(); btnCalCost.requestFocus(); }
                    }else { setDefaultWarnings(); lblInvalidDate.setVisible(true); datePicker.requestFocus(); }
                }else { setDefaultWarnings(); lblInvalidDistance.setVisible(true); txtDistance.requestFocus(); }
            }else { setDefaultWarnings(); lblInvalidRideNo.setVisible(true); cmbRideNo.requestFocus(); }
        }else { new Alert(Alert.AlertType.ERROR, "Field cannot be empty:").show(); }
    }

    public void btnViewBillOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtReturnNo.getText().isEmpty()) {
            boolean returned = false;
            try{
                returned = ReturnModel.isReturned(txtReturnNo.getText());
            }catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong.\n"+e).show();
            }
            if(returned){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bill_form.fxml"));
                Parent root = loader.load();
                BillFormController billFormController = loader.getController();
                billFormController.setLabels(txtReturnNo.getText());
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                new FadeInUp(root).play();
                stage.setScene(scene);
                stage.setTitle("Invoice");
                stage.getIcons().add(new Image("asset/icons/icon.png"));
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();
            }else{ new Alert(Alert.AlertType.ERROR, "Return not saved:\nFirst save the return using the 'save' button\nto view the bill").show(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Return_No cannot be empty:").show(); }
    }

    public void tblReturnsOnAction(MouseEvent event) {
        tblReturns.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtReturnNo.setText(newSelection.getReturnNo());
                cmbRideNo.setValue(newSelection.getRideNo());
                txtDistance.setText(newSelection.getDistance().toString());
                txtCost.setText(newSelection.getCost().toString());
                datePicker.setValue(LocalDate.parse(newSelection.getReDate()));
            }
        });
    }

    public void setLastBill() {
        try {
            String returnNo = ReturnModel.getLastReturnNo();
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

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigateToDashBoard(root);
    }
}