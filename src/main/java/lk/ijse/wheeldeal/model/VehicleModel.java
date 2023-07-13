package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Vehicle;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VehicleModel {
    public static Vehicle getVehicle(String vehicleNo) throws SQLException {
        String sql = "SELECT * FROM vehicle WHERE VehiNo = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, vehicleNo);
        if(resultSet.next()){
            return new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    public static String generateNextVehicleNo() throws SQLException {
        String sql = "SELECT VehiNo FROM vehicle ORDER BY VehiNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()){
            return splitVehicleNo(resultSet.getString(1));
        }
        return splitVehicleNo(null);
    }

    private static String splitVehicleNo(String currentVehicleNo) {
        if(currentVehicleNo !=null){
            String[] strings = currentVehicleNo.split("[V]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("V%03d", lastDigit);
        }
        return "V001";
    }

    public static List<Vehicle> getAll() throws SQLException {
        List<Vehicle> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM vehicle");
        while (resultSet.next()) {
            data.add(new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static boolean addVehicle(String vehiNo, String vehiType, String model, double cost, String availability) throws SQLException {
        String sql = "INSERT INTO vehicle ( VehiNo, VehiType, Model, CostPerKM, Availability ) VALUES ( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(sql, vehiNo, vehiType, model, cost, availability);
    }

    public static boolean updateVehicle(String vehiNo, String vehiType, String model, double cost, String availability) throws SQLException {
        String sql = "UPDATE vehicle set VehiType = ?, Model = ?, CostPerKM = ?, Availability = ? WHERE VehiNo = ?";
        return CrudUtil.execute(sql, vehiType, model, cost, availability, vehiNo);
    }

    public static boolean deleteVehicle(String vehiNo) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE VehiNo = ?";
        return CrudUtil.execute(sql, vehiNo);
    }

    public static List<Vehicle> getAllAvailableVehicles(LocalDate rideDate) throws SQLException {
        List<Vehicle> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM vehicle WHERE Availability = 'Available'");
        while (resultSet.next()) {
            String vehiNo = resultSet.getString(1);
            String vehiType = resultSet.getString(2);
            String model = resultSet.getString(3);
            double costPerKM = resultSet.getDouble(4);
            String availability = resultSet.getString(5);

            if (RideVehicleModel.isAvailableOnDate(vehiNo, rideDate)) {
                data.add(new Vehicle(vehiNo, vehiType, model, costPerKM, availability));
            }
        }
        return data;
    }

    public static double getVehiCostPerKM(String vehiNo) throws SQLException {
        String sql = "SELECT * FROM vehicle WHERE VehiNo = ?;";
        ResultSet resultSet = CrudUtil.execute(sql, vehiNo);
        if(resultSet.next()){
            return  resultSet.getDouble(4);
        }
        return -1;
    }

    public static String getVehicleModel(String vehiNo) throws SQLException {
        String sql = "SELECT Model FROM vehicle WHERE VehiNo = ?;";
        ResultSet resultSet = CrudUtil.execute(sql, vehiNo);
        if(resultSet.next()){
            return  resultSet.getString(1);
        }
        return null;
    }

    public static double getVehicleCount(String type) throws SQLException {
        String sql = "SELECT * FROM vehicle WHERE VehiType = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, type);
        double count = 0;
        while(resultSet.next()) {
            count++;
        }
        return count;
    }
}