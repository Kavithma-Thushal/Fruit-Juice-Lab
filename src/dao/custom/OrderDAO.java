package dao;

import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO {

    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    boolean existOrders(String orderId) throws SQLException, ClassNotFoundException;

    int saveOrder(String orderId, String customerId, LocalDate orderDate) throws SQLException, ClassNotFoundException;
}
