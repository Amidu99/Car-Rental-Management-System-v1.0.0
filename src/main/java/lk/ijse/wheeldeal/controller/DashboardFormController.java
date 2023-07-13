package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.wheeldeal.dto.Vehicle;
import lk.ijse.wheeldeal.dto.tm.VehicleTM;
import lk.ijse.wheeldeal.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import lk.ijse.wheeldeal.report.Report;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;

public class DashboardFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane checkforAdminPane;

    @FXML
    private AnchorPane adminPane;

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
    private Rectangle imgBorder;

    @FXML
    private ImageView vehiImage;

    @FXML
    private PieChart pieChartMembers;

    @FXML
    private PieChart pieChartVehicles;

    @FXML
    private Label lblMyImg;

    @FXML
    private Label lblHello;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblReport1;

    @FXML
    private Label lblReport2;

    @FXML
    private Label lblReport3;

    @FXML
    private Label lblReport4;

    @FXML
    private Label lblVehicles;

    @FXML
    private JFXButton btnAdmin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private JFXButton btnEnterAdminPass;

    @FXML
    private JFXButton btnUsers;

    public static String currentUserName;
    public static String currentUserPassword;
    public static double platinumCount;
    public static double goldCount;
    public static double silverCount;
    public static double carCount;
    public static double vanCount;
    public static double lorryCount;
    private Map<String, Object> map;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        setCellValueFactory();
        loadCurrentValues();
        setPieChart();
        setAnimation();
        getAllAvailableVehicles(LocalDate.now());
        displayUsername(currentUserName);
        if (currentUserPassword != null) {
            adminPane.setVisible(true);
        }
    }

    public static void loadCurrentValues() {
        try {
            platinumCount = CustomerModel.getMembCount("M1");
            goldCount = CustomerModel.getMembCount("M2");
            silverCount = CustomerModel.getMembCount("M3");
            carCount = VehicleModel.getVehicleCount("car");
            vanCount = VehicleModel.getVehicleCount("van");
            lorryCount = VehicleModel.getVehicleCount("lorry");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Something Went wrong\n" + e).show(); }
    }

    private void setPieChart() {
        ObservableList<PieChart.Data> membChartData = FXCollections.observableArrayList(
                new PieChart.Data("Platinum", platinumCount),
                new PieChart.Data("Gold", goldCount),
                new PieChart.Data("Sliver", silverCount));
        membChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " memb: ", data.pieValueProperty())));
        pieChartMembers.getData().addAll(membChartData);
        membChartData.get(0).getNode().setStyle("-fx-pie-color: #E59866;");
        membChartData.get(1).getNode().setStyle("-fx-pie-color: #FFD700;");
        membChartData.get(2).getNode().setStyle("-fx-pie-color: #C4CACE;");

        ObservableList<PieChart.Data> vehiChartData = FXCollections.observableArrayList(
                new PieChart.Data("Cars", carCount),
                new PieChart.Data("Vans", vanCount),
                new PieChart.Data("Lorries", lorryCount));
        vehiChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " count: ", data.pieValueProperty())));
        pieChartVehicles.getData().addAll(vehiChartData);
        vehiChartData.get(0).getNode().setStyle("-fx-pie-color: #E74C3C;");
        vehiChartData.get(1).getNode().setStyle("-fx-pie-color: #F7DC6F;");
        vehiChartData.get(2).getNode().setStyle("-fx-pie-color: #2ECC71;");
    }

    private void setAnimation() {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(lblMyImg);
        translateTransition.setDuration(Duration.millis(10000));
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setByX(969);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    private void setCellValueFactory() {
        colVehiNo.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiNo1.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiNo2.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colVehiModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colVehiModel1.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colVehiModel2.setCellValueFactory(new PropertyValueFactory<>("Model"));
    }

    public void getAllAvailableVehicles(LocalDate rideDate) {
        try {
            ObservableList<VehicleTM> obList = FXCollections.observableArrayList();
            List<Vehicle> vehicleList = VehicleModel.getAllAvailableVehicles(rideDate);
            for (Vehicle vehicle : vehicleList) {
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
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n" + e).show();
        }
    }

    public void btnAdminOnAction(ActionEvent event) {
        if (currentUserPassword == null) {
            checkforAdminPane.setVisible(true);
            txtPassword.requestFocus();
        } else {
            checkforAdminPane.setVisible(false);
            adminPane.setVisible(true);
        }
    }

    public void btnAdminExitOnAction(ActionEvent event) {
        adminPane.setVisible(false);
        btnAdmin.setDisable(false);
        currentUserPassword = null;
        txtPassword.clear();
    }

    public void displayUsername(String username) {
        currentUserName = username;
        lblHello.setText("Hello " + username + ",");
    }

    public void btnLogoutOnAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        if (alert.showAndWait().get() == ButtonType.OK) {
            currentUserPassword = null;
            AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login_form.fxml")));
            new FadeInUp(anchorPane).play();
            Scene scene = new Scene(anchorPane);
            Stage logWindow = (Stage) root.getScene().getWindow();
            logWindow.setScene(scene);
            logWindow.setTitle("Welcome to WheelDeal Rental Services!");
            logWindow.centerOnScreen();
        }
    }

    public void btnEnterAdminPassOnAction(ActionEvent actionEvent) {
        String password = txtPassword.getText();
        if (!password.isEmpty()) {
            boolean isAdmin = false;
            try {
                isAdmin = UserModel.isAdmin(password);
            } catch (SQLException e) {
                checkforAdminPane.setVisible(false);
                new Alert(Alert.AlertType.ERROR, "Oops! Something went wrong!\n" + e).show();
            }
            if (isAdmin) {
                checkforAdminPane.setVisible(false);
                currentUserPassword = password;
                adminPane.setVisible(true);
                btnUsers.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Access Denied:\nYou do not have permission.").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nField cannot be empty.").show();
        }
    }

    public void btnEmployeesOnAction(ActionEvent event) throws IOException {
        Navigation.setWindow(root, "employee_form.fxml");
    }

    public void btnUsersOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "user_form.fxml");
    }

    public void btnMembershipsOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "membership_form.fxml");
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "customer_form.fxml");
    }

    public void btnDriverOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "driver_form.fxml");
    }

    public void btnVehicleOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "vehicle_form.fxml");
    }

    public void btnSetrideOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "ride_form.fxml");
    }

    public void btnReturnsOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.setWindow(root, "return_form.fxml");
    }

    public void btnQrScannerOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/set_qr_option_form.fxml"));
        Parent root = loader.load();
        new FadeInUp(root).play();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Select a Option");
        stage.getIcons().add(new Image("asset/icons/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void btnReport1OnAction(ActionEvent actionEvent) {
        map = new HashMap<String, Object>();
        Report.createReport(map, Report.getReport("customer_report"));
        Report.showReport();
    }

    public void btnReport2OnAction(ActionEvent actionEvent) {
        map = new HashMap<String, Object>();
        Report.createReport(map, Report.getReport("driver_report"));
        Report.showReport();
    }

    public void btnReport3OnAction(ActionEvent actionEvent) {
        map = new HashMap<String, Object>();
        Report.createReport(map, Report.getReport("vehicle_report"));
        Report.showReport();
    }

    public void btnReport4OnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/set_year_month_form.fxml"));
        Parent root = loader.load();
        new FadeInUp(root).play();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("View Income report");
        stage.getIcons().add(new Image("asset/icons/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void tblCarOnAction(MouseEvent event) {
        tblCar.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String vehiNo = newSelection.getVehiNo();
                String vehiModel = newSelection.getModel();
                setVehiImage(vehiNo, vehiModel);
            }
        });
    }

    public void tblVanOnAction(MouseEvent event) {
        tblVan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String vehiNo = newSelection.getVehiNo();
                String vehiModel = newSelection.getModel();
                setVehiImage(vehiNo, vehiModel);
            }
        });
    }

    public void tblLorryOnAction(MouseEvent event) {
        tblLorry.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String vehiNo = newSelection.getVehiNo();
                String vehiModel = newSelection.getModel();
                setVehiImage(vehiNo, vehiModel);
            }
        });
    }

    public void exitTblOnAction(MouseEvent event) {
        imgBorder.setVisible(false);
        vehiImage.setVisible(false);
        lblVehicles.setText("vehicles");
        lblVehicles.setStyle("-fx-text-fill: #000000;");
        tblCar.getSelectionModel().clearSelection();
        tblVan.getSelectionModel().clearSelection();
        tblLorry.getSelectionModel().clearSelection();
    }

    public void setVehiImage(String vehiNo, String vehiModel) {
        try {
            InputStream inputStream = VehicleImageModel.getImage(vehiNo);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                vehiImage.setImage(image);
                imgBorder.setVisible(true);
                vehiImage.setVisible(true);
                lblVehicles.setText(vehiModel);
                lblVehicles.setStyle("-fx-text-fill: #ffffff;");
            }else{
                String imagePath = "asset/icons/no-data.png";
                Image image = new Image(imagePath);
                vehiImage.setImage(image);
                lblVehicles.setText("No  Image");
                lblVehicles.setStyle("-fx-text-fill: #D21404;");
            }
        } catch (SQLException | ClassNotFoundException e) {e.printStackTrace();}
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        btnEnterAdminPass.requestFocus();
    }

    public void btnReport1OnMouseEnter(MouseEvent event) {
        lblReport1.setVisible(true);
    }

    public void btnReport1OnMouseExit(MouseEvent event) {
        lblReport1.setVisible(false);
    }

    public void btnReport2OnMouseEnter(MouseEvent event) {
        lblReport2.setVisible(true);
    }

    public void btnReport2OnMouseExit(MouseEvent event) {
        lblReport2.setVisible(false);
    }

    public void btnReport3OnMouseEnter(MouseEvent event) {
        lblReport3.setVisible(true);
    }

    public void btnReport3OnMouseExit(MouseEvent event) {
        lblReport3.setVisible(false);
    }

    public void btnReport4OnMouseEnter(MouseEvent event) {
        lblReport4.setVisible(true);
    }

    public void btnReport4OnMouseExit(MouseEvent event) { lblReport4.setVisible(false); }
}