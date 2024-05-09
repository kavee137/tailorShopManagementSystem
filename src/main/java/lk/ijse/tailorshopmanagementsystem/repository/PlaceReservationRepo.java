package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.PlaceReservation;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceReservationRepo {

    public static boolean placeReservation(PlaceReservation pr, double netTotal, String paymentType) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

            try {
                boolean isMakePayment = PaymentRepo.newPayment(pr.getReservation().getPaymentID(), netTotal, paymentType);
                boolean isOrderSaved = ReservationRepo.save(pr.getReservation());
                boolean isQtyUpdated = ProductRepo.reservatioQtyUpdate(pr.getRdList());
                boolean isOrderDetailSaved = ReservationDetailsRepo.save(pr.getRdList());

                if (isMakePayment && isOrderSaved && isQtyUpdated && isOrderDetailSaved) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }  catch (SQLException e) {
                connection.rollback();
                throw e; // Re-throw the exception to propagate it
            } finally {
                connection.setAutoCommit(true);
            }
        }


}
