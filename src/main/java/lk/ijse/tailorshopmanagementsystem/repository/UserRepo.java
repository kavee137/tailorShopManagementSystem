package lk.ijse.tailorshopmanagementsystem.repository;

import lk.ijse.tailorshopmanagementsystem.db.DbConnection;
import lk.ijse.tailorshopmanagementsystem.model.Supplier;
import lk.ijse.tailorshopmanagementsystem.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {

    public static String getTotalUser() throws SQLException {
        String sql = "SELECT COUNT(*) AS allUser FROM user";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String count = resultSet.getString(1);
            return count;
        }
        return null;
    }














    public static boolean saveUser(String id, String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO user VALUES(?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);
        pstm.setObject(2, name);
        pstm.setObject(3, email);
        pstm.setObject(4, password);
        pstm.setObject(5, "Active");

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE user SET status = 'Inactive' WHERE userID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(User user) throws SQLException {
        String sql = "UPDATE user SET userID = ?, userName = ?, userEmail = ?, password = ?, status = ? WHERE userID = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, user.getUserID());
        pstm.setObject(2, user.getUserName());
        pstm.setObject(3, user.getUserEmail());
        pstm.setObject(4, user.getPassword());
        pstm.setObject(5, user.getStatus());
        pstm.setObject(6, user.getUserID());
        return pstm.executeUpdate() > 0;
    }

    public static boolean save(User user) throws SQLException {
        String sql = "INSERT INTO user VALUES(?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, user.getUserID());
        pstm.setObject(2, user.getUserName());
        pstm.setObject(3, user.getUserEmail());
        pstm.setObject(4, user.getPassword());
        pstm.setObject(5, user.getStatus());

        return pstm.executeUpdate() > 0;
    }

    public static List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM user";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<User> supList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String userName = resultSet.getString(2);
            String email = resultSet.getString(3);
            String password = resultSet.getString(4);
            String status = resultSet.getString(5);

            User user = new User(id, userName, email, password, status);
            supList.add(user);
        }
        return supList;
    }
    public static String getUserId() throws SQLException {
        String sql = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String currentUserId = resultSet.getString(1);
            return currentUserId;
        }
        return null;
    }

}
