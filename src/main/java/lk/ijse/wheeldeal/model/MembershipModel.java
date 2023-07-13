package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Membership;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembershipModel {
    public static Membership getMembership(String code) throws SQLException {
        String sql = "SELECT * FROM membership WHERE Code = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, code);
        if(resultSet.next()){
            return new Membership(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4)
            );
        }
        return null;
    }

    public static List<Membership> getAll() throws SQLException {
        List<Membership> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM membership");
        while (resultSet.next()) {
            data.add(new Membership(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4)
            ));
        }
        return data;
    }

    public static boolean addMembership(String code, String type, double discount, double fee) throws SQLException {
        String sql = "INSERT INTO membership ( Code, Type, Discount, Fee ) VALUES ( ?, ?, ?, ? )";
        return CrudUtil.execute(sql, code, type, discount, fee);
    }

    public static boolean updateMembership(String code, String type, double discount, double fee) throws SQLException {
        String sql = "UPDATE membership SET Type = ?, Discount = ?, Fee = ? WHERE Code = ?";
        return CrudUtil.execute(sql, type, discount, fee, code);
    }

    public static boolean deleteMembership(String code) throws SQLException {
        String sql = "DELETE FROM membership WHERE Code = ?";
        return CrudUtil.execute(sql, code);
    }

    public static List<String> getCodes() throws SQLException {
        List<String> codes = new ArrayList<>();
        String sql = "SELECT Code FROM membership";
        ResultSet resultSet = CrudUtil.execute(sql);
        while(resultSet.next()) {
            codes.add(resultSet.getString(1));
        }
        return codes;
    }

    public static double getDiscount(String membCode) throws SQLException {
        String sql = "SELECT * FROM membership WHERE Code = ?";
        ResultSet resultSet = CrudUtil.execute(sql, membCode);
        if(resultSet.next()){
            return resultSet.getDouble(3);
        }
        return -1;
    }
}