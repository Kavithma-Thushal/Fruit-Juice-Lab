package dao.custom;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO {

    int saveOrders(String orderId, String customerId, LocalDate orderDate) throws SQLException, ClassNotFoundException;

    boolean exist(String orderId) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;
}
