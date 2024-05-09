package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {



    public static String getTotalSuppliers() throws SQLException {
        String sql = "SELECT COUNT(*) AS allSupplier FROM supplier";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }














    public static List<String> getSuppliereIds() throws SQLException {
            String sql = "SELECT supplierID FROM supplier WHERE status = ?";

        String status = "Active";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, status);


        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String nic = resultSet.getString(2);
            String name = resultSet.getString(3);
            String address = resultSet.getString(4);
            String tel = resultSet.getString(5);
            String status = resultSet.getString(6);

            Supplier supplier = new Supplier(id, nic, name, address, tel, status);
            supList.add(supplier);
        }
        return supList;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "UPDATE supplier SET status = 'Inactive' WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }

    public static boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, supplier.getSupplierID());
        pstm.setObject(2, supplier.getNIC());
        pstm.setObject(3, supplier.getSupplierName());
        pstm.setObject(4, supplier.getSupplierAddress());
        pstm.setObject(5, supplier.getSupplierContact());
        pstm.setObject(6, supplier.getStatus());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET supplierID = ?, NIC = ?, supplierName = ?, supplierAddress = ?, supplierContact = ?, status = ? WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, supplier.getSupplierID());
        pstm.setObject(2, supplier.getNIC());
        pstm.setObject(3, supplier.getSupplierName());
        pstm.setObject(4, supplier.getSupplierAddress());
        pstm.setObject(5, supplier.getSupplierContact());
        pstm.setObject(6, supplier.getStatus());
        pstm.setObject(7, supplier.getNIC());
        return pstm.executeUpdate() > 0;
    }
    public static Supplier nicSearch(String nic) throws SQLException {
        String sql = "SELECT supplierID, NIC, supplierName, supplierAddress, supplierContact, status FROM supplier WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);
        ResultSet resultSet = pstm.executeQuery();


        if(resultSet.next()) {
            String supplierId = resultSet.getString(1);
            String NIC = resultSet.getString(2);
            String supplierName = resultSet.getString(3);
            String supplierAddress = resultSet.getString(4);
            String supplierContact = resultSet.getString(5);
            String status = resultSet.getString(6);

            Supplier supplier = new Supplier(supplierId, NIC, supplierName, supplierAddress, supplierContact, status);


            return supplier;
        }
        return null;
    }
    public static String  getSupId() throws SQLException {
        String sql = "SELECT supplierID FROM supplier ORDER BY supplierID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String supplierID = resultSet.getString(1);

            return supplierID;
        }
        return null;
    }

}
