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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.dto.Vehicle;
import lk.ijse.wheeldeal.dto.tm.VehicleTM;
import lk.ijse.wheeldeal.model.VehicleModel;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.QRGenerator;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VehicleFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private BarChart<String,Integer> barChart;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXComboBox<String> cmbAvailability;

    @FXML
    private JFXComboBox<String> cmbVehicleType;

    @FXML
    private TableColumn<?, ?> colAvailability;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colModel;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colVehicleNo;

    @FXML
    private Label lblNextVehicleNo;

    @FXML
    private Label lblInvalidVehiNo;

    @FXML
    private Label lblInvalidModel;

    @FXML
    private Label lblInvalidCost;

    @FXML
    private Label lblClock;

    @FXML
    private TableView <VehicleTM> tblVehicle;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtVehicleModel;

    @FXML
    private TextField txtVehicleNo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        generateNextVehicleNo();
        setCellValueFactory();
        loadVehicleTypes();
        loadAvailability();
        setBarChart();
        getAll();
    }

    private void loadAvailability() {
        ObservableList <String> obList = FXCollections.observableArrayList();
        obList.add(0,"Available");
        obList.add(1,"Not Available");
        cmbAvailability.setItems(obList);
    }

    private void loadVehicleTypes() {
        ObservableList <String> obList = FXCollections.observableArrayList();
        obList.add(0,"Car");
        obList.add(1,"Van");
        obList.add(2,"Lorry");
        cmbVehicleType.setItems(obList);
    }

    private void generateNextVehicleNo() {
        try {
            String nextNo = VehicleModel.generateNextVehicleNo();
            lblNextVehicleNo.setText(nextNo);
            txtVehicleNo.setText(nextNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colType.setCellValueFactory(new PropertyValueFactory<>("VehiType"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("CostPerKM"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("Availability"));
    }
    private void setDefaultWarnings(){
        lblInvalidVehiNo.setVisible(false);
        lblInvalidModel.setVisible(false);
        lblInvalidCost.setVisible(false);
    }

    private void setBarChart() {
        int carCount = (int) DashboardFormController.carCount;
        int vanCount = (int) DashboardFormController.vanCount;
        int lorryCount = (int) DashboardFormController.lorryCount;

        XYChart.Series<String, Integer> car = new XYChart.Series<>();
        XYChart.Series<String, Integer> van = new XYChart.Series<>();
        XYChart.Series<String, Integer> lorry = new XYChart.Series<>();
        car.setName("Cars");
        van.setName("Vans");
        lorry.setName("Lorries");
        car.getData().add(new XYChart.Data("Car",carCount));
        van.getData().add(new XYChart.Data("Van",vanCount));
        lorry.getData().add(new XYChart.Data("Lorry",lorryCount));
        barChart.getData().addAll(car,van,lorry);
    }

    private void getAll() {
        try {
            ObservableList<VehicleTM> obList = FXCollections.observableArrayList();
            List<Vehicle> vehicleList = VehicleModel.getAll();

            for(Vehicle vehicle : vehicleList) {
                obList.add(new VehicleTM(
                        vehicle.getVehiNo(),
                        vehicle.getVehiType(),
                        vehicle.getModel(),
                        vehicle.getCostPerKM(),
                        vehicle.getAvailability()
                ));
            }
            tblVehicle.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblVehicle.getSelectionModel().clearSelection();
        txtVehicleNo.clear();
        cmbVehicleType.setValue(null);
        txtVehicleModel.clear();
        txtCost.clear();
        cmbAvailability.setValue(null);
        barChart.getData().clear();
        setDefaultWarnings();
        setBarChart();
        generateNextVehicleNo();
        getAll();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtVehicleNo.getText().isEmpty() && cmbVehicleType.getValue() != null && !txtVehicleModel.getText().isEmpty() && !txtCost.getText().isEmpty() && cmbAvailability.getValue() != null ){
            boolean isVehiNoMatched = RegExPatterns.getVehicleNoPattern().matcher(txtVehicleNo.getText()).matches();
            boolean isModelMatched = RegExPatterns.getVehiModelPattern().matcher(txtVehicleModel.getText()).matches();
            boolean isCostMatched = RegExPatterns.getCostKmPattern().matcher(txtCost.getText()).matches();

            String vehiNo;
            String vehiType;
            String model;
            double cost;
            String availability;
            if(isVehiNoMatched){
                if(isModelMatched){
                    if(isCostMatched){
                        vehiNo = txtVehicleNo.getText();
                        vehiType = cmbVehicleType.getValue();
                        model = txtVehicleModel.getText();
                        cost = Double.parseDouble(txtCost.getText());
                        availability = cmbAvailability.getValue();
                        boolean added = false;
                        try {
                            added = VehicleModel.addVehicle(vehiNo, vehiType, model, cost, availability);
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Added.\n"+e).show();
                        }
                        if(added){
                            String content = "Vehicle No : "+vehiNo+"\nVehicle Type : "+vehiType+"\nVehicle Model : "+model+"\nCost per Km : Rs."+cost;
                            String fileName = "Vehicle QRs\\"+vehiNo+".png";
                            QRGenerator.generateQRCode(content,fileName);
                            new Alert(Alert.AlertType.INFORMATION, "Vehicle added.. &\nQR Code generated successfully..").showAndWait();
                            DashboardFormController.loadCurrentValues();
                            btnResetOnAction(actionEvent);
                            cmbVehicleType.requestFocus();
                        }
                    }else{ setDefaultWarnings(); lblInvalidCost.setVisible(true); txtCost.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidModel.setVisible(true); txtVehicleModel.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidVehiNo.setVisible(true); txtVehicleNo.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty:").show(); }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtVehicleNo.getText().isEmpty() && cmbVehicleType.getValue() != null && !txtVehicleModel.getText().isEmpty() && !txtCost.getText().isEmpty() && cmbAvailability.getValue() != null ){
            boolean isVehiNoMatched = RegExPatterns.getVehicleNoPattern().matcher(txtVehicleNo.getText()).matches();
            boolean isModelMatched = RegExPatterns.getVehiModelPattern().matcher(txtVehicleModel.getText()).matches();
            boolean isCostMatched = RegExPatterns.getCostKmPattern().matcher(txtCost.getText()).matches();
            String vehiNo;
            String vehiType;
            String model;
            double cost;
            String availability;
            if(isVehiNoMatched){
                if(isModelMatched){
                    if(isCostMatched){
                        vehiNo = txtVehicleNo.getText();
                        vehiType = cmbVehicleType.getValue();
                        model = txtVehicleModel.getText();
                        cost = Double.parseDouble(txtCost.getText());
                        availability = cmbAvailability.getValue();
                        boolean updated = false;
                        try {
                            updated = VehicleModel.updateVehicle(vehiNo, vehiType, model, cost, availability);
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Updated.\n"+e).show();
                        }
                        if(updated){
                            String content = "Vehicle No : "+vehiNo+"\nVehicle Type : "+vehiType+"\nVehicle Model : "+model+"\nCost per Km : Rs."+cost;
                            String fileName = "Vehicle QRs\\"+vehiNo+" (updated).png";
                            QRGenerator.generateQRCode(content,fileName);
                            new Alert(Alert.AlertType.INFORMATION, "Vehicle details updated.. &\nQR Code updated successfully..").showAndWait();
                            DashboardFormController.loadCurrentValues();
                            btnResetOnAction(actionEvent);
                        }
                    }else{ setDefaultWarnings(); lblInvalidCost.setVisible(true); txtCost.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidModel.setVisible(true); txtVehicleModel.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidVehiNo.setVisible(true); txtVehicleNo.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        btnDelete.setOnAction((e) -> {
            String vehiNo = txtVehicleNo.getText();
            if(!vehiNo.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = VehicleModel.deleteVehicle(vehiNo);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted..\n"+ee).show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "Vehicle deleted successfully.").showAndWait();
                        DashboardFormController.loadCurrentValues();
                        btnResetOnAction(actionEvent);
                    }else{new Alert(Alert.AlertType.ERROR, "Invalid No:\nPlease insert a valid Vehicle No").show();}
                }
            }else{
                new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter a Vehicle No").show();
            }
        });
    }

    public void btnImageOnAction(ActionEvent actionEvent) throws IOException {
        setDefaultWarnings();
        if (cmbVehicleType.getValue() != null) {
            boolean isVehiNoMatched = RegExPatterns.getVehicleNoPattern().matcher(txtVehicleNo.getText()).matches();
            if (isVehiNoMatched) {
                String vehiNo = txtVehicleNo.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vehicleimage_form.fxml"));
                Parent root = loader.load();
                VehicleImageFormController vehicleImageFormController = loader.getController();
                vehicleImageFormController.setVehicleNoLabel(vehiNo);
                vehicleImageFormController.setImage(vehiNo);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                new FadeInUp(root).play();
                stage.setScene(scene);
                stage.getIcons().add(new Image("asset/icons/icon.png"));
                stage.setTitle("Image viewer");
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();
            } else {
                lblInvalidVehiNo.setVisible(true);
                txtVehicleNo.requestFocus();
            }
        }
    }

    public void txtVehicleNoOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        boolean isVehiNoMatched = RegExPatterns.getVehicleNoPattern().matcher(txtVehicleNo.getText()).matches();
        if(isVehiNoMatched){
            Vehicle vehicle = null;
            try {
                vehicle = VehicleModel.getVehicle(txtVehicleNo.getText());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\n"+e).show();
            }
            if(vehicle != null){
                cmbVehicleType.setValue(vehicle.getVehiType());
                txtVehicleModel.setText(vehicle.getModel());
                txtCost.setText(vehicle.getCostPerKM().toString());
                cmbAvailability.setValue(vehicle.getAvailability());
            }else{
                new Alert(Alert.AlertType.INFORMATION, "This Vehicle No is not available").showAndWait();
                cmbVehicleType.requestFocus();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter a Vehicle No").show();
            lblInvalidVehiNo.setVisible(true);
            txtVehicleNo.requestFocus();
        }
    }

    public void tblVehicleOnAction(MouseEvent event) {
        tblVehicle.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtVehicleNo.setText(newSelection.getVehiNo());
                cmbVehicleType.setValue(newSelection.getVehiType());
                txtVehicleModel.setText(newSelection.getModel());
                txtCost.setText(newSelection.getCostPerKM().toString());
                cmbAvailability.setValue(newSelection.getAvailability());
            }
        });
    }

    public void cmbVehicleTypeOnAction(ActionEvent actionEvent) {
        txtVehicleModel.requestFocus();
    }

    public void txtVehicleModelOnAction(ActionEvent actionEvent) {
        txtCost.requestFocus();
    }

    public void txtCostOnAction(ActionEvent actionEvent) {
        cmbAvailability.requestFocus();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigateToDashBoard(root);
    }
}