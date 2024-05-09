package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.ReservationDetails;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReservationDetailsRepo {
    public static boolean save(List<ReservationDetails> rdList) throws SQLException {
        for (ReservationDetails od : rdList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(ReservationDetails rd) throws SQLException {
        String sql = "INSERT INTO reservationDetails VALUES(?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, rd.getReservationID());
        pstm.setInt(2, rd.getProductID());
        pstm.setInt(3, rd.getQty());

        return pstm.executeUpdate() > 0;    //false ->  |
    }
}
