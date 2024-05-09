package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.PlaceOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderRepo {
    public static boolean placeOrder(PlaceOrder po, String paymentType, double netTotal) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isMakePayment = PaymentRepo.newPayment(po.getOrder().getPaymentID(), netTotal, paymentType);
            System.out.println("isMakePayment");
            if (isMakePayment) {
                boolean isOrderSaved = OrderRepo.save(po.getOrder());
                System.out.println("isOrderSaved");
                if (isOrderSaved) {
                    boolean isQtyUpdated = FabricRepo.update(po.getOdList());
                    System.out.println("isQtyUpdated");
                    System.out.println(isQtyUpdated);
                    if (isQtyUpdated) {
                        boolean isOrderDetailSaved = OrderDetailsRepo.save(po.getOdList());
                        System.out.println("isOrderDetailSaved");
                        if (isOrderDetailSaved) {
                            System.out.println("commit");
                            connection.commit();
                            return true;
                        }
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
