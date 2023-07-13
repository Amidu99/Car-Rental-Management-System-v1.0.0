package lk.ijse.wheeldeal.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.model.ReturnModel;
import lk.ijse.wheeldeal.report.Report;
import lk.ijse.wheeldeal.util.RegExPatterns;
import java.sql.SQLException;
import java.util.HashMap;

public class SetYearMonthFormController {
    @FXML
    private JFXButton btnAdd;

    @FXML
    private Label lblWarning;

    @FXML
    private TextField txtMonth;

    @FXML
    private TextField txtYear;

    public void txtMonthOnAction(ActionEvent event) {
    btnAdd.requestFocus();
    }


    public void txtYearOnAction(ActionEvent event) {
    txtMonth.requestFocus();
    }

    public void btnAddOnAction(ActionEvent event) {
        lblWarning.setVisible(false);
        if(!txtYear.getText().isEmpty() && !txtMonth.getText().isEmpty()) {
            boolean isValidYear = RegExPatterns.getYearPattern().matcher(txtYear.getText()).matches();
            boolean isValidMonth = RegExPatterns.getMonthPattern().matcher(txtMonth.getText()).matches();
            String year;
            int monthNo;
            if (isValidYear) {
                if (isValidMonth) {
                    year = txtYear.getText();
                    String month = null;
                    monthNo = Integer.parseInt(txtMonth.getText());
                    if (monthNo == 1) {
                        month = "January";
                    } else if (monthNo == 2) {
                        month = "February";
                    } else if (monthNo == 3) {
                        month = "March";
                    } else if (monthNo == 4) {
                        month = "April";
                    } else if (monthNo == 5) {
                        month = "May";
                    } else if (monthNo == 6) {
                        month = "June";
                    } else if (monthNo == 7) {
                        month = "July";
                    } else if (monthNo == 8) {
                        month = "August";
                    } else if (monthNo == 9) {
                        month = "September";
                    } else if (monthNo == 10) {
                        month = "October";
                    } else if (monthNo == 11) {
                        month = "November";
                    } else if (monthNo == 12) {
                        month = "December";
                    }
                    String total = null;
                    try {
                        total = ReturnModel.getMonthlyIncome(year, month);
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong..\n" + e).showAndWait();
                    }
                    if (total != null) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Year", year);
                        map.put("Month", month);
                        map.put("Total", total);
                        Report.createReport(map, Report.getReport("month_report"));
                        Report.showReport();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "No Information to show").showAndWait();
                    }
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                } else {
                    lblWarning.setVisible(true);
                }
            } else {
                lblWarning.setVisible(true);
            }
        }else if(!txtYear.getText().isEmpty() && txtMonth.getText().isEmpty()){
            boolean isValidYear = RegExPatterns.getYearPattern().matcher(txtYear.getText()).matches();
            String year;
            if(isValidYear) {
                year = txtYear.getText();
                String total=null;
                String JanuaryIncome=null;
                String FebruaryIncome=null;
                String MarchIncome=null;
                String AprilIncome=null;
                String MayIncome=null;
                String JuneIncome=null;
                String JulyIncome=null;
                String AugustIncome=null;
                String SeptemberIncome=null;
                String OctoberIncome=null;
                String NovemberIncome=null;
                String DecemberIncome=null;
                try {
                    JanuaryIncome = ReturnModel.getMonthlyIncome(year,"January");   if(JanuaryIncome==null){JanuaryIncome="-";}
                    FebruaryIncome = ReturnModel.getMonthlyIncome(year,"February");   if(FebruaryIncome==null){FebruaryIncome="-";}
                    MarchIncome = ReturnModel.getMonthlyIncome(year,"March");   if(MarchIncome==null){MarchIncome="-";}
                    AprilIncome = ReturnModel.getMonthlyIncome(year,"April");   if(AprilIncome==null){AprilIncome="-";}
                    MayIncome = ReturnModel.getMonthlyIncome(year,"May");   if(MayIncome==null){MayIncome="-";}
                    JuneIncome = ReturnModel.getMonthlyIncome(year,"June");   if(JuneIncome==null){JuneIncome="-";}
                    JulyIncome = ReturnModel.getMonthlyIncome(year,"July");   if(JulyIncome==null){JulyIncome="-";}
                    AugustIncome = ReturnModel.getMonthlyIncome(year,"August");   if(AugustIncome==null){AugustIncome="-";}
                    SeptemberIncome = ReturnModel.getMonthlyIncome(year,"September");   if(SeptemberIncome==null){SeptemberIncome="-";}
                    OctoberIncome = ReturnModel.getMonthlyIncome(year,"October");   if(OctoberIncome==null){OctoberIncome="-";}
                    NovemberIncome = ReturnModel.getMonthlyIncome(year,"November");   if(NovemberIncome==null){NovemberIncome="-";}
                    DecemberIncome = ReturnModel.getMonthlyIncome(year,"December");   if(DecemberIncome==null){DecemberIncome="-";}
                    total = ReturnModel.getAnnualIncome(year);
                }
                catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Something went wrong..\n"+e).showAndWait(); }
                if(total!=null) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("M1", "January");
                    map.put("M2", "February");
                    map.put("M3", "March");
                    map.put("M4", "April");
                    map.put("M5", "May");
                    map.put("M6", "June");
                    map.put("M7", "July");
                    map.put("M8", "August");
                    map.put("M9", "September");
                    map.put("M10", "October");
                    map.put("M11", "November");
                    map.put("M12", "December");
                    map.put("M13","Total Annual Income");
                    map.put("JanuaryIncome", JanuaryIncome);
                    map.put("FebruaryIncome", FebruaryIncome);
                    map.put("MarchIncome", MarchIncome);
                    map.put("AprilIncome", AprilIncome);
                    map.put("MayIncome", MayIncome);
                    map.put("JuneIncome", JuneIncome);
                    map.put("JulyIncome", JulyIncome);
                    map.put("AugustIncome", AugustIncome);
                    map.put("SeptemberIncome", SeptemberIncome);
                    map.put("OctoberIncome", OctoberIncome);
                    map.put("NovemberIncome", NovemberIncome);
                    map.put("DecemberIncome", DecemberIncome);
                    map.put("YearName",year);
                    map.put("Total", total);

                    Report.createReport(map, Report.getReport("annual_report"));
                    Report.showReport();
                }else{ new Alert(Alert.AlertType.INFORMATION, "No Information to show").showAndWait(); }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }else{lblWarning.setVisible(true);}
        }else{ new Alert(Alert.AlertType.ERROR, "Year cannot be empty").show(); }
    }
}