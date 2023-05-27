package dao.custom.impl;

import dao.custom.OrderDAO;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1;");
        return resultSet.next() ? String.format("OID-%03d", (Integer.parseInt(resultSet.getString("orderId").replace("OID-", "")) + 1)) : "OID-001";
    }

    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT orderId FROM orders WHERE orderId=?");
        preparedStatement.setString(1, orderId);
        return preparedStatement.executeQuery().next();
    }

    @Override
    public int saveOrders(String orderId, String customerId, LocalDate orderDate) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (orderId, customerID, date) VALUES (?,?,?)");
        preparedStatement.setString(1, orderId);
        preparedStatement.setString(2, customerId);
        preparedStatement.setDate(3, Date.valueOf(orderDate));
        return preparedStatement.executeUpdate();
    }
}
