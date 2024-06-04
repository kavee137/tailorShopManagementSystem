package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Fabric;
import lk.ijse.tailorshopmanagementsystem.model.MonthlySales;
import lk.ijse.tailorshopmanagementsystem.model.PieChartData;
import lk.ijse.tailorshopmanagementsystem.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainDashBoardRepo {


    public static List<Product> getMostReservedProduct() throws SQLException {
        List<Product> product = new ArrayList<>();
        String sql = "SELECT p.productID, p.productName, p.productColor, p.productSize, p.qtyOnHand FROM product p INNER JOIN (SELECT productID, COUNT(*) AS reservationCount FROM reservationDetails WHERE productID IN (SELECT productID FROM product WHERE productName = 'Blazer') GROUP BY productID ORDER BY reservationCount DESC LIMIT 1) AS r ON p.productID = r.productID";
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String productSize = resultSet.getString("productSize");
                String productColor = resultSet.getString("productColor");
                int qtyOnHand = resultSet.getInt("qtyOnHand");
                product.add(new Product(productID, productColor, productSize, qtyOnHand));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String sql2 = "SELECT p.productID, p.productName, p.productColor, p.productSize, p.qtyOnHand FROM product p INNER JOIN (SELECT productID, COUNT(*) AS reservationCount FROM reservationDetails WHERE productID IN (SELECT productID FROM product WHERE productName = 'Trouser') GROUP BY productID ORDER BY reservationCount DESC LIMIT 1) AS r ON p.productID = r.productID";
        Connection connection1 = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection1.prepareStatement(sql2);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String productSize = resultSet.getString("productSize");
                String productColor = resultSet.getString("productColor");
                int qtyOnHand = resultSet.getInt("qtyOnHand");
                product.add(new Product(productID, productColor, productSize, qtyOnHand));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }










        return product;
    }













    public static List<MonthlySales> getMonthlySalesFor2024() throws SQLException {
        List<MonthlySales> monthlySales = new ArrayList<>();
        String sql = "SELECT MONTH(o.orderDate) AS order_month, SUM(od.total) AS total_sales " +
                "FROM orders o " +
                "JOIN orderDetails od ON o.orderID = od.orderID " +
                "WHERE YEAR(o.orderDate) = 2024 " +
                "GROUP BY MONTH(o.orderDate)";

        Connection connection = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int orderMonth = resultSet.getInt("order_month");
                double totalSales = resultSet.getDouble("total_sales");
                monthlySales.add(new MonthlySales(orderMonth, totalSales));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return monthlySales;
    }


    public static List<Fabric> getLowStockFabrics() throws SQLException {
        List<Fabric> fabrics = new ArrayList<>();
        String sql = "SELECT fabricID, fabricName, fabricColor, fabricQtyOnHand FROM fabric WHERE fabricQtyOnHand < 50";
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String fabricID = resultSet.getString("fabricID");
                String fabricName = resultSet.getString("fabricName");
                String fabricColor = resultSet.getString("fabricColor");
                int fabricQtyOnHand = resultSet.getInt("fabricQtyOnHand");
                fabrics.add(new Fabric(fabricID, fabricName, fabricColor, fabricQtyOnHand));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fabrics;
    }


    public static List<PieChartData> getPieChartData() throws SQLException {
        List<PieChartData> data = new ArrayList<>();
        String sql = "SELECT product_name, sales_count FROM sales_table"; // Adjust SQL as needed

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String label = resultSet.getString("product_name");
            double value = resultSet.getDouble("sales_count");
            data.add(new PieChartData(label, value));
        }

        return data;
    }


}
