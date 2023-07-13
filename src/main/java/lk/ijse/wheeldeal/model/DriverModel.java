package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Driver;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DriverModel {
    public static List<Driver> getAll() throws SQLException {
        List<Driver> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM driver");
        while (resultSet.next()) {
            data.add(new Driver(
                 resultSet.getString(1),
                 resultSet.getString(2),
                 resultSet.getString(3),
                 resultSet.getString(4),
                 resultSet.getString(5)
                ));
        }
        return data;
    }

    public static List<Driver> getAllAvailableDrivers(LocalDate rideDate) throws SQLException {
        List<Driver> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM driver WHERE Availability = 'Available'");
            while (resultSet.next()) {
                String driverID = resultSet.getString(1);
                String name = resultSet.getString(2);
                String location = resultSet.getString(3);
                String tel = resultSet.getString(4);
                String availability = resultSet.getString(5);

                if (RideDriverModel.isAvailableOnDate(driverID, rideDate)) {
                    data.add(new Driver(driverID, name, location, tel, availability));
                }
            }
        return data;
    }

    public static String generateNextDriverID() throws SQLException {
        String sql = "SELECT DriverID FROM driver ORDER BY DriverID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()){
            return splitDriverID(resultSet.getString(1));
        }
        return splitDriverID(null);
    }

    private static String splitDriverID(String currentDriverID) {
        if(currentDriverID !=null){
            String[] strings = currentDriverID.split("[D]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("D%03d", lastDigit);
        }
        return "D001";
    }

    public static Driver getDriver(String driverID) throws SQLException {
        String sql = "SELECT * FROM driver WHERE DriverID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, driverID);
        if(resultSet.next()){
            Driver driver = new Driver(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            return driver;
        }
        return null;
    }

    public static boolean addDriver(String driverId, String name, String location, String tel, String availability) throws SQLException {
        String sql = "INSERT INTO driver ( DriverID, Name, Location, Tel, Availability ) VALUES ( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(sql, driverId, name, location, tel, availability);
    }

    public static boolean updateDriver(String driverId, String name, String location, String tel, String availability) throws SQLException {
        String sql = "UPDATE driver set Name = ?, Location = ?, Tel = ?, Availability = ? WHERE DriverID = ?";
        return CrudUtil.execute(sql, name, location, tel, availability, driverId);
    }

    public static boolean deleteDriver(String driverId) throws SQLException{
        String sql = "DELETE FROM driver WHERE DriverID = ?";
        return CrudUtil.execute(sql, driverId);
    }
}