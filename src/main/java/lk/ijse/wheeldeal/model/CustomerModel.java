package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Customer;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    public static Customer getCustomer(String custID) throws SQLException {
        String sql = "SELECT * FROM customer WHERE CustID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, custID);
        if(resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    public static List<Customer> getAll() throws SQLException {
        List<Customer> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        while (resultSet.next()) {
            data.add(new Customer(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5)
            ));
        }
        return data;
    }

    public static String generateNextCustomerID() throws SQLException{
        String sql = "SELECT CustID FROM customer ORDER BY CustID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()){
            return splitCustomerID(resultSet.getString(1));
        }
        return splitCustomerID(null);
    }

    private static String splitCustomerID(String currentCustID) {
        if(currentCustID !=null){
            String[] strings = currentCustID.split("[C]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("C%03d", lastDigit);
        }
        return "C001";
    }

    public static List<String> getCustomerIDs() throws SQLException {
        List<String> codes = new ArrayList<>();
        String sql = "SELECT CustID FROM customer";
        ResultSet resultSet = CrudUtil.execute(sql);
        while(resultSet.next()) {
            codes.add(resultSet.getString(1));
        }
        return codes;
    }

    public static boolean addCustomer(String custId, String id, String name, String tel, String membCode) throws SQLException {
        String sql = "INSERT INTO customer ( CustID, ID, Name, Tel, MembCode ) VALUES ( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(sql, custId, id, name, tel, membCode);
    }

    public static boolean updateCustomer(String custId, String id, String name, String tel, String membCode) throws SQLException {
        String sql = "UPDATE customer set ID = ?, Name = ?, Tel = ?, MembCode = ? WHERE CustID = ?";
        return CrudUtil.execute(sql, id, name, tel, membCode, custId);
    }

    public static boolean deleteCustomer(String custId) throws SQLException {
        String sql = "DELETE FROM customer WHERE CustID = ?";
        return CrudUtil.execute(sql, custId);
    }

    public static String getCustomerMemb(String custID) throws SQLException {
        String sql = "SELECT * FROM customer WHERE CustID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, custID);
        if(resultSet.next()) {
            return resultSet.getString(5);
        }
        return null;
    }

    public static String getCustomerName(String custID) throws SQLException {
        String sql = "SELECT Name FROM customer WHERE CustID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, custID);
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static double getMembCount(String code) throws SQLException {
        String sql = "SELECT * FROM customer WHERE MembCode = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, code);
        double count = 0;
        while(resultSet.next()) {
            count++;
        }
        return count;
    }
}