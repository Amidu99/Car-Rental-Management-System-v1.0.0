package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.util.CrudUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleImageModel {
    public static boolean addImage(FileInputStream fis, String vehiNo) throws SQLException {
        String sql = "INSERT INTO vehicleimage (Image, VehiNo) VALUES (?, ?)";
        return CrudUtil.execute(sql, fis, vehiNo);
    }

    public static boolean updateImage(FileInputStream fis, String vehiNo) throws SQLException {
        String sql = "UPDATE vehicleimage SET Image=? WHERE VehiNo=? ";
        return CrudUtil.execute(sql, fis, vehiNo);
    }

    public static InputStream getImage(String vehiNo) throws SQLException, ClassNotFoundException {
        String sql ="SELECT Image FROM vehicleimage Where VehiNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, vehiNo);
        if(resultSet.next()) {
            return resultSet.getBinaryStream(1);
        }
        return null;
    }

    public static boolean isEmpty(String vehiNo) throws SQLException {
        ResultSet result = CrudUtil.execute("SELECT Image FROM vehicleimage Where VehiNo = ?", vehiNo);
        return !result.next();
    }
}