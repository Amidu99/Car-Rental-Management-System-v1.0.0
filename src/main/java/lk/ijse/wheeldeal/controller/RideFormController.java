package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.dto.Ride;
import lk.ijse.wheeldeal.dto.tm.RideTM;
import lk.ijse.wheeldeal.model.*;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RideFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lblRideNo;

    @FXML
    private Label lblInvalidRideNo;

    @FXML
    private Label lblInvalidCustID;

    @FXML
    private Button btnSetDriver;

    @FXML
    private Button btnDelete;

    @FXML
    private JFXComboBox<String> cmbCustID;

    @FXML
    private TableView<RideTM> tblRide;

    @FXML
    private TableColumn<?, ?> colCustId;

    @FXML
    private TableColumn<?, ?> colRideDate;

    @FXML
    private TableColumn<?, ?> colRideNo;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField txtRideNo;

    @FXML
    private Label lblCustName;

    @FXML
    private Label lblMembership;

    @FXML
    private Label lblRideDate;

    @FXML
    private Label lblRideNo1;

    @FXML
    private Label lblVehicleModel;

    @FXML
    private Label lblVehicleNo;

    @FXML
    private Label lblClock;

    @FXML
    private ImageView vehiImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        setCellValueFactory();
        setLastReservation();
        loadCustomerIDs();
        generateNextRideNo();
        setToday();
        getAllPending();
    }

    private void generateNextRideNo() {
        try {
            String nextNo = RideModel.generateNextRideNo();
            lblRideNo.setText(nextNo);
            txtRideNo.setText(nextNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerIDs() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> customerIDsIDs = CustomerModel.getCustomerIDs();
            for (String id : customerIDsIDs) {
                obList.add(id);
            }
            cmbCustID.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    private void setToday() {
        datePicker.setValue(LocalDate.now());
    }

    private void setCellValueFactory() {
        colRideNo.setCellValueFactory(new PropertyValueFactory<>("RideNo"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        colRideDate.setCellValueFactory(new PropertyValueFactory<>("RideDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
    }

    private void setDefaultWarnings(){
        lblInvalidRideNo.setVisible(false);
        lblInvalidCustID.setVisible(false);
    }

    private void setLastReservation() {
        try {
            Ride ride = RideModel.getLastRide();
            assert ride != null;
            String rideNo = ride.getRideNo();
            String custID = ride.getCustID();
            String rideDate = ride.getRideDate();
            String custName = CustomerModel.getCustomerName(custID);
            String membType = CustomerModel.getCustomerMemb(custID);
            assert membType != null;
            String membership;
            if( membType.equalsIgnoreCase("m1")){ membership = "Platinum"; }
            else if ( membType.equalsIgnoreCase("m2")){ membership = "Gold"; }
            else{ membership = "Silver"; }
            String vehiNo = RideVehicleModel.getVehicle(rideNo);
            String vehiModel = VehicleModel.getVehicleModel(vehiNo);
            InputStream inputStream = VehicleImageModel.getImage(vehiNo);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                vehiImage.setImage(image);
                vehiImage.setPreserveRatio(false);
            }
            lblRideNo1.setText(rideNo);
            lblRideDate.setText(rideDate);
            lblCustName.setText(custName);
            lblMembership.setText(membership);
            lblVehicleNo.setText(vehiNo);
            lblVehicleModel.setText(vehiModel);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAllPending() {
        try {
            ObservableList<RideTM> obList = FXCollections.observableArrayList();
            List<Ride> rideList = RideModel.getAllPending();

            for(Ride ride : rideList) {
                obList.add(new RideTM(
                        ride.getRideNo(),
                        ride.getCustID(),
                        ride.getRideDate(),
                        ride.getStatus()
                ));
            }
            tblRide.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblRide.getSelectionModel().clearSelection();
        cmbCustID.setValue(null);
        setToday();
        getAllPending();
        setLastReservation();
        setDefaultWarnings();
        generateNextRideNo();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        setDefaultWarnings();
        if(!txtRideNo.getText().isEmpty() && cmbCustID.getValue() != null && datePicker.getValue() != null){
            boolean isRideNoMatched = RegExPatterns.getRideNoPattern().matcher(txtRideNo.getText()).matches();
            boolean isCustIDMatched = RegExPatterns.getCustomerIDPattern().matcher(cmbCustID.getValue()).matches();
            boolean isDateMatched = RegExPatterns.datePattern(datePicker.getValue());
            String rideNo;
            String custId;
            LocalDate rideDate;
            String status;
            if(isRideNoMatched){
                if(isCustIDMatched){
                    if(isDateMatched) {
                        rideNo = txtRideNo.getText();
                        custId = cmbCustID.getValue();
                        rideDate = datePicker.getValue();
                        status = "Pending";
                        boolean added = false;
                        try{
                            added = RideModel.addRide(rideNo, custId, rideDate, status);
                        }catch(SQLException e){
                            new Alert(Alert.AlertType.ERROR, "Something went wrong:\nRide not added.\n"+e).show();
                        }
                        if(added){
                            new Alert(Alert.AlertType.INFORMATION, "Ride added successfully..").showAndWait();
                            btnSetDriverOnAction(actionEvent);
                            btnResetOnAction(actionEvent);
                        }
                    }else{ new Alert(Alert.AlertType.ERROR, "Invalid Date:\nThe date must be an upcoming date..").showAndWait(); datePicker.requestFocus();}
                }else{ setDefaultWarnings(); lblInvalidCustID.setVisible(true); cmbCustID.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidRideNo.setVisible(true); txtRideNo.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty.").show(); }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        btnDelete.setOnAction((e) -> {
            String rideNo = txtRideNo.getText();
            if(!rideNo.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = RideModel.deleteRide(rideNo);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted.\n"+ee).show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "Ride deleted successfully..").showAndWait();
                        btnResetOnAction(actionEvent);
                    }else{ new Alert(Alert.AlertType.ERROR, "Invalid Ride:\nPlease insert a valid Ride No").show(); }
                }
            }else{ new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the Ride No").show();}
        });
    }

    public void btnSetDriverOnAction(ActionEvent actionEvent) throws IOException {
        String rideNo = txtRideNo.getText();
        LocalDate rideDate = datePicker.getValue();
        if(!rideNo.isEmpty() && rideDate != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ride_driver_form.fxml"));
            Parent root = loader.load();
            RideDriverFormController rideDriverFormController = loader.getController();
            rideDriverFormController.setTextfields(rideNo, rideDate);
            rideDriverFormController.getAllAvailableDrivers(rideDate);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            new FadeInUp(root).play();
            stage.setScene(scene);
            stage.setTitle("Set Driver");
            stage.getIcons().add(new Image("asset/icons/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        }else {new Alert(Alert.AlertType.ERROR, "Fields cannot be empty:\nSet valid Ride to get a Driver.").show();}
    }

    public void btnSetVehicleOnAction(ActionEvent actionEvent) throws IOException {
        String rideNo = txtRideNo.getText();
        LocalDate rideDate = datePicker.getValue();
        if(!rideNo.isEmpty() && rideDate != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ride_vehicle_form.fxml"));
            Parent root = loader.load();
            RideVehicleFormController rideVehicleFormController = loader.getController();
            rideVehicleFormController.setTextfields(rideNo, rideDate);
            rideVehicleFormController.getAllAvailableVehicles(rideDate);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            new FadeInUp(root).play();
            stage.setScene(scene);
            stage.setTitle("Set Vehicle");
            stage.getIcons().add(new Image("asset/icons/icon.png"));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        }else {new Alert(Alert.AlertType.ERROR, "Fields cannot be empty:\nSet valid Ride to get a Vehicle.").show();}
    }

    public void txtRideNoOnAction(ActionEvent actionEvent) {
        cmbCustID.requestFocus();
    }

    public void cmbCustIDOnAction(ActionEvent actionEvent) {
        datePicker.requestFocus();
    }

    public void datePickerOnAction(ActionEvent actionEvent) {
        btnSetDriver.requestFocus();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        if(checkIsAddDriver() && checkIsAddVehicle()){
            Navigation.navigateToDashBoard(root);
        }else{
            new Alert(Alert.AlertType.ERROR, "Complete the ride before leave:\nAdd driver & vehicle for the ride").showAndWait();
            btnSetDriver.requestFocus();
        }
    }

    private boolean checkIsAddVehicle() {
        try {
            List<Ride> rideList = RideModel.getAllPending();
            for(Ride ride : rideList) {
                if(!RideVehicleModel.isAvailable(ride.getRideNo())){
                    return false;
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
        return true;
    }

    private boolean checkIsAddDriver() {
        try {
            List<Ride> rideList = RideModel.getAllPending();
            for(Ride ride : rideList) {
                if(!RideVehicleModel.isAvailable(ride.getRideNo())){
                    return false;
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
        return true;
    }

    public void tblRideOnAction(MouseEvent event) {
        tblRide.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtRideNo.setText(newSelection.getRideNo());
                cmbCustID.setValue(newSelection.getCustID());
                datePicker.setValue(LocalDate.parse(newSelection.getRideDate()));
            }
        });
    }
}