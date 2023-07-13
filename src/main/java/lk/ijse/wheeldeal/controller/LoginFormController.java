package lk.ijse.wheeldeal.controller;

import animatefx.animation.JackInTheBox;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.model.EmployeeModel;
import lk.ijse.wheeldeal.model.UserModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginFormController {
    @FXML
    private Button btnCreate;

    @FXML
    private JFXButton btnLog;

    @FXML
    private Button btnLogin;

    @FXML
    private JFXButton btnReg;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private Label lblgetHint;

    @FXML
    private Label lblHint;

    @FXML
    private TextField txtUserIDtoHint;

    @FXML
    private Label lblUid;

    @FXML
    private Label lblName;

    @FXML
    private Label lblRole;

    @FXML
    private AnchorPane logPane;

    @FXML
    private AnchorPane registerPane;

    @FXML
    private TextField txtEid;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUid;

    @FXML
    private TextField txtUname;

    @FXML
    private TextField txtUpass;

    @FXML
    private TextField txtUpassHint;

    @FXML
    private TextField txtUsername;

    @FXML
    public void btnLoginOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if(!username.isEmpty() && !password.isEmpty()){
            boolean verified = UserModel.verifyLogin(username,password);
            if(verified){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard_form.fxml"));
                Parent root = loader.load();
                DashboardFormController dashboardFormController = loader.getController();
                dashboardFormController.displayUsername(username);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                new JackInTheBox(root).play();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("WheelDeal Auto Rental");
                stage.centerOnScreen();
                stage.setResizable(false);
            }else{
                new Alert(Alert.AlertType.ERROR, "Login Failed :\nInvalid Username or Password!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nUsername or Password cannot be empty.").show();
        }
    }

    public void lblGetHintOnAction(MouseEvent event) {
        lblgetHint.setVisible(true);
        txtUserIDtoHint.setVisible(true);
        txtUserIDtoHint.requestFocus();
    }

    public void btnRegOnAction(ActionEvent actionEvent) {
        logPane.setVisible(false);
        registerPane.setVisible(true);
        btnLog.setDisable(false);
        btnReg.setDisable(true);
        txtEid.requestFocus();
    }

    public void btnLogOnAction(ActionEvent actionEvent) {
        setDefaultRegister();
        registerPane.setVisible(false);
        logPane.setVisible(true);
        btnReg.setDisable(false);
        btnLog.setDisable(true);
        txtUsername.requestFocus();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String empId=txtEid.getText();
        String empName;
        String empRole;
        if(!empId.isEmpty()){
            try {
                boolean verified = EmployeeModel.checkEmployee(empId);
                if(verified) {
                    empName= EmployeeModel.getEmployeeName(empId);
                    empRole= EmployeeModel.getEmployeeRole(empId);
                    setLabels(empName, empRole);
                    boolean isAvailable = UserModel.isAvailableUser(empId);

                    if(!isAvailable && Objects.requireNonNull(empRole).equalsIgnoreCase("manager") || !isAvailable && empRole.equalsIgnoreCase("receptionist")){
                        lblUid.setVisible(true);
                        txtUid.setText(UserModel.generateNextUserID());
                        txtUname.setDisable(false);
                        txtUpass.setDisable(false);
                        txtUpassHint.setDisable(false);
                        txtUname.requestFocus();
                        btnCreate.setDisable(false);
                    }else if(isAvailable){
                        new Alert(Alert.AlertType.INFORMATION, "Already you have an user account..\nPlease use it to login..").showAndWait();
                        setDefaultRegister();
                    }else if(Objects.requireNonNull(empRole).equalsIgnoreCase("manager") || !empRole.equalsIgnoreCase("receptionist")){
                        new Alert(Alert.AlertType.WARNING, "Access Denied..\nYou do not have permission to create accounts.\nPlease contact Manager..").showAndWait();
                        setDefaultRegister();
                    }
                }else{
                    new Alert(Alert.AlertType.WARNING, "Access Denied.\nYou do not have permission to create accounts.\nPlease contact Manager..").show();
                    setDefaultRegister();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nField cannot be empty.").show();
        }
    }

    private void setLabels(String empName, String empRole) {
        lblName.setText("Name     :  "+empName);
        lblName.setVisible(true);
        lblRole.setText("JobRole :  "+empRole);
        lblRole.setVisible(true);
    }

    public void btnCreateOnAction(ActionEvent actionEvent) {
        String userID = txtUid.getText();
        String userName = txtUname.getText();
        String password = txtUpass.getText();
        String passHint = txtUpassHint.getText();
        String empID=txtEid.getText();

        if(!userName.isEmpty() && !password.isEmpty() && !passHint.isEmpty()){
            try {
                boolean isAdded = UserModel.addUser(userID, userName, password, passHint, empID);
                if (isAdded){
                    new Alert(Alert.AlertType.INFORMATION, "You have successfully create the user account\nLogin with your Username & Password").show();
                    setDefaultRegister();
                    btnLogOnAction(actionEvent);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Oops! something went wrong.").show();
                setDefaultRegister();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nFields cannot be empty.").show();
        }
        setDefaultRegister();
    }

    private void setDefaultRegister() {
        txtEid.clear();
        lblName.setVisible(false);
        lblRole.setVisible(false);
        lblUid.setVisible(false);
        txtUid.clear();
        txtUname.clear();
        txtUpass.clear();
        txtUpassHint.clear();
        txtUserIDtoHint.clear();
        txtUname.setDisable(true);
        txtUpass.setDisable(true);
        txtUpassHint.setDisable(true);
        btnCreate.setDisable(true);
        lblgetHint.setVisible(false);
        txtUserIDtoHint.setVisible(false);
        lblHint.setVisible(false);
    }
    public void txtUserIDtoHintOnAction(ActionEvent actionEvent) {
        String userID = txtUserIDtoHint.getText();
        if (!userID.isEmpty()){
            String hint = null;
            try {
                hint = UserModel.getPasswordHint(userID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(hint != null){
                lblHint.setText("your hint = "+hint);
                lblHint.setVisible(true);
            } else { new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nThis ID is not available").show(); }
        } else { new Alert(Alert.AlertType.ERROR, "Oops! Try again..\nFields cannot be empty.").show(); }
    }

    public void txtUsernameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        btnLogin.requestFocus();
    }

    public void txtEidOnAction(ActionEvent actionEvent) {
        btnSearch.requestFocus();
    }

    public void txtUnameOnAction(ActionEvent actionEvent) {
        txtUpass.requestFocus();
    }

    public void txtUpaasOnAction(ActionEvent actionEvent) { txtUpassHint.requestFocus(); }

    public void txtUpaasHintOnAction(ActionEvent actionEvent) { btnCreate.requestFocus(); }
}