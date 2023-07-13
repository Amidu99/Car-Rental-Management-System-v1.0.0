package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.User;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public static List<User> getAll() throws SQLException {
        List<User> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");
        while (resultSet.next()) {
            data.add(new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static boolean addUser(String userID, String userName, String password, String passHint, String empID) throws SQLException {
        String sql = "INSERT INTO user ( UserID, UserName, Password, PassHint, EmpID ) VALUES ( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(sql, userID, userName, password, passHint, empID);
    }

    public static boolean updateUser(String userID, String userName, String password, String passHint, String empID) throws SQLException {
        String sql = "UPDATE user SET UserID = ?, UserName = ?, Password = ?, PassHint = ? WHERE EmpID = ?";
        return CrudUtil.execute(sql, userID, userName, password, passHint, empID);
    }

    public static boolean deleteUser(String userID) throws SQLException {
        String sql = "DELETE FROM user WHERE UserID = ?";
        return CrudUtil.execute(sql, userID);
    }

    public static User getUser(String userID) throws SQLException {
        String sql = "SELECT * FROM user WHERE UserID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, userID);
        if(resultSet.next()){
            User user = new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            return user;
        }
        return null;
    }

    public static boolean verifyLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE UserName = ? AND Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, username, password);
        return resultSet.next();
    }

    public static boolean isAdmin(String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, password);
        if(resultSet.next()) {
            String empID = resultSet.getString(5);
            return EmployeeModel.isAdmin(empID);
        }
        return false;
    }

    public static boolean isAvailableUser(String empId) throws SQLException {
        String sql = "SELECT * FROM user WHERE EmpID = ?";
        ResultSet resultSet = CrudUtil.execute(sql,empId);
        return resultSet.next();
    }

    public static String generateNextUserID() throws SQLException {
        String sql = "SELECT UserID FROM user ORDER BY UserID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitUserId(resultSet.getString(1));
        }
        return splitUserId(null);
    }

    private static String splitUserId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("[U]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("U%03d", lastDigit);
        }
        return "U001";
    }

    public static String getPasswordHint(String userID) throws SQLException {
        String sql = "SELECT * FROM user WHERE UserID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userID);
        if(resultSet.next()) {
            return resultSet.getString(4);
        }
        return null;
    }
}