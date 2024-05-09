package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.*;
import lk.ijse.tailorshopmanagementsystem.model.tm.ProductTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {

    public static String getTotalProduct() throws SQLException {
        String sql = "SELECT COUNT(*) AS allProduct FROM product";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }



    public static boolean reservatioQtyUpdate(List<ReservationDetails> prList) throws SQLException {
        for (ReservationDetails od : prList) {
            boolean isUpdateQty = updateQty(od.getQty(), od.getProductID());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(int qty, int productID) throws SQLException {
        String sql = "UPDATE product SET qtyOnHand = qtyOnHand - ? WHERE productID = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setInt(2, productID);

        return pstm.executeUpdate() > 0;
    }



























    public static List<ProductTm> getAll() throws SQLException {
        String sql = "SELECT * FROM product";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ProductTm> productList = new ArrayList<>();

        while (resultSet.next()) {
            int productID = resultSet.getInt(1);
            String productName = resultSet.getString(2);
            String productColor = resultSet.getString(3);
            String productSize = resultSet.getString(4);
            double unitPrice = resultSet.getDouble(5);
            int qtyOnHand = resultSet.getInt(6);

            ProductTm product = new ProductTm(productID, productName, productColor, productSize, unitPrice, qtyOnHand);
            productList.add(product);
        }
        return productList;
    }


    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM product WHERE productID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Product product) throws SQLException {
        String sql = "UPDATE product SET productID = ?, productName = ?, productColor = ?, productSize = ?, unitPrice = ?, qtyOnHand = ? WHERE  productID =  ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, product.getProductID());
        pstm.setObject(2, product.getProductName());
        pstm.setObject(3, product.getProductColor());
        pstm.setObject(4, product.getProductSize());
        pstm.setObject(5, product.getUnitPrice());
        pstm.setObject(6, product.getQtyOnHand());
        pstm.setObject(7, product.getProductID());

        return pstm.executeUpdate() > 0;
    }

    public static boolean save(Product product) throws SQLException {
        String sql = "INSERT INTO product VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, product.getProductID());
        pstm.setObject(2, product.getProductName());
        pstm.setObject(3, product.getProductColor());
        pstm.setObject(4, product.getProductSize());
        pstm.setObject(5, product.getUnitPrice());
        pstm.setObject(6, product.getQtyOnHand());


        return pstm.executeUpdate() > 0;
    }

    public static Product1 productSearch(String name, String color, String size) throws SQLException {
        String sql = "SELECT productID, unitPrice, qtyOnHand FROM product WHERE productName = ? AND productColor = ? AND productSize = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);
        pstm.setObject(3, size);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String  productID = resultSet.getString(1);
            String  unitPrice = resultSet.getString(2);
            String  qtyOnHand = resultSet.getString(3);

            return new Product1(productID, unitPrice, qtyOnHand);
        }

        return null;
    }

    public static List<String> getProductSize(String color, String name) throws SQLException {
        String sql = "SELECT DISTINCT productSize FROM product WHERE productName = ? AND productColor = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);

        List<String> sizeList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            sizeList.add(resultSet.getString(1));
        }
        return sizeList;
    }

    public static List<String> getProductColor(String productName) throws SQLException {
        String sql = "SELECT DISTINCT productColor FROM product WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, productName);

        List<String> colorList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            colorList.add(resultSet.getString(1));
        }
        return colorList;
    }

    public static int getCurrentId() throws SQLException {
        String sql = "SELECT productID FROM product ORDER BY productID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            int productId = Integer.parseInt(resultSet.getString(1));
            return productId;
        }
        return 1;
    }

    public static List<String> getProductName() throws SQLException {
        String sql = "SELECT DISTINCT productName FROM product";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> productList = new ArrayList<>();
        while (resultSet.next()) {
            productList.add(resultSet.getString(1));
        }
        return productList;
    }

}
