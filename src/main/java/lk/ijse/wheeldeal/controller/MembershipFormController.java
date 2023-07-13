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
import lk.ijse.wheeldeal.dto.Membership;
import lk.ijse.wheeldeal.dto.tm.MembershipTM;
import lk.ijse.wheeldeal.model.MembershipModel;
import lk.ijse.wheeldeal.util.Clock;
import lk.ijse.wheeldeal.util.Navigation;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MembershipFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private Label lblInvalidFee;

    @FXML
    private Label lblInvalidDiscount;

    @FXML
    private Label lblInvalidTypeName;

    @FXML
    private Label lblInvalidMembCode;

    @FXML
    private Label lblClock;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtType;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<MembershipTM> tblMembership;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        setCellValueFactory();
        getAll();
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        colType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("Discount"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("Fee"));
    }

    private void getAll() {
        try {
            ObservableList<MembershipTM> obList = FXCollections.observableArrayList();
            List<Membership> membList = MembershipModel.getAll();

            for(Membership membership : membList) {
                obList.add(new MembershipTM(
                        membership.getCode(),
                        membership.getType(),
                        membership.getDiscount(),
                        membership.getFee()
                ));
            }
            tblMembership.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong").show();
        }
    }

    private void setDefaultWarnings(){
        lblInvalidMembCode.setVisible(false);
        lblInvalidDiscount.setVisible(false);
        lblInvalidTypeName.setVisible(false);
        lblInvalidFee.setVisible(false);
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Navigation.navigateToDashBoard(root);
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        tblMembership.getSelectionModel().clearSelection();
        setDefaultWarnings();
        txtDiscount.clear();
        txtCode.clear();
        txtType.clear();
        txtFee.clear();
        getAll();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        setDefaultWarnings();
        if(!txtCode.getText().isEmpty() && !txtType.getText().isEmpty() && !txtDiscount.getText().isEmpty() && !txtFee.getText().isEmpty()){
            boolean isMembCodeMatched = RegExPatterns.getMembIDPattern().matcher(txtCode.getText()).matches();
            boolean isTypeMatched = RegExPatterns.getNamePattern().matcher(txtType.getText()).matches();
            boolean isDiscountMatched = RegExPatterns.getDiscountPattern().matcher(txtDiscount.getText()).matches();
            boolean isFeeMatched = RegExPatterns.getAmountPattern().matcher(txtFee.getText()).matches();
            String code;
            String type;
            double discount;
            double fee;
            if(isMembCodeMatched) {
                if (isTypeMatched) {
                    if (isDiscountMatched) {
                        if (isFeeMatched) {
                            code = txtCode.getText();
                            type = txtType.getText();
                            discount = Double.parseDouble(txtDiscount.getText());
                            fee = Double.parseDouble(txtFee.getText());
                            boolean added = false;
                            try {
                                added = MembershipModel.addMembership(code, type, discount, fee);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Added.\n"+e).show();
                            }
                            if(added){
                                new Alert(Alert.AlertType.INFORMATION, "Membership added successfully..").showAndWait();
                                btnResetOnAction(event);
                            }
                        }else{ setDefaultWarnings(); lblInvalidFee.setVisible(true); txtFee.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidDiscount.setVisible(true); txtDiscount.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidTypeName.setVisible(true); txtType.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidMembCode.setVisible(true); txtCode.requestFocus();}
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        setDefaultWarnings();
        if(!txtCode.getText().isEmpty() && !txtType.getText().isEmpty() && !txtDiscount.getText().isEmpty() && !txtFee.getText().isEmpty()){
            boolean isMembCodeMatched = RegExPatterns.getMembIDPattern().matcher(txtCode.getText()).matches();
            boolean isTypeMatched = RegExPatterns.getNamePattern().matcher(txtType.getText()).matches();
            boolean isDiscountMatched = RegExPatterns.getDiscountPattern().matcher(txtDiscount.getText()).matches();
            boolean isFeeMatched = RegExPatterns.getAmountPattern().matcher(txtFee.getText()).matches();
            String code;
            String type;
            double discount;
            double fee;
            if(isMembCodeMatched) {
                if (isTypeMatched) {
                    if (isDiscountMatched) {
                        if (isFeeMatched) {
                            code = txtCode.getText();
                            type = txtType.getText();
                            discount = Double.parseDouble(txtDiscount.getText());
                            fee = Double.parseDouble(txtFee.getText());
                            boolean updated = false;
                            try {
                                updated = MembershipModel.updateMembership(code, type, discount, fee);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Updated.\n"+e).show();
                            }
                            if(updated){
                                new Alert(Alert.AlertType.INFORMATION, "Membership updated successfully..").showAndWait();
                                btnResetOnAction(event);
                            }
                        }else{ setDefaultWarnings(); lblInvalidFee.setVisible(true); txtFee.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidDiscount.setVisible(true); txtDiscount.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidTypeName.setVisible(true); txtType.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidMembCode.setVisible(true); txtCode.requestFocus();}
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        btnDelete.setOnAction((e) -> {
            String code = txtCode.getText();
            if(!code.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = MembershipModel.deleteMembership(code);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted..").show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "Membership deleted successfully.").showAndWait();
                        btnResetOnAction(event);
                    }else{ new Alert(Alert.AlertType.ERROR, "Invalid Membership:\nPlease insert a valid Membership code").show(); }
                }
            }else{
                new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the Membership code").show();
            }
        });
    }

    public void txtCodeOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();
        if(!code.isEmpty()){
            Membership membership = null;
            try {
                membership = MembershipModel.getMembership(code);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\n"+e).show();
            }
            if(membership != null){
                txtType.setText(membership.getType());
                txtDiscount.setText(membership.getDiscount().toString());
                txtFee.setText(membership.getFee().toString());
            }else{
                new Alert(Alert.AlertType.INFORMATION, "This Employee ID is not available").show();
                txtType.requestFocus();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the Employee ID").show();
            txtCode.requestFocus();
        }
    }

    public void tblMembershipOnAction(MouseEvent event) {
        tblMembership.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getCode());
                txtType.setText(newSelection.getType());
                txtDiscount.setText(newSelection.getDiscount().toString());
                txtFee.setText(newSelection.getFee().toString());
            }
        });
    }

    public void txtTypeOnAction(ActionEvent actionEvent) {
        txtDiscount.requestFocus();
    }

    public void txtDiscountOnAction(ActionEvent actionEvent) {
        txtFee.requestFocus();
    }
}