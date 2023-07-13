package lk.ijse.wheeldeal.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.wheeldeal.dto.Driver;
import lk.ijse.wheeldeal.dto.tm.DriverTM;
import lk.ijse.wheeldeal.model.DriverModel;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.QRGenerator;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DriverFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXComboBox<String> cmbAvailability;

    @FXML
    private TableColumn<?, ?> colAvailability;

    @FXML
    private TableColumn<?, ?> colDriverID;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private Label lblNextDriverID;

    @FXML
    private TableView<DriverTM> tblDriver;

    @FXML
    private TableView<DriverTM> tblTodayDrivers;

    @FXML
    private TableColumn<?, ?> colDID;

    @FXML
    private TableColumn<?, ?> colDName;

    @FXML
    private TextField txtDriverID;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @FXML
    private Label lblInvalidDriverID;

    @FXML
    private Label lblInvalidName;

    @FXML
    private Label lblInvalidLocation;

    @FXML
    private Label lblInvalidTel;

    @FXML
    private Label lblClock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        generateNextDriverID();
        setCellValueFactory();
        loadAvailability();
        getTodayAvailable();
        getAll();
    }

    private void generateNextDriverID() {
        try{
            String nextID = DriverModel.generateNextDriverID();
            lblNextDriverID.setText(nextID);
            txtDriverID.setText(nextID);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colDriverID.setCellValueFactory(new PropertyValueFactory<>("DriverID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("Availability"));
        colDID.setCellValueFactory(new PropertyValueFactory<>("DriverID"));
        colDName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }

    private void getTodayAvailable() {
        try {
            ObservableList<DriverTM> obList = FXCollections.observableArrayList();
            List<Driver> driverList = DriverModel.getAllAvailableDrivers(LocalDate.now());
            for(Driver driver : driverList) { obList.add(new DriverTM(driver.getDriverID(), driver.getName())); }
            tblTodayDrivers.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    private void getAll() {
        try {
            ObservableList<DriverTM> obList = FXCollections.observableArrayList();
            List<Driver> driverList = DriverModel.getAll();

            for(Driver driver : driverList) {
                obList.add(new DriverTM(
                        driver.getDriverID(),
                        driver.getName(),
                        driver.getLocation(),
                        driver.getTel(),
                        driver.getAvailability()
                ));
            }
            tblDriver.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    private void loadAvailability() {
        ObservableList <String> obList = FXCollections.observableArrayList();
        obList.add(0,"Available");
        obList.add(1,"Not Available");
        cmbAvailability.setItems(obList);
    }

    private void setDefaultWarnings(){
        lblInvalidDriverID.setVisible(false);
        lblInvalidName.setVisible(false);
        lblInvalidLocation.setVisible(false);
        lblInvalidTel.setVisible(false);
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigateToDashBoard(root);
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblDriver.getSelectionModel().clearSelection();
        txtDriverID.clear();
        txtName.clear();
        txtLocation.clear();
        txtTel.clear();
        cmbAvailability.setValue(null);
        getAll();
        setDefaultWarnings();
        getTodayAvailable();
        generateNextDriverID();
    }

    public void txtDriverIDOnAction(ActionEvent actionEvent) {
        String driverID = txtDriverID.getText();
        if(!driverID.isEmpty()){
            Driver driver = null;
            try {
                driver = DriverModel.getDriver(driverID);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\n"+e).show();
            }
            if(driver != null){
                txtName.setText(driver.getName());
                txtLocation.setText(driver.getLocation());
                txtTel.setText(driver.getTel());
                cmbAvailability.setValue(driver.getAvailability());
            }else{
                new Alert(Alert.AlertType.INFORMATION, "This Driver ID is not available").show();
                txtName.requestFocus();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter a Driver ID").show();
            txtDriverID.requestFocus();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtDriverID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtLocation.getText().isEmpty() && !txtTel.getText().isEmpty() && cmbAvailability.getValue() != null){
            boolean isDriverIDMatched = RegExPatterns.getDriverIDPattern().matcher(txtDriverID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getNamePattern().matcher(txtName.getText()).matches();
            boolean isLocationMatched = RegExPatterns.getNamePattern().matcher(txtLocation.getText()).matches();
            boolean isTelMatched = RegExPatterns.getTelPattern().matcher(txtTel.getText()).matches();
            String driverId;
            String name;
            String location;
            String tel;
            String availability;
            if(isDriverIDMatched) {
                if (isNameMatched) {
                    if (isLocationMatched) {
                        if (isTelMatched) {
                            driverId = txtDriverID.getText();
                            name = txtName.getText();
                            location = txtLocation.getText();
                            tel = txtTel.getText();
                            availability = cmbAvailability.getValue();

                            boolean added = false;
                            try {
                                added = DriverModel.addDriver(driverId, name, location, tel, availability);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Added.\n"+e).show();
                            }
                            if(added){
                                String content = "Driver ID : "+driverId+"\nDri_Name : "+name+"\nLocation : "+location+"\nTel_No : "+tel;
                                String fileName = "Driver QRs\\"+driverId+".png";
                                QRGenerator.generateQRCode(content,fileName);
                                new Alert(Alert.AlertType.INFORMATION, "Driver added.. &\nQR Code Generated successfully..").showAndWait();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidTel.setVisible(true); txtTel.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidLocation.setVisible(true); txtLocation.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtName.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidDriverID.setVisible(true); txtDriverID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty.").show(); }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtDriverID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtLocation.getText().isEmpty() && !txtTel.getText().isEmpty() && cmbAvailability.getValue() != null){
            boolean isDriverIDMatched = RegExPatterns.getDriverIDPattern().matcher(txtDriverID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getNamePattern().matcher(txtName.getText()).matches();
            boolean isLocationMatched = RegExPatterns.getNamePattern().matcher(txtLocation.getText()).matches();
            boolean isTelMatched = RegExPatterns.getTelPattern().matcher(txtTel.getText()).matches();
            String driverId;
            String name;
            String location;
            String tel;
            String availability;
            if(isDriverIDMatched) {
                if (isNameMatched) {
                    if (isLocationMatched) {
                        if (isTelMatched) {
                            driverId = txtDriverID.getText();
                            name = txtName.getText();
                            location = txtLocation.getText();
                            tel = txtTel.getText();
                            availability = cmbAvailability.getValue();
                            boolean updated = false;
                            try {
                                updated = DriverModel.updateDriver(driverId, name, location, tel, availability);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Updated.\n"+e).show();
                            }
                            if(updated){
                                String content = "Driver ID : "+driverId+"\nDri_Name : "+name+"\nLocation : "+location+"\nTel_No : "+tel;
                                String fileName = "Driver QRs\\"+driverId+" (updated).png";
                                QRGenerator.generateQRCode(content,fileName);
                                new Alert(Alert.AlertType.INFORMATION, "Driver details updated.. &\nQR Code updated successfully..").showAndWait();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidTel.setVisible(true); txtTel.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidLocation.setVisible(true); txtLocation.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtName.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidDriverID.setVisible(true); txtDriverID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        btnDelete.setOnAction((e) -> {
            String driverId = txtDriverID.getText();
            if(!driverId.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = DriverModel.deleteDriver(driverId);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted.\n"+ee).show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "Driver deleted successfully.").showAndWait();
                        btnResetOnAction(actionEvent);
                    }else{ new Alert(Alert.AlertType.ERROR, "Invalid ID:\nPlease insert a valid Driver ID").show();}
                }
            }else{ new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter a Driver ID").show(); }
        });
    }

    public void tblDriverOnAction(MouseEvent event) {
        tblDriver.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDriverID.setText(newSelection.getDriverID());
                txtName.setText(newSelection.getName());
                txtLocation.setText(newSelection.getLocation());
                txtTel.setText(newSelection.getTel());
                cmbAvailability.setValue(newSelection.getAvailability());
            }
        });
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtLocation.requestFocus();
    }

    public void txtLocationOnAction(ActionEvent actionEvent) {
        txtTel.requestFocus();
    }

    public void txtTelOnAction(ActionEvent actionEvent) {
        cmbAvailability.requestFocus();
    }
}