package lk.ijse.wheeldeal.model;

import lk.ijse.wheeldeal.dto.Employee;
import lk.ijse.wheeldeal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public static List<Employee> getAll() throws SQLException {
        List<Employee> data = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        while (resultSet.next()) {
            data.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return data;
    }

    public static boolean isAdmin(String empID) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmpID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, empID);
        if(resultSet.next()){
            String jobRole = resultSet.getString(3);
            return jobRole.equalsIgnoreCase("owner") || jobRole.equalsIgnoreCase("manager");
        }
        return false;
    }

    public static Employee getEmployee(String empID) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmpID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, empID);
        if(resultSet.next()){
            System.out.println("result awa");
            Employee employee = new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            System.out.println("return");
            return employee;
        }
        return null;
    }

    public static boolean addEmployee(String empID, String name, String jobRole, String tel) throws SQLException {
        String sql = "INSERT INTO employee ( EmpID, Name, JobRole, Tel ) VALUES ( ?, ?, ?, ? )";
        return CrudUtil.execute(sql, empID, name, jobRole, tel);
    }

    public static boolean updateEmployee(String empId, String name, String jobRole, String tel) throws SQLException {
        String sql = "UPDATE employee SET Name = ?, JobRole = ?, Tel = ? WHERE EmpID = ?";
        return CrudUtil.execute(sql, name, jobRole, tel, empId);
    }

    public static boolean deleteEmployee(String empId) throws SQLException {
        String sql = "DELETE FROM employee WHERE EmpID = ?";
        return CrudUtil.execute(sql, empId);
    }

    public static boolean checkEmployee(String empId) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmpID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, empId);
        return resultSet.next();
    }

    public static String getEmployeeName(String empId) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmpID = ?";
        ResultSet resultSet = CrudUtil.execute(sql,empId);
        if(resultSet.next()) {
            return resultSet.getString(2);
        }
        return null;
    }

    public static String getEmployeeRole(String empId) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmpID = ?";
        ResultSet resultSet = CrudUtil.execute(sql,empId);
        if(resultSet.next()) {
            return resultSet.getString(3);
        }
        return null;
    }

    public static String generateNextEmployeeID() throws SQLException {
        String sql = "SELECT EmpID FROM employee ORDER BY EmpID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitEmployeeId(resultSet.getString(1));
        }
        return splitEmployeeId(null);
    }

    private static String splitEmployeeId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("[E]");
            int lastDigit = Integer.parseInt(strings[1]);
            lastDigit++;
            return String.format("E%03d", lastDigit);
        }
        return "E001";
    }
}