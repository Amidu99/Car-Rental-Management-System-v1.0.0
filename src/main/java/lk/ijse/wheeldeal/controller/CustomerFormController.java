package lk.ijse.wheeldeal.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.wheeldeal.dto.Customer;
import lk.ijse.wheeldeal.dto.tm.CustomerTM;
import lk.ijse.wheeldeal.model.CustomerModel;
import lk.ijse.wheeldeal.model.MembershipModel;
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

public class CustomerFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lblNextCustID;

    @FXML
    private Label lblInvalidCustID;

    @FXML
    private Label lblInvalidID;

    @FXML
    private Label lblInvalidName;

    @FXML
    private Label lblInvalidTel;

    @FXML
    private Label lblClock;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private BarChart<String,Integer> barChart;

    @FXML
    private TableColumn<?, ?> colCustID;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colMemb;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private JFXComboBox<String> cmbMembCodes;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TextField txtCustID;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock.setClock(lblClock);
        generateNextCustomerID();
        loadMembershipCodes();
        setCellValueFactory();
        setBarChart();
        getAll();
    }

    private void setBarChart() {
        int platinumCount = (int) DashboardFormController.platinumCount;
        int goldCount = (int) DashboardFormController.goldCount;
        int silverCount = (int) DashboardFormController.silverCount;
        XYChart.Series<String, Integer> platinum = new XYChart.Series<>();
        XYChart.Series<String, Integer> gold = new XYChart.Series<>();
        XYChart.Series<String, Integer> silver = new XYChart.Series<>();
        platinum.setName("Platinum Customers");
        gold.setName("Gold Customers");
        silver.setName("Silver Customers");
        platinum.getData().add(new XYChart.Data("Platinum",platinumCount));
        gold.getData().add(new XYChart.Data("Gold",goldCount));
        silver.getData().add(new XYChart.Data("Silver",silverCount));
        barChart.getData().addAll(platinum,gold,silver);
    }

    private void generateNextCustomerID() {
        try{
            String nextID = CustomerModel.generateNextCustomerID();
            lblNextCustID.setText(nextID);
            txtCustID.setText(nextID);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void loadMembershipCodes() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> codes = MembershipModel.getCodes();
            for (String code : codes) {
                obList.add(code);
            }
            cmbMembCodes.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    private void setCellValueFactory() {
        colCustID.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
        colMemb.setCellValueFactory(new PropertyValueFactory<>("MembCode"));
    }

    private void setDefaultWarnings(){
        lblInvalidCustID.setVisible(false);
        lblInvalidID.setVisible(false);
        lblInvalidTel.setVisible(false);
        lblInvalidName.setVisible(false);
    }

    private void getAll() {
        try {
            ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
            List<Customer> custList = CustomerModel.getAll();

            for(Customer customer : custList) {
                obList.add(new CustomerTM(
                        customer.getCustID(),
                        customer.getID(),
                        customer.getName(),
                        customer.getTel(),
                        customer.getMembCode()
                ));
            }
            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something Went wrong\n"+e).show();
        }
    }

    public void txtCustIDOnAction(ActionEvent actionEvent) {
        String custID = txtCustID.getText();
        if(!custID.isEmpty()){
            Customer customer = null;
            try {
                customer = CustomerModel.getCustomer(custID);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong:\n"+e ).show();
            }

            if(customer != null){
                txtName.setText(customer.getName());
                txtID.setText(customer.getID());
                txtTel.setText(customer.getTel());
                cmbMembCodes.setValue(customer.getMembCode());
            }else{
                new Alert(Alert.AlertType.INFORMATION, "This Customer ID is not available:\nYou can add new customer..").show();
                txtName.requestFocus();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the Customer ID").show();
            txtCustID.requestFocus();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        tblCustomer.getSelectionModel().clearSelection();
        txtCustID.clear();
        txtID.clear();
        txtName.clear();
        txtTel.clear();
        cmbMembCodes.setValue(null);
        getAll();
        barChart.getData().clear();
        setBarChart();
        setDefaultWarnings();
        generateNextCustomerID();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtCustID.getText().isEmpty() && !txtID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtTel.getText().isEmpty() && cmbMembCodes.getValue() != null ){
            boolean isCustIDMatched = RegExPatterns.getCustomerIDPattern().matcher(txtCustID.getText()).matches();
            boolean isIDMatched = RegExPatterns.getIdPattern().matcher(txtID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getNamePattern().matcher(txtName.getText()).matches();
            boolean isTelMatched = RegExPatterns.getTelPattern().matcher(txtTel.getText()).matches();
            String custId;
            String id;
            String name;
            String tel;
            String membCode;
            if(isCustIDMatched) {
                if (isIDMatched) {
                    if (isNameMatched) {
                        if (isTelMatched) {
                            custId = txtCustID.getText();
                            id = txtID.getText();
                            name = txtName.getText();
                            tel = txtTel.getText();
                            membCode = cmbMembCodes.getValue();
                            boolean added = false;
                            try {
                                added = CustomerModel.addCustomer(custId, id, name, tel, membCode);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Added\n" + e).show();
                            }
                            if (added) {
                                String membership = getMembershipName(membCode);
                                String content = "Customer ID : "+custId+"\nNIC No : "+id+"\nName  : "+name+"\nTel_No : "+tel+"\nMembership : "+membership;
                                String fileName = "Customer QRs\\"+custId+".png";
                                QRGenerator.generateQRCode(content,fileName);
                                new Alert(Alert.AlertType.INFORMATION, "Customer added.. &\nQR Code Generated successfully..").showAndWait();
                                DashboardFormController.loadCurrentValues();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidTel.setVisible(true); txtTel.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtName.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidID.setVisible(true); txtID.requestFocus(); }
            }else{ setDefaultWarnings(); lblInvalidCustID.setVisible(true); txtCustID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty.").show(); }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        setDefaultWarnings();
        if(!txtCustID.getText().isEmpty() && !txtID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtTel.getText().isEmpty() && cmbMembCodes.getValue() != null){
            boolean isCustIDMatched = RegExPatterns.getCustomerIDPattern().matcher(txtCustID.getText()).matches();
            boolean isIDMatched = RegExPatterns.getIdPattern().matcher(txtID.getText()).matches();
            boolean isNameMatched = RegExPatterns.getNamePattern().matcher(txtName.getText()).matches();
            boolean isTelMatched = RegExPatterns.getTelPattern().matcher(txtTel.getText()).matches();
            String custId;
            String id;
            String name;
            String tel;
            String membCode;
            if(isCustIDMatched) {
                if (isIDMatched) {
                    if (isNameMatched) {
                        if (isTelMatched) {
                            custId = txtCustID.getText();
                            id = txtID.getText();
                            name = txtName.getText();
                            tel = txtTel.getText();
                            membCode = cmbMembCodes.getValue();
                            boolean updated = false;
                            try {
                                updated = CustomerModel.updateCustomer(custId, id, name, tel, membCode);
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Updated..").show();
                            }
                            if(updated){
                                String membership = getMembershipName(membCode);
                                String content = "Customer ID : "+custId+"\nNIC No : "+id+"\nName  : "+name+"\nTel_No : "+tel+"\nMembership : "+membership;
                                String fileName = "Customer QRs\\"+custId+" (updated).png";
                                QRGenerator.generateQRCode(content,fileName);
                                new Alert(Alert.AlertType.INFORMATION, "Customer updated.. &\nQR Code updated successfully..").showAndWait();
                                DashboardFormController.loadCurrentValues();
                                btnResetOnAction(actionEvent);
                            }
                        }else{ setDefaultWarnings(); lblInvalidTel.setVisible(true); txtTel.requestFocus(); }
                    }else{ setDefaultWarnings(); lblInvalidName.setVisible(true); txtName.requestFocus(); }
                }else{ setDefaultWarnings(); lblInvalidID.setVisible(true); txtID.requestFocus(); }
            }else{ lblInvalidCustID.setVisible(true); txtCustID.requestFocus(); }
        }else{ new Alert(Alert.AlertType.ERROR, "Fields cannot be empty").show(); }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        btnDelete.setOnAction((e) -> {
            String custId = txtCustID.getText();
            if(!custId.isEmpty()){
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete?", yes, no).showAndWait();
                if (result.orElse(no) == yes) {
                    boolean deleted = false;
                    try {
                        deleted = CustomerModel.deleteCustomer(custId);
                    } catch (SQLException ee) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong:\nNot Deleted.\n"+ee).show();
                    }
                    if(deleted){
                        new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully.").showAndWait();
                        DashboardFormController.loadCurrentValues();
                        btnResetOnAction(actionEvent);
                    }else{new Alert(Alert.AlertType.ERROR, "Invalid ID:\nPlease insert a valid Customer ID").show();}
                }
            }else{ new Alert(Alert.AlertType.ERROR, "Field cannot be empty:\nPlease enter the Customer ID").show(); }
        });
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigateToDashBoard(root);
    }

    public void tblCustomerOnAction(MouseEvent event) {
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCustID.setText(newSelection.getCustID());
                txtID.setText(newSelection.getID());
                txtName.setText(newSelection.getName());
                txtTel.setText(newSelection.getTel());
                cmbMembCodes.setValue(newSelection.getMembCode());
            }
        });
    }

    private String getMembershipName(String membCode) {
        String membership;
        if( membCode.equalsIgnoreCase("m1")){ membership = "M1 Platinum Member"; }
        else if ( membCode.equalsIgnoreCase("m2")){ membership = "M2 Gold Member"; }
        else{ membership = "M3 Silver Member"; }
        return membership;
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
        txtName.requestFocus();
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtTel.requestFocus();
    }

    public void txtTelOnAction(ActionEvent actionEvent) {
        cmbMembCodes.requestFocus();
    }
}