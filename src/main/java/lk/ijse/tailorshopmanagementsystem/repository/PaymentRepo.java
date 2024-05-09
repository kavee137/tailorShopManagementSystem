package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.User;
import lk.ijse.tailorshopmanagementsystem.model.tm.PaymentTm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepo {

    public static List<PaymentTm> getDetails() throws SQLException {
        String sql = "SELECT r.reservationID, o.orderID, p.paymentID AS payment_paymentID, p.paymentType, p.price FROM reservation r LEFT JOIN orders o ON r.customerID = o.customerID LEFT JOIN payment p ON r.paymentID = p.paymentID";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<PaymentTm> paymentList = new ArrayList<>();

        while (resultSet.next()) {
            int resId = Integer.parseInt(resultSet.getString(1));
            String orderId = resultSet.getString(2);
            int paymentId = Integer.parseInt(resultSet.getString(3));
            String payementType = resultSet.getString(4);
            double price = Double.parseDouble(resultSet.getString(5));

            PaymentTm details = new PaymentTm(resId, orderId, paymentId, payementType, price);
            paymentList.add(details);
        }
        return paymentList;
    }














    public static String getTotalPayment() throws SQLException {
        String sql = "SELECT SUM(price) AS totalPayment FROM payment";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }

    static boolean newPayment(int paymentId, double netTotal, String paymentType) throws SQLException {
        String sql = "INSERT INTO payment VALUES  (?, ? ,?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, paymentId);
        pstm.setString(2, paymentType);
        pstm.setDouble(3, netTotal);

        return pstm.executeUpdate() > 0;

    }
}
