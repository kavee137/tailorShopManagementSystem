package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentRepo {

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
