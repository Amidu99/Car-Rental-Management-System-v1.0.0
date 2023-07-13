package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RideDriverModel {
    public static boolean isAvailableOnDate(String driverId, LocalDate rideDate) throws SQLException {
        boolean isAvailable = true;
        String sql = "SELECT * FROM ride_driver_details WHERE DriverID = ? AND RideDate = ?;";
        ResultSet resultSet = CrudUtil.execute(sql, driverId, rideDate);
        if(resultSet.next()){
            isAvailable = false;
        }
        return isAvailable;
    }

    public static boolean addRide(String rideNo, String driverID, LocalDate rideDate) throws SQLException {
        String sql = "INSERT INTO ride_driver_details ( RideNo, DriverID, RideDate ) VALUES ( ?, ?, ? )";
        return CrudUtil.execute(sql, rideNo, driverID, rideDate);
    }
}