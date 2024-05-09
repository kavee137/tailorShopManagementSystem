package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.model.*;
import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.tm.FabricTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FabricRepo {


    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM fabric WHERE fabricID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }


    public static boolean update(Fabric fabric) throws SQLException {
        String sql = "UPDATE fabric SET supplierID = ?, fabricName = ?, fabricColor = ?, fabricQtyOnHand = ? WHERE fabricID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, fabric.getSupplierID());
        pstm.setObject(2, fabric.getFabricName());
        pstm.setObject(3, fabric.getFabricColor());
        pstm.setObject(4, fabric.getFabricQtyOnHand());
        pstm.setObject(5, fabric.getFabricID());

        return pstm.executeUpdate() > 0;
    }


    public static boolean save(Fabric fabric) throws SQLException {
        String sql = "INSERT INTO fabric VALUES(?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, fabric.getFabricID());
        pstm.setObject(2, fabric.getSupplierID());
        pstm.setObject(3, fabric.getFabricName());
        pstm.setObject(4, fabric.getFabricColor());
        pstm.setObject(5, fabric.getFabricQtyOnHand());

        return pstm.executeUpdate() > 0;
    }

    public static Fabric fabricSearch(String name, String color) throws SQLException {
        String sql = "SELECT fabricID, supplierID, fabricQtyOnHand FROM fabric WHERE fabricName = ? AND fabricColor = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String  fabricID = resultSet.getString(1);
            String  supplierID = resultSet.getString(2);
            int  fabricQtyOnHand = resultSet.getInt(3);

            return new Fabric(fabricID, supplierID, name, color, fabricQtyOnHand);
        }

        return null;
    }


    public static List<FabricTm> getAll() throws SQLException {
        String sql = "SELECT * FROM fabric";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<FabricTm> fabList = new ArrayList<>();

        while (resultSet.next()) {
            String productId = resultSet.getString(1);
            String supplierId = resultSet.getString(2);
            String fName = resultSet.getString(3);
            String fColor = resultSet.getString(4);
            int qty = resultSet.getInt(5);

            FabricTm fabric = new FabricTm(productId, supplierId, fName, fColor, qty);
            fabList.add(fabric);
        }
        return fabList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT fabricID FROM fabric ORDER BY fabricID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String fabricID = resultSet.getString(1);
            return fabricID;
        }
        return null;
    }

    public static List<String> getFabricName() throws SQLException {
        String sql = "SELECT DISTINCT fabricName FROM fabric";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> fabricList = new ArrayList<>();
        while (resultSet.next()) {
            fabricList.add(resultSet.getString(1));
        }
        return fabricList;
    }


    public static boolean update(List<OrderDetails> odList) throws SQLException {
        System.out.println(odList.get(0).getQty());
        for (OrderDetails od : odList) {
            boolean isUpdateQty = updateQty(od);
            if(!isUpdateQty) {
                System.out.println("bla " + isUpdateQty);
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(OrderDetails od) throws SQLException {
        String sql = "UPDATE fabric SET fabricQtyOnHand = fabricQtyOnHand - ? WHERE fabricID = ?";
        int fabricSize = (int) od.getFabricSize();

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        System.out.println("\nUpdateQty test " + fabricSize + " " + od.getFabricID());

        pstm.setInt(1, fabricSize);
        pstm.setString(2, od.getFabricID());

        return pstm.executeUpdate() > 0;

    }


}
