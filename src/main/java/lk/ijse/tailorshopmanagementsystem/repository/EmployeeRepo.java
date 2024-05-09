package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Customer;
import lk.ijse.tailorshopmanagementsystem.model.Customer1;
import lk.ijse.tailorshopmanagementsystem.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {


    public static String getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS activeEmployeeCount FROM employee WHERE status = 'Active'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }














    public static Employee nicSearch(String nic) throws SQLException {
        String sql = "SELECT employeeID, userID, NIC, position, employeeName, phoneNumber, employeeAddress, salary, status FROM employee WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);
        ResultSet resultSet = pstm.executeQuery();


        if(resultSet.next()) {
            String employeeId = resultSet.getString(1);
            String userId = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String position = resultSet.getString(4);
            String name = resultSet.getString(5);
            String tel = resultSet.getString(6);
            String address = resultSet.getString(7);
            String salary = resultSet.getString(8);
            String status = resultSet.getString(9);


            Employee employee = new Employee(employeeId, userId, NIC, position, name, tel, address, salary, status);

            return employee;
        }
        return null;
    }


    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET employeeID = ?, userID = ?, NIC = ?, position = ?, employeeName = ?, phoneNumber = ?, employeeAddress = ?, salary = ?, status = ? WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, employee.getEmployeeID());
        pstm.setObject(2, employee.getUserID());
        pstm.setObject(3, employee.getNIC());
        pstm.setObject(4, employee.getPosition());
        pstm.setObject(5, employee.getEmployeeName());
        pstm.setObject(6, employee.getPhoneNumber());
        pstm.setObject(7, employee.getEmployeeAddress());
        pstm.setObject(8, employee.getSalary());
        pstm.setObject(9, employee.getStatus());
        pstm.setObject(10, employee.getNIC());

        return pstm.executeUpdate() > 0;
    }


    public static boolean delete(String nic) throws SQLException {
        String sql = "UPDATE employee SET status = 'Inactive' WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }


    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getEmployeeID());
        pstm.setObject(2, employee.getUserID());
        pstm.setObject(3, employee.getNIC());
        pstm.setObject(4, employee.getPosition());
        pstm.setObject(5, employee.getEmployeeName());
        pstm.setObject(6, employee.getPhoneNumber());
        pstm.setObject(7, employee.getEmployeeAddress());
        pstm.setObject(8, employee.getSalary());
        pstm.setObject(9, employee.getStatus());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getPosition() throws SQLException {
        String sql = "SELECT DISTINCT position FROM employee";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static String searchByPosition(String name) throws SQLException {
            String sql = "SELECT salary FROM employee WHERE position = ?";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, name);

            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                String  salary = resultSet.getString(1);

//                Customer customer = new Customer(cus_id, name, address, tel);

                return salary;
            }

            return null;
        }

    public static String getEmpId() throws SQLException {
        String sql = "SELECT employeeID FROM employee ORDER BY employeeID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderID = resultSet.getString(1);
            return orderID;
        }
        return null;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> empList = new ArrayList<>();

        while (resultSet.next()) {
            String employeeID = resultSet.getString(1);
            String userID = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String position = resultSet.getString(4);
            String employeeName = resultSet.getString(5);
            String phoneNumber = resultSet.getString(6);
            String employeeAddress = resultSet.getString(7);
            String salary = resultSet.getString(8);
            String status = resultSet.getString(9);

            Employee employee = new Employee(employeeID, userID, NIC, position, employeeName, phoneNumber, employeeAddress, salary, status);
            empList.add(employee);
        }
        return empList;
    }
}
