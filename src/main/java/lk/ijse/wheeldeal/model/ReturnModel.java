package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.db.DBConnection;
import lk.ijse.wheeldeal.dto.Return;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnModel {
    public static String generateNextReturnNo() throws SQLException {
        String sql = "SELECT ReturnNo FROM returns ORDER BY ReturnNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()) {
            return splitReturnNo(resultSet.getString(1));
        }
        return splitReturnNo(null);
    }

    private static String splitReturnNo(String currentReturnNo) {
        if(currentReturnNo !=null){
            String[] strings = currentReturnNo.split("[H]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("H%03d", lastDigit);
        }
        return "H001";
    }

    public static List<Return> getAll() throws SQLException {
        List<Return> data = new ArrayList<>();
        String sql = "SELECT * FROM returns";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            data.add(new Return(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static List<Return> getLast20Returns() throws SQLException {
        List<Return> data = new ArrayList<>();
        String sql = "SELECT * FROM returns ORDER BY ReturnNo DESC LIMIT 20";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            data.add(new Return(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static Return getReturn(String returnNo) throws SQLException {
        String sql = "SELECT * FROM returns WHERE ReturnNo = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, returnNo);
        if(resultSet.next()){
            Return aReturn = new Return(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
            return aReturn;
        }
        return null;
    }

    public static String getDate(String returnNo) throws SQLException {
        String sql = "SELECT ReDate FROM returns WHERE ReturnNo = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, returnNo);
        String date = null;
        if(resultSet.next()){
            date = resultSet.getString(1);
        }
       return date;
    }

    public static boolean isReturned(String returnNo) throws SQLException {
        String sql = "SELECT * FROM returns WHERE ReturnNo = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, returnNo);
        return resultSet.next();
    }

    public static String getLastReturnNo() throws SQLException {
        String sql = "SELECT ReturnNo FROM returns ORDER BY ReturnNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static String getMonthlyIncome(String year, String month) throws SQLException {
        String sql = "SELECT SUM(Cost) FROM returns WHERE monthname(ReDate) = ? AND year(ReDate) = ? ";
        ResultSet resultSet = CrudUtil.execute(sql,month,year);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static String getAnnualIncome(String year) throws SQLException {
        String sql = "SELECT SUM(Cost) FROM returns WHERE year(ReDate) = ? ";
        ResultSet resultSet = CrudUtil.execute(sql,year);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean addReturn(String returnNo, String rideNo, double distance, double cost, LocalDate reDate) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isUpdated = RideModel.updateStatus(rideNo);
            if(isUpdated){
                String sql = "INSERT INTO returns ( ReturnNo, RideNo, Distance, Cost, Redate ) VALUES ( ?, ?, ?, ?, ? )";
                boolean isAdded = CrudUtil.execute(sql, returnNo, rideNo, distance, cost, reDate);
                if(isAdded){
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException e){
            e.printStackTrace();
            assert connection != null;
            connection.rollback();
            return false;
        } finally {
            assert connection != null;
            connection.setAutoCommit(true);
        }
    }
}