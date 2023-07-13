package lk.ijse.wheeldeal.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.dto.Vehicle;
import lk.ijse.wheeldeal.dto.tm.VehicleTM;
import lk.ijse.wheeldeal.model.RideVehicleModel;
import lk.ijse.wheeldeal.model.VehicleImageModel;
import lk.ijse.wheeldeal.model.VehicleModel;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RideVehicleFormController implements Initializable {
    @FXML
    private AnchorPane detailPane;

    @FXML
    private AnchorPane imagePane;

    @FXML
    private ImageView vehiImage;

    @FXML
    private JFXComboBox<String> cmbVehiType;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colCost1;

    @FXML
    private TableColumn<?, ?> colCost2;

    @FXML
    private TableColumn<?, ?> colVehiModel;

    @FXML
    private TableColumn<?, ?> colVehiModel1;

    @FXML
    private TableColumn<?, ?> colVehiModel2;

    @FXML
    private TableColumn<?, ?> colVehiNo;

    @FXML
    private TableColumn<?, ?> colVehiNo1;

    @FXML
    private TableColumn<?, ?> colVehiNo2;

    @FXML
    private TableView<VehicleTM> tblCar;

    @FXML
    private TableView<VehicleTM> tblLorry;

    @FXML
    private TableView<VehicleTM> tblVan;

    @FXML
    private DatePicker txtRideDate;

    @FXML
    private Label lblVehicleType;

    @FXML
    private Label lblvehiImageModel;

    @FXML
    private TextField txtRideNo;

    @FXML
    private TextField txtVehiNo;

    @FXML
    private Button btnFullExit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadVehicleTypes();
    }

    private void setCellValueFactory() {
        colVehiNo.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiNo1.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiNo2.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colVehiModel1.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colVehiModel2.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("CostPerKM"));
        colCost1.setCellValueFactory(new PropertyValueFactory<>("CostPerKM"));
        colCost2.setCellValueFactory(new PropertyValueFactory<>("CostPerKM"));
    }

    private void loadVehicleTypes() {
        ObservableList <String> obList = FXCollections.observableArrayList();
        obList.add(0,"Car");
        obList.add(1,"Van");
        obList.add(2,"Lorry");
        cmbVehiType.setItems(obList);
    }

    public void setTextfields(String rideNo, LocalDate rideDate){
        txtRideNo.setText(rideNo);
        txtRideDate.setValue(rideDate);
    }

    public void getAllAvailableVehicles(LocalDate rideDate) {
        try {
            ObservableList<VehicleTM> obList = FXCollections.observableArrayList();
            List<Vehicle> vehicleList = VehicleModel.getAllAvailableVehicles(rideDate);

            for(Vehicle vehicle : vehicleList) {
                obList.add(new VehicleTM(
                        vehicle.getVehiNo(),
                        vehicle.getVehiType(),
                        vehicle.getModel(),
                        vehicle.getCostPerKM(),
                        vehicle.getAvailability()
                ));
            }

            tblCar.setItems(obList.filtered(vehicleTM -> vehicleTM.getVehiType().equalsIgnoreCase("car")));
            tblVan.setItems(obList.filtered(vehicleTM -> vehicleTM.getVehiType().equalsIgnoreCase("van")));
            tblLorry.setItems(obList.filtered(vehicleTM -> vehicleTM.getVehiType().equalsIgnoreCase("lorry")));

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong").show();
        }
    }

    public void handleTableSelection(TableView<VehicleTM> table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtVehiNo.setText(newSelection.getVehiNo());
                lblvehiImageModel.setText(newSelection.getModel());
                try {
                    InputStream inputStream = VehicleImageModel.getImage(newSelection.getVehiNo());
                    if (inputStream != null) {
                        Image image = new Image(inputStream);
                        vehiImage.setImage(image);
                        vehiImage.setPreserveRatio(false);
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void tblCarOnAction(MouseEvent event) {
        handleTableSelection(tblCar);
    }

    public void tblVanOnAction(MouseEvent event) {
        handleTableSelection(tblVan);
    }

    public void tblLorryOnAction(MouseEvent event) {
        handleTableSelection(tblLorry);
    }

    public void cmbVehiTypeOnAction(ActionEvent actionEvent) {
        if (cmbVehiType.getValue().equalsIgnoreCase("car")){
            tblCar.setVisible(true);
            tblVan.setVisible(false);
            tblLorry.setVisible(false);
            lblVehicleType.setText("Available Cars in "+txtRideDate.getValue().toString());
        }else if(cmbVehiType.getValue().equalsIgnoreCase("van")){
            tblCar.setVisible(false);
            tblVan.setVisible(true);
            tblLorry.setVisible(false);
            lblVehicleType.setText("Available Vans in "+txtRideDate.getValue().toString());
        }else if(cmbVehiType.getValue().equalsIgnoreCase("lorry")) {
            tblCar.setVisible(false);
            tblVan.setVisible(false);
            tblLorry.setVisible(true);
            lblVehicleType.setText("Available Lorries in "+txtRideDate.getValue().toString());
        }
    }

    public void btnViewImageOnAction(ActionEvent actionEvent) {
        if(!txtVehiNo.getText().isEmpty()) {
            btnFullExit.setDisable(true);
            detailPane.setVisible(false);
            imagePane.setVisible(true);
        }
    }

    public void btnImgExitOnAction(ActionEvent actionEvent) {
        btnFullExit.setDisable(false);
        lblvehiImageModel.setText("");
        detailPane.setVisible(true);
        String imagePath = "asset/icons/no-data.png";
        Image image = new Image(imagePath);
        vehiImage.setImage(image);
        imagePane.setVisible(false);
    }

    public void btnSaveOnAction(ActionEvent event) {
        String rideNo = txtRideNo.getText();
        LocalDate rideDate = txtRideDate.getValue();
        if(!txtVehiNo.getText().isEmpty() && rideDate.isAfter(LocalDate.now()) || rideDate.isEqual(LocalDate.now())){
            boolean added = false;
            String vehiNo = txtVehiNo.getText();
            try {
                added = RideVehicleModel.addRide(rideNo, vehiNo, rideDate);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nMake sure the ride is placed successfully,\nbefore add a Vehicle").show();
            }
            if (added) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle added successfully..").showAndWait();
                btnFullExitOnAction(event);
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Fields cannot be empty or invalid").show();
        }
    }

    public void btnFullExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}