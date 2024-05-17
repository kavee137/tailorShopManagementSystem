package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainDashBoardRepo {

//    public static <SQLException> List<MostAppointmentTM> getMostSellItem() throws SQLException, java.sql.SQLException {
//        List<MostAppointmentTM> sellItems = new ArrayList<>();
//        String sql = "SELECT c.customerId, c.customerName, COUNT(a.appointmentId) AS visitCount " +
//                "FROM customer c LEFT JOIN appointment a ON c.customerId = a.customerId " +
//                "GROUP BY c.customerId, c.customerName";
//
//        Connection connection = DbConnection.getInstance().getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        try {
//
//
//            while (resultSet.next()) {
//                String id = resultSet.getString("customerId");
//                String name = resultSet.getString("customerName");
//                int visitCount = resultSet.getInt("visitCount");
//
//                MostAppointmentTM mostAppointmentTM = new MostAppointmentTM(id, name, visitCount);
//                sellItems.add(mostAppointmentTM);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception appropriately, e.g., log it
//        }
//
//        return sellItems;
//    }
}
