package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Customer;
import lk.ijse.tailorshopmanagementsystem.model.Customer1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {









    public static String getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS activeCustomerCount FROM customer WHERE status = 'Active'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "UPDATE customer SET status = 'Inactive' WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET customerID = ?, NIC = ?, customerName = ?, customerAddress = ?, customerTel = ?, userID = ?, status = ? WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, customer.getCustomerID());
        pstm.setObject(2, customer.getNIC());
        pstm.setObject(3, customer.getCustomerName());
        pstm.setObject(4, customer.getCustomerAddress());
        pstm.setObject(5, customer.getCustomerTel());
        pstm.setObject(6, customer.getUserID());
        pstm.setObject(7, customer.getStatus());
        pstm.setObject(8, customer.getNIC());

        return pstm.executeUpdate() > 0;
    }
    public static Customer1 nicSearch(String nic) throws SQLException {
        String sql = "SELECT customerID, customerName, customerAddress, customerTel, status FROM customer WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);
        ResultSet resultSet = pstm.executeQuery();



        if(resultSet.next()) {
            String customerId = resultSet.getString(1);
            String customerName = resultSet.getString(2);
            String customerAddress = resultSet.getString(3);
            String customerTel = resultSet.getString(4);
            String status = resultSet.getString(5);


            Customer1 customer1 = new Customer1(customerId, customerName, customerAddress, customerTel, status);

            return customer1;
        }
        return null;
    }

    public static boolean save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getCustomerID());
        pstm.setObject(2, customer.getNIC());
        pstm.setObject(3, customer.getCustomerName());
        pstm.setObject(4, customer.getCustomerAddress());
        pstm.setObject(5, customer.getCustomerTel());
        pstm.setObject(6, customer.getUserID());
        pstm.setObject(7, customer.getStatus());

        return pstm.executeUpdate() > 0;
    }
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT customerID FROM customer ORDER BY customerID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String customerId = resultSet.getString(1);
            return customerId;
        }
        return null;
    }

    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String customerID = resultSet.getString(1);
            String NIC = resultSet.getString(2);
            String customerName = resultSet.getString(3);
            String customerAddress = resultSet.getString(4);
            String customerTel = resultSet.getString(5);
            String userID = resultSet.getString(6);
            String status = resultSet.getString(7);

            Customer customer = new Customer(customerID, NIC, customerName, customerAddress, customerTel, userID, status );
            cusList.add(customer);
        }
        return cusList;
    }
}