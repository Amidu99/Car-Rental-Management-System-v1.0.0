package lk.ijse.wheeldeal.report;

import javafx.scene.control.Alert;
import lk.ijse.wheeldeal.db.DBConnection;
import lk.ijse.wheeldeal.util.CrudUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class Report {
    private static JasperPrint jasperPrint;

    public static void createReport ( Map<String,Object> map, InputStream inputStream){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, map, connection);

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Fields cannot be empty\n"+e).show();
            e.printStackTrace();
        }
    }

    public static void showReport(){
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }

    public static InputStream getReport(String reportName) {
        String sql = "SELECT report_file FROM reports WHERE report_name = ? ";
        InputStream inputStream = null;
        ResultSet resultSet;
        try {
            resultSet = CrudUtil.execute(sql, reportName);
            if (resultSet.next()){
                inputStream = resultSet.getBinaryStream(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}