package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.OrderDetails;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailsRepo {
    public static boolean save(List<OrderDetails> odList) throws SQLException {
        for (OrderDetails od : odList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(OrderDetails od) throws SQLException {
        String sql = "INSERT INTO orderDetails VALUES(?, ?, ?, ?, ?, ?, ?)";

//        | orderID | fabricID | description | measurements      | fabricSize | unitPrice | qty  |

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getOrderID());
        pstm.setString(2, od.getFabricID());
        pstm.setString(3, od.getDescription());
        pstm.setString(4, od.getMeasurements());
        pstm.setDouble(5, od.getFabricSize());
        pstm.setDouble(6, od.getUnitPrice());
        pstm.setInt(7, od.getQty());

        return pstm.executeUpdate() > 0;    //false ->  |
    }
}
