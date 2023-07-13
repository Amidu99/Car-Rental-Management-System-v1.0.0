package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Ride;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RideModel {
    public static String generateNextRideNo() throws SQLException {
        String sql = "SELECT RideNo FROM ride ORDER BY RideNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()) {
            return splitRideNo(resultSet.getString(1));
        }
        return splitRideNo(null);
    }

    private static String splitRideNo(String currentRideNo) {
        if(currentRideNo !=null){
            String[] strings = currentRideNo.split("[R]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("R%03d", lastDigit);
        }
        return "R001";
    }

    public static Ride getLastRide() throws SQLException {
        String sql = "SELECT * FROM ride ORDER BY RideNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()){
            return new Ride(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }

    public static List<Ride> getAllPending() throws SQLException {
        List<Ride> data = new ArrayList<>();
        String sql = "SELECT * FROM ride WHERE Status = 'Pending'";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            data.add(new Ride(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return data;
    }

    public static boolean addRide(String rideNo, String custId, LocalDate rideDate, String status) throws SQLException {
        String sql = "INSERT INTO ride ( RideNo, CustID, RideDate, Status ) VALUES ( ?, ?, ?, ? )";
        return CrudUtil.execute(sql, rideNo, custId, rideDate, status);
    }

    public static boolean deleteRide(String rideNo) throws SQLException {
        String sql = "DELETE FROM ride WHERE RideNo = ?";
        return CrudUtil.execute(sql, rideNo);
    }

    public static double getCustomerDiscount(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride WHERE RideNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, rideNo);
        if (resultSet.next()) {
            String custID = resultSet.getString(2);
            String membCode = CustomerModel.getCustomerMemb(custID);
            return MembershipModel.getDiscount(membCode);
        }
        return -1;
    }

    public static Ride getRide(String rideNo) throws SQLException {
        String sql = "SELECT * FROM ride WHERE RideNo = ?";
            ResultSet resultSet = CrudUtil.execute(sql, rideNo);
            if(resultSet.next()){
                return new Ride(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }
        return null;
    }

    public static List<String> getPendingRideNos() throws SQLException {
        List<String> nos = new ArrayList<>();
        String sql = "SELECT * FROM ride WHERE Status = 'Pending'";
        ResultSet resultSet = CrudUtil.execute(sql);
        while(resultSet.next()) {
            nos.add(resultSet.getString(1));
        }
        return nos;
    }

    public static boolean updateStatus(String rideNo) throws SQLException {
        String sql = "UPDATE ride set Status = 'Returned' WHERE RideNo = ?";
        return CrudUtil.execute(sql, rideNo);
    }
}