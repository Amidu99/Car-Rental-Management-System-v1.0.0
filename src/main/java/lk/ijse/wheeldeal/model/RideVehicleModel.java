package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RideVehicleModel {
    public static boolean isAvailableOnDate(String vehiNo, LocalDate rideDate) throws SQLException {
        boolean isAvailable = true;
        String sql = "SELECT * FROM ride_vehicle_details WHERE VehiNo = ? AND RideDate = ?";
        ResultSet resultSet = CrudUtil.execute(sql, vehiNo, rideDate);
        if(resultSet.next()){
            isAvailable = false;
        }
        return isAvailable;
    }

    public static boolean addRide(String rideNo, String vehiNo, LocalDate rideDate) throws SQLException {
        String sql = "INSERT INTO ride_vehicle_details ( RideNo, VehiNo, RideDate ) VALUES ( ?, ?, ? )";
        return CrudUtil.execute(sql, rideNo, vehiNo, rideDate);
    }

    public static double getVehiCostPerKM(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride_vehicle_details WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        if(resultSet.next()){
            String vehiNo = resultSet.getString(2);
            return VehicleModel.getVehiCostPerKM(vehiNo);
        }
        return -1;
    }

    public static String getVehicle(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride_vehicle_details WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        if(resultSet.next()) {
            return resultSet.getString(2);
        }
        return null;
    }

    public static boolean isAvailable(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride_vehicle_details WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        return resultSet.next();
    }

    public static int getCount(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride_vehicle_details WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        int count=0;
        while (resultSet.next()) {
            count++;
        }
        return count;
    }

    public static List<String> getVehiNos(String rideNo) throws SQLException {
        List<String> vehiNos = new ArrayList<>();
        String sql = "SELECT VehiNo FROM ride_vehicle_details WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        while(resultSet.next()) {
            vehiNos.add(resultSet.getString(1));
        }
        return vehiNos;
    }

    public static boolean addDistance(String rideNo, String vehiNo, double distance) throws SQLException {
        String sql = "UPDATE ride_vehicle_details set Distance = ? WHERE VehiNo = ? AND RideNo = ?";
        return CrudUtil.execute(sql, distance, vehiNo, rideNo);
    }

    public static double getDistance(String rideNo, String vehiNo) throws SQLException {
        String sql = "SELECT Distance FROM ride_vehicle_details WHERE VehiNo = ? AND RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, vehiNo, rideNo);
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }
}