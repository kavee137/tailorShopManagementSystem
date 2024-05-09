package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Order;
import lk.ijse.tailorshopmanagementsystem.model.Product1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo {














    public static String getOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS processingOrderCount FROM orders WHERE status = 'Processing'";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }

    public static boolean updateStatus(String  orderId, String status) throws SQLException {

        String sql = "UPDATE orders SET status = ? WHERE orderID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, status);
        pstm.setObject(2, orderId);

        return pstm.executeUpdate() > 0;
    }

    public static ResultSet getOrderCartTable(String  orderId) throws SQLException {
        String sql = "SELECT od.description, f.fabricName, f.fabricColor, od.measurements, od.fabricSize, od.unitPrice, od.qty FROM orders o JOIN orderDetails od ON o.orderID = od.orderID JOIN fabric f ON od.fabricID = f.fabricID WHERE o.orderID = ?";


        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, orderId);

        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    public static List<String> getOrderDetails(String id) throws SQLException {
        String sql = "SELECT o.orderDate, o.returnDate, o.employeeID, o.status, o.paymentID, c.NIC, c.customerName, c.customerID, e.employeeName, p.paymentType, p.price FROM orders o JOIN customer c ON o.customerID = c.customerID JOIN employee e ON o.employeeID = e.employeeID JOIN payment p ON o.paymentID = p.paymentID WHERE o.orderID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        ArrayList<String> rowData = new ArrayList<>();

        if (resultSet.next()) {
            rowData.add(resultSet.getString(1));
            rowData.add(resultSet.getString(2));
            rowData.add(resultSet.getString(3));
            rowData.add(resultSet.getString(4));
            rowData.add(resultSet.getString(5));
            rowData.add(resultSet.getString(6));
            rowData.add(resultSet.getString(7));
            rowData.add(resultSet.getString(8));
            rowData.add(resultSet.getString(9));
            rowData.add(resultSet.getString(10));
            rowData.add(resultSet.getString(11));
        }

        return rowData;
    }


    public static boolean save(Order order) throws SQLException {

        String sql = "INSERT INTO orders VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, order.getOrderID());
        pstm.setString(2, order.getCustomerID());
        pstm.setInt(3, order.getPaymentID());
        pstm.setString(4, order.getEmployeeID());
        pstm.setDate(5, Date.valueOf(String.valueOf(order.getOrderDate())));
        pstm.setDate(6, Date.valueOf(String.valueOf(order.getReturnDate())));
        pstm.setString(7, order.getStatus());

        return pstm.executeUpdate() > 0;
    }

    public static int getCurrentPaymentId() throws SQLException {
        String sql = "SELECT paymentID FROM payment ORDER BY paymentID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            int paymentId = resultSet.getInt(1);
            return paymentId;
        }
        return 1;
    }

    public static String getEmployeeName(String id) throws SQLException {
        String sql = "SELECT employeeName FROM employee WHERE employeeID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        List<String> idList = new ArrayList<>();

//        String empName;

        ResultSet resultSet = pstm.executeQuery();
        String empName = null;
        while (resultSet.next()) {
            empName = resultSet.getString(1);
        }
        return empName;
    }

    public static  List<String> getQtyOnHand(String name, String color) throws SQLException {
        String sql = "SELECT fabricQtyOnHand FROM fabric WHERE fabricName = ? AND fabricColor = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);
        pstm.setObject(2, color);


         ResultSet resultSet = pstm.executeQuery();
        List<String> qty = new ArrayList<>();
        while (resultSet.next()) {
            qty.add(resultSet.getString(1));
        }
        return qty;
    }

    public static List<String> getEmployeeIds() throws SQLException {
        String sql = "SELECT employeeID FROM employee where position = ?";

        String position = "Tailor";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, position);


        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static List<String> customerSearch(String cusNic) throws SQLException {
        String sql = "SELECT customerName, customerID FROM customer WHERE NIC = ? AND status = ?";
        String status = "Active";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, cusNic);
        pstm.setObject(2, status);

        List<String> a = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            a.add(resultSet.getString(1));
            a.add(resultSet.getString(2));

            return a;
        }

        return null;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT orderID FROM orders ORDER BY orderID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderID = resultSet.getString(1);
            return orderID;
        }
        return null;
    }

    public static List<String> getFabricName() throws SQLException {
        String sql = "SELECT DISTINCT fabricName FROM fabric";
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

    public static List<String> getColorsForFabric(String fabricName) throws SQLException {
        String sql = "SELECT DISTINCT fabricColor FROM fabric WHERE fabricName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, fabricName);


        ResultSet resultSet = pstm.executeQuery();
        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }


}
