package lk.ijse.wheeldeal.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.wheeldeal.dto.User;
import lk.ijse.wheeldeal.dto.tm.UserTM;
import lk.ijse.wheeldeal.model.UserModel;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    public TableView<UserTM> tblUser;

    @FXML
    private AnchorPane root;

    @FXML
    private Label lblNextUserID;

    @FXML
    private Label lblInvalidEmployeeID;

    @FXML
    private Label lblInvalidUserID;

    @FXML
    private Label lblInvalidName;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblInvalidPassword;

    @FXML
    private TextField txtUserID;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPassHint;

    @FXML
    private TextField txtEmpID;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableColumn<?, ?> colUserID;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colPassHint;

    @FXML
    private TableColumn<?, ?> colEmpID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        generateNextUserID();
        setCellValueFactory();
        getAll();
    }

    private void generateNextUserID() {
        try{
            String nextID = UserModel.generateNextUserID();
            lblNextUserID.setText(nextID);
            txtUserID.setText(nextID);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    void setCellValueFactory() {
        colUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        colPassHint.setCellValueFactory(new PropertyValueFactory<>("PassHint"));
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("EmpID"));
    }

    void getAll() {
        try {
            ObservableList<UserTM> obList = FXCollections.observableArrayList();
            List<User> cusList = UserModel.getAll();

            for(User user : cusList) {
                obList.add(new UserTM(
                        user.getUserID(),
                        user.getUserName(),
                        user.getPassword(),
                        user.getPassHint(),
                        user.getEmpID()
                ));
            }
            tblUser.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong").show();
        }
    }

    private void setDefaultWarnings() {
        lblInvalidEmployeeID.setVisible(false);
        lblInvalidName.setVisible(false);
        lblInvalidPassword.setVisible(false);
        lblInvalidUserID.setVisible(false);
    }

    public void txtUserIDOnAction(ActionEvent actionEvent) {
        String UserID = txtUserID.getText();
        if(!UserID.isEmpty()){
            User user = null;
            try {
                user = UserModel.getUser(UserID);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\n"+e).show();
            }
            if(user != null){
                txtUserName.setText(user.getUserName());
                txtPassword.setText(user.getPassword());
                txtPassHint.setText(user.getPassHint());
                txtEmpID.setText(user.getEmpID());
            }else{
                new Alert(Alert.AlertType.INFORMATION, "This User ID is not available").show();
                txtUserName.requestFocus();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the User ID").show();
            txtUserID.requestFocus();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblUser.getSelectionModel().clearSelection();
        txtUserID.clear();
        txtUserName.clear();
        txtPassword.clear();
        txtPassHint.clear();
        txtEmpID.clear();
        setDefaultWarnings();
        generateNextUserID();
        getAll();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtUserID.getText().isEmpty() && !txtUserName.getText().isEmpty() && !txtEmpID.getText().isEmpty() && !txtPassword.getText().isEmpty() && !txtPassHint.getText().isEmpty()){
            boolean isUserIDMatched = RegExPatterns.getUserIDPattern().matcher(txtUserID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getUsernamePattern().matcher(txtUserName.getText()).matches();
            boolean isPasswordMatched = RegExPatterns.getPasswordPattern().matcher(txtPassword.getText()).matches();
            boolean isEmployeeIDMatched = RegExPatterns.getEmployeeIDPattern().matcher(txtEmpID.getText()).matches();
            String UserID;
            String UseName;
            String Password;
            String PassHint;
            String EmpID;
            if(isUserIDMatched) {
                if (isNameMatched) {
                    if (isPasswordMatched) {
                        if (isEmployeeIDMatched) {
                            UserID = txtUserID.getText();
                            UseName = txtUserName.getText();
                            Password = txtPassword.getText();
                            PassHint = txtPassHint.getText();
                            EmpID = txtEmpID.getText();
                            boolean added = false;
                            try {
                                added = UserModel.addUser(UserID, UseName, Password, PassHint, EmpID);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Added.\n"+e).show();
                            }
                            if(added){
                                new Alert(Alert.AlertType.INFORMATION, "User added successfully..").showAndWait();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidEmployeeID.setVisible(true); txtEmpID.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidPassword.setVisible(true); txtPassword.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtUserName.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidUserID.setVisible(true); txtUserID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtUserID.getText().isEmpty() && !txtUserName.getText().isEmpty() && !txtEmpID.getText().isEmpty() && !txtPassword.getText().isEmpty() && !txtPassHint.getText().isEmpty()){
            boolean isUserIDMatched = RegExPatterns.getUserIDPattern().matcher(txtUserID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getUsernamePattern().matcher(txtUserName.getText()).matches();
            boolean isPasswordMatched = RegExPatterns.getPasswordPattern().matcher(txtPassword.getText()).matches();
            boolean isEmployeeIDMatched = RegExPatterns.getEmployeeIDPattern().matcher(txtEmpID.getText()).matches();
            String UserID;
            String UseName;
            String Password;
            String PassHint;
            String EmpID;
            if(isUserIDMatched) {
                if (isNameMatched) {
                    if (isPasswordMatched) {
                        if (isEmployeeIDMatched) {
                            UserID = txtUserID.getText();
                            UseName = txtUserName.getText();
                            Password = txtPassword.getText();
                            PassHint = txtPassHint.getText();
                            EmpID = txtEmpID.getText();
                            boolean updated = false;
                            try {
                                updated = UserModel.updateUser(UserID, UseName, Password, PassHint, EmpID);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Updated.\n"+e).show();
                            }
                            if(updated){
                                new Alert(Alert.AlertType.INFORMATION, "User updated successfully..").showAndWait();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidEmployeeID.setVisible(true); txtEmpID.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidPassword.setVisible(true); txtPassword.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtUserName.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidUserID.setVisible(true); txtUserID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
            btnDelete.setOnAction((e) -> {
                String UserID = txtUserID.getText();
                if(!UserID.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = UserModel.deleteUser(UserID);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted.\n"+ee).show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "User deleted successfully.").showAndWait();
                        btnResetOnAction(e);
                    }else{new Alert(Alert.AlertType.ERROR, "Invalid User:\nPlease insert a valid User ID").show();}
                }
            }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the User ID").show();
            }
        });
    }

    public void tblUserOnAction(MouseEvent event) {
        tblUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtUserID.setText(newSelection.getUserID());
                txtUserName.setText(newSelection.getUserName());
                txtPassword.setText(newSelection.getPassword());
                txtPassHint.setText(newSelection.getPassHint());
                txtEmpID.setText(newSelection.getEmpID());
            }
        });
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) { txtEmpID.requestFocus(); }

    public void txtUserNameOnAction(ActionEvent actionEvent) { txtPassword.requestFocus(); }

    public void txtEmpIDOnAction(ActionEvent actionEvent) { txtPassHint.requestFocus(); }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigateToDashBoard(root);
    }
}